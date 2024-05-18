/*
 * Copyright (C) 2024 Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cn.gdrfgdrf.core;

import cn.gdrfgdrf.core.api.PluginManager;
import cn.gdrfgdrf.core.api.loader.PluginLoader;
import cn.gdrfgdrf.core.bean.BeanManager;
import cn.gdrfgdrf.core.common.Constants;
import cn.gdrfgdrf.core.config.ConfigManager;
import cn.gdrfgdrf.core.config.common.Config;
import cn.gdrfgdrf.core.exceptionhandler.GlobalUncaughtExceptionHandler;
import cn.gdrfgdrf.core.locale.LanguageLoader;
import cn.gdrfgdrf.core.utils.stack.StackUtils;
import cn.gdrfgdrf.core.utils.stack.common.MethodInformation;

/**
 * @Description 核心主程序，该程序可以直接初始化类，同时部分方法也只允许该类调用
 * @Author gdrfgdrf
 * @Date 2024/5/18
 */
public class SmartCore {
    /**
     * @Description 开始初始化，该方法将会分析堆栈调用以寻找调用该方法的类，
     * 当 Bean 创建流程开始时，将会在核心 Bean 加载完成后加载那个类所在的包名下的所有 Bean 类
     *
     * @throws Exception
     *         初始化错误
     * @Author gdrfgdrf
     * @Date 2024/5/18
     */
    public static void run() throws Exception {
        GlobalUncaughtExceptionHandler.getInstance().initialize();

        ConfigManager.getInstance().load(Constants.CONFIG_FILE_NAME);
        Config config = ConfigManager.getInstance().getConfig();

        LanguageLoader.getInstance().load(config.getLanguage());

        PluginLoader pluginLoader = PluginLoader.getInstance();
        pluginLoader.startLoading();

        Class<?> mainApplicationClass = getMainApplicationClass();
        BeanManager.initialize(mainApplicationClass);
        BeanManager.getInstance().startCreating();

        PluginManager.getInstance().enableAllPlugin();
        PluginManager.getInstance().loadAllPlugin();

        BeanManager.getInstance().startCreatingPluginBeans();
    }

    /**
     * @Description 分析堆栈以获取调用 {@link SmartCore#run()} 方法的类
     * @throws ClassNotFoundException
     *         找不到调用 {@link SmartCore#run()} 的类
     * @return java.lang.Class<?>
     *         调用 {@link SmartCore#run()} 方法的类
     * @Author gdrfgdrf
     * @Date 2024/5/18
     */
    private static Class<?> getMainApplicationClass() throws ClassNotFoundException {
        MethodInformation caller = StackUtils.getCaller(2);
        return Class.forName(caller.getClassName());
    }
}

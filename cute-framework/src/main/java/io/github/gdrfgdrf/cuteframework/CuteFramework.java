/*
 * Copyright 2024 CuteFramework's Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.gdrfgdrf.cuteframework;

import io.github.gdrfgdrf.cuteframework.api.PluginManager;
import io.github.gdrfgdrf.cuteframework.api.loader.PluginLoader;
import io.github.gdrfgdrf.cuteframework.bean.BeanManager;
import io.github.gdrfgdrf.cuteframework.common.Constants;
import io.github.gdrfgdrf.cuteframework.config.ConfigManager;
import io.github.gdrfgdrf.cuteframework.config.common.Config;
import io.github.gdrfgdrf.cuteframework.exceptionhandler.GlobalUncaughtExceptionHandler;
import io.github.gdrfgdrf.cuteframework.locale.LanguageLoader;
import io.github.gdrfgdrf.cuteframework.locale.exception.NotFoundLanguagePackageException;
import io.github.gdrfgdrf.cuteframework.utils.asserts.exception.AssertNotNullException;
import io.github.gdrfgdrf.cuteframework.utils.stack.StackUtils;
import io.github.gdrfgdrf.cuteframework.utils.stack.common.MethodInformation;

import java.io.IOException;

/**
 * 核心主程序，该程序可以直接初始化类，同时部分方法也只允许该类调用
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
public class CuteFramework {
    /**
     * 开始初始化，该方法将会分析堆栈调用以寻找调用该方法的类，
     * 当 Bean 创建流程开始时，将会在核心 Bean 加载完成后加载那个类所在的包名下的所有 Bean 类
     *
     * @throws Exception
     *         初始化错误
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public static void run() throws Exception {
        GlobalUncaughtExceptionHandler.getInstance().initialize();

        Config config = ConfigManager.getInstance().load(
                "cute-framework",
                Constants.CONFIG_FILE_NAME,
                Config.class
        );
        ConfigManager.getInstance().setConfig(config);

        loadCuteframeworkLanguage(config.getLanguage());

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
     * 加载框架语言
     * @param language
	 *        语言
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    private static void loadCuteframeworkLanguage(String language) throws
            AssertNotNullException,
            NotFoundLanguagePackageException,
            IOException,
            IllegalAccessException
    {
        LanguageLoader.getInstance().load(
                CuteFramework.class.getClassLoader(),
                "io.github.gdrfgdrf.cuteframework.locale.collect",
                "io.github.gdrfgdrf.cuteframework.locale.language",
                "cute-framework",
                language
        );
    }

    /**
     * 分析堆栈以获取调用 {@link CuteFramework#run()} 方法的类
     * @throws ClassNotFoundException
     *         找不到调用 {@link CuteFramework#run()} 的类
     * @return java.lang.Class<?>
     *         调用 {@link CuteFramework#run()} 方法的类
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    private static Class<?> getMainApplicationClass() throws ClassNotFoundException {
        MethodInformation caller = StackUtils.getCaller(2);
        return Class.forName(caller.getClassName());
    }
}

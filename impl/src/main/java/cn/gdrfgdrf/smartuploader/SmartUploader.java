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

package cn.gdrfgdrf.smartuploader;

import cn.gdrfgdrf.core.api.PluginManager;
import cn.gdrfgdrf.core.api.base.Plugin;
import cn.gdrfgdrf.core.api.event.PluginEvent;
import cn.gdrfgdrf.core.api.loader.PluginLoader;
import cn.gdrfgdrf.core.bean.BeanManager;
import cn.gdrfgdrf.core.common.Constants;
import cn.gdrfgdrf.core.config.ConfigManager;
import cn.gdrfgdrf.core.config.common.Config;
import cn.gdrfgdrf.core.event.EventManager;
import cn.gdrfgdrf.core.locale.LanguageLoader;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Description 程序主类
 * @Author gdrfgdrf
 * @Date 2024/5/1
 */
@Slf4j
public class SmartUploader {
    private static SmartUploader INSTANCE;

    private SmartUploader() {}

    /**
     * @Description 单例模式，获取 {@link SmartUploader} 实例
     * @return cn.gdrfgdrf.smartuploader.SmartUploader
     *         {@link SmartUploader} 实例
     * @Author gdrfgdrf
     * @Date 2024/5/1
     */
    public static SmartUploader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SmartUploader();
        }
        return INSTANCE;
    }

    public static void main(String[] args) throws Exception {
        SmartUploader.getInstance().run();
    }

    /**
     * @Description 程序开始运行
     * @throws Exception
     *         程序出现未被捕获的错误，此时程序将会直接崩溃
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    public void run() throws Exception {
        EventManager.getInstance().register(this);

        ConfigManager.getInstance().load(Constants.CONFIG_FILE_NAME);
        Config config = ConfigManager.getInstance().getConfig();

        LanguageLoader.getInstance().load(config.getLanguage());

        PluginLoader pluginLoader = PluginLoader.getInstance();
        pluginLoader.startLoading();

        BeanManager.getInstance().startCreating();

        Map<String, Plugin> plugins = PluginManager.getInstance().getPlugins();
        for (String name : plugins.keySet()) {
            Plugin plugin = plugins.get(name);

            PluginManager.getInstance().enablePlugin(name);
            PluginManager.getInstance().loadPlugin(name);
            PluginManager.getInstance().disablePlugin(name);
        }
    }

    @Subscribe
    public static void onPluginStateChange(PluginEvent.StateChange.Pre post) {
        System.out.println("Plugin " + post.getPlugin().getPluginDescription().getName() + " state change from " + post.getPreviousPluginState() + " to " + post.getTargetPluginState());
    }

}

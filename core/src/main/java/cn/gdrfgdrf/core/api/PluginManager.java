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

package cn.gdrfgdrf.core.api;

import cn.gdrfgdrf.core.api.base.Plugin;
import cn.gdrfgdrf.core.api.common.PluginState;
import cn.gdrfgdrf.core.api.event.PluginEvent;
import cn.gdrfgdrf.core.api.exception.PluginNameConflictException;
import cn.gdrfgdrf.core.event.EventManager;
import cn.gdrfgdrf.core.utils.asserts.AssertUtils;
import cn.gdrfgdrf.core.utils.asserts.exception.AssertNotNullException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @Description 插件管理器
 * @Author gdrfgdrf
 * @Date 2024/5/2
 */
public class PluginManager {
    private static PluginManager INSTANCE;

    /**
     * 插件名到插件主类实例的映射，存储了所有插件主类实例
     */
    private final Map<String, Plugin> PLUGIN_MAP = new ConcurrentHashMap<>();
    /**
     * 插件主类实例到插件状态的映射，存储了所有插件的状态
     */
    private final Map<Plugin, PluginState> PLUGIN_STATE_MAP = new ConcurrentHashMap<>();

    private PluginManager() {}

    /**
     * @Description 单例模式，获取 {@link PluginManager} 实例
     * @return cn.gdrfgdrf.core.api.PluginManager
     *         {@link PluginManager} 实例
     * @Author gdrfgdrf
     * @Date 2024/5/2
     */
    public static PluginManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PluginManager();
        }
        return INSTANCE;
    }

    /**
     * @Description 注册插件，此时插件将会被设置为 {@link PluginState#REGISTERED} 状态
     * @param name
	 *        插件名
	 * @param plugin
	 *        插件主类实例
     * @throws AssertNotNullException
     *         当 name 或 plugin 为 null 时抛出
     * @throws PluginNameConflictException
     *         先前已经有个一个同名的插件注册过时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public void registerPlugin(String name, Plugin plugin) throws AssertNotNullException, PluginNameConflictException {
        AssertUtils.notNull("plugin name", name);
        AssertUtils.notNull("plugin main class instance", plugin);

        if (PLUGIN_MAP.containsKey(name)) {
            throw new PluginNameConflictException(PLUGIN_MAP.get(name), plugin);
        }

        EventManager.getInstance().post(new PluginEvent.Registered.Pre(plugin));

        PLUGIN_MAP.put(name, plugin);
        PLUGIN_STATE_MAP.put(plugin, PluginState.REGISTERED);

        EventManager.getInstance().post(new PluginEvent.Registered.Post(plugin));
    }

    /**
     * @Description 移除插件
     * @param name
	 *        插件名
     * @throws AssertNotNullException
     *         当 name 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public void unregisterPlugin(String name) throws AssertNotNullException {
        AssertUtils.notNull("plugin name", name);

        Plugin plugin = PLUGIN_MAP.get(name);
        if (plugin != null) {
            EventManager.getInstance().post(new PluginEvent.Unregistered.Pre(plugin));

            PLUGIN_STATE_MAP.remove(plugin);
        }
        PLUGIN_MAP.remove(name);

        if (plugin != null) {
            EventManager.getInstance().post(new PluginEvent.Unregistered.Post(plugin));
        }
    }

    /**
     * @Description 查询插件是否注册
     * @param name
	 *        插件名
     * @return boolean
     *         插件是否注册
     * @throws AssertNotNullException
     *         当 name 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public boolean isPluginRegistered(String name) throws AssertNotNullException {
        AssertUtils.notNull("plugin name", name);
        return PLUGIN_MAP.containsKey(name);
    }

    /**
     * @Description 获取插件状态
     * @param name
	 *        插件名
     * @return cn.gdrfgdrf.core.api.common.PluginState
     *         插件状态
     * @throws AssertNotNullException
     *         当 name 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public PluginState getPluginState(String name) throws AssertNotNullException {
        Plugin plugin = getPlugin(name);
        AssertUtils.notNull("plugin main class instance", plugin);

        return getPluginState(plugin);
    }

    /**
     * @Description 获取插件状态
     * @param plugin
	 *        插件主类实例
     * @return cn.gdrfgdrf.core.api.common.PluginState
     *         插件状态
     * @throws AssertNotNullException
     *         当 plugin 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public PluginState getPluginState(Plugin plugin) throws AssertNotNullException {
        AssertUtils.notNull("plugin main class instance", plugin);
        return PLUGIN_STATE_MAP.get(plugin);
    }

    /**
     * @Description 启用插件
     * @param name
	 *        插件名
     * @throws AssertNotNullException
     *         当 name 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public void enablePlugin(String name) throws AssertNotNullException {
        updatePluginState(name, PluginState.ENABLED, Plugin::onEnable);
    }

    /**
     * @Description 加载插件
     * @param name
	 *        插件名
     * @throws AssertNotNullException
     *         当 name 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public void loadPlugin(String name) throws AssertNotNullException {
        updatePluginState(name, PluginState.LOADED, Plugin::onLoad);
    }

    /**
     * @Description 停止插件
     * @param name
	 *        插件名
     * @throws AssertNotNullException
     *         当 name 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public void stopPlugin(String name) throws AssertNotNullException {
        updatePluginState(name, PluginState.STOPPED, Plugin::onStop);
    }

    /**
     * @Description 禁用插件
     * @param name
	 *        插件名
     * @throws AssertNotNullException
     *         当 name 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public void disablePlugin(String name) throws AssertNotNullException {
        updatePluginState(name, PluginState.DISABLED, Plugin::onDisable);
    }

    /**
     * @Description 调用插件主类的方法并更新到对应的插件状态
     * @param name
	 *        插件名
	 * @param targetState
	 *        需要更新到的插件状态
	 * @param consumer
	 *        需要调用的插件主类的方法
     * @throws AssertNotNullException
     *         当 name 或 targetState 或 consumer 或 获取到的插件主类实例 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    private void updatePluginState(
            String name,
            PluginState targetState,
            Consumer<Plugin> consumer
    ) throws AssertNotNullException {
        AssertUtils.notNull("plugin name", name);
        AssertUtils.notNull("target plugin state", targetState);
        AssertUtils.notNull("plugin main class method", consumer);

        Plugin plugin = getPlugin(name);
        AssertUtils.notNull("plugin main class instance", plugin);

        PluginState previousState = PLUGIN_STATE_MAP.get(plugin);
        EventManager.getInstance().post(new PluginEvent.StateChange.Pre(
                plugin,
                targetState,
                previousState
        ));

        consumer.accept(plugin);
        PLUGIN_STATE_MAP.put(plugin, targetState);

        EventManager.getInstance().post(new PluginEvent.StateChange.Post(
                plugin,
                targetState,
                previousState
        ));
    }

    /**
     * @Description 获取插件
     * @param name
	 *        插件名
     * @return cn.gdrfgdrf.core.api.base.Plugin
     *         插件主类实例
     * @throws AssertNotNullException
     *         当 name 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public Plugin getPlugin(String name) throws AssertNotNullException {
        AssertUtils.notNull("plugin name", name);
        return PLUGIN_MAP.get(name);
    }

    /**
     * @Description 获取所有插件
     * @return java.util.Map<java.lang.String,cn.gdrfgdrf.core.api.base.Plugin>
     *         插件映射表
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public Map<String, Plugin> getPlugins() {
        return PLUGIN_MAP;
    }

    /**
     * @Description 获取所有插件状态
     * @return java.util.Map<cn.gdrfgdrf.core.api.base.Plugin,cn.gdrfgdrf.core.api.common.PluginState>
     *         插件状态映射表
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public Map<Plugin, PluginState> getPluginStates() {
        return PLUGIN_STATE_MAP;
    }
}

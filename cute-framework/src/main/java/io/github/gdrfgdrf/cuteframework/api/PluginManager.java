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

package io.github.gdrfgdrf.cuteframework.api;

import io.github.gdrfgdrf.cuteframework.api.base.Plugin;
import io.github.gdrfgdrf.cuteframework.api.common.PluginState;
import io.github.gdrfgdrf.cuteframework.api.event.PluginEvent;
import io.github.gdrfgdrf.cuteframework.api.exception.PluginIllegalStateChangeException;
import io.github.gdrfgdrf.cuteframework.api.exception.PluginNameConflictException;
import io.github.gdrfgdrf.cuteframework.event.EventManager;
import io.github.gdrfgdrf.cuteframework.utils.asserts.AssertUtils;
import io.github.gdrfgdrf.cuteframework.utils.asserts.exception.AssertNotNullException;

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
     * @return io.github.gdrfgdrf.cuteframework.api.PluginManager
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
     * @return io.github.gdrfgdrf.cuteframework.api.common.PluginState
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
     * @return io.github.gdrfgdrf.cuteframework.api.common.PluginState
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
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public void enablePlugin(String name) throws AssertNotNullException, PluginIllegalStateChangeException {
        updatePluginState(name, PluginState.ENABLED, Plugin::onEnable);
    }

    /**
     * @Description 将所有能更新到 {@link PluginState#ENABLED} 的插件更新到 {@link PluginState#ENABLED} 状态
     * @Author gdrfgdrf
     * @Date 2024/5/18
     */
    public void enableAllPlugin() throws AssertNotNullException {
        updateAllPluginState(PluginState.ENABLED, Plugin::onEnable);
    }

    /**
     * @Description 加载插件
     * @param name
	 *        插件名
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public void loadPlugin(String name) throws AssertNotNullException, PluginIllegalStateChangeException {
        updatePluginState(name, PluginState.LOADED, Plugin::onLoad);
    }

    /**
     * @Description 将所有能更新到 {@link PluginState#LOADED} 的插件更新到 {@link PluginState#LOADED} 状态
     * @Author gdrfgdrf
     * @Date 2024/5/18
     */
    public void loadAllPlugin() throws AssertNotNullException {
        updateAllPluginState(PluginState.LOADED, Plugin::onLoad);
    }

    /**
     * @Description 停止插件
     * @param name
	 *        插件名
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public void stopPlugin(String name) throws AssertNotNullException, PluginIllegalStateChangeException {
        updatePluginState(name, PluginState.STOPPED, Plugin::onStop);
    }

    /**
     * @Description 将所有能更新到 {@link PluginState#STOPPED} 的插件更新到 {@link PluginState#STOPPED} 状态
     * @Author gdrfgdrf
     * @Date 2024/5/18
     */
    public void stopAllPlugin() throws AssertNotNullException {
        updateAllPluginState(PluginState.STOPPED, Plugin::onStop);
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
    public void disablePlugin(String name) throws AssertNotNullException, PluginIllegalStateChangeException {
        updatePluginState(name, PluginState.DISABLED, Plugin::onDisable);
    }

    /**
     * @Description 将所有能更新到 {@link PluginState#DISABLED} 的插件更新到 {@link PluginState#DISABLED} 状态
     * @Author gdrfgdrf
     * @Date 2024/5/18
     */
    public void disableAllPlugin() throws AssertNotNullException {
        updateAllPluginState(PluginState.DISABLED, Plugin::onDisable);
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
     * @throws PluginIllegalStateChangeException
     *         异常的状态变化顺序
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    private void updatePluginState(
            String name,
            PluginState targetState,
            Consumer<Plugin> consumer
    ) throws AssertNotNullException, PluginIllegalStateChangeException {
        AssertUtils.notNull("plugin name", name);
        AssertUtils.notNull("target plugin state", targetState);
        AssertUtils.notNull("plugin main class method", consumer);

        Plugin plugin = getPlugin(name);
        AssertUtils.notNull("plugin main class instance", plugin);

        PluginState currentPluginState = PLUGIN_STATE_MAP.get(plugin);
        if (!currentPluginState.validate(targetState)) {
            throw new PluginIllegalStateChangeException(plugin, currentPluginState, targetState);
        }

        EventManager.getInstance().post(new PluginEvent.StateChange.Pre(
                plugin,
                targetState,
                currentPluginState
        ));

        consumer.accept(plugin);
        PLUGIN_STATE_MAP.put(plugin, targetState);

        EventManager.getInstance().post(new PluginEvent.StateChange.Post(
                plugin,
                targetState,
                currentPluginState
        ));
    }

    /**
     * @Description 更新全部能够更新状态的插件到指定状态，将会过滤出能过滤到指定状态的插件，并改变其状态
     * @param targetState
	 *        需要更新到的插件状态
	 * @param consumer
	 *        需要调用的插件主类的方法
     * @throws AssertNotNullException
     *         当 targetState 或 consumer 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/18
     */
    private void updateAllPluginState(
            PluginState targetState,
            Consumer<Plugin> consumer
    ) throws AssertNotNullException {
        AssertUtils.notNull("target plugin state", targetState);
        AssertUtils.notNull("plugin main class method", consumer);

        PLUGIN_MAP.forEach((name, plugin) -> {
            try {
                updatePluginState(name, targetState, consumer);
            } catch (Exception ignored) {

            }
        });
    }

    /**
     * @Description 获取插件
     * @param name
	 *        插件名
     * @return io.github.gdrfgdrf.cuteframework.api.base.Plugin
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
     * @return java.util.Map<java.lang.String,io.github.gdrfgdrf.cuteframework.api.base.Plugin>
     *         插件映射表
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public Map<String, Plugin> getPlugins() {
        return PLUGIN_MAP;
    }

    /**
     * @Description 获取所有插件状态
     * @return java.util.Map<io.github.gdrfgdrf.cuteframework.api.base.Plugin,io.github.gdrfgdrf.cuteframework.api.common.PluginState>
     *         插件状态映射表
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public Map<Plugin, PluginState> getPluginStates() {
        return PLUGIN_STATE_MAP;
    }
}

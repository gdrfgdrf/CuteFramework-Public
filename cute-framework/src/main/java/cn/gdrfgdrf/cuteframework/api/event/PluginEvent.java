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

package cn.gdrfgdrf.cuteframework.api.event;

import cn.gdrfgdrf.cuteframework.api.base.Plugin;
import cn.gdrfgdrf.cuteframework.api.common.PluginState;
import cn.gdrfgdrf.cuteframework.api.exception.PluginLoadException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 插件事件
 * @Author gdrfgdrf
 * @Date 2024/5/5
 */
@Getter
@AllArgsConstructor
public abstract class PluginEvent {
    /**
     * 插件主类实例
     */
    private final Plugin plugin;

    /**
     * @Description 插件主类加载错误事件，此时 {@link PluginEvent#plugin} 为 null，
     * 若已经成功加载的插件想要接收该事件
     * 则需要手动的去调用 {@link cn.gdrfgdrf.cuteframework.event.EventManager#registerAsynchronous(Object)}，
     * 不建议对已经拥有 {@link cn.gdrfgdrf.cuteframework.event.annotation.EventListener} 注解的类
     * 去调用 {@link cn.gdrfgdrf.cuteframework.event.EventManager#registerAsynchronous(Object)}，
     * 这会导致发布一个事件但订阅者被调用两次，
     * 建议是新建一个没有 {@link cn.gdrfgdrf.cuteframework.event.annotation.EventListener} 注解的类
     * 去调用 {@link cn.gdrfgdrf.cuteframework.event.EventManager#registerAsynchronous(Object)}
     *
     * @Author gdrfgdrf
     * @Date 2024/5/18
     */
    @Getter
    public static class LoadError extends PluginEvent {
        /**
         * 插件主类加载异常实例
         */
        private final PluginLoadException exception;

        public LoadError(PluginLoadException exception) {
            super(null);
            this.exception = exception;
        }
    }

    /**
     * @Description 插件被注册事件
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public static abstract class Registered extends PluginEvent {
        public Registered(Plugin plugin) {
            super(plugin);
        }

        /**
         * @Description 插件被注册前事件
         * @Author gdrfgdrf
         * @Date 2024/5/5
         */
        public static class Pre extends Registered {
            public Pre(Plugin plugin) {
                super(plugin);
            }
        }

        /**
         * @Description 插件被注册后事件
         * @Author gdrfgdrf
         * @Date 2024/5/5
         */
        public static class Post extends Registered {
            public Post(Plugin plugin) {
                super(plugin);
            }
        }
    }

    /**
     * @Description 插件被移除事件
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public static abstract class Unregistered extends PluginEvent {
        public Unregistered(Plugin plugin) {
            super(plugin);
        }

        /**
         * @Description 插件被移除前事件
         * @Author gdrfgdrf
         * @Date 2024/5/5
         */
        public static class Pre extends Unregistered {
            public Pre(Plugin plugin) {
                super(plugin);
            }
        }

        /**
         * @Description 插件被移除后事件
         * @Author gdrfgdrf
         * @Date 2024/5/5
         */
        public static class Post extends Unregistered {
            public Post(Plugin plugin) {
                super(plugin);
            }
        }
    }

    /**
     * @Description 插件状态变化事件
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    @Getter
    public static abstract class StateChange extends PluginEvent {
        /**
         * 插件需要变化到的状态
         */
        private final PluginState targetPluginState;
        /**
         * 插件变化前的状态
         */
        private final PluginState previousPluginState;

        public StateChange(Plugin plugin, PluginState targetPluginState, PluginState previousPluginState) {
            super(plugin);
            this.targetPluginState = targetPluginState;
            this.previousPluginState = previousPluginState;
        }

        /**
         * @Description 插件状态变化前事件
         * @Author gdrfgdrf
         * @Date 2024/5/5
         */
        public static class Pre extends StateChange {
            public Pre(Plugin plugin, PluginState targetPluginState, PluginState previousState) {
                super(plugin, targetPluginState, previousState);
            }
        }

        /**
         * @Description 插件状态变化后事件
         * @Author gdrfgdrf
         * @Date 2024/5/5
         */
        public static class Post extends StateChange {
            public Post(Plugin plugin, PluginState targetPluginState, PluginState previousState) {
                super(plugin, targetPluginState, previousState);
            }
        }
    }
}

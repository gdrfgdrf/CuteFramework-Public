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

package io.github.gdrfgdrf.cuteframework.api.event;

import io.github.gdrfgdrf.cuteframework.api.base.Plugin;
import io.github.gdrfgdrf.cuteframework.api.common.PluginState;
import io.github.gdrfgdrf.cuteframework.api.exception.PluginLoadException;
import io.github.gdrfgdrf.cuteframework.event.EventManager;
import io.github.gdrfgdrf.cuteframework.event.annotation.EventListener;
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
     * 则需要手动的去调用 {@link EventManager#registerAsynchronous(Object)}，
     * 不建议对已经拥有 {@link EventListener} 注解的类
     * 去调用 {@link EventManager#registerAsynchronous(Object)}，
     * 这会导致发布一个事件但订阅者被调用两次，
     * 建议是新建一个没有 {@link EventListener} 注解的类
     * 去调用 {@link EventManager#registerAsynchronous(Object)}
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

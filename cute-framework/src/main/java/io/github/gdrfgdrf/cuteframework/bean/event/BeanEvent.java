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

package io.github.gdrfgdrf.cuteframework.bean.event;

import io.github.gdrfgdrf.cuteframework.bean.BeanManager;
import lombok.Getter;

/**
 * @description Bean 事件
 * @author gdrfgdrf
 * @since 2024/5/4
 */
@Getter
public abstract class BeanEvent {
    /**
     * Bean 实例
     */
    public final Object bean;
    /**
     * Bean 名称
     */
    public final String beanName;

    public BeanEvent(Object bean, String beanName) {
        this.bean = bean;
        this.beanName = beanName;
    }

    /**
     * @description 单个 Bean 被加载事件
     * @author gdrfgdrf
     * @since 2024/5/4
     */
    public static abstract class Load extends BeanEvent {
        public Load(Object bean, String beanName) {
            super(bean, beanName);
        }

        /**
         * @description Bean 被加载前事件，此时 {@link BeanEvent#bean} 为空，{@link BeanEvent#beanName} 存在
         * @author gdrfgdrf
         * @since 2024/5/4
         */
        public static class Pre extends Load {
            public Pre(Object bean, String beanName) {
                super(null, beanName);
            }
        }

        /**
         * @description Bean 被加载后事件，此时 {@link BeanEvent#bean} 和 {@link BeanEvent#beanName} 均存在
         * @author gdrfgdrf
         * @since 2024/5/4
         */
        public static class Post extends Load {
            public Post(Object bean, String beanName) {
                super(bean, beanName);
            }
        }
    }

    /**
     * @description  Bean 全部被加载事件，
     * 此时 {@link BeanEvent#bean} 和 {@link BeanEvent#beanName} 都为空，该事件仅会被 {@link BeanManager#startCreating()} 发布
     *
     * @author gdrfgdrf
     * @since 2024/5/4
     */
    public static abstract class LoadAll extends BeanEvent {
        public LoadAll() {
            super(null, null);
        }

        /**
         * @description  Bean 全部被加载前事件
         * @author gdrfgdrf
         * @since 2024/5/4
         */
        public static class Pre extends LoadAll {}

        /**
         * @description  Bean 全部被加载后事件
         * @author gdrfgdrf
         * @since 2024/5/4
         */
        public static class Post extends LoadAll {}
    }
}

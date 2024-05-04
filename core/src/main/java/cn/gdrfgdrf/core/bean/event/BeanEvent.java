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

package cn.gdrfgdrf.core.bean.event;

import cn.gdrfgdrf.core.bean.BeanManager;
import cn.gdrfgdrf.core.event.base.Event;
import lombok.Getter;

/**
 * @Description Bean 事件
 * @Author gdrfgdrf
 * @Date 2024/5/4
 */
@Getter
public abstract class BeanEvent extends Event {
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
     * @Description 单个 Bean 被加载事件
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    public static abstract class Load extends BeanEvent {
        public Load(Object bean, String beanName) {
            super(bean, beanName);
        }

        /**
         * @Description Bean 被加载前事件，此时 Bean 实例为空，beanName 存在
         * @Author gdrfgdrf
         * @Date 2024/5/4
         */
        public static class Pre extends Load {
            public Pre(Object bean, String beanName) {
                super(null, beanName);
            }
        }

        /**
         * @Description Bean 被加载后事件，此时 Bean 和 beanName 均存在
         * @Author gdrfgdrf
         * @Date 2024/5/4
         */
        public static class Post extends Load {
            public Post(Object bean, String beanName) {
                super(bean, beanName);
            }
        }
    }

    /**
     * @Description  Bean 全部被加载事件，此时 Bean 和 beanName 都为空，该事件会被 {@link BeanManager#startCreating()} 发布
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    public static abstract class LoadAll extends BeanEvent {
        public LoadAll() {
            super(null, null);
        }

        /**
         * @Description  Bean 全部被加载前事件
         * @Author gdrfgdrf
         * @Date 2024/5/4
         */
        public static class Pre extends LoadAll {

        }

        /**
         * @Description  Bean 全部被加载后事件
         * @Author gdrfgdrf
         * @Date 2024/5/4
         */
        public static class Post extends LoadAll {

        }
    }
}

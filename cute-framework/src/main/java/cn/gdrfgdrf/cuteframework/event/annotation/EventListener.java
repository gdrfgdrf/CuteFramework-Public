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

package cn.gdrfgdrf.cuteframework.event.annotation;

import cn.gdrfgdrf.cuteframework.bean.annotation.Component;
import cn.gdrfgdrf.cuteframework.event.enums.SubscriberType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 标记一个类是异常监听类，被标记的类将会被作为 Bean 创建并自动注册到 {@link cn.gdrfgdrf.cuteframework.event.EventManager}
 * @Author gdrfgdrf
 * @Date 2024/5/6
 */
@Component
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EventListener {
    /**
     * @Description 注册类型，不同的注册类型会注册到不同的 EventBus，默认为同步类型
     * @return cn.gdrfgdrf.cuteframework.event.enums.SubscriberType
     *         注册类型
     * @Author gdrfgdrf
     * @Date 2024/5/18
     */
    SubscriberType type() default SubscriberType.SYNC;
}

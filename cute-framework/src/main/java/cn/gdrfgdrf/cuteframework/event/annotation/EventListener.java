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

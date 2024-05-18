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

package cn.gdrfgdrf.cuteframework.bean.annotation;

import java.lang.annotation.*;

/**
 * @Description 被注解的类将会被 {@link cn.gdrfgdrf.cuteframework.bean.BeanManager} 扫描到并作为 Bean 加载
 * @Author gdrfgdrf
 * @Date 2024/4/20
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Component {
    /**
     * @Description 定义 Bean 名称，为空则默认为类名
     * @return java.lang.String
     *         Bean 名称
     * @Author gdrfgdrf
     * @Date 2024/5/2
     */
    String name() default "";
}
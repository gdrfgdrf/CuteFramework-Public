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

package io.github.gdrfgdrf.cuteframework.bean.resolver.method.annotation;

import io.github.gdrfgdrf.cuteframework.bean.annotation.Component;
import io.github.gdrfgdrf.cuteframework.bean.annotation.Order;
import io.github.gdrfgdrf.cuteframework.bean.BeanManager;

import java.lang.annotation.*;

/**
 * @Description 标记一个类是 Bean 方法解析器，作为 Bean 方法解析器将会被 {@link BeanManager} 最先创建
 * @Author gdrfgdrf
 * @Date 2024/5/4
 */
@Order(1)
@Component
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BeanMethodResolverAnnotation {
    Class<? extends Annotation> targetMethodAnnotation();
}

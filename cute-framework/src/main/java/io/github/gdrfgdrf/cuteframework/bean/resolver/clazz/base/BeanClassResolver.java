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

package io.github.gdrfgdrf.cuteframework.bean.resolver.clazz.base;

import io.github.gdrfgdrf.cuteframework.bean.resolver.clazz.annotation.BeanClassResolverAnnotation;
import io.github.gdrfgdrf.cuteframework.bean.BeanManager;

/**
 * Bean 类解析器，该类必须拥有 {@link BeanClassResolverAnnotation}
 * 才能被 {@link BeanManager} 识别到，
 * 运行时将会传递所有拥有 {@link BeanClassResolverAnnotation#targetClassAnnotation()} 注解的 Bean 实例进来
 *
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
public interface BeanClassResolver {
    /**
     * 所有拥有 {@link BeanClassResolverAnnotation#targetClassAnnotation()} 注解的 Bean 实例将会被传递进来
      * @param bean
	 *         Bean 实例
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    void resolve(Object bean) throws Exception;
}

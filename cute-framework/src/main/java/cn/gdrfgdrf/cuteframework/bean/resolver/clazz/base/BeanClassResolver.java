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

package cn.gdrfgdrf.cuteframework.bean.resolver.clazz.base;

import cn.gdrfgdrf.cuteframework.bean.resolver.clazz.annotation.BeanClassResolverAnnotation;

/**
 * @Description Bean 类解析器，该类必须拥有 {@link cn.gdrfgdrf.cuteframework.bean.resolver.clazz.annotation.BeanClassResolverAnnotation}
 * 才能被 {@link cn.gdrfgdrf.cuteframework.bean.BeanManager} 识别到，
 * 运行时将会传递所有拥有 {@link BeanClassResolverAnnotation#targetClassAnnotation()} 注解的 Bean 实例进来
 *
 * @Author gdrfgdrf
 * @Date 2024/5/6
 */
public interface BeanClassResolver {
    /**
     * @Description 所有拥有 {@link BeanClassResolverAnnotation#targetClassAnnotation()} 注解的 Bean 实例将会被传递进来
      * @param bean
	 *         Bean 实例
     * @Author gdrfgdrf
     * @Date 2024/5/6
     */
    void resolve(Object bean) throws Exception;
}
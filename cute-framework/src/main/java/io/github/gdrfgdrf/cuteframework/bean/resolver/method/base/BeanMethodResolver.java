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

package io.github.gdrfgdrf.cuteframework.bean.resolver.method.base;

import io.github.gdrfgdrf.cuteframework.bean.BeanManager;
import io.github.gdrfgdrf.cuteframework.bean.resolver.BeanMethodResolverManager;
import io.github.gdrfgdrf.cuteframework.bean.resolver.method.exception.BeanMethodArgumentTypeMismatchException;
import io.github.gdrfgdrf.cuteframework.bean.resolver.method.annotation.BeanMethodResolverAnnotation;
import io.github.gdrfgdrf.cuteframework.exceptionhandler.ExceptionDispatcher;
import io.github.gdrfgdrf.cuteframework.exceptionhandler.annotation.ExceptionHandler;

import java.lang.reflect.Method;

/**
 * Bean 方法解析器，Bean 由 {@link BeanManager} 实例化完成后，
 * 将会使用 {@link BeanMethodResolverManager} 分发到指定的 {@link BeanMethodResolver}，
 * 该类可对 Bean 进行初始化，
 * 比如说获取所有带有 {@link ExceptionHandler} 注解的方法，
 * 并注册到 {@link ExceptionDispatcher}。
 * 建议该类只进行例如解析注解并注册到管理器之类的操作，不推荐进行设置 Bean 的字段之类的操作
 * 设置 Bean 字段的操作应该在构造函数内进行，也就是被 {@link BeanManager} 实例化时
 * 该类必须拥有 {@link BeanMethodResolverAnnotation}
 * 才能被 {@link BeanManager} 识别到
 *
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
public abstract class BeanMethodResolver {
    /**
     * 对 Bean 进行解析，
     * @param bean
	 *        Bean 实例
     * @param method
     *        需要解析的方法
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public abstract void resolve(Object bean, Method method) throws Exception;

    /**
     * 检查某个方法的参数是否是指定参数
     * @param method
	 *        需要检查的方法
	 * @param expectParameterTypes
	 *        指定参数
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public void checkMethodArgument(Method method, Class<?>... expectParameterTypes)
            throws BeanMethodArgumentTypeMismatchException
    {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != expectParameterTypes.length) {
            throw new BeanMethodArgumentTypeMismatchException(method, expectParameterTypes, method.getDeclaringClass());
        }

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            Class<?> expectedParameterType = expectParameterTypes[i];

            if (!expectedParameterType.isAssignableFrom(parameterType)) {
                throw new BeanMethodArgumentTypeMismatchException(
                        method,
                        expectParameterTypes,
                        method.getDeclaringClass()
                );
            }
        }
    }
}

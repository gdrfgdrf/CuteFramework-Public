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

package io.github.gdrfgdrf.cuteframework.exceptionhandler.resolver;

import io.github.gdrfgdrf.cuteframework.bean.resolver.method.annotation.BeanMethodResolverAnnotation;
import io.github.gdrfgdrf.cuteframework.bean.resolver.method.base.BeanMethodResolver;
import io.github.gdrfgdrf.cuteframework.exceptionhandler.ExceptionDispatcher;
import io.github.gdrfgdrf.cuteframework.exceptionhandler.annotation.ExceptionHandler;
import io.github.gdrfgdrf.cuteframework.exceptionhandler.exception.ExceptionHandleMethodIsNotStaticException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @description 解析所有 Bean 中拥有 {@link ExceptionHandler} 的所有方法，并将其注册到 {@link ExceptionDispatcher}
 * @author gdrfgdrf
 * @since 2024/5/4
 */
@BeanMethodResolverAnnotation(targetMethodAnnotation = ExceptionHandler.class)
public class ExceptionHandlerResolver extends BeanMethodResolver {
    @Override
    public void resolve(Object bean, Method method) throws Exception {
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new ExceptionHandleMethodIsNotStaticException(bean.getClass(), method);
        }
        checkMethodArgument(method, Thread.class, Throwable.class);

        ExceptionHandler exceptionHandler = method.getAnnotation(ExceptionHandler.class);
        Class<? extends Throwable>[] support = exceptionHandler.support();

        for (Class<? extends Throwable> throwableType : support) {
            ExceptionDispatcher.getInstance().registerExceptionHandler(throwableType, method);
        }
    }
}

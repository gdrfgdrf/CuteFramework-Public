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

package cn.gdrfgdrf.core.exceptionhandler.resolver;

import cn.gdrfgdrf.core.bean.resolver.annotation.BeanMethodResolverAnnotation;
import cn.gdrfgdrf.core.bean.resolver.base.BeanMethodResolver;
import cn.gdrfgdrf.core.exceptionhandler.ExceptionDispatcher;
import cn.gdrfgdrf.core.exceptionhandler.annotation.ExceptionHandler;
import cn.gdrfgdrf.core.exceptionhandler.exception.ExceptionHandleMethodIsNotStaticException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @Description 解析所有 Bean 中拥有 {@link ExceptionHandler} 的所有方法，并将其注册到 {@link ExceptionDispatcher}
 * @Author gdrfgdrf
 * @Date 2024/5/4
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

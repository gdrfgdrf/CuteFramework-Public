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

package cn.gdrfgdrf.cuteframework.bean.resolver.method.base;

import cn.gdrfgdrf.cuteframework.bean.resolver.BeanMethodResolverManager;
import cn.gdrfgdrf.cuteframework.bean.resolver.method.exception.BeanMethodArgumentTypeMismatchException;

import java.lang.reflect.Method;

/**
 * @Description Bean 方法解析器，Bean 由 {@link cn.gdrfgdrf.cuteframework.bean.BeanManager} 实例化完成后，
 * 将会使用 {@link BeanMethodResolverManager} 分发到指定的 {@link BeanMethodResolver}，
 * 该类可对 Bean 进行初始化，
 * 比如说获取所有带有 {@link cn.gdrfgdrf.cuteframework.exceptionhandler.annotation.ExceptionHandler} 注解的方法，
 * 并注册到 {@link cn.gdrfgdrf.cuteframework.exceptionhandler.ExceptionDispatcher}。
 * 建议该类只进行例如解析注解并注册到管理器之类的操作，不推荐进行设置 Bean 的字段之类的操作
 * 设置 Bean 字段的操作应该在构造函数内进行，也就是被 {@link cn.gdrfgdrf.cuteframework.bean.BeanManager} 实例化时
 * 该类必须拥有 {@link cn.gdrfgdrf.cuteframework.bean.resolver.method.annotation.BeanMethodResolverAnnotation}
 * 才能被 {@link cn.gdrfgdrf.cuteframework.bean.BeanManager} 识别到
 *
 * @Author gdrfgdrf
 * @Date 2024/4/29
 */
public abstract class BeanMethodResolver {
    /**
     * @Description 对 Bean 进行解析，
     * @param bean
	 *        Bean 实例
     * @param method
     *        需要解析的方法
     * @Author gdrfgdrf
     * @Date 2024/4/29
     */
    public abstract void resolve(Object bean, Method method) throws Exception;

    /**
     * @Description 检查某个方法的参数是否是指定参数
     * @param method
	 *        需要检查的方法
	 * @param expectParameterTypes
	 *        指定参数
     * @Author gdrfgdrf
     * @Date 2024/5/4
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

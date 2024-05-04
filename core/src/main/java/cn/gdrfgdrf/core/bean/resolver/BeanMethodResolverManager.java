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

package cn.gdrfgdrf.core.bean.resolver;

import cn.gdrfgdrf.core.bean.resolver.base.BeanMethodResolver;
import cn.gdrfgdrf.core.bean.resolver.exception.BeanMethodResolverException;
import cn.gdrfgdrf.core.utils.asserts.AssertUtils;
import cn.gdrfgdrf.core.utils.asserts.exception.AssertNotNullException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 管理 Bean 方法解析器，对其进行注册等操作，
 * Bean 由 {@link cn.gdrfgdrf.core.bean.BeanManager} 实例化完成后，
 * 将会使用该类分发 Bean 内的方法 到指定的 Bean 方法解析器进行解析
 *
 * @Author gdrfgdrf
 * @Date 2024/4/29
 */
public class BeanMethodResolverManager {
    private static BeanMethodResolverManager INSTANCE;

    /**
     * Bean 内方法所使用的注解类型到 Bean 方法解析器的映射
     */
    private final Map<Class<? extends Annotation>, BeanMethodResolver> BEAN_METHOD_RESOLVER_MAP = new ConcurrentHashMap<>();

    private BeanMethodResolverManager() {}

    /**
     * @Description 单例模式，获取 {@link BeanMethodResolverManager} 实例
     * @return cn.gdrfgdrf.smartuploader.bean.resolver.BeanResolverManager
     *         {@link BeanMethodResolverManager} 实例
     * @Author gdrfgdrf
     * @Date 2024/4/29
     */
    public static BeanMethodResolverManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BeanMethodResolverManager();
        }
        return INSTANCE;
    }

    /**
     * @Description 注册 Bean 解析器
     * @param beanAnnotationType
	 *        Bean 类型
	 * @param resolver
	 *        Bean 解析器
     * @throws AssertNotNullException
     *         当 beanAnnotationType 或 resolver 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/29
     */
    public void registerBeanResolver(Class<? extends Annotation> beanAnnotationType, BeanMethodResolver resolver) throws AssertNotNullException {
        AssertUtils.notNull("bean annotation type", beanAnnotationType);
        AssertUtils.notNull("bean resolver", resolver);
        BEAN_METHOD_RESOLVER_MAP.put(beanAnnotationType, resolver);
    }

    /**
     * @Description 移除 Bean 解析器
     * @param beanAnnotationType
	 *        Bean 类型
     * @throws AssertNotNullException
     *         当 beanAnnotationType 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/29
     */
    public void removeBeanResolver(Class<?> beanAnnotationType) throws AssertNotNullException {
        AssertUtils.notNull("bean annotation type", beanAnnotationType);
        BEAN_METHOD_RESOLVER_MAP.remove(beanAnnotationType);
    }

    /**
     * @Description 获取并调用对应的 Bean 方法解析器
     * @param bean
	 *        Bean 实例
     * @throws AssertNotNullException
     *         当 bean 为 null 时抛出
     * @throws BeanMethodResolverException
     *         当 Bean 方法解析器发生错误时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/29
     */
    public void resolve(Object bean) throws AssertNotNullException, BeanMethodResolverException {
        AssertUtils.notNull("bean", bean);

        Method[] methods = Arrays.stream(bean.getClass().getDeclaredMethods())
                .filter(method -> {
                    Annotation[] annotations = method.getAnnotations();
                    return Arrays.stream(annotations).anyMatch(annotation ->
                            BEAN_METHOD_RESOLVER_MAP.containsKey(annotation.annotationType()));
                })
                .toArray(Method[]::new);


        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();

            for (Annotation annotation : annotations) {
                BeanMethodResolver resolver = BEAN_METHOD_RESOLVER_MAP.get(annotation.annotationType());
                if (resolver == null) {
                    continue;
                }

                try {
                    resolver.resolve(bean, method);
                } catch (Exception e) {
                    throw new BeanMethodResolverException(bean, resolver, e);
                }
            }
        }
    }
}

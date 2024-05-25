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

package io.github.gdrfgdrf.cuteframework.bean.resolver;

import io.github.gdrfgdrf.cuteframework.bean.resolver.method.base.BeanMethodResolver;
import io.github.gdrfgdrf.cuteframework.bean.resolver.method.exception.BeanMethodResolverException;
import io.github.gdrfgdrf.cuteframework.utils.asserts.AssertUtils;
import io.github.gdrfgdrf.cuteframework.utils.asserts.exception.AssertNotNullException;
import io.github.gdrfgdrf.cuteframework.bean.BeanManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理 Bean 方法解析器，对其进行注册等操作，
 * Bean 由 {@link BeanManager} 实例化完成后，
 * 将会使用该类分发 Bean 内的方法 到指定的 Bean 方法解析器进行解析
 *
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
public class BeanMethodResolverManager {
    private static BeanMethodResolverManager INSTANCE;

    /**
     * Bean 内方法所使用的注解类型到 Bean 方法解析器的映射
     */
    private final Map<Class<? extends Annotation>, BeanMethodResolver> BEAN_METHOD_RESOLVER_MAP = new ConcurrentHashMap<>();

    private BeanMethodResolverManager() {}

    /**
     * 单例模式，获取 {@link BeanMethodResolverManager} 实例
     * @return io.github.gdrfgdrf.cuteframework.bean.resolver.BeanResolverManager
     *         {@link BeanMethodResolverManager} 实例
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public static BeanMethodResolverManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BeanMethodResolverManager();
        }
        return INSTANCE;
    }

    /**
     * 注册 Bean 方法解析器
     * @param beanAnnotationType
	 *        Bean 类型
	 * @param resolver
	 *        Bean 解析器
     * @throws AssertNotNullException
     *         当 beanAnnotationType 或 resolver 为 null 时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public void registerBeanMethodResolver(Class<? extends Annotation> beanAnnotationType, BeanMethodResolver resolver) throws AssertNotNullException {
        AssertUtils.notNull("bean method annotation type", beanAnnotationType);
        AssertUtils.notNull("bean method resolver", resolver);
        BEAN_METHOD_RESOLVER_MAP.put(beanAnnotationType, resolver);
    }

    /**
     * 移除 Bean 方法解析器
     * @param beanAnnotationType
	 *        Bean 类型
     * @throws AssertNotNullException
     *         当 beanAnnotationType 为 null 时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public void removeBeanMethodResolver(Class<? extends Annotation> beanAnnotationType) throws AssertNotNullException {
        AssertUtils.notNull("bean method annotation type", beanAnnotationType);
        BEAN_METHOD_RESOLVER_MAP.remove(beanAnnotationType);
    }

    /**
     * 获取并调用对应的 Bean 方法解析器
     * @param bean
	 *        Bean 实例
     * @throws AssertNotNullException
     *         当 bean 为 null 时抛出
     * @throws BeanMethodResolverException
     *         当 Bean 方法解析器发生错误时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
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

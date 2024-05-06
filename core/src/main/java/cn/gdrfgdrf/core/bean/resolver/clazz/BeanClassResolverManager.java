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

package cn.gdrfgdrf.core.bean.resolver.clazz;

import cn.gdrfgdrf.core.bean.resolver.clazz.base.BeanClassResolver;
import cn.gdrfgdrf.core.bean.resolver.clazz.exception.BeanClassResolverException;
import cn.gdrfgdrf.core.utils.asserts.AssertUtils;
import cn.gdrfgdrf.core.utils.asserts.exception.AssertNotNullException;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 管理 Bean 类解析器，对其进行注册等操作
 * @Author gdrfgdrf
 * @Date 2024/5/6
 */
public class BeanClassResolverManager {
    private static BeanClassResolverManager INSTANCE;

    /**
     * Bean 类所使用的注解类型到 Bean 类解析器的映射
     */
    private final Map<Class<? extends Annotation>, BeanClassResolver> BEAN_CLASS_RESOLVER_MAP =
            new ConcurrentHashMap<>();

    private BeanClassResolverManager() {}

    /**
     * @Description 单例模式，获取 {@link BeanClassResolverManager} 实例
     * @return cn.gdrfgdrf.core.bean.resolver.clazz.BeanClassResolverManager
     *         {@link BeanClassResolverManager} 实例
     * @Author gdrfgdrf
     * @Date 2024/5/6
     */
    public static BeanClassResolverManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BeanClassResolverManager();
        }
        return INSTANCE;
    }

    /**
     * @Description 注册 Bean 类解析器
     * @param beanAnnotationType
     *        Bean 类型
     * @param resolver
     *        Bean 解析器
     * @throws AssertNotNullException
     *         当 beanAnnotationType 或 resolver 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/6
     */
    public void registerBeanClassResolver(Class<? extends Annotation> beanAnnotationType, BeanClassResolver resolver)
            throws AssertNotNullException
    {
        AssertUtils.notNull("bean class annotation type", beanAnnotationType);
        AssertUtils.notNull("bean class resolver", resolver);
        BEAN_CLASS_RESOLVER_MAP.put(beanAnnotationType, resolver);
    }

    /**
     * @Description 移除 Bean 类解析器
     * @param beanAnnotationType
     *        Bean 类型
     * @throws AssertNotNullException
     *         当 beanAnnotationType 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/6
     */
    public void removeBeanClassResolver(Class<? extends Annotation> beanAnnotationType) throws AssertNotNullException {
        AssertUtils.notNull("bean class annotation type", beanAnnotationType);
        BEAN_CLASS_RESOLVER_MAP.remove(beanAnnotationType);
    }

    /**
     * @Description 获取并调用对应的 Bean 类解析器
     * @param bean
     *        Bean 实例
     * @throws AssertNotNullException
     *         当 bean 为 null 时抛出
     * @throws BeanClassResolverException
     *         当 Bean 类解析器发生错误时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/6
     */
    public void resolve(Object bean) throws AssertNotNullException, BeanClassResolverException {
        AssertUtils.notNull("bean", bean);

        Annotation[] annotations = bean.getClass().getAnnotations();
        for (Annotation annotation : annotations) {
            BeanClassResolver resolver = BEAN_CLASS_RESOLVER_MAP.get(annotation.annotationType());
            if (resolver == null) {
                continue;
            }

            try {
                resolver.resolve(bean);
            } catch (Exception e) {
                throw new BeanClassResolverException(bean, resolver, e);
            }
        }
    }
}

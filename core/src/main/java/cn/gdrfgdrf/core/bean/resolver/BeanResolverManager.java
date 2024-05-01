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

import cn.gdrfgdrf.core.bean.resolver.base.BeanResolver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 管理 Bean 解析器，对其进行注册等操作，
 * Bean 由 {@link cn.gdrfgdrf.core.bean.BeanManager} 实例化完成后，
 * 将会使用该类分发到指定的 Bean 解析器进行解析
 *
 * @Author gdrfgdrf
 * @Date 2024/4/29
 */
public class BeanResolverManager {
    private static BeanResolverManager INSTANCE;

    /**
     * Bean 类型到 Bean 解析器的映射
     */
    private final Map<Class<?>, BeanResolver> BEAN_RESOLVER_MAP = new ConcurrentHashMap<>();

    private BeanResolverManager() {}

    /**
     * @Description 单例模式，获取 {@link BeanResolverManager} 实例
     * @return cn.gdrfgdrf.smartuploader.bean.resolver.BeanResolverManager
     *         {@link BeanResolverManager} 实例
     * @Author gdrfgdrf
     * @Date 2024/4/29
     */
    public static BeanResolverManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BeanResolverManager();
        }
        return INSTANCE;
    }

    /**
     * @Description 注册 Bean 解析器
     * @param beanType
	 *        Bean 类型
	 * @param resolver
	 *        Bean 解析器
     * @Author gdrfgdrf
     * @Date 2024/4/29
     */
    public void registerBeanResolver(Class<?> beanType, BeanResolver resolver) {
        BEAN_RESOLVER_MAP.put(beanType, resolver);
    }

    /**
     * @Description 移除 Bean 解析器
     * @param beanType
	 *        Bean 类型
     * @Author gdrfgdrf
     * @Date 2024/4/29
     */
    public void removeBeanResolver(Class<?> beanType) {
        BEAN_RESOLVER_MAP.remove(beanType);
    }

    /**
     * @Description 获取并调用对应的 Bean 解析器
     * @param bean
	 *        Bean 实例
     * @Author gdrfgdrf
     * @Date 2024/4/29
     */
    public void resolve(Object bean) {
        BeanResolver resolver = BEAN_RESOLVER_MAP.get(bean.getClass());
        if (resolver == null) {
            return;
        }

        resolver.resolve(bean);
    }
}

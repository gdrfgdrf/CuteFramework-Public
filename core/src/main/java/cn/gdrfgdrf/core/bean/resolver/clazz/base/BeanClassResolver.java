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

package cn.gdrfgdrf.core.bean.resolver.clazz.base;

import cn.gdrfgdrf.core.bean.resolver.clazz.annotation.BeanClassResolverAnnotation;

/**
 * @Description Bean 类解析器，该类必须拥有 {@link cn.gdrfgdrf.core.bean.resolver.clazz.annotation.BeanClassResolverAnnotation}
 * 才能被 {@link cn.gdrfgdrf.core.bean.BeanManager} 识别到，
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

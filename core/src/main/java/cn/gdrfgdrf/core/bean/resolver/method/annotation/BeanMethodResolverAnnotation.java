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

package cn.gdrfgdrf.core.bean.resolver.method.annotation;

import cn.gdrfgdrf.core.bean.annotation.Component;
import cn.gdrfgdrf.core.bean.annotation.Order;

import java.lang.annotation.*;

/**
 * @Description 标记一个类是 Bean 方法解析器，作为 Bean 方法解析器将会被 {@link cn.gdrfgdrf.core.bean.BeanManager} 最先创建
 * @Author gdrfgdrf
 * @Date 2024/5/4
 */
@Order(1)
@Component
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BeanMethodResolverAnnotation {
    Class<? extends Annotation> targetMethodAnnotation();
}

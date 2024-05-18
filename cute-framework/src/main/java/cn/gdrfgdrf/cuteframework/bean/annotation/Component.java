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

package cn.gdrfgdrf.cuteframework.bean.annotation;

import java.lang.annotation.*;

/**
 * @Description 被注解的类将会被 {@link cn.gdrfgdrf.cuteframework.bean.BeanManager} 扫描到并作为 Bean 加载
 * @Author gdrfgdrf
 * @Date 2024/4/20
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Component {
    /**
     * @Description 定义 Bean 名称，为空则默认为类名
     * @return java.lang.String
     *         Bean 名称
     * @Author gdrfgdrf
     * @Date 2024/5/2
     */
    String name() default "";
}

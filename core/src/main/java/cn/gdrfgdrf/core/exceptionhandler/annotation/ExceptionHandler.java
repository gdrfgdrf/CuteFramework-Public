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

package cn.gdrfgdrf.core.exceptionhandler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 标记某一个方法是异常处理方法，该方法的入参必须为 {@link Thread} 和 {@link Throwable}，并且必须为静态方法
 * @Author gdrfgdrf
 * @Date 2024/4/24
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ExceptionHandler {
    /**
     * @Description 支持的异常类型，
     * {@link cn.gdrfgdrf.core.exceptionhandler.ExceptionDispatcher} 将根据该值分发到对应的拥有该注解的方法
     *
     * @return java.lang.Class<? extends java.lang.Throwable>[]
     *         支持的异常类型
     * @Author gdrfgdrf
     * @Date 2024/4/7
     */
    Class<? extends Throwable>[] support();
}

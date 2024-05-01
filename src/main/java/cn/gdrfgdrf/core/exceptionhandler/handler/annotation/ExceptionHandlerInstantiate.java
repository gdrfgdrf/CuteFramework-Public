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

package cn.gdrfgdrf.core.exceptionhandler.handler.annotation;

import cn.gdrfgdrf.core.exceptionhandler.base.ExceptionHandler;

/**
 * @Description 对 {@link cn.gdrfgdrf.core.exceptionhandler.handler.manager.ExceptionHandlerManager} 进行的注解，
 * 提供所有 {@link cn.gdrfgdrf.core.exceptionhandler.base.ExceptionHandler} 的类给
 * {@link cn.gdrfgdrf.core.exceptionhandler.handler.manager.ExceptionHandlerManager} 进行实例化
 *
 * @Author gdrfgdrf
 * @Date 2024/4/17
 */
public @interface ExceptionHandlerInstantiate {
    /**
     * @Description 获取所有需要实例化的 {@link ExceptionHandler} 的类
     * @return java.lang.Class<? extends cn.gdrfgdrf.smartuploader.exceptionhandler.base.ExceptionHandler>[]
     *         所有 {@link ExceptionHandler} 的类
     * @Author gdrfgdrf
     * @Date 2024/4/17
     */
    Class<? extends ExceptionHandler>[] classes();
}

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

package cn.gdrfgdrf.smartuploader.exceptionhandler.handler;

import cn.gdrfgdrf.smartuploader.exceptionhandler.annotation.ExceptionSupport;
import cn.gdrfgdrf.smartuploader.exceptionhandler.base.ExceptionHandler;

/**
 * @Description 默认异常处理器，
 * 接受所有类型的异常，
 * 当 {@link cn.gdrfgdrf.smartuploader.exceptionhandler.ExceptionDispatcher} 找不到特定的异常处理器时将使用该类
 *
 * @Author gdrfgdrf
 * @Date 2024/4/8
 */
@ExceptionSupport(support = Throwable.class)
public class DefaultExceptionHandler implements ExceptionHandler {
    @Override
    public void handle(Thread thread, Throwable throwable) {

    }
}

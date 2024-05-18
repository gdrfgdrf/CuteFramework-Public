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

package cn.gdrfgdrf.cuteframework.exceptionhandler.handler;

import cn.gdrfgdrf.cuteframework.bean.annotation.Component;
import cn.gdrfgdrf.cuteframework.exceptionhandler.annotation.ExceptionHandler;
import cn.gdrfgdrf.cuteframework.exceptionhandler.base.CustomException;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 默认异常处理器
 * @Author gdrfgdrf
 * @Date 2024/4/8
 */
@Slf4j
@Component
public class DefaultExceptionHandler {
    /**
     * @Description 默认异常处理方法，
     * 接受所有类型的异常，
     * 当 {@link cn.gdrfgdrf.cuteframework.exceptionhandler.ExceptionDispatcher} 找不到定制的异常处理器时将使用该方法
     *
     * @param thread
	 *        异常所在线程
	 * @param throwable
	 *        异常实例
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    @ExceptionHandler(support = Throwable.class)
    public static void handle(Thread thread, Throwable throwable) {
        if (throwable instanceof CustomException customException) {
            log.error(customException.getMessage(), throwable);
            return;
        }

        log.error("Unknown error occurred", throwable);
    }
}

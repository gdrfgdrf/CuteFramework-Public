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

package cn.gdrfgdrf.cuteframework.event.exceptionhandler;

import cn.gdrfgdrf.cuteframework.bean.annotation.Component;
import cn.gdrfgdrf.cuteframework.event.exception.EventException;
import cn.gdrfgdrf.cuteframework.exceptionhandler.annotation.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 事件异常处理器
 * @Author gdrfgdrf
 * @Date 2024/4/24
 */
@Slf4j
@Component
public class DefaultEventExceptionHandler {
    /**
     * @Description 异常处理方法，当事件处理出现异常时，
     * 会被 {@link EventExceptionHandler} 捕获并分发到该方法
     *
     * @param thread
	 *        异常所在线程
	 * @param eventException
	 *        异常实例
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    @ExceptionHandler(support = EventException.class)
    public static void handle(Thread thread, EventException eventException) {
        log.error(eventException.getMessage(), eventException.getThrowable());
    }
}

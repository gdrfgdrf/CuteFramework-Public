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

import cn.gdrfgdrf.smartuploader.event.exception.EventException;
import cn.gdrfgdrf.smartuploader.exceptionhandler.annotation.ExceptionSupport;
import cn.gdrfgdrf.smartuploader.exceptionhandler.base.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 事件异常处理器，当事件处理出现异常时，会被 {@link cn.gdrfgdrf.smartuploader.event.EventExceptionHandler} 捕获
 * 并分发到该类
 *
 * @Author gdrfgdrf
 * @Date 2024/4/24
 */
@Slf4j
@ExceptionSupport(support = EventException.class)
public class DefaultEventExceptionHandler implements ExceptionHandler {
    @Override
    public void handle(Thread thread, Throwable throwable) {
        EventException eventException = (EventException) throwable;
        log.error(eventException.getMessage(), eventException.getThrowable());
    }
}

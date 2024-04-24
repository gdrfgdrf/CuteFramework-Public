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

package cn.gdrfgdrf.smartuploader.event;

import cn.gdrfgdrf.smartuploader.event.exception.EventException;
import cn.gdrfgdrf.smartuploader.exceptionhandler.ExceptionDispatcher;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

/**
 * @Description 事件处理时出现错误会被该类捕获并提供给 {@link cn.gdrfgdrf.smartuploader.exceptionhandler.ExceptionDispatcher} 进行分发
 * @Author gdrfgdrf
 * @Date 2024/4/24
 */
public class EventExceptionHandler implements SubscriberExceptionHandler {
    @Override
    public void handleException(Throwable exception, SubscriberExceptionContext context) {
        EventException eventException = new EventException(exception, context);
        ExceptionDispatcher.getInstance().dispatch(Thread.currentThread(), eventException);
    }
}

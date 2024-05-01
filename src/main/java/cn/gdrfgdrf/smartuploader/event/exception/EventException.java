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

package cn.gdrfgdrf.smartuploader.event.exception;

import cn.gdrfgdrf.smartuploader.exceptionhandler.base.CustomRuntimeException;
import cn.gdrfgdrf.smartuploader.locale.collect.ExceptionLanguage;
import com.google.common.eventbus.SubscriberExceptionContext;
import lombok.Getter;

/**
 * @Description 当事件处理时发生异常，将由 {@link cn.gdrfgdrf.smartuploader.event.EventExceptionHandler} 捕获
 * 并使用该类对异常实例进行包装提供给 {@link cn.gdrfgdrf.smartuploader.exceptionhandler.ExceptionDispatcher} 进行分发
 * @Author gdrfgdrf
 * @Date 2024/4/24
 */
@Getter
public class EventException extends CustomRuntimeException {
    private final Throwable throwable;
    private final SubscriberExceptionContext context;

    public EventException(Throwable throwable, SubscriberExceptionContext context) {
        this.throwable = throwable;
        this.context = context;
    }

    @Override
    public String getI18NMessage() {
        return ExceptionLanguage.EVENT_PROCESSING_EXCEPTION
                .get()
                .format(context.getEvent().getClass().getSimpleName())
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "An exception occurred while processing event " + context.getClass().getSimpleName();
    }
}

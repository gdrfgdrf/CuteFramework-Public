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

package cn.gdrfgdrf.core.event.exception;

import cn.gdrfgdrf.core.event.exceptionhandler.EventExceptionHandler;
import cn.gdrfgdrf.core.exceptionhandler.base.CustomException;
import cn.gdrfgdrf.core.locale.collect.ExceptionLanguage;
import com.google.common.eventbus.SubscriberExceptionContext;
import lombok.Getter;

/**
 * @Description 当事件处理时发生异常，将由 {@link EventExceptionHandler} 捕获
 * 并使用该类对异常实例进行包装提供给 {@link cn.gdrfgdrf.core.exceptionhandler.ExceptionDispatcher} 进行分发
 * @Author gdrfgdrf
 * @Date 2024/4/24
 */
@Getter
public class EventException extends CustomException {
    /**
     * 事件处理时抛出的异常实例
     */
    private final Throwable throwable;
    /**
     * 事件订阅者上下文
     */
    private final SubscriberExceptionContext context;

    public EventException(Throwable throwable, SubscriberExceptionContext context) {
        this.throwable = throwable;
        this.context = context;
    }

    @Override
    public String getI18NMessage() {
        return ExceptionLanguage.EVENT_PROCESSING_ERROR
                .get()
                .format(context.getEvent().getClass().getName(), throwable.getMessage())
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "Error occurred while processing " +
                context.getEvent().getClass().getName() +
                " event: " +
                throwable.getMessage();
    }
}

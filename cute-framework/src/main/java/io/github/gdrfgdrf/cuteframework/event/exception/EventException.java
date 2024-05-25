/*
 * Copyright 2024 CuteFramework's Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.gdrfgdrf.cuteframework.event.exception;

import io.github.gdrfgdrf.cuteframework.event.exceptionhandler.EventExceptionHandler;
import io.github.gdrfgdrf.cuteframework.exceptionhandler.base.CustomException;
import io.github.gdrfgdrf.cuteframework.locale.collect.ExceptionLanguage;
import com.google.common.eventbus.SubscriberExceptionContext;
import io.github.gdrfgdrf.cuteframework.exceptionhandler.ExceptionDispatcher;
import lombok.Getter;

/**
 * @description 当事件处理时发生异常，将由 {@link EventExceptionHandler} 捕获
 * 并使用该类对异常实例进行包装提供给 {@link ExceptionDispatcher} 进行分发
 * @author gdrfgdrf
 * @since 2024/4/24
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

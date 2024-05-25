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

package io.github.gdrfgdrf.cuteframework.event.exceptionhandler;

import io.github.gdrfgdrf.cuteframework.event.exception.EventException;
import io.github.gdrfgdrf.cuteframework.exceptionhandler.ExceptionDispatcher;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * @Description 事件处理时出现错误会被该类捕获并提供给 {@link ExceptionDispatcher} 进行分发
 * @Author gdrfgdrf
 * @Date 2024/4/24
 */
@Slf4j
public class EventExceptionHandler implements SubscriberExceptionHandler {
    private static EventExceptionHandler INSTANCE;

    private EventExceptionHandler() {}

    /**
     * @Description 单例模式，获取 {@link EventExceptionHandler} 实例
     * @return io.github.gdrfgdrf.cuteframework.event.exceptionhandler.EventExceptionHandler
     *         {@link EventExceptionHandler} 实例
     * @Author gdrfgdrf
     * @Date 2024/5/18
     */
    public static EventExceptionHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventExceptionHandler();
        }
        return INSTANCE;
    }

    @Override
    public void handleException(@NotNull Throwable exception, @NotNull SubscriberExceptionContext context) {
        EventException eventException = new EventException(exception, context);
        ExceptionDispatcher.getInstance().dispatchSafety(Thread.currentThread(), eventException);
    }
}

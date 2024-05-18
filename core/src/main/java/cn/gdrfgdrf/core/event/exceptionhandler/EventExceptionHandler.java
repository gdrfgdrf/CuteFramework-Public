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

package cn.gdrfgdrf.core.event.exceptionhandler;

import cn.gdrfgdrf.core.event.exception.EventException;
import cn.gdrfgdrf.core.exceptionhandler.ExceptionDispatcher;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * @Description 事件处理时出现错误会被该类捕获并提供给 {@link cn.gdrfgdrf.core.exceptionhandler.ExceptionDispatcher} 进行分发
 * @Author gdrfgdrf
 * @Date 2024/4/24
 */
@Slf4j
public class EventExceptionHandler implements SubscriberExceptionHandler {
    private static EventExceptionHandler INSTANCE;

    private EventExceptionHandler() {}

    /**
     * @Description 单例模式，获取 {@link EventExceptionHandler} 实例
     * @return cn.gdrfgdrf.core.event.exceptionhandler.EventExceptionHandler
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

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

package io.github.gdrfgdrf.cuteframework.exceptionhandler.event;

import io.github.gdrfgdrf.cuteframework.exceptionhandler.ExceptionDispatcher;
import io.github.gdrfgdrf.cuteframework.exceptionhandler.annotation.Undispatchable;
import lombok.Getter;

/**
 * 异常事件
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
@Getter
public abstract class ExceptionEvent {
    /**
     * 异常所在线程
     */
    private final Thread thread;
    /**
     * 异常实例
     */
    private final Throwable throwable;

    public ExceptionEvent(Thread thread, Throwable throwable) {
        this.thread = thread;
        this.throwable = throwable;
    }

    /**
     * 无法被分发的异常被抛出，
     * 所有拥有的 {@link Undispatchable} 注解的异常类
     * 都不会被 {@link ExceptionDispatcher} 分发，
     * 但是会发布该事件说明有无法被分发的异常被抛出，
     *
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public static class UndispatchableExceptionThrownEvent extends ExceptionEvent {
        public UndispatchableExceptionThrownEvent(Thread thread, Throwable throwable) {
            super(thread, throwable);
        }
    }
}

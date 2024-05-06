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

package cn.gdrfgdrf.core.exceptionhandler.event;

import lombok.Getter;

/**
 * @Description 异常事件
 * @Author gdrfgdrf
 * @Date 2024/5/6
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
     * @Description 无法被分发的异常被抛出，
     * 所有拥有的 {@link cn.gdrfgdrf.core.exceptionhandler.annotation.Undispatchable} 注解的异常类
     * 都不会被 {@link cn.gdrfgdrf.core.exceptionhandler.ExceptionDispatcher} 分发，
     * 但是会发布该事件说明有无法被分发的异常被抛出，
     *
     * @Author gdrfgdrf
     * @Date 2024/5/6
     */
    public static class UndispatchableExceptionThrownEvent extends ExceptionEvent {
        public UndispatchableExceptionThrownEvent(Thread thread, Throwable throwable) {
            super(thread, throwable);
        }
    }
}

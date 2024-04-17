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

package cn.gdrfgdrf.smartuploader.exceptionhandler.base;

/**
 * @Description 异常处理器，
 * 若程序发生没有没捕获的异常，
 * 将会被 {@link cn.gdrfgdrf.smartuploader.exceptionhandler.GlobalUncaughtExceptionHandler} 捕获，
 * 并提供给 {@link cn.gdrfgdrf.smartuploader.exceptionhandler.ExceptionDispatcher} 分发到指定的 {@link ExceptionHandler}，
 * 该类必须拥有一个 {@link cn.gdrfgdrf.smartuploader.exceptionhandler.annotation.ExceptionSupport} 注解
 *
 * @Author gdrfgdrf
 * @Date 2024/4/7
 */
public interface ExceptionHandler {
    /**
     * @Description 处理异常，由 {@link cn.gdrfgdrf.smartuploader.exceptionhandler.ExceptionDispatcher} 调用
     * @param thread
	 *        异常所在的线程
	 * @param throwable
	 *        异常实例
     * @Author gdrfgdrf
     * @Date 2024/4/7
     */
    void handle(Thread thread, Throwable throwable);
}

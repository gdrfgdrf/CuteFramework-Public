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

package cn.gdrfgdrf.core.exceptionhandler;

import cn.gdrfgdrf.core.bean.annotation.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 全局异常捕获器，当异常没有被 try ... catch 捕获时，将会被该捕获器捕获
 * @Author gdrfgdrf
 * @Date 2024/4/7
 */
@Slf4j
@Component
public class GlobalUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    public GlobalUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * @Description 发送给 {@link ExceptionDispatcher} 以分发异常到异常处理方法
     * @param t
	 *        异常所在线程
	 * @param e
	 *        异常实例
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        ExceptionDispatcher.getInstance().dispatchSafety(t, e);
    }
}

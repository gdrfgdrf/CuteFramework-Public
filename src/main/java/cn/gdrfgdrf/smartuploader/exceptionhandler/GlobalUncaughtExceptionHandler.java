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

package cn.gdrfgdrf.smartuploader.exceptionhandler;

import cn.gdrfgdrf.smartuploader.bean.annotation.Component;

/**
 * @Description 全局异常捕获器，当异常没有被 try ... catch 捕获时，将会被该捕获器捕获
 * @Author gdrfgdrf
 * @Date 2024/4/7
 */
@Component
public class GlobalUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    public GlobalUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        ExceptionDispatcher.getInstance().dispatch(t, e);
    }
}

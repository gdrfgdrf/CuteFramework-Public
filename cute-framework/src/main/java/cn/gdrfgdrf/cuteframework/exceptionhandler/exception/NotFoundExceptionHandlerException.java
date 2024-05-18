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

package cn.gdrfgdrf.cuteframework.exceptionhandler.exception;

import cn.gdrfgdrf.cuteframework.exceptionhandler.annotation.Undispatchable;
import cn.gdrfgdrf.cuteframework.exceptionhandler.base.CustomException;
import cn.gdrfgdrf.cuteframework.locale.collect.ExceptionLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 无法获取到异常处理器时抛出，
 * 该异常不会被 {@link cn.gdrfgdrf.cuteframework.exceptionhandler.ExceptionDispatcher} 分发
 *
 * @Author gdrfgdrf
 * @Date 2024/4/8
 */
@Getter
@Undispatchable
@AllArgsConstructor
public class NotFoundExceptionHandlerException extends CustomException {
    private final Throwable throwable;

    @Override
    public String getI18NMessage() {
        return ExceptionLanguage.NOT_FOUND_EXCEPTION_HANDLER
                .get()
                .format(throwable.getClass().getName())
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "Not found exception handler by exception type " + throwable.getClass().getName();
    }
}

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

package io.github.gdrfgdrf.cuteframework.exceptionhandler.exception;

import io.github.gdrfgdrf.cuteframework.exceptionhandler.annotation.Undispatchable;
import io.github.gdrfgdrf.cuteframework.exceptionhandler.base.CustomException;
import io.github.gdrfgdrf.cuteframework.locale.collect.ExceptionLanguage;
import io.github.gdrfgdrf.cuteframework.exceptionhandler.ExceptionDispatcher;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 无法获取到异常处理器时抛出，
 * 该异常不会被 {@link ExceptionDispatcher} 分发
 *
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
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

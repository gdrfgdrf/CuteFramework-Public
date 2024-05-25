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

package io.github.gdrfgdrf.cuteframework.exceptionhandler.handler;

import io.github.gdrfgdrf.cuteframework.bean.annotation.Component;
import io.github.gdrfgdrf.cuteframework.exceptionhandler.annotation.ExceptionHandler;
import io.github.gdrfgdrf.cuteframework.exceptionhandler.base.CustomException;
import io.github.gdrfgdrf.cuteframework.exceptionhandler.ExceptionDispatcher;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认异常处理器
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
@Slf4j
@Component
public class DefaultExceptionHandler {
    /**
     * 默认异常处理方法，
     * 接受所有类型的异常，
     * 当 {@link ExceptionDispatcher} 找不到定制的异常处理器时将使用该方法
     *
     * @param thread
	 *        异常所在线程
	 * @param throwable
	 *        异常实例
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    @ExceptionHandler(support = Throwable.class)
    public static void handle(Thread thread, Throwable throwable) {
        if (throwable instanceof CustomException customException) {
            log.error(customException.getMessage(), throwable);
            return;
        }

        log.error("Unknown error occurred", throwable);
    }
}

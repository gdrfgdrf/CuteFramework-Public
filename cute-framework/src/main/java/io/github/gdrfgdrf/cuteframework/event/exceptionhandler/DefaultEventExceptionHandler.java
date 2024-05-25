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

import io.github.gdrfgdrf.cuteframework.bean.annotation.Component;
import io.github.gdrfgdrf.cuteframework.event.exception.EventException;
import io.github.gdrfgdrf.cuteframework.exceptionhandler.annotation.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @description 事件异常处理器
 * @author gdrfgdrf
 * @since 2024/4/24
 */
@Slf4j
@Component
public class DefaultEventExceptionHandler {
    /**
     * @description 异常处理方法，当事件处理出现异常时，
     * 会被 {@link EventExceptionHandler} 捕获并分发到该方法
     *
     * @param thread
	 *        异常所在线程
	 * @param eventException
	 *        异常实例
     * @author gdrfgdrf
     * @since 2024/5/4
     */
    @ExceptionHandler(support = EventException.class)
    public static void handle(Thread thread, EventException eventException) {
        log.error(eventException.getMessage(), eventException.getThrowable());
    }
}

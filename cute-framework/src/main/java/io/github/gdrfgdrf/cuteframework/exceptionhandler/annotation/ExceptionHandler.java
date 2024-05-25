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

package io.github.gdrfgdrf.cuteframework.exceptionhandler.annotation;

import io.github.gdrfgdrf.cuteframework.exceptionhandler.ExceptionDispatcher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记某一个方法是异常处理方法，该方法的入参必须为 {@link Thread} 和 具体的异常类，并且必须为静态方法
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ExceptionHandler {
    /**
     * 支持的异常类型，
     * {@link ExceptionDispatcher} 将根据该值分发到对应的拥有该注解的方法
     *
     * @return java.lang.Class<? extends java.lang.Throwable>[]
     *         支持的异常类型
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    Class<? extends Throwable>[] support();
}

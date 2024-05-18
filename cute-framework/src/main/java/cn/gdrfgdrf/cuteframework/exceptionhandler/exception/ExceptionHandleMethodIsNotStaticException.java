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

package cn.gdrfgdrf.cuteframework.exceptionhandler.exception;

import cn.gdrfgdrf.cuteframework.exceptionhandler.annotation.Undispatchable;
import cn.gdrfgdrf.cuteframework.exceptionhandler.base.CustomException;
import cn.gdrfgdrf.cuteframework.locale.collect.ExceptionLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;

/**
 * @Description 当异常处理方法不是静态时抛出
 * @Author gdrfgdrf
 * @Date 2024/5/4
 */
@Getter
@Undispatchable
@AllArgsConstructor
public class ExceptionHandleMethodIsNotStaticException extends CustomException {
    /**
     * 异常处理方法所在类
     */
    private final Class<?> clazz;
    /**
     * 异常处理方法
     */
    private final Method exceptionHandleMethod;

    @Override
    public String getI18NMessage() {
        return ExceptionLanguage.EXCEPTION_HANDLE_METHOD_IS_NOT_STATIC
                .get()
                .format(exceptionHandleMethod.getName(), clazz.getName())
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "Exception handle method " +
                exceptionHandleMethod.getName() +
                " is not a static method, the class " +
                clazz.getSimpleName();
    }
}

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

package io.github.gdrfgdrf.cuteframework.bean.resolver.method.exception;

import io.github.gdrfgdrf.cuteframework.exceptionhandler.base.CustomException;
import io.github.gdrfgdrf.cuteframework.locale.collect.ExceptionLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @description 当方法参数和需要参数不相同时抛出
 * @author gdrfgdrf
 * @since 2024/5/4
 */
@Getter
@AllArgsConstructor
public class BeanMethodArgumentTypeMismatchException extends CustomException {
    /**
     * Bean 方法
     */
    private final Method method;
    /**
     * 需要的参数
     */
    private final Class<?>[] need;
    /**
     * Bean 方法所在类
     */
    private final Class<?> clazz;

    @Override
    public String getI18NMessage() {
        return ExceptionLanguage.BEAN_METHOD_ARGUMENT_TYPE_MISMATCH
                .get()
                .format(method, Arrays.toString(need), clazz.getName())
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "The parameter of the bean method " +
                method.getName() +
                " must be " +
                Arrays.toString(need) +
                ", the class " +
                clazz.getName();
    }
}

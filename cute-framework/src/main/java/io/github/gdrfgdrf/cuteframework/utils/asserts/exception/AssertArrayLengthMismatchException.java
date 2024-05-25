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

package io.github.gdrfgdrf.cuteframework.utils.asserts.exception;

import io.github.gdrfgdrf.cuteframework.exceptionhandler.base.CustomException;
import io.github.gdrfgdrf.cuteframework.locale.collect.AssertLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 当数组长度与需要的不匹配是抛出
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
@Getter
@AllArgsConstructor
public class AssertArrayLengthMismatchException extends CustomException {
    /**
     * 数组名
     */
    private final String parameterName;
    /**
     * 需要的长度
     */
    private final int length;


    @Override
    public String getI18NMessage() {
        return AssertLanguage.ARRAY_MIN
                .get()
                .format(parameterName, length)
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "The length of the array " + parameterName + " must be greater than or equal to " + length;
    }
}

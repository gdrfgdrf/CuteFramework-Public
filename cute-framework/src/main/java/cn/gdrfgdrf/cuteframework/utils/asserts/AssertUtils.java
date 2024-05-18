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

package cn.gdrfgdrf.cuteframework.utils.asserts;

import cn.gdrfgdrf.cuteframework.utils.asserts.base.AssertErrorException;
import cn.gdrfgdrf.cuteframework.utils.asserts.exception.AssertArrayLengthMismatchException;
import cn.gdrfgdrf.cuteframework.utils.asserts.exception.AssertNotNullException;

import java.lang.reflect.Array;

/**
 * @Description 断言工具类，断言失败时抛出 {@link AssertErrorException}
 * @Author gdrfgdrf
 * @Date 2024/4/8
 */
public class AssertUtils {
    private AssertUtils() {}

    public static <T extends Throwable> void expression(boolean expression, T throwable) throws T {
        if (!expression) {
            throw throwable;
        }
    }

    public static void notNull(String parameterName, Object o) throws AssertNotNullException {
        if (o == null) {
            throw new AssertNotNullException(parameterName);
        }
    }

    public static void arrayMin(String parameterName, Object array, int length) throws AssertArrayLengthMismatchException {
        if (Array.getLength(array) < length) {
            throw new AssertArrayLengthMismatchException(parameterName, length);
        }
    }
}

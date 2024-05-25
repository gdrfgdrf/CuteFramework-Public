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

package io.github.gdrfgdrf.cuteframework.utils.asserts;

import io.github.gdrfgdrf.cuteframework.utils.asserts.base.AssertErrorException;
import io.github.gdrfgdrf.cuteframework.utils.asserts.exception.AssertArrayLengthMismatchException;
import io.github.gdrfgdrf.cuteframework.utils.asserts.exception.AssertNotNullException;

import java.lang.reflect.Array;

/**
 * @description 断言工具类，断言失败时抛出 {@link AssertErrorException}
 * @author gdrfgdrf
 * @since 2024/4/8
 */
public class AssertUtils {
    private AssertUtils() {}

    /**
     * @description 表达式是否满足，若不满足则抛出指定的错误
     * @param expression
	 *        表达式
	 * @param throwable
	 *        不满足抛出的错误
     * @throws T
     *         不满足抛出的错误
     * @author gdrfgdrf
     * @since 2024/5/25
     */
    public static <T extends Throwable> void expression(boolean expression, T throwable) throws T {
        if (!expression) {
            throw throwable;
        }
    }

    /**
     * @description 某个实例是否为 null，为 null 则抛出 {@link AssertNotNullException}
     * @param parameterName
	 *        实例名
	 * @param o
	 *        实例
     * @throws AssertNotNullException
     *         实例为 null 时抛出
     * @author gdrfgdrf
     * @since 2024/5/25
     */
    public static void notNull(String parameterName, Object o) throws AssertNotNullException {
        if (o == null) {
            throw new AssertNotNullException(parameterName);
        }
    }

    /**
     * @description 检查数组的长度是否大于等于某个数，若小于某个数则抛出 {@link AssertArrayLengthMismatchException}
     * @param parameterName
	 *        数组名
	 * @param array
	 *        数组实例
	 * @param length
	 *        需要的长度
     * @throws AssertArrayLengthMismatchException
     *         数组小于指定的长度时抛出
     * @author gdrfgdrf
     * @since 2024/5/25
     */
    public static void arrayMin(String parameterName, Object array, int length) throws AssertArrayLengthMismatchException {
        if (Array.getLength(array) < length) {
            throw new AssertArrayLengthMismatchException(parameterName, length);
        }
    }
}

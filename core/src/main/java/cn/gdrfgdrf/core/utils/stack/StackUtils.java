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

package cn.gdrfgdrf.core.utils.stack;

import cn.gdrfgdrf.core.utils.StringUtils;
import cn.gdrfgdrf.core.utils.asserts.AssertUtils;
import cn.gdrfgdrf.core.utils.asserts.exception.AssertArrayLengthMismatchException;
import cn.gdrfgdrf.core.utils.asserts.exception.AssertNotNullException;
import cn.gdrfgdrf.core.utils.stack.common.MethodInformation;
import cn.gdrfgdrf.core.utils.stack.exception.StackIllegalArgumentException;
import cn.gdrfgdrf.core.utils.stack.exception.StackIllegalOperationException;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Description 堆栈工具类
 * @Author gdrfgdrf
 * @Date 2024/4/30
 */
public class StackUtils {
    /**
     * 相对堆栈深度，正确使用时可以直接指向需要被保护的方法
     */
    public static final int RELATIVE_STACK_DEPTH = 4;

    /**
     * @Description 仅允许某个方法执行某个方法
     * @param allowedMethod
     *        允许执行操作的方法
     * @throws AssertNotNullException
     *         当 allowedMethod 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/30
     */
    public static void onlyMethod(Method allowedMethod) throws
            AssertNotNullException,
            StackIllegalOperationException,
            StackIllegalArgumentException
    {
        AssertUtils.notNull("be allowed method", allowedMethod);
        onlyCheckerInternal(
                allowedMethod.getDeclaringClass().getName(),
                allowedMethod.getName(),
                1,
                0
        );
    }

    /**
     * @Description 仅允许某些方法执行某个方法
     * @param allowedMethodArray
	 *        允许执行操作的方法数组
     * @throws AssertNotNullException
     *         allowedMethodArray 为 null 时抛出
     * @throws AssertArrayLengthMismatchException
     *         数组 allowedMethodArray 的长度小于 1 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    public static void onlyMethod(Method[] allowedMethodArray) throws
            AssertNotNullException,
            AssertArrayLengthMismatchException,
            StackIllegalOperationException,
            StackIllegalArgumentException
    {
        AssertUtils.notNull("be allowed method", allowedMethodArray);
        AssertUtils.arrayMin("be allowed method", allowedMethodArray, 1);

        String[] allowedClassNameArray = Arrays.stream(allowedMethodArray)
                .map(method -> method.getDeclaringClass().getName())
                .toArray(String[]::new);
        String[] allowedMethodNameArray = Arrays.stream(allowedMethodArray)
                .map(Method::getName)
                .toArray(String[]::new);

        onlyCheckerInternal(allowedClassNameArray, allowedMethodNameArray);
    }

    /**
     * @Description 仅允许某个方法执行某个方法
     * @param allowedClass
	 *        允许执行操作的方法所在的类
	 * @param allowedMethodName
	 *        允许执行操作的方法的方法名
     * @throws AssertNotNullException
     *         当 allowedClass 或 allowedMethodName 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/30
     */
    public static void onlyMethod(Class<?> allowedClass, String allowedMethodName) throws
            AssertNotNullException,
            StackIllegalOperationException,
            StackIllegalArgumentException
    {
        AssertUtils.notNull("be allowed class", allowedClass);
        AssertUtils.notNull("be allowed method name", allowedMethodName);
        onlyCheckerInternal(allowedClass.getName(), allowedMethodName, 1, 0);
    }

    /**
     * @Description 仅允许某些方法执行某个方法
     * @param allowedClassArray
     *        允许执行操作的方法所在的类的数组
     * @param allowedMethodNameArray
     *        允许执行操作的方法的方法名的数组
     * @throws AssertNotNullException
     *         数组 allowedClassArray 或 数组 allowedMethodNameArray 为 null 时抛出
     * @throws AssertArrayLengthMismatchException
     *         数组 allowedClassArray 或 数组 allowedMethodNameArray 的长度小于 1 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    public static void onlyMethod(Class<?>[] allowedClassArray, String[] allowedMethodNameArray) throws
            AssertNotNullException,
            AssertArrayLengthMismatchException,
            StackIllegalOperationException,
            StackIllegalArgumentException
    {
        AssertUtils.notNull("be allowed class array", allowedClassArray);
        AssertUtils.notNull("be allowed method name array", allowedMethodNameArray);
        AssertUtils.arrayMin("be allowed class", allowedClassArray, 1);
        AssertUtils.arrayMin("be allowed method name", allowedMethodNameArray, 1);

        String[] allowedClassNameArray = Arrays.stream(allowedClassArray)
                .map(Class::getName)
                .toArray(String[]::new);

        onlyCheckerInternal(allowedClassNameArray, allowedMethodNameArray);
    }

    /**
     * @Description 仅允许某个方法执行某个方法
     * @param allowedClassName
     *        允许执行操作的方法所在的类的类名
     * @param allowedMethodName
     *        允许执行操作的方法的方法名
     * @throws AssertNotNullException
     *         当 allowedClassName 或 allowedMethodName 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/30
     */
    public static void onlyMethod(String allowedClassName, String allowedMethodName) throws
            AssertNotNullException,
            StackIllegalOperationException,
            StackIllegalArgumentException
    {
        AssertUtils.notNull("be allowed class name", allowedClassName);
        AssertUtils.notNull("be allowed method name", allowedMethodName);
        onlyCheckerInternal(allowedClassName, allowedMethodName, 1, 0);
    }

    /**
     * @Description 仅允许某些方法执行某个方法
     * @param allowedClassNameArray
     *        允许执行操作的方法所在的类的类名的数组
     * @param allowedMethodNameArray
     *        允许执行操作的方法的方法名的数组
     * @throws AssertNotNullException
     *         当 数组 allowedClassNameArray 或 数组 allowedMethodNameArray 为 null 时抛出
     * @throws AssertArrayLengthMismatchException
     *         当 数组 allowedClassNameArray 或 数组 allowedMethodNameArray 的长度小于 1 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/30
     */
    public static void onlyMethod(String[] allowedClassNameArray, String[] allowedMethodNameArray) throws
            AssertNotNullException,
            AssertArrayLengthMismatchException,
            StackIllegalOperationException,
            StackIllegalArgumentException
    {
        AssertUtils.notNull("be allowed class name array", allowedClassNameArray);
        AssertUtils.notNull("be allowed method name array", allowedMethodNameArray);
        AssertUtils.arrayMin("be allowed class name", allowedClassNameArray, 1);
        AssertUtils.arrayMin("be allowed method name", allowedMethodNameArray, 1);

        onlyCheckerInternal(allowedClassNameArray, allowedMethodNameArray);
    }

    /**
     * @Description 仅允许某个类执行某个方法
     * @param allowedClass
     *        允许执行操作的类
     * @throws AssertNotNullException
     *         当 allowedClass 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/30
     */
    public static void onlyClass(Class<?> allowedClass) throws
            AssertNotNullException,
            StackIllegalOperationException,
            StackIllegalArgumentException
    {
        AssertUtils.notNull("be allowed class", allowedClass);
        onlyCheckerInternal(allowedClass.getName(), null, 1, 0);
    }

    /**
     * @Description 仅允许某些类执行某个方法
     * @param allowedClassArray
     *        允许执行操作的类的数组
     * @throws AssertNotNullException
     *         当 数组 allowedClassArray 为 null 时抛出
     * @throws AssertArrayLengthMismatchException
     *         当 数组 allowedClassArray 的长度小于 1 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    public static void onlyClass(Class<?>[] allowedClassArray) throws
            AssertNotNullException,
            AssertArrayLengthMismatchException,
            StackIllegalOperationException,
            StackIllegalArgumentException
    {
        AssertUtils.notNull("be allowed class array", allowedClassArray);
        AssertUtils.arrayMin("be allowed class", allowedClassArray, 1);

        String[] allowedClassNameArray = Arrays.stream(allowedClassArray)
                .map(Class::getName)
                .toArray(String[]::new);

        onlyCheckerInternal(allowedClassNameArray, null);
    }

    /**
     * @Description 仅允许某个类执行某个方法
     * @param allowedClassName
	 *        允许执行操作的类的类名
     * @throws AssertNotNullException
     *         当 allowedClassName 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/30
     */
    public static void onlyClass(String allowedClassName) throws
            AssertNotNullException,
            StackIllegalOperationException,
            StackIllegalArgumentException
    {
        AssertUtils.notNull("be allowed class name", allowedClassName);
        onlyCheckerInternal(allowedClassName, null, 1, 0);
    }

    /**
     * @Description 仅允许某些类执行某个方法
     * @param allowedClassNameArray
	 *        允许执行操作的类的类名的数组
     * @throws AssertNotNullException
     *         当 allowedClassNameArray 为 null 时抛出
     * @throws AssertArrayLengthMismatchException
     *         数组 allowedClassNameArray 的长度小于 1 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    public static void onlyClass(String[] allowedClassNameArray) throws
            AssertNotNullException,
            AssertArrayLengthMismatchException,
            StackIllegalOperationException,
            StackIllegalArgumentException
    {
        AssertUtils.notNull("be allowed class name array", allowedClassNameArray);
        AssertUtils.arrayMin("be allowed class name", allowedClassNameArray, 1);

        onlyCheckerInternal(allowedClassNameArray, null);
    }

    /**
     * @Description 对数组进行检测，
     * 两个数组一一对应，一一对应的调用 {@link StackUtils#onlyCheckerInternal(String, String, int, int)}，
     * 若有一对数组通过则不直接返回，若全部数组均不通过则抛出异常
     *
     * @param allowedClassNameArray
	 *        允许执行操作的类的类名的数组
	 * @param allowedMethodNameArray
	 *        允许执行操作的方法的方法名的数组
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    private static void onlyCheckerInternal(String[] allowedClassNameArray, String[] allowedMethodNameArray) throws
            StackIllegalOperationException,
            StackIllegalArgumentException
    {
        Exception exception = null;
        boolean pass = false;

        for (int i = 0; i < allowedClassNameArray.length; i++) {
            String allowedClassName = allowedClassNameArray[i];
            String allowedMethodName = null;

            if (allowedMethodNameArray != null && allowedMethodNameArray.length >= i) {
                allowedMethodName = allowedMethodNameArray[i];
            }

            try {
                onlyCheckerInternal(allowedClassName, allowedMethodName, 2, 1);
                pass = true;
                break;
            } catch (StackIllegalOperationException | StackIllegalArgumentException e) {
                exception = e;
            }
        }

        if (!pass) {
            if (exception instanceof StackIllegalOperationException stackIllegalOperationException) {
                throw stackIllegalOperationException;
            }
            if (exception instanceof StackIllegalArgumentException stackIllegalArgumentException) {
                throw stackIllegalArgumentException;
            }
        }
    }

    /**
     * @Description 检测器内部实现，优先对比类名，若类名为空则对比方法名，
     * 若两者都为空则抛出 {@link StackIllegalArgumentException}，
     * 将会对比以下事物：
     * 调用方所在类的类名 和 被允许的类的类名，
     * 调用方的方法名 和 被允许的方法名。
     * 类名对比成功下一步将会对比方法名，
     * 类名对比失败则直接抛出 {@link StackIllegalOperationException}，
     * 方法名对比失败也直接抛出 {@link StackIllegalOperationException}
     *
     * @param allowedClassName
	 *        允许执行操作的类的类名
	 * @param allowedMethodName
	 *        允许执行操作的方法的方法名
     * @param callerOffset
     *        获取被保护的方法的调用者的偏移量
     * @param protectOffset
     *        获取被保护的方法的偏移量
     * @throws StackIllegalOperationException
     *         检测到不被允许的调用方时抛出
     * @throws StackIllegalArgumentException
     *         当 allowedClassName 和 allowedMethodName 同时为空时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/30
     */
    private static void onlyCheckerInternal(
            String allowedClassName,
            String allowedMethodName,
            int callerOffset,
            int protectOffset
    ) throws
            StackIllegalOperationException,
            StackIllegalArgumentException
    {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = stackTrace[RELATIVE_STACK_DEPTH];

        MethodInformation callerMethodInformation = parseStack(RELATIVE_STACK_DEPTH + callerOffset);
        MethodInformation protectMethodInformation = parseStack(RELATIVE_STACK_DEPTH + protectOffset);

        if (!StringUtils.isBlank(allowedClassName)) {
            if (!allowedClassName.equals(callerMethodInformation.getClassName())) {
                throw new StackIllegalOperationException(
                        callerMethodInformation.getClassName(),
                        callerMethodInformation.getMethodName(),
                        protectMethodInformation.getClassName(),
                        protectMethodInformation.getMethodName()
                );
            }

            if (StringUtils.isBlank(allowedMethodName)) {
                return;
            }
        }
        if (!StringUtils.isBlank(allowedMethodName)) {
            if (!allowedMethodName.equals(callerMethodInformation.getMethodName()))  {
                throw new StackIllegalOperationException(
                        callerMethodInformation.getClassName(),
                        callerMethodInformation.getMethodName(),
                        protectMethodInformation.getClassName(),
                        protectMethodInformation.getMethodName()
                );
            }
            return;
        }

        throw new StackIllegalArgumentException(
                protectMethodInformation.getClassName(),
                protectMethodInformation.getMethodName()
        );
    }

    /**
     * @Description 解析堆栈，获取指定深度的方法信息，深度包括该方法
     * @return cn.gdrfgdrf.core.utils.stack.common.CallerInformation
     *         调用方信息集合
     * @Author gdrfgdrf
     * @Date 2024/4/30
     */
    public static MethodInformation parseStack(int relativeStackDepth) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = stackTrace[relativeStackDepth];

        return new MethodInformation(
                stackTraceElement.getClassName(),
                stackTraceElement.getMethodName()
        );
    }
}

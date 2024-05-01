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

package cn.gdrfgdrf.smartuploader.utils.stack;

import cn.gdrfgdrf.smartuploader.utils.StringUtils;
import cn.gdrfgdrf.smartuploader.utils.asserts.AssertUtils;
import cn.gdrfgdrf.smartuploader.utils.stack.common.MethodInformation;
import cn.gdrfgdrf.smartuploader.utils.stack.exception.StackIllegalArgumentException;
import cn.gdrfgdrf.smartuploader.utils.stack.exception.StackIllegalOperationException;

import java.lang.reflect.Method;

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
     * @throws cn.gdrfgdrf.smartuploader.utils.stack.exception.StackIllegalOperationException
     *         不被允许的调用方操作时抛出
     * @throws cn.gdrfgdrf.smartuploader.utils.asserts.exception.AssertNotNullException
     *         当 allowedMethod 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/30
     */
    public static void onlyMethod(Method allowedMethod) {
        AssertUtils.notNull("be allowed method", allowedMethod);
        onlyCheckerInternal(allowedMethod.getDeclaringClass().getName(), allowedMethod.getName());
    }

    /**
     * @Description 仅允许某个方法执行某个方法
     * @param allowedClass
	 *        允许执行操作的方法所在的类
	 * @param allowedMethodName
	 *        允许执行操作的方法的方法名
     * @throws cn.gdrfgdrf.smartuploader.utils.stack.exception.StackIllegalOperationException
     *         不被允许的调用方操作时抛出
     * @throws cn.gdrfgdrf.smartuploader.utils.asserts.exception.AssertNotNullException
     *         当 allowedClass 或 allowedMethodName 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/30
     */
    public static void onlyMethod(Class<?> allowedClass, String allowedMethodName) {
        AssertUtils.notNull("be allowed class", allowedClass);
        AssertUtils.notNull("be allowed method name", allowedMethodName);
        onlyCheckerInternal(allowedClass.getName(), allowedMethodName);
    }

    /**
     * @Description 仅允许某个方法执行某个方法
     * @param allowedClassName
     *        允许执行操作的方法所在的类的类名
     * @param allowedMethodName
     *        允许执行操作的方法的方法名
     * @throws cn.gdrfgdrf.smartuploader.utils.stack.exception.StackIllegalOperationException
     *         不被允许的调用方操作时抛出
     * @throws cn.gdrfgdrf.smartuploader.utils.asserts.exception.AssertNotNullException
     *         当 allowedClassName 或 allowedMethodName 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/30
     */
    public static void onlyMethod(String allowedClassName, String allowedMethodName) {
        AssertUtils.notNull("be allowed class name", allowedClassName);
        AssertUtils.notNull("be allowed method name", allowedMethodName);
        onlyCheckerInternal(allowedClassName, allowedMethodName);
    }

    /**
     * @Description 仅允许某个类执行某个方法
     * @param allowedClass
     *        允许执行操作的类
     * @throws cn.gdrfgdrf.smartuploader.utils.stack.exception.StackIllegalOperationException
     *         检测到不被允许的调用方时抛出
     * @throws cn.gdrfgdrf.smartuploader.utils.asserts.exception.AssertNotNullException
     *         当 allowedClass 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/30
     */
    public static void onlyClass(Class<?> allowedClass) {
        AssertUtils.notNull("be allowed class", allowedClass);
        onlyCheckerInternal(allowedClass.getName(), null);
    }

    /**
     * @Description 仅允许某个类执行某个方法
     * @param allowedClassName
	 *        允许执行操作的类的类名
     * @throws cn.gdrfgdrf.smartuploader.utils.stack.exception.StackIllegalOperationException
     *         检测到不被允许的调用方时抛出
     * @throws cn.gdrfgdrf.smartuploader.utils.asserts.exception.AssertNotNullException
     *         当 allowedClassName 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/30
     */
    public static void onlyClass(String allowedClassName) {
        AssertUtils.notNull("be allowed class name", allowedClassName);
        onlyCheckerInternal(allowedClassName, null);
    }

    /**
     * @Description 检测器内部实现，优先对比类名，若类名为空则对比方法名，
     * 若两者都为空则抛出 {@link StackIllegalArgumentException}，
     * 将会对比以下事物：
     * 调用方所在类的类名 和 被允许的类的类名，
     * 调用方的方法名 和 被允许的方法名。
     * 类名对比成功下一步将会对比方法名，
     * 类名对比失败则直接抛出 {@link cn.gdrfgdrf.smartuploader.utils.stack.exception.StackIllegalOperationException}，
     * 方法名对比失败也直接抛出 {@link cn.gdrfgdrf.smartuploader.utils.stack.exception.StackIllegalOperationException}
     *
     * @param allowedClassName
	 *        允许执行操作的类的类名
	 * @param allowedMethodName
	 *        允许执行操作的方法的方法名
     * @throws cn.gdrfgdrf.smartuploader.utils.stack.exception.StackIllegalOperationException
     *         检测到不被允许的调用方时抛出
     * @throws StackIllegalArgumentException
     *         当 allowedClassName 和 allowedMethodName 同时为空时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/30
     */
    private static void onlyCheckerInternal(String allowedClassName, String allowedMethodName) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = stackTrace[RELATIVE_STACK_DEPTH];

        MethodInformation callerMethodInformation = parseStack(RELATIVE_STACK_DEPTH + 1);
        MethodInformation protectMethodInformation = parseStack(RELATIVE_STACK_DEPTH);

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
     * @return cn.gdrfgdrf.smartuploader.utils.stack.common.CallerInformation
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

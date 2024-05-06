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

package cn.gdrfgdrf.core.exceptionhandler;

import cn.gdrfgdrf.core.event.EventManager;
import cn.gdrfgdrf.core.exceptionhandler.annotation.Undispatchable;
import cn.gdrfgdrf.core.exceptionhandler.event.ExceptionEvent;
import cn.gdrfgdrf.core.exceptionhandler.exception.NotFoundExceptionHandlerException;
import cn.gdrfgdrf.core.utils.ClassUtils;
import cn.gdrfgdrf.core.utils.asserts.AssertUtils;
import cn.gdrfgdrf.core.utils.asserts.exception.AssertNotNullException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Description 异常分发器
 * @Author gdrfgdrf
 * @Date 2024/4/7
 */
@Slf4j
public class ExceptionDispatcher {
    private static ExceptionDispatcher INSTANCE;

    /**
     * 异常类型到异常处理方法的映射，
     * {@link ExceptionDispatcher#dispatch(Thread, Throwable)} 将根据该映射分发到对应的异常处理方法
     */
    private final Map<Class<? extends Throwable>, List<Method>> EXCEPTION_HANDLER_MAP = new ConcurrentHashMap<>();

    private ExceptionDispatcher() {}

    /**
     * @Description 单例模式，获取 {@link ExceptionDispatcher} 实例
     * @return cn.gdrfgdrf.core.exceptionhandler.ExceptionDispatcher
     *         {@link ExceptionDispatcher} 实例
     * @Author gdrfgdrf
     * @Date 2024/4/7
     */
    public static ExceptionDispatcher getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ExceptionDispatcher();
        }
        return INSTANCE;
    }

    /**
     * @Description 注册异常处理方法，会根据异常类型添加到对应的 {@link ExceptionDispatcher#EXCEPTION_HANDLER_MAP}
     * @param throwableType
	 *        异常类型
	 * @param exceptionHandleMethod
	 *        异常处理方法
     * @throws cn.gdrfgdrf.core.utils.asserts.exception.AssertNotNullException
     *         当 throwableType 或 exceptionHandleMethod 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/8
     */
    public void registerExceptionHandler(Class<? extends Throwable> throwableType, Method exceptionHandleMethod)
            throws AssertNotNullException
    {
        AssertUtils.notNull("exception type", throwableType);
        AssertUtils.notNull("exception handle method", exceptionHandleMethod);

        List<Method> exceptionHandlers = EXCEPTION_HANDLER_MAP.computeIfAbsent(
                throwableType,
                clazz -> new CopyOnWriteArrayList<>()
        );
        exceptionHandlers.add(exceptionHandleMethod);
    }

    /**
     * @Description 移除移除处理器，会根据异常类型从 {@link ExceptionDispatcher#EXCEPTION_HANDLER_MAP} 中异常对应的异常处理方法
     * @param throwableType
     *        异常类型
     * @param exceptionHandleMethod
     *        异常处理方法
     * @throws cn.gdrfgdrf.core.utils.asserts.exception.AssertNotNullException
     *         当 throwableType 或 exceptionHandler 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/17
     */
    public void unregisterExceptionHandler(Class<? extends Throwable> throwableType, Method exceptionHandleMethod)
            throws AssertNotNullException
    {
        AssertUtils.notNull("exception type", throwableType);
        AssertUtils.notNull("exception handle method", exceptionHandleMethod);

        if (!EXCEPTION_HANDLER_MAP.containsKey(throwableType)) {
            return;
        }
        List<Method> exceptionHandlers = EXCEPTION_HANDLER_MAP.get(throwableType);
        exceptionHandlers.remove(exceptionHandleMethod);

        if (exceptionHandlers.isEmpty()) {
            EXCEPTION_HANDLER_MAP.remove(throwableType);
        }
    }

    /**
     * @Description 根据索引移除移除处理器，
     * 会根据异常类型从 {@link ExceptionDispatcher#EXCEPTION_HANDLER_MAP} 中移除对应的异常处理方法
     * @param throwableType
     *        异常类型
     * @param index
     *        异常处理方法在 {@link ExceptionDispatcher#EXCEPTION_HANDLER_MAP} 的值中的序号
     * @throws cn.gdrfgdrf.core.utils.asserts.exception.AssertNotNullException
     *         当 throwableType 或 exceptionHandler 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/17
     */
    public void unregisterExceptionHandler(Class<? extends Throwable> throwableType, int index)
            throws AssertNotNullException
    {
        AssertUtils.notNull("exception type", throwableType);
        AssertUtils.notNull("exception handler index", index);

        if (!EXCEPTION_HANDLER_MAP.containsKey(throwableType)) {
            return;
        }
        List<Method> exceptionHandlers = EXCEPTION_HANDLER_MAP.get(throwableType);
        exceptionHandlers.remove(index);

        if (exceptionHandlers.isEmpty()) {
            EXCEPTION_HANDLER_MAP.remove(throwableType);
        }
    }

    /**
     * @Description 分发异常，
     * 将根据异常类型从 {@link ExceptionDispatcher#EXCEPTION_HANDLER_MAP} 中获取并调用异常处理方法，
     * 若无法获取到定制异常处理方法，
     * 将会使用 {@link cn.gdrfgdrf.core.exceptionhandler.handler.DefaultExceptionHandler} 的 handle 方法，
     * 若无法获取到 {@link cn.gdrfgdrf.core.exceptionhandler.handler.DefaultExceptionHandler} 的 handle 方法，
     * 将会抛出 {@link cn.gdrfgdrf.core.exceptionhandler.exception.NotFoundExceptionHandlerException}
     * 当异常类拥有 {@link Undispatchable} 注解时，该方法不会生效
     *
     * @param thread
	 *        异常所处在的线程
	 * @param throwable
	 *        异常实例
     * @throws cn.gdrfgdrf.core.utils.asserts.exception.AssertNotNullException
     *         当 thread 或 throwable 为 null 时抛出
     * @throws cn.gdrfgdrf.core.exceptionhandler.exception.NotFoundExceptionHandlerException
     *         无法获取定制异常处理方法以及默认异常处理方法时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/8
     */
    public void dispatch(Thread thread, Throwable throwable) throws
            AssertNotNullException,
            NotFoundExceptionHandlerException
    {
        AssertUtils.notNull("exception thread", thread);
        AssertUtils.notNull("throwable instance", throwable);

        if (throwable.getClass().isAnnotationPresent(Undispatchable.class)) {
            EventManager.getInstance().post(new ExceptionEvent.UndispatchableExceptionThrownEvent(thread, throwable));
            return;
        }

        List<Method> exceptionHandlers = EXCEPTION_HANDLER_MAP.get(throwable.getClass());
        if (exceptionHandlers == null || exceptionHandlers.isEmpty()) {
            exceptionHandlers = EXCEPTION_HANDLER_MAP.get(Throwable.class);
            if (exceptionHandlers == null || exceptionHandlers.isEmpty()) {
                throw new NotFoundExceptionHandlerException(throwable);
            }
        }

        for (Method exceptionHandleMethod : exceptionHandlers) {
            ClassUtils.safetyInvoke(null, exceptionHandleMethod, thread, throwable);
        }
    }

    /**
     * @Description 安全的分发异常到异常处理方法，该方法不会抛出错误，
     * 当 {@link ExceptionDispatcher#dispatch(Thread, Throwable)} 抛出一般错误时将会输出一段日志，
     * 当 {@link ExceptionDispatcher#dispatch(Thread, Throwable)} 抛出了 {@link NotFoundExceptionHandlerException} 时
     * 将会在此对其进行一次分发，但由于该异常类被 {@link Undispatchable} 所注解，
     * 所以将会发布 {@link cn.gdrfgdrf.core.exceptionhandler.event.ExceptionEvent.UndispatchableExceptionThrownEvent} 事件
     *
     * @param thread
	 *        异常所在线程
	 * @param throwable
	 *        异常实例
     * @Author gdrfgdrf
     * @Date 2024/5/6
     */
    public void dispatchSafety(Thread thread, Throwable throwable) {
        try {
            dispatch(thread, throwable);
        } catch (NotFoundExceptionHandlerException dispatcherException) {
            dispatchSafety(thread, dispatcherException);
        } catch (Exception e) {
            log.error("An exception occurred in the exception dispatcher", e);
        }
    }

    /**
     * @Description 获取异常类型到异常处理方法的映射
     * @return java.util.Map<java.lang.Class<? extends java.lang.Throwable>,java.util.List<java.lang.reflect.Method>>
     *         异常类型到异常处理方法的映射
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    public Map<Class<? extends Throwable>, List<Method>> getExceptionHandlerMap() {
         return EXCEPTION_HANDLER_MAP;
    }


}

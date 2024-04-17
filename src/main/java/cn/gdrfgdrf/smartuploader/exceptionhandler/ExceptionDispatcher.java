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

package cn.gdrfgdrf.smartuploader.exceptionhandler;

import cn.gdrfgdrf.smartuploader.exceptionhandler.annotation.Undispatchable;
import cn.gdrfgdrf.smartuploader.exceptionhandler.base.ExceptionHandler;
import cn.gdrfgdrf.smartuploader.exceptionhandler.exception.NotFoundExceptionHandlerException;
import cn.gdrfgdrf.smartuploader.utils.asserts.AssertUtils;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Description 异常分发器
 * @Author gdrfgdrf
 * @Date 2024/4/7
 */
public class ExceptionDispatcher {
    private static ExceptionDispatcher INSTANCE;

    /**
     * 异常类型到异常处理器的映射，
     * {@link ExceptionDispatcher#dispatch(Thread, Throwable)} 将根据该映射分发到对应的 {@link ExceptionHandler}
     */
    private final Map<Class<? extends Throwable>, List<ExceptionHandler>> EXCEPTION_HANDLER_MAP = new ConcurrentHashMap<>();

    private ExceptionDispatcher() {}

    /**
     * @Description 单例模式，获取 {@link ExceptionDispatcher} 实例
     * @return cn.gdrfgdrf.smartuploader.exceptionhandler.ExceptionDispatcher
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
     * @Description 注册异常处理器，会根据异常类型添加到对应的 {@link ExceptionDispatcher#EXCEPTION_HANDLER_MAP}
     * @param throwableType
	 *        异常类型
	 * @param exceptionHandler
	 *        异常处理器实例
     * @throws cn.gdrfgdrf.smartuploader.utils.asserts.exception.AssertNotNullException
     *         当 throwableType 或 exceptionHandler 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/8
     */
    public void registerExceptionHandler(Class<? extends Throwable> throwableType, ExceptionHandler exceptionHandler) {
        AssertUtils.notNull("exception type", throwableType);
        AssertUtils.notNull("exception handler", exceptionHandler);

        List<ExceptionHandler> exceptionHandlers = EXCEPTION_HANDLER_MAP.computeIfAbsent(
                throwableType,
                clazz -> new CopyOnWriteArrayList<>()
        );
        exceptionHandlers.add(exceptionHandler);
    }

    /**
     * @Description 移除移除处理器，会根据异常类型从 {@link ExceptionDispatcher#EXCEPTION_HANDLER_MAP} 中异常对应的 {@link ExceptionHandler}
     * @param throwableType
     *        异常类型
     * @param exceptionHandler
     *        异常处理器
     * @throws cn.gdrfgdrf.smartuploader.utils.asserts.exception.AssertNotNullException
     *         当 throwableType 或 exceptionHandler 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/17
     */
    public void unregisterExceptionHandler(Class<? extends Throwable> throwableType, ExceptionHandler exceptionHandler) {
        AssertUtils.notNull("exception type", throwableType);
        AssertUtils.notNull("exception handler", exceptionHandler);

        if (!EXCEPTION_HANDLER_MAP.containsKey(throwableType)) {
            return;
        }
        List<ExceptionHandler> exceptionHandlers = EXCEPTION_HANDLER_MAP.get(throwableType);
        exceptionHandlers.remove(exceptionHandler);

        if (exceptionHandlers.isEmpty()) {
            EXCEPTION_HANDLER_MAP.remove(throwableType);
        }
    }

    /**
     * @Description 根据索引移除移除处理器，会根据异常类型从 {@link ExceptionDispatcher#EXCEPTION_HANDLER_MAP} 中异常对应的 {@link ExceptionHandler}
     * @param throwableType
     *        异常类型
     * @param index
     *        异常处理器在 {@link ExceptionDispatcher#EXCEPTION_HANDLER_MAP} 的值中的序号
     * @throws cn.gdrfgdrf.smartuploader.utils.asserts.exception.AssertNotNullException
     *         当 throwableType 或 exceptionHandler 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/17
     */
    public void unregisterExceptionHandler(Class<? extends Throwable> throwableType, int index) {
        AssertUtils.notNull("exception type", throwableType);
        AssertUtils.notNull("exception handler index", index);

        if (!EXCEPTION_HANDLER_MAP.containsKey(throwableType)) {
            return;
        }
        List<ExceptionHandler> exceptionHandlers = EXCEPTION_HANDLER_MAP.get(throwableType);
        exceptionHandlers.remove(index);

        if (exceptionHandlers.isEmpty()) {
            EXCEPTION_HANDLER_MAP.remove(throwableType);
        }
    }

    /**
     * @Description 分发异常，
     * 将根据异常类型从 {@link ExceptionDispatcher#EXCEPTION_HANDLER_MAP} 中获取异常处理器，
     * 并调用 {@link ExceptionHandler#handle(Thread, Throwable)}，
     * 若无法获取到定制异常处理器，将会使用 {@link cn.gdrfgdrf.smartuploader.exceptionhandler.handler.DefaultExceptionHandler}
     * 若无法获取到 {@link cn.gdrfgdrf.smartuploader.exceptionhandler.handler.DefaultExceptionHandler}，
     * 将会抛出 {@link cn.gdrfgdrf.smartuploader.exceptionhandler.exception.NotFoundExceptionHandlerException}
     * 当异常类拥有 {@link Undispatchable} 注解时，该方法不会生效
     *
     * @param thread
	 *        异常所处在的线程
	 * @param throwable
	 *        异常实例
     * @throws cn.gdrfgdrf.smartuploader.utils.asserts.exception.AssertNotNullException
     *         当 thread 或 throwable 为 null 时抛出
     * @throws cn.gdrfgdrf.smartuploader.exceptionhandler.exception.NotFoundExceptionHandlerException
     *         无法获取定制异常处理器以及默认异常处理器时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/8
     */
    public void dispatch(Thread thread, Throwable throwable) {
        AssertUtils.notNull("exception thread", thread);
        AssertUtils.notNull("throwable instance", throwable);

        if (throwable.getClass().isAnnotationPresent(Undispatchable.class)) {
            return;
        }

        List<ExceptionHandler> exceptionHandlers = EXCEPTION_HANDLER_MAP.get(throwable.getClass());
        if (exceptionHandlers == null || exceptionHandlers.isEmpty()) {
            exceptionHandlers = EXCEPTION_HANDLER_MAP.get(Throwable.class);
            if (exceptionHandlers == null || exceptionHandlers.isEmpty()) {
                throw new NotFoundExceptionHandlerException(throwable);
            }

            exceptionHandlers.forEach(exceptionHandler -> {
                exceptionHandler.handle(thread, throwable);
            });

            return;
        }

        exceptionHandlers.forEach(exceptionHandler -> {
            exceptionHandler.handle(thread, throwable);
        });
    }

    /**
     * @Description 获取异常类型到异常处理器的映射
     * @return java.util.Map<java.lang.Class<? extends java.lang.Throwable>,java.util.List<cn.gdrfgdrf.smartuploader.exceptionhandler.base.ExceptionHandler>>
     *         异常类型到异常处理器的映射
     * @Author gdrfgdrf
     * @Date 2024/4/8
     */
    public Map<Class<? extends Throwable>, List<ExceptionHandler>> getExceptionHandlerMap() {
         return EXCEPTION_HANDLER_MAP;
    }
}

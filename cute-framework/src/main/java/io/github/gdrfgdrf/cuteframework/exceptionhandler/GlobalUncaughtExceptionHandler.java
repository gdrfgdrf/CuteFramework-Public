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

package io.github.gdrfgdrf.cuteframework.exceptionhandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @description 全局异常捕获器，当异常没有被 try ... catch 捕获时，将会被该捕获器捕获
 * @author gdrfgdrf
 * @since 2024/4/7
 */
@Slf4j
public class GlobalUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static GlobalUncaughtExceptionHandler INSTANCE;

    private GlobalUncaughtExceptionHandler() {}

    /**
     * @description 单例模式，获取 {@link GlobalUncaughtExceptionHandler} 实例
     * @return io.github.gdrfgdrf.cuteframework.exceptionhandler.GlobalUncaughtExceptionHandler
     *         {@link GlobalUncaughtExceptionHandler} 实例
     * @author gdrfgdrf
     * @since 2024/5/10
     */
    public static GlobalUncaughtExceptionHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GlobalUncaughtExceptionHandler();
        }
        return INSTANCE;
    }

    /**
     * @description 初始化该类，将该类注册为全局异常捕获器
     * @author gdrfgdrf
     * @since 2024/5/10
     */
    public void initialize() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * @description 发送给 {@link ExceptionDispatcher} 以分发异常到异常处理方法
     * @param t
	 *        异常所在线程
	 * @param e
	 *        异常实例
     * @author gdrfgdrf
     * @since 2024/5/4
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        ExceptionDispatcher.getInstance().dispatchSafety(t, e);
    }
}

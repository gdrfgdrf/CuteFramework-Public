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

package io.github.gdrfgdrf.cuteframework.utils.thread;

import java.util.concurrent.*;

/**
 * 线程池支持
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
public class ThreadPoolService {
    private ThreadPoolService() {}

    /**
     * 公共线程池
     */
    private static final ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(
            4,
            40,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024),
            new NamedThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    /**
     * 异步执行事件订阅者的线程池
     */
    private static final ExecutorService EVENT_EXECUTOR_SERVICE = new ThreadPoolExecutor(
            0,
            1024,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024),
            new NamedThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    /**
     * 往公共线程池添加任务
     * @param runnable
	 *        任务
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public static void newTask(Runnable runnable) {
        EXECUTOR_SERVICE.execute(runnable);
    }

    /**
     * 获取异步执行事件订阅者的线程池
     * @return java.util.concurrent.ExecutorService
     *         异步执行事件订阅者的线程池
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public static ExecutorService getEventExecutorService() {
        return EVENT_EXECUTOR_SERVICE;
    }
}

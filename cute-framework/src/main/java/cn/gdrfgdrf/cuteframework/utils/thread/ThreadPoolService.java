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

package cn.gdrfgdrf.cuteframework.utils.thread;

import java.util.concurrent.*;

/**
 * @Description 线程池支持
 * @Author gdrfgdrf
 * @Date 2024/4/8
 */
public class ThreadPoolService {
    private ThreadPoolService() {}

    private static final ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(
            4,
            40,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024),
            new NamedThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    private static final ExecutorService EVENT_EXECUTOR_SERVICE = new ThreadPoolExecutor(
            0,
            1024,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024),
            new NamedThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    public static void newTask(Runnable runnable) {
        EXECUTOR_SERVICE.execute(runnable);
    }

    public static ExecutorService getEventExecutorService() {
        return EVENT_EXECUTOR_SERVICE;
    }
}

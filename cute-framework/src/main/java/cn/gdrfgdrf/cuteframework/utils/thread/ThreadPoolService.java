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

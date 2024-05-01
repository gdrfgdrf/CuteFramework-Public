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

package cn.gdrfgdrf.core.utils.thread;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description 线程工厂，创建带有名称的线程，格式为 Pool-{POOL_COUNT} Thread-{COUNT}
 * @Author gdrfgdrf
 * @Date 2024/4/8
 */
public class NamedThreadFactory implements ThreadFactory {
    private final AtomicInteger poolCount = new AtomicInteger();
    private final AtomicInteger count = new AtomicInteger();
    private final ThreadGroup group;

    @SuppressWarnings("all")
    public NamedThreadFactory() {
        poolCount.incrementAndGet();
        SecurityManager securityManager = System.getSecurityManager();
        group = securityManager != null ?
                securityManager.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(@NotNull Runnable r) {
        Thread result = new Thread(group, r);
        result.setName("Pool-" + poolCount.incrementAndGet() + " Thread-" + count.incrementAndGet());
        return result;
    }
}

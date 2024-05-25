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

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description 线程工厂，创建带有名称的线程，格式为 Pool-{POOL_COUNT} Thread-{COUNT}
 * @author gdrfgdrf
 * @since 2024/4/8
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

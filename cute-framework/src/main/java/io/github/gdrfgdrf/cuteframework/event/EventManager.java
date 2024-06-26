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

package io.github.gdrfgdrf.cuteframework.event;

import io.github.gdrfgdrf.cuteframework.event.exceptionhandler.EventExceptionHandler;
import io.github.gdrfgdrf.cuteframework.utils.asserts.AssertUtils;
import io.github.gdrfgdrf.cuteframework.utils.asserts.exception.AssertNotNullException;
import io.github.gdrfgdrf.cuteframework.utils.thread.ThreadPoolService;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

/**
 * 事件管理器，进行发送，订阅事件等操作，使用了 Guava 的事件模块，若不作说明，则默认为同步
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
public class EventManager {
    private static EventManager INSTANCE;

    /**
     * 事件总线，事件处理时的异常将由 {@link EventExceptionHandler} 捕获
     */
    private final EventBus EVENT_BUS = new EventBus(EventExceptionHandler.getInstance());
    /**
     * 异步事件总线，事件处理时的异常将由 {@link EventExceptionHandler} 捕获
     */
    private final EventBus ASYNC_EVENT_BUS = new AsyncEventBus(
            ThreadPoolService.getEventExecutorService(),
            EventExceptionHandler.getInstance()
    );

    private EventManager() {}

    /**
     * 单例模式，获取 {@link EventManager} 实例
     * @return io.github.gdrfgdrf.cuteframework.event.EventManager
     *         {@link EventManager} 实例
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public static EventManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventManager();
        }
        return INSTANCE;
    }

    /**
     * 发布一个事件，这会同时调用同步和异步的 EventBus，异步的 EventBus 会先被调用
     * @param event
	 *        事件
     * @throws AssertNotNullException
     *         当 event 为 null 时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public void post(Object event) throws AssertNotNullException {
        AssertUtils.notNull("event", event);
        ASYNC_EVENT_BUS.post(event);
        EVENT_BUS.post(event);
    }

    /**
     * 发布一个异步事件，该方法仅会发布到异步的 EventBus 的订阅者
     * @param event
	 *        事件
     * @throws AssertNotNullException
     *         当 event 为 null 时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public void postAsynchronously(Object event) throws AssertNotNullException {
        AssertUtils.notNull("event", event);
        ASYNC_EVENT_BUS.post(event);
    }

    /**
     * 发布一个事件，该方法仅会发布到同步的 EventBus 的订阅者
     * @param event
	 *        事件
     * @throws AssertNotNullException
     *         当 event 为 null 时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public void postSynchronously(Object event) throws AssertNotNullException {
        AssertUtils.notNull("event", event);
        EVENT_BUS.post(event);
    }

    /**
     * 注册一个事件订阅者
     * @param eventSubscriber
	 *        事件订阅者实例
     * @throws AssertNotNullException
     *         当 eventSubscriber 为 null 时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public void register(Object eventSubscriber) throws AssertNotNullException {
        AssertUtils.notNull("event subscriber", eventSubscriber);
        EVENT_BUS.register(eventSubscriber);
    }

    /**
     * 注册一个异步的事件订阅者
     * @param eventSubscriber
	 *        事件订阅者实例
     * @throws AssertNotNullException
     *         当 eventSubscriber 为 null 时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public void registerAsynchronous(Object eventSubscriber) throws AssertNotNullException {
        AssertUtils.notNull("event subscriber", eventSubscriber);
        ASYNC_EVENT_BUS.register(eventSubscriber);
    }

    /**
     * 移除一个事件订阅者
     * @param eventSubscriber
	 *        事件订阅者实例
     * @throws AssertNotNullException
     *         当 eventSubscriber 为 null 时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public void unregister(Object eventSubscriber) throws AssertNotNullException {
        AssertUtils.notNull("event subscriber", eventSubscriber);
        EVENT_BUS.unregister(eventSubscriber);
    }

    /**
     * 移除一个异步的事件订阅者
     * @param eventSubscriber
     *        事件订阅者实例
     * @throws AssertNotNullException
     *         当 eventSubscriber 为 null 时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public void unregisterAsynchronous(Object eventSubscriber) throws AssertNotNullException {
        AssertUtils.notNull("event subscriber", eventSubscriber);
        ASYNC_EVENT_BUS.unregister(eventSubscriber);
    }

    /**
     * 获取事件总线
     * @return com.google.common.eventbus.EventBus
     *         事件总线
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public EventBus getEventBus() {
        return EVENT_BUS;
    }
}

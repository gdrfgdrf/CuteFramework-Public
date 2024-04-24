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

package cn.gdrfgdrf.smartuploader.event;

import cn.gdrfgdrf.smartuploader.event.base.Event;
import cn.gdrfgdrf.smartuploader.utils.asserts.AssertUtils;
import cn.gdrfgdrf.smartuploader.utils.thread.ThreadPoolService;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

/**
 * @Description 事件管理器，进行发送，订阅事件等操作，使用了 Guava 的事件模块
 * @Author gdrfgdrf
 * @Date 2024/4/24
 */
public class EventManager {
    private static EventManager INSTANCE;

    /**
     * 事件总线，事件处理时的异常将由 {@link EventExceptionHandler} 捕获
     */
    private final EventBus EVENT_BUS = new EventBus(new EventExceptionHandler());

    private EventManager() {}

    /**
     * @Description 单例模式，获取 {@link EventManager} 实例
     * @return cn.gdrfgdrf.smartuploader.event.EventManager
     *         {@link EventManager} 实例
     * @Author gdrfgdrf
     * @Date 2024/4/24
     */
    public static EventManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventManager();
        }
        return INSTANCE;
    }

    /**
     * @Description 发布一个事件
     * @param event
	 *        事件
     * @throws cn.gdrfgdrf.smartuploader.utils.asserts.exception.AssertNotNullException
     *         当 event 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/24
     */
    public void post(Event event) {
        AssertUtils.notNull("event", event);
        EVENT_BUS.post(event);
    }

    /**
     * @Description 注册一个事件订阅者
     * @param eventSubscriber
	 *        事件订阅者实例
     * @throws cn.gdrfgdrf.smartuploader.utils.asserts.exception.AssertNotNullException
     *         当 eventSubscriber 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/24
     */
    public void register(Object eventSubscriber) {
        AssertUtils.notNull("event subscriber", eventSubscriber);
        EVENT_BUS.register(eventSubscriber);
    }

    /**
     * @Description 移除一个事件订阅者
     * @param eventSubscriber
	 *        事件订阅者实例
     * @throws cn.gdrfgdrf.smartuploader.utils.asserts.exception.AssertNotNullException
     *         当 eventSubscriber 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/24
     */
    public void unregister(Object eventSubscriber) {
        EVENT_BUS.unregister(eventSubscriber);
    }

    /**
     * @Description 获取事件总线
     * @return com.google.common.eventbus.EventBus
     *         事件总线
     * @Author gdrfgdrf
     * @Date 2024/4/24
     */
    public EventBus getEventBus() {
        return EVENT_BUS;
    }
}

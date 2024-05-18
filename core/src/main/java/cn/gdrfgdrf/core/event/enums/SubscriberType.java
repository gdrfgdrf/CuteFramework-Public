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

package cn.gdrfgdrf.core.event.enums;

/**
 * @Description 事件订阅者类型，
 * Guava 的 EventBus 支持同步和异步，但是需要两个 EventBus 实例去支持同步和异步
 * 在同步的 EventBus 注册的订阅者不能接收异步的事件，
 * 同理，在异步的 EventBus 注册的订阅者不能接收同步的事件
 *
 * @Author gdrfgdrf
 * @Date 2024/5/18
 */
public enum SubscriberType {
    /**
     * 同步执行的事件订阅者
     */
    SYNC,
    /**
     * 异步执行的事件订阅者
     */
    ASYNC,
    /**
     * 可同步可异步的事件订阅者
     */
    ALL
}

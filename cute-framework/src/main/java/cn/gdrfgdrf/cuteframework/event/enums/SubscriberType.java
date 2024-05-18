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

package cn.gdrfgdrf.cuteframework.event.enums;

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

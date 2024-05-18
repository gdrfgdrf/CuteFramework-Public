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

package cn.gdrfgdrf.cuteframeworkimpl;

import cn.gdrfgdrf.cuteframework.api.exception.PluginIllegalStateChangeException;
import cn.gdrfgdrf.cuteframework.event.annotation.EventListener;
import cn.gdrfgdrf.cuteframework.exceptionhandler.annotation.ExceptionHandler;
import cn.gdrfgdrf.cuteframework.exceptionhandler.event.ExceptionEvent;
import com.google.common.eventbus.Subscribe;

/**
 * @Description
 * @Author gdrfgdrf
 * @Date 2024/5/6
 */
@EventListener
public class TestExceptionHandler {

    @ExceptionHandler(support = {Throwable.class})
    public static void onException(Thread thread, Throwable throwable) {
        System.out.println("1、捕获到线程 " + thread.getName() + " 的异常 " + throwable.getMessage());
    }

    @ExceptionHandler(support = {PluginIllegalStateChangeException.class})
    public static void onPluginIllegalStateChangeException(Thread thread, PluginIllegalStateChangeException throwable) {
        System.out.println("2、捕获到线程 " + thread.getName() + " 的异常 " + throwable.getMessage());
    }

    @Subscribe
    public static void onUndispatchableExceptionThrown(ExceptionEvent.UndispatchableExceptionThrownEvent event) {
        System.out.println("3、捕获到线程 " + event.getThread().getName() + " 的异常 " + event.getThrowable().getMessage());
    }
}

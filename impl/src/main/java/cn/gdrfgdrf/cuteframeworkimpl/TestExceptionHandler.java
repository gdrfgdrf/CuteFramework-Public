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

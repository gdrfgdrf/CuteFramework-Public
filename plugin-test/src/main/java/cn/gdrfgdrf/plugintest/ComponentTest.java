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

package cn.gdrfgdrf.plugintest;

import cn.gdrfgdrf.core.bean.annotation.Component;
import cn.gdrfgdrf.core.exceptionhandler.annotation.ExceptionHandler;

/**
 * @Description
 * @Author gdrfgdrf
 * @Date 2024/5/6
 */
@Component
public class ComponentTest {

    @ExceptionHandler(support = Throwable.class)
    public static void onException(Thread thread, Throwable throwable) {
        System.out.println("ComponentTest received a exception which is from the thread " + thread.getName() + ": " + throwable.getMessage());
    }

}

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

package cn.gdrfgdrf.plugintest;

import cn.gdrfgdrf.cuteframework.bean.annotation.Component;
import cn.gdrfgdrf.cuteframework.exceptionhandler.annotation.ExceptionHandler;

/**
 * @Description
 * @Author gdrfgdrf
 * @Date 2024/5/6
 */
@Component
public class ComponentTest {
    public ComponentTest() {
        System.out.println("ComponentTest initialize");
    }

    @ExceptionHandler(support = Throwable.class)
    public static void onException(Thread thread, Throwable throwable) {
        System.out.println("ComponentTest received a exception which is from the thread " + thread.getName() + ": " + throwable.getMessage());
    }

}

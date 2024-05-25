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

package io.github.gdrfgdrf.cuteframework.utils.stack.common;

import lombok.Getter;

/**
 * 方法信息
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
@Getter
public class MethodInformation {
    /**
     * 方法所在的全限定类名
     */
    private final String className;
    /**
     * 方法的方法名
     */
    private final String methodName;

    public MethodInformation(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }
}

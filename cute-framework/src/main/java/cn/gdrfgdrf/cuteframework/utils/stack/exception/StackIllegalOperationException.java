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

package cn.gdrfgdrf.cuteframework.utils.stack.exception;

import cn.gdrfgdrf.cuteframework.exceptionhandler.base.CustomException;
import cn.gdrfgdrf.cuteframework.locale.collect.ExceptionLanguage;
import lombok.Getter;

/**
 * @Description 当 {@link cn.gdrfgdrf.cuteframework.utils.stack.StackUtils} 检测到违规操作时抛出
 * @Author gdrfgdrf
 * @Date 2024/4/30
 */
@Getter
public class StackIllegalOperationException extends CustomException {
    /**
     * 违规操作的调用方所在的类
     */
    private final String callerClassName;
    /**
     * 违规操作的调用方
     */
    private final String caller;
    /**
     * 被保护的方法所在的类
     */
    private final String protectClassName;
    /**
     * 被保护的方法的方法名
     */
    private final String protect;

    public StackIllegalOperationException(String callerClassName, String caller, String protectClassName, String protect) {
        this.callerClassName = callerClassName;
        this.caller = caller;
        this.protectClassName = protectClassName;
        this.protect = protect;
    }

    @Override
    public String getI18NMessage() {
        return ExceptionLanguage.STACK_ILLEGAL_OPERATION
                .get()
                .format(callerClassName, caller, protectClassName, protect)
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "Method " + caller + " of " + callerClassName + " calls method " + protect + " of " + protectClassName + " illegally, and has terminated its caller's operation";
    }
}

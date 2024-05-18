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
 * @Description 当 {@link cn.gdrfgdrf.cuteframework.utils.stack.StackUtils} 的 onlyCheckerInternal 方法
 * 接收到的前两者的值都为空时抛出，此时无论任何操作都会被终止
 *
 * @Author gdrfgdrf
 * @Date 2024/4/30
 */
@Getter
public class StackIllegalArgumentException extends CustomException {
    /**
     * 被保护的方法所在的类的类名
     */
    private final String protectClassName;
    /**
     * 被保护的方法的方法名
     */
    private final String protectMethodName;

    public StackIllegalArgumentException(String protectClassName, String protectMethodName) {
        this.protectClassName = protectClassName;
        this.protectMethodName = protectMethodName;
    }

    @Override
    public String getI18NMessage() {
        return ExceptionLanguage.STACK_ILLEGAL_ARGUMENT
                .get()
                .format(protectClassName, protectMethodName)
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "The " + protectMethodName + " method of class " + protectClassName + " needs to be protected, but provided the wrong argument and has terminated its caller's operation";
    }
}
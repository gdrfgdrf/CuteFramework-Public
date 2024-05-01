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

package cn.gdrfgdrf.core.utils.stack.exception;

import cn.gdrfgdrf.core.exceptionhandler.base.CustomRuntimeException;
import cn.gdrfgdrf.core.locale.collect.ExceptionLanguage;

/**
 * @Description 当 {@link cn.gdrfgdrf.core.utils.stack.StackUtils} 检测到违规操作时抛出
 * @Author gdrfgdrf
 * @Date 2024/4/30
 */
public class StackIllegalOperationException extends CustomRuntimeException {
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
        return ExceptionLanguage.STACK_ILLEGAL_OPERATION_EXCEPTION
                .get()
                .format(callerClassName, caller, protectClassName, protect)
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "Method " + caller + " of " + callerClassName + " calls method " + protect + " of " + protectClassName + " illegally, and has terminated its caller's operation";
    }
}

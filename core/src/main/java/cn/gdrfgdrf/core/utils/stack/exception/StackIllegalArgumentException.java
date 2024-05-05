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

import cn.gdrfgdrf.core.exceptionhandler.base.CustomException;
import cn.gdrfgdrf.core.locale.collect.ExceptionLanguage;
import lombok.Getter;

/**
 * @Description 当 {@link cn.gdrfgdrf.core.utils.stack.StackUtils} 的 onlyCheckerInternal 方法
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

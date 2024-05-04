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

package cn.gdrfgdrf.core.utils.asserts.exception;

import cn.gdrfgdrf.core.exceptionhandler.base.CustomException;
import cn.gdrfgdrf.core.locale.collect.AssertLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 当数组长度与需要的不匹配是抛出
 * @Author gdrfgdrf
 * @Date 2024/5/4
 */
@Getter
@AllArgsConstructor
public class AssertArrayLengthMismatchException extends CustomException {
    private final String parameterName;
    private final int length;


    @Override
    public String getI18NMessage() {
        return AssertLanguage.ARRAY_MIN
                .get()
                .format(parameterName, length)
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "The length of the array " + parameterName + " must be greater than or equal to " + length;
    }
}

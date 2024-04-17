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

package cn.gdrfgdrf.smartuploader.utils.asserts.exception;

import cn.gdrfgdrf.smartuploader.utils.asserts.base.AssertErrorException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 当实例为 null 时抛出
 * @Author gdrfgdrf
 * @Date 2024/4/8
 */
@AllArgsConstructor
public class AssertNotNullException extends AssertErrorException {
    @Getter
    private final String parameterName;

    @Override
    public String getI18NMessage() {
        return "";
    }

    @Override
    public String getDefaultMessage() {
        return parameterName + " cannot be null";
    }
}

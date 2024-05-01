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

package cn.gdrfgdrf.smartuploader.utils.stack.common;

import lombok.Getter;

/**
 * @Description 方法信息
 * @Author gdrfgdrf
 * @Date 2024/4/30
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

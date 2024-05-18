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

package cn.gdrfgdrf.cuteframework.exceptionhandler.base;

import cn.gdrfgdrf.cuteframework.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 自定义运行时错误
 * @Author gdrfgdrf
 * @Date 2024/4/7
 */
@Slf4j
public abstract class CustomException extends Exception {
    /**
     * @Description 获取错误信息，优先获取当前语言下的错误信息，若为空则获取默认字符串
     * @return java.lang.String
     *         字符串
     * @Author gdrfgdrf
     * @Date 2024/4/24
     */
    @Override
    public String getMessage() {
        String message = null;
        try {
            message = getI18NMessage();
        } catch (Exception ignored) {
        }
        if (StringUtils.isBlank(message)) {
            message = getDefaultMessage();
        }
        return message;
    }

    /**
     * @Description 获取当前语言下的错误信息，该字符串可能会被显示到前台
     * @return java.lang.String
     *         经过翻译的字符串
     * @Author gdrfgdrf
     * @Date 2024/4/16
     */
    public abstract String getI18NMessage();
    /**
     * @Description 当语言模块的内容未能正确加载时，将调用该方法获取默认字符串，该字符串必须为全英文
     * @return java.lang.String
     *         全英文字符串
     * @Author gdrfgdrf
     * @Date 2024/4/16
     */
    public abstract String getDefaultMessage();
}

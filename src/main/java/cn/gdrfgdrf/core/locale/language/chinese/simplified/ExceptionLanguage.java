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

package cn.gdrfgdrf.core.locale.language.chinese.simplified;

import cn.gdrfgdrf.core.locale.LanguageString;
import cn.gdrfgdrf.core.locale.base.LanguageBlock;

/**
 * Basic:
 * gdrfgdrf | 2024-04-17
 */
class ExceptionLanguage implements LanguageBlock {
    public static final LanguageString NOT_FOUND_EXCEPTION_HANDLER = new LanguageString("无法为 {} 找到异常处理器");
    public static final LanguageString EVENT_PROCESSING_EXCEPTION = new LanguageString("处理 {} 事件时发生错误");

    public static final LanguageString STACK_ILLEGAL_OPERATION_EXCEPTION = new LanguageString("{} 类的 {} 方法违规调用了 {} 类的 {} 方法，已终止其操作");
    public static final LanguageString STACK_ILLEGAL_ARGUMENT_EXCEPTION = new LanguageString("{} 类的 {} 需要被保护，但提供了错误的参数，已终止其调用方的操作");
}

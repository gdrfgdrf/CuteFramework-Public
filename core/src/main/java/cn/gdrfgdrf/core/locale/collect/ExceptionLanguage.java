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

package cn.gdrfgdrf.core.locale.collect;

import cn.gdrfgdrf.core.locale.LanguageString;
import cn.gdrfgdrf.core.locale.base.LanguageCollect;

/**
 * @Description 异常信息语言汇总类
 * @Author gdrfgdrf
 * @Date 2024/4/17
 */
public class ExceptionLanguage implements LanguageCollect {
    public static LanguageString BEAN_METHOD_ARGUMENT_TYPE_MISMATCH;
    public static LanguageString EXCEPTION_HANDLE_METHOD_IS_NOT_STATIC;
    public static LanguageString NOT_FOUND_EXCEPTION_HANDLER;
    public static LanguageString EVENT_PROCESSING_ERROR;

    public static LanguageString BEAN_NAME_CONFLICT;
    public static LanguageString BEAN_CLASS_RESOLVER_PROCESSING_ERROR;
    public static LanguageString BEAN_METHOD_RESOLVER_PROCESSING_ERROR;

    public static LanguageString STACK_ILLEGAL_OPERATION;
    public static LanguageString STACK_ILLEGAL_ARGUMENT;

    public static LanguageString PLUGIN_LOAD_FAILED;
    public static LanguageString PLUGIN_MAIN_CLASS_LOAD_ERROR;
    public static LanguageString UNSUPPORTED_PLUGIN;
    public static LanguageString PLUGIN_UNDEFINED_PROPERTY;
    public static LanguageString PLUGIN_MAIN_CLASS_EXTEND_ERROR;
    public static LanguageString PLUGIN_NAME_CONFLICT;
    public static LanguageString PLUGIN_ILLEGAL_STATE_CHANGE;
}

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
    public static final LanguageString BEAN_METHOD_ARGUMENT_TYPE_MISMATCH = new LanguageString("Bean 方法 {} 的参数必须是 {}，所在类 {}");
    public static final LanguageString EXCEPTION_HANDLE_METHOD_IS_NOT_STATIC = new LanguageString("异常处理方法 {} 不是静态方法，所在类 {}");
    public static final LanguageString NOT_FOUND_EXCEPTION_HANDLER = new LanguageString("无法为 {} 找到异常处理器");
    public static final LanguageString EVENT_PROCESSING_ERROR = new LanguageString("处理 {} 事件时发生错误：{}");

    public static final LanguageString BEAN_NAME_CONFLICT = new LanguageString("无法创建 Bean 类 {}，因为已经有了一个同名的 Bean 实例存在");
    public static final LanguageString BEAN_METHOD_RESOLVER_PROCESSING_ERROR = new LanguageString("Bean 方法解析器 {} 处理 Bean {} 时发生错误：{}");

    public static final LanguageString STACK_ILLEGAL_OPERATION = new LanguageString("{} 类的 {} 方法违规调用了 {} 类的 {} 方法，已终止其操作");
    public static final LanguageString STACK_ILLEGAL_ARGUMENT = new LanguageString("{} 类的 {} 需要被保护，但提供了错误的参数，已终止其调用方的操作");

    public static final LanguageString PLUGIN_LOAD_FAILED = new LanguageString("插件 {} 加载错误：{}");
    public static final LanguageString UNSUPPORTED_PLUGIN = new LanguageString("不支持的插件 {}，因为其定义的核心版本 {} 大于当前的核心版本 {}");
    public static final LanguageString PLUGIN_UNDEFINED_PROPERTY = new LanguageString("插件 {} 中有未定义的必需属性 {}");
    public static final LanguageString PLUGIN_MAIN_CLASS_EXTEND_ERROR = new LanguageString("插件 {} 的主类 {} 没有继承 cn.gdrfgdrf.core.api.base.Plugin");
    public static final LanguageString PLUGIN_NAME_CONFLICT = new LanguageString("无法注册插件 {}，因为先前已注册了同名插件 {}");
}

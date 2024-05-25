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

package io.github.gdrfgdrf.cuteframework.locale.language.chinese.simplified;

import io.github.gdrfgdrf.cuteframework.locale.LanguageString;
import io.github.gdrfgdrf.cuteframework.locale.base.LanguageBlock;

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
    public static final LanguageString BEAN_CLASS_RESOLVER_PROCESSING_ERROR = new LanguageString("Bean 类解析器 {} 处理 Bean {} 是发生错误：{}");
    public static final LanguageString BEAN_METHOD_RESOLVER_PROCESSING_ERROR = new LanguageString("Bean 方法解析器 {} 处理 Bean {} 时发生错误：{}");

    public static final LanguageString STACK_ILLEGAL_OPERATION = new LanguageString("{} 类的 {} 方法违规调用了 {} 类的 {} 方法，已终止其操作");
    public static final LanguageString STACK_ILLEGAL_ARGUMENT = new LanguageString("{} 类的 {} 需要被保护，但提供了错误的参数，已终止其调用方的操作");

    public static final LanguageString PLUGIN_LOAD_FAILED = new LanguageString("插件 {} 加载错误：{}");
    public static final LanguageString PLUGIN_MAIN_CLASS_LOAD_ERROR = new LanguageString("插件 {} 的主类加载错误，异常信息：{}，异常类：{}");
    public static final LanguageString UNSUPPORTED_PLUGIN = new LanguageString("不支持的插件 {}，因为其定义的核心版本 {} 在当前版本的程序中找不到");
    public static final LanguageString PLUGIN_UNDEFINED_PROPERTY = new LanguageString("插件 {} 中有未定义的必需属性 {}");
    public static final LanguageString PLUGIN_MAIN_CLASS_EXTEND_ERROR = new LanguageString("插件 {} 的主类 {} 没有继承 io.github.gdrfgdrf.cuteframework.api.base.Plugin");
    public static final LanguageString PLUGIN_NAME_CONFLICT = new LanguageString("无法注册插件 {}，因为先前已注册了同名插件 {}");
    public static final LanguageString PLUGIN_ILLEGAL_STATE_CHANGE = new LanguageString("无法改变插件 {} 的状态，从状态 {} 变化到状态 {}");
}

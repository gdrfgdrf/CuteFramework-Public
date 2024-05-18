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

package cn.gdrfgdrf.cuteframework.locale.collect;

import cn.gdrfgdrf.cuteframework.locale.LanguageString;
import cn.gdrfgdrf.cuteframework.locale.base.LanguageCollect;

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

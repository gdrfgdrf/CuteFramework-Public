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

/**
 * @Description 该包为插件支持，
 * 如果想要插件被程序识别到，请在插件的资源列表，即 jar 文件的根目录下放入一个名为 plugin.json 的文件
 * 该文件请包括以下内容
 * <p>
 * {
 *  "name": "插件名称",
 *  "main-class":"插件主类的全限定名",
 *  "api-version": "插件开发所使用的核心版本"
 *  "author": "插件作者"
 * }
 * <p>
 * 只有包含了以上的内容，插件才能够被正确识别到，
 * main-class 为插件主类的全限定名，插件主类即继承了 {@link cn.gdrfgdrf.cuteframework.api.base.Plugin} 的那个类，
 * 该类必须包含一个公开的无参构造函数，
 * api-version 为插件开发所使用的核心版本，即 {@link cn.gdrfgdrf.cuteframework.common.VersionEnum#CURRENT}，
 * 该值必须为其中的 {@link cn.gdrfgdrf.cuteframework.common.VersionEnum#name()}
 * 当以上内容有一个为空时将抛出 {@link cn.gdrfgdrf.cuteframework.api.exception.PluginUndefinedPropertyException}
 *
 * @Author gdrfgdrf
 * @Date 2024/5/2
 */
package cn.gdrfgdrf.cuteframework.api;
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
 * main-class 为插件主类的全限定名，插件主类即继承了 {@link cn.gdrfgdrf.core.api.base.Plugin} 的那个类，
 * 该类必须包含一个公开的无参构造函数，
 * api-version 为插件开发所使用的核心版本，即 {@link cn.gdrfgdrf.core.common.VersionEnum#CURRENT}，
 * 该值必须为其中的 {@link cn.gdrfgdrf.core.common.VersionEnum#getVersion()}
 * 当以上内容有一个为空时将抛出 {@link cn.gdrfgdrf.core.api.exception.PluginUndefinedPropertyException}
 *
 * @Author gdrfgdrf
 * @Date 2024/5/2
 */
package cn.gdrfgdrf.core.api;
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

package cn.gdrfgdrf.core.api.common;

/**
 * @Description 插件状态
 * @Author gdrfgdrf
 * @Date 2024/5/5
 */
public enum PluginState {
    /**
     * 已注册状态，插件没有进行任何的启用，加载，停止，禁用操作时为该状态，
     * 即插件被 {@link cn.gdrfgdrf.core.api.loader.PluginLoader} 加载完成后的状态，
     * 对于一个插件，该状态只能存在一次
     */
    REGISTERED,
    /**
     * 插件已被启用，此时插件还未被加载
     */
    ENABLED,
    /**
     * 插件已被加载完成
     */
    LOADED,
    /**
     * 插件已被停止，此时插件还未被禁用
     */
    STOPPED,
    /**
     * 插件已被禁用
     */
    DISABLED
}

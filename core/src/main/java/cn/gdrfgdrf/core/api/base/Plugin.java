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

package cn.gdrfgdrf.core.api.base;

import cn.gdrfgdrf.core.api.common.PluginDescription;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description 插件主类应该继承该抽象类
 * @Author gdrfgdrf
 * @Date 2024/5/2
 */
@Getter
@Setter
@EqualsAndHashCode
public abstract class Plugin {
    /**
     * 插件描述文件，plugin.json 在代码中的表示
     */
    private PluginDescription pluginDescription;

    /**
     * @Description 空方法，当插件被启用时调用
     *
     * @Author gdrfgdrf
     * @Date 2024/5/2
     */
    public void onEnable() {}

    /**
     * @Description 空方法，插件开始被核心加载时调用
     * @Author gdrfgdrf
     * @Date 2024/5/2
     */
    public void onLoad() {}

    /**
     * @Description 空方法，当插件需要被停止时调用
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public void onStop() {}

    /**
     * @Description 空方法，当插件被禁用时调用
     * @Author gdrfgdrf
     * @Date 2024/5/2
     */
    public void onDisable() {}
}

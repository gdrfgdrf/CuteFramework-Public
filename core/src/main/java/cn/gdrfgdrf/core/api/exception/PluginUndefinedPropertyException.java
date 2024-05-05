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

package cn.gdrfgdrf.core.api.exception;

import cn.gdrfgdrf.core.exceptionhandler.base.CustomException;
import cn.gdrfgdrf.core.locale.collect.ExceptionLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

/**
 * @Description 当插件的 plugin.json 中有必要的属性未定义时抛出
 * @Author gdrfgdrf
 * @Date 2024/5/5
 */
@Getter
@AllArgsConstructor
public class PluginUndefinedPropertyException extends CustomException {
    /**
     * 插件文件
     */
    private final File pluginFile;
    /**
     * 未定义的必需属性
     */
    private final String undefinedProperty;

    @Override
    public String getI18NMessage() {
        return ExceptionLanguage.PLUGIN_UNDEFINED_PROPERTY
                .get()
                .format(pluginFile.getName(), undefinedProperty)
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "Plugin " + pluginFile.getName() + " has undefined required attributes " + undefinedProperty;
    }
}

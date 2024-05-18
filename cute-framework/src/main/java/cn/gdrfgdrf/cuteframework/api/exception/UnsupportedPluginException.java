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

package cn.gdrfgdrf.cuteframework.api.exception;

import cn.gdrfgdrf.cuteframework.common.VersionEnum;
import cn.gdrfgdrf.cuteframework.exceptionhandler.base.CustomException;
import cn.gdrfgdrf.cuteframework.locale.collect.ExceptionLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

/**
 * @Description 不支持的插件，当插件描述文件中的 api-version 加载时被解析为 {@link VersionEnum#UNAVAILABLE} 时抛出
 * @Author gdrfgdrf
 * @Date 2024/5/5
 */
@Getter
@AllArgsConstructor
public class UnsupportedPluginException extends CustomException {
    /**
     * 插件文件
     */
    private final File pluginFile;
    /**
     * 插件的 api-version
     */
    private final String pluginApiVersion;

    @Override
    public String getI18NMessage() {
        return ExceptionLanguage.UNSUPPORTED_PLUGIN
                .get()
                .format(pluginFile.getName(), pluginApiVersion, VersionEnum.CURRENT.name())
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return null;
    }
}

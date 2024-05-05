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
 * @Description 插件加载错误，当 {@link cn.gdrfgdrf.core.api.loader.PluginLoader} 加载插件错误时将抛出此类，
 * 错误实例将会包含在该类中并继续抛出
 *
 * @Author gdrfgdrf
 * @Date 2024/5/5
 */
@Getter
@AllArgsConstructor
public class PluginLoadException extends CustomException {
    /**
     * 插件文件
     */
    private final File pluginFile;
    /**
     * 加载插件时的错误实例
     */
    private final Throwable throwable;

    @Override
    public String getI18NMessage() {
        return ExceptionLanguage.PLUGIN_LOAD_FAILED
                .get()
                .format(pluginFile.getName(), throwable.getMessage())
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "An error occurred while loading plugin " + pluginFile.getName() + ": " + throwable.getMessage();
    }
}

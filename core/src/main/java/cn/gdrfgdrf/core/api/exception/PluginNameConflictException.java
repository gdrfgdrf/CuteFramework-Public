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

import cn.gdrfgdrf.core.api.base.Plugin;
import cn.gdrfgdrf.core.exceptionhandler.base.CustomException;
import cn.gdrfgdrf.core.locale.collect.ExceptionLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 当拥有相同名称的插件尝试注册到 {@link cn.gdrfgdrf.core.api.PluginManager} 时抛出，
 * 此时将会放弃注册后来的插件，保留前来的插件
 *
 * @Author gdrfgdrf
 * @Date 2024/5/5
 */
@Getter
@AllArgsConstructor
public class PluginNameConflictException extends CustomException {
    /**
     * 前来的插件
     */
    private final Plugin plugin1;
    /**
     * 后来的插件
     */
    private final Plugin plugin2;

    @Override
    public String getI18NMessage() {
        return ExceptionLanguage.PLUGIN_NAME_CONFLICT
                .get()
                .format(
                        plugin2.getPluginDescription().getPluginFile().getName(),
                        plugin1.getPluginDescription().getPluginFile().getName()
                )
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "Plugin " +
                plugin2.getPluginDescription().getPluginFile().getName() +
                " cannot be registered because plugin " +
                plugin1.getPluginDescription().getPluginFile().getName() +
                " with the same name was previously registered";
    }
}

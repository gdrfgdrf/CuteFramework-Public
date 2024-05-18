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

import cn.gdrfgdrf.cuteframework.api.common.PluginDescription;
import cn.gdrfgdrf.cuteframework.exceptionhandler.base.CustomException;
import cn.gdrfgdrf.cuteframework.locale.collect.ExceptionLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 当 main-class 不是继承的 {@link cn.gdrfgdrf.cuteframework.api.base.Plugin} 时抛出
 * @Author gdrfgdrf
 * @Date 2024/5/5
 */
@Getter
@AllArgsConstructor
public class PluginMainClassExtendException extends CustomException {
    /**
     * 插件描述
     */
    private final PluginDescription pluginDescription;
    /**
     * 插件的 main-class，该类必须继承 {@link cn.gdrfgdrf.cuteframework.api.base.Plugin}，
     * 但当该异常抛出时，该类没有继承 {@link cn.gdrfgdrf.cuteframework.api.base.Plugin}
     */
    private final Class<?> mainClass;

    @Override
    public String getI18NMessage() {
        return ExceptionLanguage.PLUGIN_MAIN_CLASS_EXTEND_ERROR
                .get()
                .format(pluginDescription.getName(), mainClass.getName())
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "The main class " +
                mainClass.getName() +
                " of plugin " +
                pluginDescription.getName() +
                " must be a subclass of cn.gdrfgdrf.cuteframework.api.base.Plugin";
    }
}

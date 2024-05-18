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
 * @Description 插件主类无法被类加载器 {@link cn.gdrfgdrf.cuteframework.api.loader.JarClassLoader} 加载，
 * 这时插件主类还没有被实例化，该异常类会包括具体地无法加载异常实例
 *
 * @Author gdrfgdrf
 * @Date 2024/5/18
 */
@Getter
@AllArgsConstructor
public class PluginMainClassLoadException extends CustomException {
    /**
     * 插件的描述文件
     */
    private final PluginDescription pluginDescription;
    /**
     * 插件的 main-class 所定义的值，该值无法被 {@link cn.gdrfgdrf.cuteframework.api.loader.JarClassLoader} 加载为类
     */
    private final String mainClass;
    /**
     * 加载 main-class 时发生的异常实例
     */
    private final Throwable throwable;

    @Override
    public String getI18NMessage() {
        return ExceptionLanguage.PLUGIN_MAIN_CLASS_LOAD_ERROR
                .get()
                .format(pluginDescription.getName(), throwable.getMessage(), throwable.getClass())
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "Plugin " +
                pluginDescription.getName() +
                " main class loading error, exception message: " +
                throwable.getMessage() +
                ", exception class: " +
                throwable.getClass();
    }
}

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

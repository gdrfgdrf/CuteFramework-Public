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

package io.github.gdrfgdrf.cuteframework.api.exception;

import io.github.gdrfgdrf.cuteframework.api.common.PluginDescription;
import io.github.gdrfgdrf.cuteframework.exceptionhandler.base.CustomException;
import io.github.gdrfgdrf.cuteframework.locale.collect.ExceptionLanguage;
import io.github.gdrfgdrf.cuteframework.api.base.Plugin;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 当 main-class 不是继承的 {@link Plugin} 时抛出
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
     * 插件的 main-class，该类必须继承 {@link Plugin}，
     * 但当该异常抛出时，该类没有继承 {@link Plugin}
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
                " must be a subclass of io.github.gdrfgdrf.cuteframework.api.base.Plugin";
    }
}

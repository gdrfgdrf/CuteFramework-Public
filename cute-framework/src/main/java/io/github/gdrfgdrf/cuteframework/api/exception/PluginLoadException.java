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

import io.github.gdrfgdrf.cuteframework.exceptionhandler.base.CustomException;
import io.github.gdrfgdrf.cuteframework.locale.collect.ExceptionLanguage;
import io.github.gdrfgdrf.cuteframework.api.event.PluginEvent;
import io.github.gdrfgdrf.cuteframework.api.loader.PluginLoader;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

/**
 * 插件加载错误，当 {@link PluginLoader} 加载插件错误时将抛出此类，
 * 错误实例将会包含在该类中并以 {@link PluginEvent.LoadError} 的形式发布
 *
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
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

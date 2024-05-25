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

import io.github.gdrfgdrf.cuteframework.api.base.Plugin;
import io.github.gdrfgdrf.cuteframework.exceptionhandler.base.CustomException;
import io.github.gdrfgdrf.cuteframework.locale.collect.ExceptionLanguage;
import io.github.gdrfgdrf.cuteframework.api.PluginManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description 当拥有相同名称的插件尝试注册到 {@link PluginManager} 时抛出，
 * 此时将会放弃注册后来的插件，保留前来的插件
 *
 * @author gdrfgdrf
 * @since 2024/5/5
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

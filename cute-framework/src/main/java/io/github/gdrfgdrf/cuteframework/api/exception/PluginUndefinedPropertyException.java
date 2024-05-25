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
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

/**
 * @description 当插件的 plugin.json 中有必要的属性未定义时抛出
 * @author gdrfgdrf
 * @since 2024/5/5
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

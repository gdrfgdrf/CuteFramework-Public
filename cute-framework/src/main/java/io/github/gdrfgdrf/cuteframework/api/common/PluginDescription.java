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

package io.github.gdrfgdrf.cuteframework.api.common;

import io.github.gdrfgdrf.cuteframework.common.VersionEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.File;

/**
 * @description 插件描述，plugin.json 在代码中的表示，运行时将会把 plugin.json 反序列化为该类，
 * 同时该类也会存储一些 plugin.json 文件之中没有的信息，比如插件文件
 *
 * @author gdrfgdrf
 * @since 2024/5/5
 */
@Data
public class PluginDescription {
    /**
     * 插件名
     */
    private final String name;
    /**
     * 插件主类，在文件中表示为 main-class，代码中表示为 mainClass
     */
    private final String mainClass;
    /**
     * 插件开发所使用的核心版本，
     * 即 {@link VersionEnum#CURRENT}，
     * 在文件中表示为 api-version，代码中表示为 apiVersion，
     * 当文件中定义的 api-version 在 {@link VersionEnum} 中找不到时将会默认定义为 {@link VersionEnum#UNAVAILABLE}
     */
    private final VersionEnum apiVersion;
    /**
     * 插件开发所使用的核心版本，该字段将会和文件中的相同，不会被解析为 {@link VersionEnum}
     */
    private final String rawApiVersion;
    /**
     * 插件作者
     */
    private final String author;
    /**
     * 插件文件
     */
    private File pluginFile;
    /**
     * 加载该插件时使用的类加载器
     */
    private ClassLoader classLoader;

    @JsonCreator
    public PluginDescription(
            @JsonProperty("name") String name,
            @JsonProperty("main-class") String mainClass,
            @JsonProperty(value = "api-version") String apiVersion,
            @JsonProperty("author") String author
    ) {
        this.name = name;
        this.mainClass = mainClass;
        this.apiVersion = VersionEnum.get(apiVersion);
        this.rawApiVersion = apiVersion;
        this.author = author;
    }

    public PluginDescription(String name, String mainClass, VersionEnum apiVersion, String author) {
        this.name = name;
        this.mainClass = mainClass;
        this.apiVersion = apiVersion;
        this.rawApiVersion = apiVersion.getVersion();
        this.author = author;
    }

    /**
     * @description 复制一份 {@link PluginDescription}
     * @return io.github.gdrfgdrf.cuteframework.api.common.PluginDescription
     *         复制的 {@link PluginDescription}
     * @author gdrfgdrf
     * @since 2024/5/5
     */
    public PluginDescription copy() {
        return new PluginDescription(name, mainClass, apiVersion, author);
    }
}

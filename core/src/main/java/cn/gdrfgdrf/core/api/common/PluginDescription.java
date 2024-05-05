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

package cn.gdrfgdrf.core.api.common;

import cn.gdrfgdrf.core.common.VersionEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.File;

/**
 * @Description 插件描述，plugin.json 在代码中的表示，运行时将会把 plugin.json 反序列化为该类，
 * 同时该类也会存储一些 plugin.json 文件之中没有的信息，比如插件文件
 *
 * @Author gdrfgdrf
 * @Date 2024/5/5
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
     * 即 {@link cn.gdrfgdrf.core.common.VersionEnum#CURRENT}，
     * 在文件中表示为 api-version，代码中表示为 apiVersion，
     * 当文件中定义的 api-version 在 {@link VersionEnum} 中找不到时将会默认定义为 {@link VersionEnum#UNDEFINED}
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
     * @Description 复制一份 {@link PluginDescription}
     * @return cn.gdrfgdrf.core.api.common.PluginDescription
     *         复制的 {@link PluginDescription}
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    public PluginDescription copy() {
        return new PluginDescription(name, mainClass, apiVersion, author);
    }
}

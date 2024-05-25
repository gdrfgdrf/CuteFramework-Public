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

package io.github.gdrfgdrf.cuteframework.config;

import io.github.gdrfgdrf.cuteframework.config.common.Config;
import io.github.gdrfgdrf.cuteframework.config.event.ConfigEvent;
import io.github.gdrfgdrf.cuteframework.event.EventManager;
import io.github.gdrfgdrf.cuteframework.utils.FileUtils;
import io.github.gdrfgdrf.cuteframework.utils.asserts.exception.AssertNotNullException;
import io.github.gdrfgdrf.cuteframework.utils.jackson.JacksonUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 * @description 配置管理器
 * @author gdrfgdrf
 * @since 2024/5/4
 */
@Setter
@Getter
public class ConfigManager {
    private static ConfigManager INSTANCE;

    /**
     * 配置实例
     */
    private Config config;

    private ConfigManager() {}

    public static ConfigManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConfigManager();
        }
        return INSTANCE;
    }

    /**
     * @description 加载配置文件
     * @param configFileName
	 *        配置文件的文件名
     * @throws IOException
     *         配置文件 IO 流错误
     * @author gdrfgdrf
     * @since 2024/5/4
     */
    public void load(String configFileName) throws IOException, AssertNotNullException {
        EventManager.getInstance().post(new ConfigEvent.Load.Pre());

        File file = new File(configFileName);
        if (!file.exists()) {
            file.createNewFile();

            this.config = new Config();
            this.config.reset();

            save(configFileName);
        } else {
            this.config = JacksonUtils.readFile(file, Config.class);
        }

        EventManager.getInstance().post(new ConfigEvent.Load.Post(this.config, file));
    }

    /**
     * @description 保存 {@link ConfigManager#config} 到文件
     * @param targetFileName
	 *        需要保存到的配置文件的文件名
     * @throws IOException
     *         配置文件 IO 流错误
     * @author gdrfgdrf
     * @since 2024/5/4
     */
    public void save(String targetFileName) throws IOException, AssertNotNullException {
        if (this.config == null) {
            return;
        }

        File file = new File(targetFileName);
        if (!file.exists()) {
            file.createNewFile();
        }

        EventManager.getInstance().post(new ConfigEvent.Save.Pre(this.config, file));

        Writer writer = FileUtils.getWriter(file);
        writer.write(JacksonUtils.writeJsonString(this.config));
        writer.close();

        EventManager.getInstance().post(new ConfigEvent.Save.Post(this.config, file));
    }

}

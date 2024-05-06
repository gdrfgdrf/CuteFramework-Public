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

package cn.gdrfgdrf.core.config;

import cn.gdrfgdrf.core.config.common.Config;
import cn.gdrfgdrf.core.config.event.ConfigEvent;
import cn.gdrfgdrf.core.event.EventManager;
import cn.gdrfgdrf.core.utils.FileUtils;
import cn.gdrfgdrf.core.utils.asserts.exception.AssertNotNullException;
import cn.gdrfgdrf.core.utils.jackson.JacksonUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 * @Description 配置管理器
 * @Author gdrfgdrf
 * @Date 2024/5/4
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
     * @Description 加载配置文件
     * @param configFileName
	 *        配置文件的文件名
     * @throws IOException
     *         配置文件 IO 流错误
     * @Author gdrfgdrf
     * @Date 2024/5/4
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
     * @Description 保存 {@link ConfigManager#config} 到文件
     * @param targetFileName
	 *        需要保存到的配置文件的文件名
     * @throws IOException
     *         配置文件 IO 流错误
     * @Author gdrfgdrf
     * @Date 2024/5/4
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

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

package cn.gdrfgdrf.core.config.event;

import cn.gdrfgdrf.core.config.common.Config;
import lombok.Getter;

import java.io.File;

/**
 * @Description 配置文件事件
 * @Author gdrfgdrf
 * @Date 2024/5/4
 */
@Getter
public abstract class ConfigEvent {
    public final Config config;
    public final File configFile;

    public ConfigEvent(Config config, File configFile) {
        this.config = config;
        this.configFile = configFile;
    }

    /**
     * @Description 配置文件加载时事件
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    public static abstract class Load extends ConfigEvent {
        public Load(Config config, File configFile) {
            super(config, configFile);
        }

        /**
         * @Description 配置文件加载前事件，此时 {@link ConfigEvent#config} 和 {@link ConfigEvent#configFile} 均为 null
         * @Author gdrfgdrf
         * @Date 2024/5/4
         */
        public static class Pre extends Load {
            public Pre() {
                super(null, null);
            }
        }

        /**
         * @Description 配置文件加载后事件，此时 {@link ConfigEvent#config} 和 {@link ConfigEvent#configFile} 均存在
         * @Author gdrfgdrf
         * @Date 2024/5/4
         */
        public static class Post extends Load {
            public Post(Config config, File configFile) {
                super(config, configFile);
            }
        }
    }

    /**
     * @Description 配置文件保存时时事件，此时 {@link ConfigEvent#config} 和 {@link ConfigEvent#configFile} 均存在
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    public static abstract class Save extends ConfigEvent {
        public Save(Config config, File configFile) {
            super(config, configFile);
        }

        public static class Pre extends Save {
            public Pre(Config config, File configFile) {
                super(config, configFile);
            }
        }

        public static class Post extends Save {
            public Post(Config config, File configFile) {
                super(config, configFile);
            }
        }
    }



}

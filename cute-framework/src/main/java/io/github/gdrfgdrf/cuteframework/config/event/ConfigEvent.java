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

package io.github.gdrfgdrf.cuteframework.config.event;

import lombok.Getter;

import java.io.File;

/**
 * 配置文件事件
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
@Getter
public abstract class ConfigEvent {
    public final Object config;
    public final File configFile;

    public ConfigEvent(Object config, File configFile) {
        this.config = config;
        this.configFile = configFile;
    }

    /**
     * 配置文件加载时事件
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public static abstract class Load extends ConfigEvent {
        public Load(Object config, File configFile) {
            super(config, configFile);
        }

        /**
         * 配置文件加载前事件，此时 {@link ConfigEvent#config} 和 {@link ConfigEvent#configFile} 均为 null
         * @author gdrfgdrf
         * @since v1_0_0_20240525_RELEASE
         */
        public static class Pre extends Load {
            public Pre() {
                super(null, null);
            }
        }

        /**
         * 配置文件加载后事件，此时 {@link ConfigEvent#config} 和 {@link ConfigEvent#configFile} 均存在
         * @author gdrfgdrf
         * @since v1_0_0_20240525_RELEASE
         */
        public static class Post extends Load {
            public Post(Object config, File configFile) {
                super(config, configFile);
            }
        }
    }

    /**
     * 配置文件保存时时事件，此时 {@link ConfigEvent#config} 和 {@link ConfigEvent#configFile} 均存在
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public static abstract class Save extends ConfigEvent {
        public Save(Object config, File configFile) {
            super(config, configFile);
        }

        public static class Pre extends Save {
            public Pre(Object config, File configFile) {
                super(config, configFile);
            }
        }

        public static class Post extends Save {
            public Post(Object config, File configFile) {
                super(config, configFile);
            }
        }
    }



}

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

package io.github.gdrfgdrf.cuteframework.api.base;

import io.github.gdrfgdrf.cuteframework.api.common.PluginDescription;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 插件主类应该继承该抽象类
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
@Getter
@Setter
@EqualsAndHashCode
public abstract class Plugin {
    /**
     * 插件描述文件，plugin.json 在代码中的表示
     */
    private PluginDescription pluginDescription;

    /**
     * 空方法，当插件被启用时调用
     *
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public void onEnable() {}

    /**
     * 空方法，插件开始被核心加载时调用
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public void onLoad() {}

    /**
     * 空方法，当插件需要被停止时调用
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public void onStop() {}

    /**
     * 空方法，当插件被禁用时调用
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public void onDisable() {}
}

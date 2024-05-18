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

package cn.gdrfgdrf.cuteframework.api.exception;

import cn.gdrfgdrf.cuteframework.api.base.Plugin;
import cn.gdrfgdrf.cuteframework.api.common.PluginState;
import cn.gdrfgdrf.cuteframework.exceptionhandler.base.CustomException;
import cn.gdrfgdrf.cuteframework.locale.collect.ExceptionLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 当尝试对插件进行异常的状态改变时抛出，例如 disable 之后直接调用 load 而不是先调用 enable 时抛出
 * @Author gdrfgdrf
 * @Date 2024/5/6
 */
@Getter
@AllArgsConstructor
public class PluginIllegalStateChangeException extends CustomException {
    /**
     * 插件主类实例
     */
    private final Plugin plugin;
    /**
     * 插件目前的状态，由于该类抛出时说明插件状态变化失败，说明该值不变
     */
    private final PluginState currentPluginState;
    /**
     * 期望变化到的状态，但由于是非法的状态变化，该值未能正确生效
     */
    private final PluginState expectPluginState;

    @Override
    public String getI18NMessage() {
        return ExceptionLanguage.PLUGIN_ILLEGAL_STATE_CHANGE
                .get()
                .format(plugin.getPluginDescription().getName(), currentPluginState, expectPluginState)
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "Cannot change the state of the plugin " +
                plugin.getPluginDescription().getName() +
                ", from state " +
                currentPluginState +
                " to state " +
                expectPluginState;
    }
}

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

package cn.gdrfgdrf.core.api.exception;

import cn.gdrfgdrf.core.api.base.Plugin;
import cn.gdrfgdrf.core.api.common.PluginState;
import cn.gdrfgdrf.core.exceptionhandler.base.CustomException;
import cn.gdrfgdrf.core.locale.collect.ExceptionLanguage;
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

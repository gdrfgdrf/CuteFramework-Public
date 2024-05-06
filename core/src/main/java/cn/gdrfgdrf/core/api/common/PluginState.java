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

import java.util.Objects;
import java.util.function.Function;

/**
 * @Description 插件状态，
 * 若插件的状态变化顺序异常则抛出 {@link cn.gdrfgdrf.core.api.exception.PluginIllegalStateChangeException}
 *
 * @Author gdrfgdrf
 * @Date 2024/5/5
 */
public enum PluginState {
    /**
     * 已注册状态，插件没有进行任何的启用，加载，停止，禁用操作时为该状态，
     * 即插件被 {@link cn.gdrfgdrf.core.api.loader.PluginLoader} 加载完成后的状态，
     * 对于一个插件，该状态只能存在一次，
     * 该状态只能变化到 {@link PluginState#ENABLED}
     */
    REGISTERED(ValidatorCollection.REGISTERED_VALIDATOR),
    /**
     * 插件已被启用，此时插件还未被加载
     * 该状态只能变化到 {@link PluginState#LOADED}
     */
    ENABLED(ValidatorCollection.ENABLED_VALIDATOR),
    /**
     * 插件已被加载完成
     * 该状态只能变化到 {@link PluginState#STOPPED}
     */
    LOADED(ValidatorCollection.LOADED_VALIDATOR),
    /**
     * 插件已被停止，此时插件还未被禁用
     * 该状态只能变化到 {@link PluginState#DISABLED}
     */
    STOPPED(ValidatorCollection.STOPPED_VALIDATOR),
    /**
     * 插件已被禁用
     * 该状态只能变化到 {@link PluginState#ENABLED}
     */
    DISABLED(ValidatorCollection.DISABLED_VALIDATOR);

    /**
     * 校验状态变化的合法性，提供一个 {@link PluginState} 参数，返回 boolean 值，
     * 返回 true 合法，false 非法，
     * 提供的 {@link PluginState} 为期望变化到的状态，
     * 该校验器的职责就是校验从当前状态变化到期望的状态是否合法，
     * 比如说从 {@link PluginState#LOADED} 直接变化到 {@link PluginState#DISABLED} 状态就是不合法的，
     * 必须要先变化到 {@link PluginState#STOPPED} 再变化到 {@link PluginState#DISABLED}，
     * 若期望变化到的 {@link PluginState} 和当前的状态相同则返回 false
     */
    private final Function<PluginState, Boolean> validator;

    PluginState(Function<PluginState, Boolean> validator) {
        this.validator = validator;
    }

    /**
     * @Description 校验状态变化顺序是否异常
     * @param targetPluginState
	 *        期望变化到的状态
     * @return boolean
     *         正常返回 true，异常返回 false
     * @Author gdrfgdrf
     * @Date 2024/5/6
     */
    public boolean validate(PluginState targetPluginState) {
        return validator.apply(targetPluginState);
    }

    /**
     * @Description 插件状态变化合法性校验器，由于直接在枚举中写会报 Illegal forward reference 错误，故另写一个类存储
     * 该类中存储的值的具体使用在 {@link PluginState}
     *
     * @Author gdrfgdrf
     * @Date 2024/5/6
     */
    private static final class ValidatorCollection {
        public static final Function<PluginState, Boolean> REGISTERED_VALIDATOR =
                pluginState -> Objects.requireNonNull(pluginState) == PluginState.ENABLED;
        public static final Function<PluginState, Boolean> ENABLED_VALIDATOR =
                pluginState -> Objects.requireNonNull(pluginState) == PluginState.LOADED;
        public static final Function<PluginState, Boolean> LOADED_VALIDATOR =
                pluginState -> Objects.requireNonNull(pluginState) == PluginState.STOPPED;
        public static final Function<PluginState, Boolean> STOPPED_VALIDATOR =
                pluginState -> Objects.requireNonNull(pluginState) == PluginState.DISABLED;
        public static final Function<PluginState, Boolean> DISABLED_VALIDATOR =
                pluginState -> Objects.requireNonNull(pluginState) == PluginState.ENABLED;
    }
}

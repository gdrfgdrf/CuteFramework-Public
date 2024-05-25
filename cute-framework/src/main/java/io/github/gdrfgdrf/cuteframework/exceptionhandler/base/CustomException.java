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

package io.github.gdrfgdrf.cuteframework.exceptionhandler.base;

import io.github.gdrfgdrf.cuteframework.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @description 自定义运行时错误
 * @author gdrfgdrf
 * @since 2024/4/7
 */
@Slf4j
public abstract class CustomException extends Exception {
    /**
     * @description 获取错误信息，优先获取当前语言下的错误信息，若为空则获取默认字符串
     * @return java.lang.String
     *         字符串
     * @author gdrfgdrf
     * @since 2024/4/24
     */
    @Override
    public String getMessage() {
        String message = null;
        try {
            message = getI18NMessage();
        } catch (Exception ignored) {
        }
        if (StringUtils.isBlank(message)) {
            message = getDefaultMessage();
        }
        return message;
    }

    /**
     * @description 获取当前语言下的错误信息，该字符串可能会被显示到前台
     * @return java.lang.String
     *         经过翻译的字符串
     * @author gdrfgdrf
     * @since 2024/4/16
     */
    public abstract String getI18NMessage();
    /**
     * @description 当语言模块的内容未能正确加载时，将调用该方法获取默认字符串，该字符串必须为全英文
     * @return java.lang.String
     *         全英文字符串
     * @author gdrfgdrf
     * @since 2024/4/16
     */
    public abstract String getDefaultMessage();
}

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

package io.github.gdrfgdrf.cuteframework.locale;

import lombok.AccessLevel;
import lombok.Setter;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * @description 可操作的语言字符串，为了避免直接修改 {@link LanguageString}，故使用该类
 * @author gdrfgdrf
 * @since 2024/4/18
 */
public class OperableLanguageString {
    @Setter(value = AccessLevel.PRIVATE)
    private String text;

    public OperableLanguageString(String text) {
        this.text = text;
    }

    /**
     * @description 字符串格式化，和 slf4j 的格式化相同，使用 "{}" 作为占位符，提供的参数将会按照顺序设置到占位符上
     * @param obj
	 *        需要设置到占位符上的实例
     * @return io.github.gdrfgdrf.cuteframework.locale.OperableLanguageString
     *         同一个 {@link OperableLanguageString} 实例
     * @author gdrfgdrf
     * @since 2024/4/18
     */
    public OperableLanguageString format(Object... obj) {
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(text, obj);
        setText(formattingTuple.getMessage());
        return this;
    }

    /**
     * @description 转为字符串
     * @return java.lang.String
     *         字符串
     * @author gdrfgdrf
     * @since 2024/4/18
     */
    public String getString() {
        return text;
    }

    /**
     * @description 转为字符串，直接调用 {@link OperableLanguageString#getString()}
     * @return java.lang.String
     *         字符串
     * @author gdrfgdrf
     * @since 2024/4/19
     */
    @Override
    public String toString() {
        return getString();
    }
}

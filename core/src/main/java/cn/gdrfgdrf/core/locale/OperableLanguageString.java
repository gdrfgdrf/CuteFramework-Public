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

package cn.gdrfgdrf.core.locale;

import lombok.AccessLevel;
import lombok.Setter;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * @Description 可操作的语言字符串，为了避免直接修改 {@link LanguageString}，故使用该类
 * @Author gdrfgdrf
 * @Date 2024/4/18
 */
public class OperableLanguageString {
    @Setter(value = AccessLevel.PRIVATE)
    private String text;

    public OperableLanguageString(String text) {
        this.text = text;
    }

    /**
     * @Description 字符串格式化，和 slf4j 的格式化相同，使用 "{}" 作为占位符，提供的参数将会按照顺序设置到占位符上
     * @param obj
	 *        需要设置到占位符上的实例
     * @return cn.gdrfgdrf.core.locale.OperableLanguageString
     *         同一个 {@link OperableLanguageString} 实例
     * @Author gdrfgdrf
     * @Date 2024/4/18
     */
    public OperableLanguageString format(Object... obj) {
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(text, obj);
        setText(formattingTuple.getMessage());
        return this;
    }

    /**
     * @Description 转为字符串
     * @return java.lang.String
     *         字符串
     * @Author gdrfgdrf
     * @Date 2024/4/18
     */
    public String getString() {
        return text;
    }

    /**
     * @Description 转为字符串，直接调用 {@link OperableLanguageString#getString()}
     * @return java.lang.String
     *         字符串
     * @Author gdrfgdrf
     * @Date 2024/4/19
     */
    @Override
    public String toString() {
        return getString();
    }
}

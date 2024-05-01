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

/**
 * @Description 语言字符串，
 * 所有 {@link cn.gdrfgdrf.core.locale.base.LanguageBlock} 中的语言字符串必须使用该类型
 * 才能被 {@link LanguageLoader} 识别到
 *
 * @Author gdrfgdrf
 * @Date 2024/4/17
 */
public class LanguageString {
    private final String text;

    public LanguageString(String text) {
        this.text = text;
    }

    /**
     * @Description 获取到可操作的语言字符串
     * @return cn.gdrfgdrf.smartuploader.locale.AccessibleLanguageString
     *         可操作的语言字符串
     * @Author gdrfgdrf
     * @Date 2024/4/18
     */
    public OperableLanguageString get() {
        return new OperableLanguageString(text);
    }

}

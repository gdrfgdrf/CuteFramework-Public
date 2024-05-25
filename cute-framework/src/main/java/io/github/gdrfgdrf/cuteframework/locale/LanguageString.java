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

import io.github.gdrfgdrf.cuteframework.locale.base.LanguageBlock;

/**
 * @Description 语言字符串，
 * 所有 {@link LanguageBlock} 中的语言字符串必须使用该类型
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
     * @return io.github.gdrfgdrf.smartuploader.locale.AccessibleLanguageString
     *         可操作的语言字符串
     * @Author gdrfgdrf
     * @Date 2024/4/18
     */
    public OperableLanguageString get() {
        return new OperableLanguageString(text);
    }

}

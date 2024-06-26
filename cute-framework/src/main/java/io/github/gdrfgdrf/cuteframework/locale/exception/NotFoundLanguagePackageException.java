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

package io.github.gdrfgdrf.cuteframework.locale.exception;

import io.github.gdrfgdrf.cuteframework.locale.LanguageLoader;

/**
 * 当 {@link LanguageLoader} 无法找到语言包时抛出
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
public class NotFoundLanguagePackageException extends Exception {
    public NotFoundLanguagePackageException() {
    }
}

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

package io.github.gdrfgdrf.cuteframework.bean.exception;

import io.github.gdrfgdrf.cuteframework.exceptionhandler.base.CustomException;
import io.github.gdrfgdrf.cuteframework.locale.collect.ExceptionLanguage;
import io.github.gdrfgdrf.cuteframework.bean.BeanManager;
import lombok.Getter;

/**
 * Bean 名称冲突移除，
 * 当 {@link BeanManager} 创建 Bean 类时发现已经有了一个同名的 Bean 实例存在时抛出
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
@Getter
public class BeanNameConflictException extends CustomException {
    /**
     * Bean 类
     */
    private final Class<?> beanClass;

    public BeanNameConflictException(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public String getI18NMessage() {
        return ExceptionLanguage.BEAN_NAME_CONFLICT
                .get()
                .format(beanClass.getName())
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "The bean class " + beanClass.getName() + " cannot be created because a bean instance with the same name already exists";
    }
}

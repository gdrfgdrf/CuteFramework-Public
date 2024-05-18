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

package cn.gdrfgdrf.cuteframework.bean.resolver.method.exception;

import cn.gdrfgdrf.cuteframework.bean.resolver.method.base.BeanMethodResolver;
import cn.gdrfgdrf.cuteframework.exceptionhandler.base.CustomException;
import cn.gdrfgdrf.cuteframework.locale.collect.ExceptionLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 当 {@link BeanMethodResolver} 发生错误时
 * {@link cn.gdrfgdrf.cuteframework.bean.resolver.BeanMethodResolverManager} 将会把异常实例包装到该类并抛出
 *
 * @Author gdrfgdrf
 * @Date 2024/5/4
 */
@Getter
@AllArgsConstructor
public class BeanMethodResolverException extends CustomException {
    /**
     * Bean 实例
     */
    private final Object bean;
    /**
     * Bean 解析器实例
     */
    private final BeanMethodResolver resolver;
    /**
     * Bean 解析器抛出的异常实例
     */
    private final Throwable throwable;

    @Override
    public String getI18NMessage() {
        return ExceptionLanguage.BEAN_METHOD_RESOLVER_PROCESSING_ERROR
                .get()
                .format(
                        resolver.getClass().getName(),
                        bean.getClass().getName(),
                        throwable.getMessage()
                )
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "Error occurred when bean method resolver " +
                resolver.getClass().getName() +
                " processed bean " + bean.getClass().getName() +
                ": " +
                throwable.getMessage();
    }
}

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

package cn.gdrfgdrf.core.bean.resolver.exception;

import cn.gdrfgdrf.core.bean.resolver.base.BeanMethodResolver;
import cn.gdrfgdrf.core.exceptionhandler.base.CustomException;
import cn.gdrfgdrf.core.locale.collect.ExceptionLanguage;
import lombok.AllArgsConstructor;

/**
 * @Description 当 {@link cn.gdrfgdrf.core.bean.resolver.base.BeanMethodResolver} 发生错误时
 * {@link cn.gdrfgdrf.core.bean.resolver.BeanMethodResolverManager} 将会把异常实例包装到该类并抛出
 *
 * @Author gdrfgdrf
 * @Date 2024/5/4
 */
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
        return ExceptionLanguage.BEAN_METHOD_RESOLVER_EXCEPTION
                .get()
                .format(
                        resolver.getClass().getSimpleName(),
                        bean.getClass().getSimpleName(),
                        throwable.getMessage()
                )
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "Error occurred when bean method resolver " +
                resolver.getClass().getSimpleName() +
                " processed bean " + bean.getClass().getSimpleName() +
                ": " +
                throwable.getMessage();
    }
}
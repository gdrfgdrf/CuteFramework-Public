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

package cn.gdrfgdrf.core.bean.resolver.clazz.exception;

import cn.gdrfgdrf.core.bean.resolver.clazz.base.BeanClassResolver;
import cn.gdrfgdrf.core.exceptionhandler.base.CustomException;
import cn.gdrfgdrf.core.locale.collect.ExceptionLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 当 {@link cn.gdrfgdrf.core.bean.resolver.clazz.base.BeanClassResolver} 发生错误时
 * {@link cn.gdrfgdrf.core.bean.resolver.clazz.base.BeanClassResolver} 将会把异常实例包装到该类并抛出
 *
 * @Author gdrfgdrf
 * @Date 2024/5/4
 */
@Getter
@AllArgsConstructor
public class BeanClassResolverException extends CustomException {
    /**
     * Bean 实例
     */
    private final Object bean;
    /**
     * Bean 解析器实例
     */
    private final BeanClassResolver resolver;
    /**
     * Bean 解析器抛出的异常实例
     */
    private final Throwable throwable;

    @Override
    public String getI18NMessage() {
        return ExceptionLanguage.BEAN_CLASS_RESOLVER_PROCESSING_ERROR
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
        return "Error occurred when bean class resolver " +
                resolver.getClass().getName() +
                " processed bean " + bean.getClass().getName() +
                ": " +
                throwable.getMessage();
    }
}
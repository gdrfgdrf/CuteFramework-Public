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

import cn.gdrfgdrf.core.exceptionhandler.base.CustomException;
import cn.gdrfgdrf.core.locale.collect.ExceptionLanguage;
import lombok.AllArgsConstructor;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Description 当方法参数和需要参数不相同时抛出
 * @Author gdrfgdrf
 * @Date 2024/5/4
 */
@AllArgsConstructor
public class BeanMethodArgumentTypeMismatchException extends CustomException {
    /**
     * Bean 方法
     */
    private final Method method;
    /**
     * 需要的参数
     */
    private final Class<?>[] need;
    /**
     * Bean 方法所在类
     */
    private final Class<?> clazz;

    @Override
    public String getI18NMessage() {
        return ExceptionLanguage.BEAN_METHOD_ARGUMENT_TYPE_MISMATCH
                .get()
                .format(method, Arrays.toString(need), clazz.getSimpleName())
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "The parameter of the bean method " +
                method.getName() +
                " must be " +
                Arrays.toString(need) +
                ", the class " +
                clazz.getSimpleName();
    }
}

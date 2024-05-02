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

package cn.gdrfgdrf.core.bean.exception;

import cn.gdrfgdrf.core.exceptionhandler.base.CustomException;
import cn.gdrfgdrf.core.locale.collect.ExceptionLanguage;

/**
 * @Description Bean 名称冲突移除，
 * 当 {@link cn.gdrfgdrf.core.bean.BeanManager} 创建 Bean 类时发现已经有了一个同名的 Bean 实例存在时抛出
 * @Author gdrfgdrf
 * @Date 2024/5/2
 */
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
        return ExceptionLanguage.BEAN_NAME_CONFLICT_EXCEPTION
                .get()
                .format(beanClass.getName())
                .getString();
    }

    @Override
    public String getDefaultMessage() {
        return "The bean class " + beanClass.getName() + " cannot be created because a bean instance with the same name already exists";
    }
}

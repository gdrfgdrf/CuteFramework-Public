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

package cn.gdrfgdrf.smartuploader.base;

import java.lang.annotation.Annotation;

/**
 * @Description 管理器基类，继承该类可实现自动化的获取需要实例化的类
 * @param <T> 需要实例化的类，需要是一个接口或抽象类
 * @param <A> 注解类，该注解需要给 {@link BaseManager} 的子类，注解内必须有一个方法叫 classes，该方法返回所有实现或继承了 <T> 类，
 *            这些类将会提供给 {@link BaseManager#instantiate(Class)} 进行实例化
 *
 * @Author gdrfgdrf
 * @Date 2024/4/6
 */
public abstract class BaseManager<T, A extends Annotation> {
    /**
     * @Description 获取 <A> 注解中方法 classes 的返回值，
     * 该返回值必须是一个数组形式的 <T> 的实现类或子类，
     * 并进行循环提供给 {@link BaseManager#instantiate(Class)} 进行实例化
     *
     * @Author gdrfgdrf
     * @Date 2024/4/6
     */
    protected void instantiate() {
        try {


        } catch (Exception e) {

        }
    }

    /**
     * @Description 实例化某个类
     * @param clazz
	 *        类对象
     * @Author gdrfgdrf
     * @Date 2024/4/6
     */
    public abstract void instantiate(Class<? extends T> clazz);
}

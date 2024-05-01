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

package cn.gdrfgdrf.core.base;

import cn.gdrfgdrf.core.classinjector.ClassInjector;
import cn.gdrfgdrf.core.utils.TypeParameterMatcher;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 管理器基类，继承该类可实现自动化的获取需要实例化的类
 * 其他单例模式的管理器例如 {@link cn.gdrfgdrf.core.bean.BeanManager}，都不继承该类
 *
 * @param <T> 需要实例化的类，需要是一个接口或抽象类
 * @param <A> 注解类，该注解需要给 {@link BaseManager} 的子类，注解内必须有一个方法叫 classes，该方法返回所有实现或继承了 <T> 类，
 *            这些类将会提供给 {@link BaseManager#instantiate(Class)} 进行实例化
 *
 * @Author gdrfgdrf
 * @Date 2024/4/6
 */
@Slf4j
public abstract class BaseManager<T, A extends Annotation> {
    private final Map<Class<? extends T>, T> T_INSTANCE_MAP = new HashMap<>();

    /**
     * @Description 获取 <A> 注解中方法 classes 的返回值，
     * 该返回值必须是一个数组形式的 <T> 的实现类或子类，
     * 并进行循环提供给 {@link BaseManager#instantiate(Class)} 进行实例化
     *
     * @Author gdrfgdrf
     * @Date 2024/4/6
     */
    @SuppressWarnings("unchecked")
    protected void instantiate() {
        try {
            TypeParameterMatcher matcher = TypeParameterMatcher.find(this, BaseManager.class, "A");
            Class<A> type = (Class<A>) matcher.getType();
            Method method = type.getMethod("classes");
            A annotationObj = getClass().getAnnotation(type);
            if (annotationObj == null) {
                return;
            }

            T[] array = (T[]) method.invoke(annotationObj);
            for (T t : array) {
                instantiate((Class<? extends T>) t);
            }
        } catch (Exception e) {
            log.error("Error when get a array from a annotation", e);
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

    /**
     * @Description 对 <T> 进行的单例模式，保证一个 <T> 的实现类只能有一个实例
     * @param clazz
	 *        <T> 类
     * @return T
     *         <T> 对象
     * @throws InvocationTargetException
     *         {@link ClassInjector} 无法创建对象
     * @throws NoSuchMethodException
     *         {@link ClassInjector} 无法创建对象
     * @throws InstantiationException
     *         {@link ClassInjector} 无法创建对象
     * @throws IllegalAccessException
     *         {@link ClassInjector} 无法创建对象
     * @Author gdrfgdrf
     * @Date 2024/4/17
     */
    @SuppressWarnings("unchecked")
    protected T createInstance(Class<? extends T> clazz) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (T_INSTANCE_MAP.containsKey(clazz)) {
            return T_INSTANCE_MAP.get(clazz);
        }
        T instance = (T) ClassInjector.getInstance().createInstance(clazz);
        T_INSTANCE_MAP.put(clazz, instance);

        return instance;
    }
}
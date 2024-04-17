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

package cn.gdrfgdrf.smartuploader.classinjector;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 类注入器，通过动态代理创建对象，
 * 若要使用该类，动态代理的拦截器，比如 Cglib 的 MethodInterceptor，
 * 都必须存在一个返回对象实例，参数为类对象，名称为 createInstance 的公开方法
 *
 * 例子：
 *      public static Object createInstance(Class<?> clazz) {
 *          ...
 *      }
 *
 * @Author gdrfgdrf
 * @Date 2024/4/7
 */
public class ClassInjector {
    private static ClassInjector INSTANCE;

    /**
     * 类对象到 createInstance 方法的映射，
     * 当
     */
    private final Map<Class, Method> CREATE_INSTANCE_METHOD_MAP = new ConcurrentHashMap<>();

    private ClassInjector() {}

    /**
     * @Description 单例模式，获取 {@link ClassInjector} 实例
     * @return cn.gdrfgdrf.smartuploader.classinjector.ClassInjector
     *         {@link ClassInjector} 实例
     * @Author gdrfgdrf
     * @Date 2024/4/7
     */
    public static ClassInjector getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClassInjector();
        }
        return INSTANCE;
    }

    public void registerInjector(Class<?> injector) {

    }

    /**
     * @Description 通过动态代理创建一个对象，若找不到指定的动态代理创建对象的方法，
     * @param clazz
	 *
     * @return void
     *
     * @throws
     *
     * @Author gdrfgdrf
     * @Date 2024/4/7
     */
    public void createInstance(Class<?> clazz) {

    }



}

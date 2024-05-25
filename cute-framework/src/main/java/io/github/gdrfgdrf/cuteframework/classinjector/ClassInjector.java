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

package io.github.gdrfgdrf.cuteframework.classinjector;

import io.github.gdrfgdrf.cuteframework.utils.asserts.AssertUtils;
import io.github.gdrfgdrf.cuteframework.utils.asserts.exception.AssertNotNullException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description 类注入器，通过动态代理创建对象，
 * 若要使用该类，动态代理的拦截器，比如 Cglib 的 MethodInterceptor，
 * 都必须存在一个返回对象实例，参数为类对象、构造函数参数类型、构造函数参数实例，名称为 createInstance 的公开方法
 * 例子：假设返回 Object 对象，接受所有类
 *      public static Object createInstance(Class<?> clazz, Class<?>[] argumentTypes, Object... arguments) {
 *          ...
 *      }
 *
 * @author gdrfgdrf
 * @since 2024/4/7
 */
public class ClassInjector {
    private static ClassInjector INSTANCE;

    /**
     * createInstance 方法需要的类型到 createInstance 方法的映射，
     */
    private final Map<Class<?>, Method> CREATE_INSTANCE_METHOD_MAP = new ConcurrentHashMap<>();

    private ClassInjector() {}

    /**
     * @description 单例模式，获取 {@link ClassInjector} 实例
     * @return io.github.gdrfgdrf.cuteframework.classinjector.ClassInjector
     *         {@link ClassInjector} 实例
     * @author gdrfgdrf
     * @since 2024/4/7
     */
    public static ClassInjector getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClassInjector();
        }
        return INSTANCE;
    }

    /**
     * @description 注册类注入器，
     * 将通过 {@link Class#getDeclaredMethod(String, Class[])} 获取 createInstance 方法，
     * createInstance 方法的参数必须是其返回类型的类对象
     *
     * @param type
     *        createInstance 方法的返回类型
     * @param injector
	 *        createInstance 方法所在的类
     * @throws AssertNotNullException
     *         当 type 或 injector 为 null 时抛出
     * @throws NoSuchMethodException
     *         无法找到 createInstance 方法
     * @author gdrfgdrf
     * @since 2024/4/17
     */
    public void registerInjector(Class<?> type, Class<?>[] argumentTypes, Class<?> injector) throws
            AssertNotNullException,
            NoSuchMethodException
    {
        AssertUtils.notNull("type", type);
        AssertUtils.notNull("injector", injector);

        Method method = injector.getDeclaredMethod("createInstance", type, Class[].class, Object[].class);
        CREATE_INSTANCE_METHOD_MAP.put(type, method);
    }

    /**
     * @description 移除类注入器
     * @param type
	 *        createInstance 方法的返回类型
     * @throws AssertNotNullException
     *         当 type 为 null 时抛出
     * @author gdrfgdrf
     * @since 2024/4/17
     */
    public void unregisterInjector(Class<?> type) throws AssertNotNullException {
        AssertUtils.notNull("type", type);

        CREATE_INSTANCE_METHOD_MAP.remove(type);
    }

    public Object createInstance(Class<?> clazz) throws
            AssertNotNullException,
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException
    {
        return createInstance(clazz, null);
    }

    /**
     * @description 通过动态代理创建一个对象，
     * 若找不到指定的动态代理创建对象的方法，
     * 则使用 {@link java.lang.reflect.Constructor#newInstance(Object...)} 创建
     * 若找到则会调用创建对象的方法并返回
     *
     * @return java.lang.Object
     *         最终的对象
     * @param clazz
	 *        需要创建对象的类
     * @param argumentTypes
     *        构造函数需要的参数类型
     * @param arguments
     *        构造函数需要的参数实例
     * @throws AssertNotNullException
     *         当 clazz 为 null 时抛出
     * @throws NoSuchMethodException
     *         无法找到指定的动态代理创建对象的方法，尝试使用无参构造函数，但无法获取到无参构造函数时抛出
     * @throws InvocationTargetException
     *         动态代理创建对象的方法出错或无参构造函数出错时抛出
     * @throws IllegalAccessException
     *         可以找到动态代理创建对象的方法或无参构造函数，但因为访问权限而无法使用
     * @author gdrfgdrf
     * @since 2024/4/7
     */
    public Object createInstance(Class<?> clazz, Class<?>[] argumentTypes, Object... arguments) throws
            AssertNotNullException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException
    {
        AssertUtils.notNull("class", clazz);

        Method method = CREATE_INSTANCE_METHOD_MAP.get(clazz);
        if (method == null) {
            if (argumentTypes == null || arguments == null || arguments.length == 0) {
                return clazz.getDeclaredConstructor().newInstance();
            }
            return clazz.getDeclaredConstructor(argumentTypes).newInstance(arguments);
        }

        return method.invoke(null, clazz, argumentTypes, arguments);
    }



}

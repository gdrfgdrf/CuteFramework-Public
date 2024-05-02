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

package cn.gdrfgdrf.core.bean;

import cn.gdrfgdrf.core.bean.annotation.Component;
import cn.gdrfgdrf.core.bean.exception.BeanNameConflictException;
import cn.gdrfgdrf.core.utils.StringUtils;
import cn.gdrfgdrf.core.utils.asserts.AssertUtils;
import cn.gdrfgdrf.core.utils.asserts.exception.AssertNotNullException;
import cn.gdrfgdrf.core.utils.stack.StackUtils;
import cn.gdrfgdrf.core.utils.stack.exception.StackIllegalArgumentException;
import cn.gdrfgdrf.core.utils.stack.exception.StackIllegalOperationException;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description Bean 管理器，对 Bean 进行创建，移除等操作
 * @Author gdrfgdrf
 * @Date 2024/4/6
 */
public class BeanManager {
    private static BeanManager INSTANCE;

    /**
     * Bean 名称到 Bean 实例的映射
     */
    private final Map<String, Object> BEAN_MAP = new ConcurrentHashMap<>();

    private BeanManager() {}

    /**
     * @Description 单例模式，获取 {@link BeanManager} 实例
     * @return cn.gdrfgdrf.smartuploader.bean.BeanManager
     *         {@link BeanManager} 实例
     * @Author gdrfgdrf
     * @Date 2024/4/20
     */
    public static BeanManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BeanManager();
        }
        return INSTANCE;
    }

    /**
     * @Description 开始 Bean 的创建流程，该方法仅允许 cn.gdrfgdrf.smartuploader.SmartUploader 的 run 方法调用
     * 插件会被最先加载，但不最先加载插件的 Bean，
     * 最先创建核心 Bean，之后再创建 cn.gdrfgdrf.smartuploader 下的 Bean，最后再创建插件的 Bean
     * @throws cn.gdrfgdrf.core.utils.stack.exception.StackIllegalOperationException
     *         当不被允许的类或方法调用该方法时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/1
     */
    public void createByScanning() throws
            StackIllegalOperationException,
            StackIllegalArgumentException,
            AssertNotNullException,
            BeanNameConflictException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException
    {
        StackUtils.onlyMethod("cn.gdrfgdrf.smartuploader.SmartUploader", "run");

        createCoreBeans();
        createImplBeans();
    }

    /**
     * @Description 创建核心 Bean，该方法仅允许 cn.gdrfgdrf.core.bean.BeanManager 的 createByScanning 方法调用
     * @throws cn.gdrfgdrf.core.utils.stack.exception.StackIllegalOperationException
     *         当不被允许的类或方法调用该方法时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/2
     */
    private void createCoreBeans() throws
            StackIllegalOperationException,
            StackIllegalArgumentException,
            AssertNotNullException,
            BeanNameConflictException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException
    {
        StackUtils.onlyMethod("cn.gdrfgdrf.core.bean.BeanManager", "createByScanning");

        Reflections reflections = new Reflections("cn.gdrfgdrf.core");
        Set<Class<?>> components = reflections.getTypesAnnotatedWith(Component.class);
        for (Class<?> component : components) {
            create(component);
        }
    }

    /**
     * @Description 创建 cn.gdrfgdrf.smartuploader 下的 Bean，
     * 该方法仅允许 cn.gdrfgdrf.core.bean.BeanManager 的 createByScanning 方法调用
     * @throws cn.gdrfgdrf.core.utils.stack.exception.StackIllegalOperationException
     *         当不被允许的类或方法调用该方法时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/2
     */
    private void createImplBeans() throws
            StackIllegalOperationException,
            StackIllegalArgumentException,
            AssertNotNullException,
            BeanNameConflictException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException
    {
        StackUtils.onlyMethod("cn.gdrfgdrf.core.bean.BeanManager", "createByScanning");

        Reflections reflections = new Reflections("cn.gdrfgdrf.smartuploader");
        Set<Class<?>> components = reflections.getTypesAnnotatedWith(Component.class);
        for (Class<?> component : components) {
            create(component);
        }
    }

    /**
     * @Description 创建 Bean，若 Bean 名称在 {@link BeanManager#BEAN_MAP} 中存在，则直接抛出
     * @param beanClass
	 *        Bean 类
     * @throws cn.gdrfgdrf.core.utils.asserts.exception.AssertNotNullException
     *         beanClass 为 null 时抛出
     * @throws BeanNameConflictException
     *         已经有了一个同名的 Bean 实例存在时抛出 {@link BeanNameConflictException}
     * @throws NoSuchMethodException
     *         无法从 Bean 类中找到构造函数
     * @throws InvocationTargetException
     *         Bean 类的构造函数有异常抛出
     * @throws InstantiationException
     *         Bean 类的构造函数有异常抛出
     * @throws IllegalAccessException
     *         因为访问权限而无法调用 Bean 类的构造函数
     * @Author gdrfgdrf
     * @Date 2024/5/2
     */
    public void create(Class<?> beanClass) throws
            AssertNotNullException,
            BeanNameConflictException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException
    {
        AssertUtils.notNull("bean class", beanClass);

        String name = beanClass.getSimpleName();
        Component component = beanClass.getAnnotation(Component.class);
        if (component != null) {
            if (!StringUtils.isBlank(component.name())) {
                name = component.name();
            }
        }
        if (BEAN_MAP.containsKey(name)) {
            throw new BeanNameConflictException(beanClass);
        }

        Object obj = beanClass.getDeclaredConstructor().newInstance();
        BEAN_MAP.put(name, obj);
    }
}

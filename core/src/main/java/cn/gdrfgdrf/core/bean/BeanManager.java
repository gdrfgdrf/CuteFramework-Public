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

import cn.gdrfgdrf.core.api.PluginManager;
import cn.gdrfgdrf.core.api.base.Plugin;
import cn.gdrfgdrf.core.bean.annotation.Component;
import cn.gdrfgdrf.core.bean.compare.OrderComparator;
import cn.gdrfgdrf.core.bean.event.BeanEvent;
import cn.gdrfgdrf.core.bean.exception.BeanNameConflictException;
import cn.gdrfgdrf.core.bean.resolver.BeanMethodResolverManager;
import cn.gdrfgdrf.core.bean.resolver.clazz.BeanClassResolverManager;
import cn.gdrfgdrf.core.bean.resolver.clazz.annotation.BeanClassResolverAnnotation;
import cn.gdrfgdrf.core.bean.resolver.clazz.base.BeanClassResolver;
import cn.gdrfgdrf.core.bean.resolver.clazz.exception.BeanClassResolverException;
import cn.gdrfgdrf.core.bean.resolver.method.annotation.BeanMethodResolverAnnotation;
import cn.gdrfgdrf.core.bean.resolver.method.base.BeanMethodResolver;
import cn.gdrfgdrf.core.bean.resolver.method.exception.BeanMethodResolverException;
import cn.gdrfgdrf.core.classinjector.ClassInjector;
import cn.gdrfgdrf.core.event.EventManager;
import cn.gdrfgdrf.core.utils.ClassUtils;
import cn.gdrfgdrf.core.utils.StringUtils;
import cn.gdrfgdrf.core.utils.asserts.AssertUtils;
import cn.gdrfgdrf.core.utils.asserts.exception.AssertNotNullException;
import cn.gdrfgdrf.core.utils.stack.StackUtils;
import cn.gdrfgdrf.core.utils.stack.exception.StackIllegalArgumentException;
import cn.gdrfgdrf.core.utils.stack.exception.StackIllegalOperationException;

import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
     * @return cn.gdrfgdrf.core.bean.BeanManager
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
     * @Description 获取 Bean 实例
     * @param name
	 *        Bean 名称
     * @return java.lang.Object
     *         Bean 实例
     * @throws AssertNotNullException
     *         当 name 为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/10
     */
    public Object getBean(String name) throws AssertNotNullException {
        AssertUtils.notNull("bean name", name);
        return BEAN_MAP.get(name);
    }

    /**
     * @Description 开始 Bean 的创建流程，该方法仅允许 cn.gdrfgdrf.smartcoreimpl.SmartCoreImpl 的 run 方法调用
     * 插件会被最先加载，但不最先加载插件的 Bean，
     * 最先创建核心 Bean，之后再创建 cn.gdrfgdrf.smartcoreimpl 下的 Bean，
     * 最后再由 cn.gdrfgdrf.smartcoreimpl.SmartCoreImpl 的 run 方法调用 {@link BeanManager#startCreatingPluginBeans()}
     * 以创建插件的 Bean
     *
     * @throws cn.gdrfgdrf.core.utils.stack.exception.StackIllegalOperationException
     *         当不被允许的类或方法调用该方法时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/1
     */
    public void startCreating() throws
            StackIllegalOperationException,
            StackIllegalArgumentException,
            AssertNotNullException,
            BeanNameConflictException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException,
            BeanClassResolverException,
            BeanMethodResolverException
    {
        StackUtils.onlyMethod("cn.gdrfgdrf.smartcoreimpl.SmartCoreImpl", "run");

        EventManager.getInstance().post(new BeanEvent.LoadAll.Pre());

        createCoreBeans();
        createImplBeans();

        EventManager.getInstance().post(new BeanEvent.LoadAll.Post());
    }

    /**
     * @Description 创建插件的 Bean
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    public void startCreatingPluginBeans() throws StackIllegalOperationException, AssertNotNullException, StackIllegalArgumentException, BeanClassResolverException, BeanNameConflictException, BeanMethodResolverException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        StackUtils.onlyMethod("cn.gdrfgdrf.smartcoreimpl.SmartCoreImpl", "run");

        for (Map.Entry<String, Plugin> pluginEntry : PluginManager.getInstance().getPlugins().entrySet()) {
            String name = pluginEntry.getKey();
            Plugin plugin = pluginEntry.getValue();

            ClassLoader classLoader = plugin.getPluginDescription().getClassLoader();
            String mainClassPackage = plugin.getClass().getPackageName();
            String mainClassLastPackage = mainClassPackage.substring(0, mainClassPackage.lastIndexOf("."));

            Set<Class<?>> components = new LinkedHashSet<>();
            ClassUtils.searchJar(
                    classLoader,
                    mainClassLastPackage,
                    clazz -> !clazz.isAnnotation() && ClassUtils.hasAnnotation(clazz, Component.class),
                    components
            );
            components = components.stream()
                    .sorted(OrderComparator.getInstance())
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            for (Class<?> component : components) {
                create(component);
            }
        }
    }

    /**
     * @Description 创建核心 Bean，该方法仅允许 cn.gdrfgdrf.core.bean.BeanManager 的 startCreating 方法调用
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
            IllegalAccessException,
            BeanClassResolverException,
            BeanMethodResolverException
    {
        StackUtils.onlyMethod("cn.gdrfgdrf.core.bean.BeanManager", "startCreating");

        Set<Class<?>> components = new LinkedHashSet<>();
        ClassUtils.searchJar(
                Thread.currentThread().getContextClassLoader(),
                "cn.gdrfgdrf.core",
                clazz -> !clazz.isAnnotation() && ClassUtils.hasAnnotation(clazz, Component.class),
                components
        );
        components = components.stream()
                .sorted(OrderComparator.getInstance())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (Class<?> component : components) {
            create(component);
        }
    }

    /**
     * @Description 创建 cn.gdrfgdrf.smartcoreimpl 下的 Bean，
     * 该方法仅允许 cn.gdrfgdrf.core.bean.BeanManager 的 startCreating 方法调用
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
            IllegalAccessException,
            BeanClassResolverException,
            BeanMethodResolverException
    {
        StackUtils.onlyMethod("cn.gdrfgdrf.core.bean.BeanManager", "startCreating");

        Set<Class<?>> components = new LinkedHashSet<>();
        ClassUtils.searchJar(
                Thread.currentThread().getContextClassLoader(),
                "cn.gdrfgdrf.smartcoreimpl",
                clazz -> !clazz.isAnnotation() && ClassUtils.hasAnnotation(clazz, Component.class),
                components
        );
        components = components.stream()
                .sorted(OrderComparator.getInstance())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (Class<?> component : components) {
            create(component);
        }
    }

    /**
     * @Description 创建 Bean，并调用对应的 {@link BeanMethodResolver}
     * 若 Bean 名称在 {@link BeanManager#BEAN_MAP} 中存在，则直接抛出 {@link BeanNameConflictException}，
     * 若 Bean 类型为 {@link BeanMethodResolver}，则跳过调用解析器并注册到 {@link BeanMethodResolverManager}
     *
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
     * @throws BeanClassResolverException
     *         当 Bean 类解析器发生错误时抛出
     * @throws BeanMethodResolverException
     *         当 Bean 方法解析器发生错误时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/2
     */
    public void create(Class<?> beanClass) throws
            AssertNotNullException,
            BeanNameConflictException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException,
            BeanClassResolverException,
            BeanMethodResolverException
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

        EventManager.getInstance().post(new BeanEvent.Load.Pre(null, name));

        Object obj = ClassInjector.getInstance().createInstance(beanClass);
        BEAN_MAP.put(name, obj);

        if (!(obj instanceof BeanMethodResolver) && !(obj instanceof BeanClassResolver)) {
            BeanClassResolverManager.getInstance().resolve(obj);
            BeanMethodResolverManager.getInstance().resolve(obj);
        } else {
            if (obj instanceof BeanMethodResolver resolver) {
                registerBeanMethodResolver(resolver);
            }
            if (obj instanceof BeanClassResolver resolver) {
                registerBeanClassResolver(resolver);
            }
        }

        EventManager.getInstance().post(new BeanEvent.Load.Post(obj, name));
    }

    /**
     * @Description 注册 Bean 类解析器到 {@link BeanClassResolverManager}
     * @param resolver
     *        Bean 方法解析器实例
     * @throws AssertNotNullException
     *         当 Bean 类解析器没有 {@link BeanClassResolverAnnotation} 注解时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/6
     */
    private void registerBeanClassResolver(BeanClassResolver resolver) throws AssertNotNullException {
        BeanClassResolverAnnotation annotation = resolver.getClass().getAnnotation(BeanClassResolverAnnotation.class);
        AssertUtils.notNull("bean class resolver annotation", annotation);

        Class<? extends Annotation> targetClassAnnotation = annotation.targetClassAnnotation();
        BeanClassResolverManager.getInstance().registerBeanClassResolver(targetClassAnnotation, resolver);
    }

    /**
     * @Description 注册 Bean 方法解析器到 {@link BeanMethodResolverManager}
     * @param resolver
	 *        Bean 方法解析器实例
     * @throws AssertNotNullException
     *         当 Bean 方法解析器没有 {@link BeanMethodResolverAnnotation} 注解时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    private void registerBeanMethodResolver(BeanMethodResolver resolver) throws AssertNotNullException {
        BeanMethodResolverAnnotation annotation = resolver.getClass().getAnnotation(BeanMethodResolverAnnotation.class);
        AssertUtils.notNull("bean method resolver annotation", annotation);

        Class<? extends Annotation> targetMethodAnnotation = annotation.targetMethodAnnotation();
        BeanMethodResolverManager.getInstance().registerBeanMethodResolver(targetMethodAnnotation, resolver);
    }
}

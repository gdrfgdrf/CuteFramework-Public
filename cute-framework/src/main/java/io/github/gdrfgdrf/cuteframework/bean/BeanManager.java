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

package io.github.gdrfgdrf.cuteframework.bean;

import io.github.gdrfgdrf.cuteframework.CuteFramework;
import io.github.gdrfgdrf.cuteframework.api.PluginManager;
import io.github.gdrfgdrf.cuteframework.api.base.Plugin;
import io.github.gdrfgdrf.cuteframework.bean.annotation.Component;
import io.github.gdrfgdrf.cuteframework.bean.compare.OrderComparator;
import io.github.gdrfgdrf.cuteframework.bean.event.BeanEvent;
import io.github.gdrfgdrf.cuteframework.bean.exception.BeanNameConflictException;
import io.github.gdrfgdrf.cuteframework.bean.resolver.BeanMethodResolverManager;
import io.github.gdrfgdrf.cuteframework.bean.resolver.clazz.BeanClassResolverManager;
import io.github.gdrfgdrf.cuteframework.bean.resolver.clazz.annotation.BeanClassResolverAnnotation;
import io.github.gdrfgdrf.cuteframework.bean.resolver.clazz.base.BeanClassResolver;
import io.github.gdrfgdrf.cuteframework.bean.resolver.clazz.exception.BeanClassResolverException;
import io.github.gdrfgdrf.cuteframework.bean.resolver.method.annotation.BeanMethodResolverAnnotation;
import io.github.gdrfgdrf.cuteframework.bean.resolver.method.base.BeanMethodResolver;
import io.github.gdrfgdrf.cuteframework.bean.resolver.method.exception.BeanMethodResolverException;
import io.github.gdrfgdrf.cuteframework.classinjector.ClassInjector;
import io.github.gdrfgdrf.cuteframework.event.EventManager;
import io.github.gdrfgdrf.cuteframework.utils.ClassUtils;
import io.github.gdrfgdrf.cuteframework.utils.StringUtils;
import io.github.gdrfgdrf.cuteframework.utils.asserts.AssertUtils;
import io.github.gdrfgdrf.cuteframework.utils.asserts.exception.AssertNotNullException;
import io.github.gdrfgdrf.cuteframework.utils.stack.StackUtils;
import io.github.gdrfgdrf.cuteframework.utils.stack.exception.StackIllegalArgumentException;
import io.github.gdrfgdrf.cuteframework.utils.stack.exception.StackIllegalOperationException;
import lombok.Getter;

import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Bean 管理器，对 Bean 进行创建，移除等操作
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
public class BeanManager {
    private static BeanManager INSTANCE;

    /**
     * 使用该框架的主类，该类所在的包下所有的 Bean 类将会在核心 Bean 类加载完成后加载
     */
    @Getter
    private final Class<?> mainApplicationClass;
    /**
     * Bean 名称到 Bean 实例的映射
     */
    private final Map<String, Object> BEAN_MAP = new ConcurrentHashMap<>();

    private BeanManager(Class<?> mainApplicationClass) throws StackIllegalOperationException, AssertNotNullException, StackIllegalArgumentException {
        StackUtils.onlyMethod(BeanManager.class, "initialize");
        this.mainApplicationClass = mainApplicationClass;
    }

    /**
     * 进行 Bean 管理器的实例化，
     * 提供的 mainApplicationClass 将会设置到 {@link BeanManager#mainApplicationClass}，
     * 当 Bean 创建流程开始时，将会在核心 Bean 加载完成后加载 mainApplicationClass 所在包下的所有 Bean 类，加载将会进行迭代加载
     *
     * @param mainApplicationClass
	 *        使用该框架的主类
     * @throws StackIllegalOperationException
     *         当不被允许的类或方法调用该方法时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public static void initialize(Class<?> mainApplicationClass) throws StackIllegalOperationException, AssertNotNullException, StackIllegalArgumentException {
        StackUtils.onlyMethod("io.github.gdrfgdrf.cuteframework.CuteFramework", "run");

        if (INSTANCE != null) {
            return;
        }
        INSTANCE = new BeanManager(mainApplicationClass);
    }

    /**
     * 单例模式，获取 {@link BeanManager} 实例，
     * 当 {@link BeanManager#initialize(Class)} 还未被 {@link CuteFramework} 调用时，
     * 该方法返回的值将会为 null
     *
     * @return io.github.gdrfgdrf.cuteframework.bean.BeanManager
     *         {@link BeanManager} 实例
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public static BeanManager getInstance() {
        return INSTANCE;
    }

    /**
     * 获取 Bean 实例
     * @param name
	 *        Bean 名称
     * @return java.lang.Object
     *         Bean 实例
     * @throws AssertNotNullException
     *         当 name 为 null 时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public Object getBean(String name) throws AssertNotNullException {
        AssertUtils.notNull("bean name", name);
        return BEAN_MAP.get(name);
    }

    /**
     * 开始 Bean 的创建流程，该方法仅允许 io.github.gdrfgdrf.cuteframework.CuteFramework 的 run 方法调用
     * 插件会被最先加载，但不最先加载插件的 Bean，
     * 最先创建核心 Bean，之后再创建 {@link BeanManager#mainApplicationClass} 下的 Bean，
     * 最后再由 {@link CuteFramework} 的 run 方法调用 {@link BeanManager#startCreatingPluginBeans()}
     * 以创建插件的 Bean
     *
     * @throws StackIllegalOperationException
     *         当不被允许的类或方法调用该方法时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
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
        StackUtils.onlyMethod("io.github.gdrfgdrf.cuteframework.CuteFramework", "run");

        EventManager.getInstance().post(new BeanEvent.LoadAll.Pre());

        createCoreBeans();
        createImplBeans();

        EventManager.getInstance().post(new BeanEvent.LoadAll.Post());
    }

    /**
     * 创建插件的 Bean
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public void startCreatingPluginBeans() throws
            StackIllegalOperationException,
            AssertNotNullException,
            StackIllegalArgumentException,
            BeanClassResolverException,
            BeanNameConflictException,
            BeanMethodResolverException,
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException
    {
        StackUtils.onlyMethod("io.github.gdrfgdrf.cuteframework.CuteFramework", "run");

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
     * 创建核心 Bean，该方法仅允许 io.github.gdrfgdrf.cuteframework.bean.BeanManager 的 startCreating 方法调用
     * @throws StackIllegalOperationException
     *         当不被允许的类或方法调用该方法时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
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
        StackUtils.onlyMethod("io.github.gdrfgdrf.cuteframework.bean.BeanManager", "startCreating");

        Set<Class<?>> components = new LinkedHashSet<>();
        ClassUtils.searchJar(
                BeanManager.class.getClassLoader(),
                "io.github.gdrfgdrf.cuteframework",
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
     * 创建 {@link BeanManager#mainApplicationClass} 下的 Bean，
     * 该方法仅允许 {@link BeanManager#startCreating()} 方法调用
     * @throws StackIllegalOperationException
     *         当不被允许的类或方法调用该方法时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
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
        StackUtils.onlyMethod("io.github.gdrfgdrf.cuteframework.bean.BeanManager", "startCreating");

        Set<Class<?>> components = new LinkedHashSet<>();
        ClassUtils.searchJar(
                BeanManager.class.getClassLoader(),
                mainApplicationClass.getPackageName(),
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
     * 创建 Bean，并调用对应的 {@link BeanMethodResolver}
     * 若 Bean 名称在 {@link BeanManager#BEAN_MAP} 中存在，则直接抛出 {@link BeanNameConflictException}，
     * 若 Bean 类型为 {@link BeanMethodResolver}，则跳过调用解析器并注册到 {@link BeanMethodResolverManager}
     *
     * @param beanClass
	 *        Bean 类
     * @throws AssertNotNullException
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
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
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
     * 注册 Bean 类解析器到 {@link BeanClassResolverManager}
     * @param resolver
     *        Bean 方法解析器实例
     * @throws AssertNotNullException
     *         当 Bean 类解析器没有 {@link BeanClassResolverAnnotation} 注解时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    private void registerBeanClassResolver(BeanClassResolver resolver) throws AssertNotNullException {
        BeanClassResolverAnnotation annotation = resolver.getClass().getAnnotation(BeanClassResolverAnnotation.class);
        AssertUtils.notNull("bean class resolver annotation", annotation);

        Class<? extends Annotation> targetClassAnnotation = annotation.targetClassAnnotation();
        BeanClassResolverManager.getInstance().registerBeanClassResolver(targetClassAnnotation, resolver);
    }

    /**
     * 注册 Bean 方法解析器到 {@link BeanMethodResolverManager}
     * @param resolver
	 *        Bean 方法解析器实例
     * @throws AssertNotNullException
     *         当 Bean 方法解析器没有 {@link BeanMethodResolverAnnotation} 注解时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    private void registerBeanMethodResolver(BeanMethodResolver resolver) throws AssertNotNullException {
        BeanMethodResolverAnnotation annotation = resolver.getClass().getAnnotation(BeanMethodResolverAnnotation.class);
        AssertUtils.notNull("bean method resolver annotation", annotation);

        Class<? extends Annotation> targetMethodAnnotation = annotation.targetMethodAnnotation();
        BeanMethodResolverManager.getInstance().registerBeanMethodResolver(targetMethodAnnotation, resolver);
    }
}

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

package io.github.gdrfgdrf.cuteframework.api.loader;

import io.github.gdrfgdrf.cuteframework.CuteFramework;
import io.github.gdrfgdrf.cuteframework.api.PluginManager;
import io.github.gdrfgdrf.cuteframework.api.base.Plugin;
import io.github.gdrfgdrf.cuteframework.api.common.PluginDescription;
import io.github.gdrfgdrf.cuteframework.api.event.PluginEvent;
import io.github.gdrfgdrf.cuteframework.api.exception.*;
import io.github.gdrfgdrf.cuteframework.common.Constants;
import io.github.gdrfgdrf.cuteframework.common.VersionEnum;
import io.github.gdrfgdrf.cuteframework.event.EventManager;
import io.github.gdrfgdrf.cuteframework.utils.FileUtils;
import io.github.gdrfgdrf.cuteframework.utils.StringUtils;
import io.github.gdrfgdrf.cuteframework.utils.asserts.AssertUtils;
import io.github.gdrfgdrf.cuteframework.utils.asserts.exception.AssertArrayLengthMismatchException;
import io.github.gdrfgdrf.cuteframework.utils.asserts.exception.AssertNotNullException;
import io.github.gdrfgdrf.cuteframework.utils.jackson.JacksonUtils;
import io.github.gdrfgdrf.cuteframework.utils.stack.StackUtils;
import io.github.gdrfgdrf.cuteframework.utils.stack.exception.StackIllegalArgumentException;
import io.github.gdrfgdrf.cuteframework.utils.stack.exception.StackIllegalOperationException;
import lombok.Cleanup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 插件加载器，该类会被 {@link CuteFramework} 调用
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
public class PluginLoader {
    private static PluginLoader INSTANCE;

    /**
     * 注册该类到 {@link EventManager} 以接收事件，
     * 该构造函数仅允许该类的 getInstance 方法调用
     *
     * @throws StackIllegalOperationException
     *         当不被允许的类或方法调用该方法时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    private PluginLoader() throws AssertNotNullException, StackIllegalOperationException, StackIllegalArgumentException {
        StackUtils.onlyMethod("io.github.gdrfgdrf.cuteframework.api.loader.PluginLoader", "getInstance");
    }

    /**
     * 单例模式，获取 {@link PluginLoader} 实例，
     * 该方法仅允许 {@link CuteFramework#run()} 调用
     *
     * @return io.github.gdrfgdrf.cuteframework.api.loader.PluginLoader
     *         {@link PluginLoader} 实例
     * @throws StackIllegalOperationException
     *         当不被允许的类或方法调用该方法时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public static PluginLoader getInstance() throws
            AssertNotNullException,
            AssertArrayLengthMismatchException,
            StackIllegalOperationException,
            StackIllegalArgumentException
    {
        StackUtils.onlyMethod("io.github.gdrfgdrf.cuteframework.CuteFramework", "run");
        if (INSTANCE == null) {
            INSTANCE = new PluginLoader();
        }
        return INSTANCE;
    }

    /**
     * 开始加载插件，当插件加载发生错误时将抛出 {@link PluginLoadException}，错误实例将包含在其中，
     * 该错误不会直接抛出，而且会发布一个 {@link PluginEvent.LoadError} 事件，
     * 该事件将会异步发出。
     * 该方法仅允许 {@link CuteFramework#run()} 调用
     *
     * @throws StackIllegalOperationException
     *         当不被允许的类或方法调用该方法时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public void startLoading() throws
            AssertNotNullException,
            StackIllegalOperationException,
            StackIllegalArgumentException
    {
        StackUtils.onlyMethod("io.github.gdrfgdrf.cuteframework.CuteFramework", "run");

        File pluginFolder = new File(Constants.PLUGIN_FOLDER);
        File[] pluginJars = FileUtils.getFiles(pluginFolder, file ->
                !Objects.equals(FileUtils.getExtension(file), "jar"));
        if (pluginJars == null) {
            return;
        }

        for (File pluginJar : pluginJars) {
            try {
                PluginLoader.INSTANCE.load(pluginJar);
            } catch (Exception e) {
                PluginLoadException pluginLoadException = new PluginLoadException(pluginJar, e);
                EventManager.getInstance().postAsynchronously(new PluginEvent.LoadError(pluginLoadException));
            }
        }
    }

    /**
     * 加载插件，
     * 会首先获取插件的 plugin.json 文件并反序列化为 {@link PluginDescription}，
     * 之后会检查其中的 api-version，
     * 完成之后将会加载 main-class，
     * 加载完成后会注册到 {@link PluginManager}
     *
     * @param pluginFile
	 *        插件文件
     * @throws IOException
     *         插件文件无法被解析为 {@link JarFile}
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public void load(File pluginFile) throws
            IOException,
            PluginUndefinedPropertyException,
            UnsupportedPluginException,
            PluginMainClassExtendException,
            PluginMainClassLoadException,
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException,
            PluginNameConflictException,
            AssertNotNullException {
        @Cleanup
        JarFile jarFile = new JarFile(pluginFile);

        // configure plugin description
        PluginDescription pluginDescription = getPluginDescription(jarFile);
        checkPluginDescription(pluginFile, pluginDescription);
        pluginDescription.setPluginFile(pluginFile);

        // configure plugin core version
        checkPluginCoreVersion(pluginDescription);

        // instantiate plugin main class
        Plugin plugin = loadPluginClass(pluginDescription);
        plugin.setPluginDescription(pluginDescription);

        PluginManager.getInstance().registerPlugin(pluginDescription.getName(), plugin);
    }

    /**
     * 加载插件的类
     * @param pluginDescription
	 *        插件描述文件
     * @return io.github.gdrfgdrf.cuteframework.api.base.Plugin
     *         正常加载的插件主类
     * @throws PluginMainClassLoadException
     *         {@link JarClassLoader} 无法加载 main-class 所定义的类
     * @throws PluginMainClassExtendException
     *         可以获取到所定义的 main-class，但是该类不是继承的 {@link Plugin}
     * @throws NoSuchMethodException
     *         main-class 正确继承了 {@link Plugin}，但是无法获取到无参构造函数
     * @throws IllegalAccessException
     *         可以获取到 main-class 的无参构造函数，但是因为访问权限而无法调用
     * @throws InvocationTargetException
     *         可以调用 main-class 的无参构造函数，但是其中抛出了异常
     * @throws InstantiationException
     *         可以调用 main-class 的无参构造函数，但是其中抛出了异常
     * @throws IOException
     *         类加载器 {@link URLClassLoader} 错误
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    private Plugin loadPluginClass(PluginDescription pluginDescription) throws
            PluginMainClassLoadException,
            PluginMainClassExtendException,
            NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException,
            IOException {
        File pluginFile = pluginDescription.getPluginFile();
        String mainClassPath = pluginDescription.getMainClass();

        ClassLoader originClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader jarClassLoader =  new JarClassLoader(pluginFile);
        Thread.currentThread().setContextClassLoader(jarClassLoader);

        Class<?> mainClass;
        try {
            mainClass = jarClassLoader.loadClass(mainClassPath);
        } catch (Exception e) {
            Thread.currentThread().setContextClassLoader(originClassLoader);
            throw new PluginMainClassLoadException(pluginDescription, mainClassPath, e);
        }

        Thread.currentThread().setContextClassLoader(originClassLoader);

        pluginDescription.setClassLoader(jarClassLoader);
        if (mainClass.getSuperclass() != Plugin.class) {
            throw new PluginMainClassExtendException(pluginDescription, mainClass);
        }

        Class<? extends Plugin> pluginMainClass = mainClass.asSubclass(Plugin.class);
        return pluginMainClass.getDeclaredConstructor().newInstance();
    }

    /**
     * 检查插件描述文件中的开发时所使用的核心版本 api-version
     * @param pluginDescription
	 *        插件描述
     * @throws UnsupportedPluginException
     *         插件开发所使用的 api-version 在当前版本的 {@link VersionEnum} 中被解析为 {@link VersionEnum#UNAVAILABLE} 时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    private void checkPluginCoreVersion(PluginDescription pluginDescription) throws UnsupportedPluginException {
        int compareResult = VersionEnum.CURRENT.compare(pluginDescription.getApiVersion());
        AssertUtils.expression(
                compareResult >= 0,
                new UnsupportedPluginException(pluginDescription.getPluginFile(), pluginDescription.getRawApiVersion())
        );
    }

    /**
     * 检查插件描述文件在代码中的表示是否正确
     * @param pluginFile
	 *        插件文件
	 * @param pluginDescription
	 *        插件描述文件在代码中的表示
     * @throws PluginUndefinedPropertyException
     *         pluginDescription 中有必需的字段未定义时抛出
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    private void checkPluginDescription(File pluginFile, PluginDescription pluginDescription) throws PluginUndefinedPropertyException {
        AssertUtils.expression(
                !StringUtils.isBlank(pluginDescription.getName()),
                new PluginUndefinedPropertyException(pluginFile, "name")
        );
        AssertUtils.expression(
                !StringUtils.isBlank(pluginDescription.getMainClass()),
                new PluginUndefinedPropertyException(pluginFile, "main-class")
        );
        AssertUtils.expression(
                pluginDescription.getApiVersion() != null,
                new PluginUndefinedPropertyException(pluginFile, "api-version")
        );
        AssertUtils.expression(
                !StringUtils.isBlank(pluginDescription.getAuthor()),
                new PluginUndefinedPropertyException(pluginFile, "author")
        );
    }

    /**
     * 解析插件文件并从中反序列出 {@link PluginDescription}
     * @param pluginFile
	 *        插件文件
     * @return io.github.gdrfgdrf.cuteframework.api.common.PluginDescription
     *         插件描述文件在代码中的表示
     * @throws IOException
     *         无法找到 plugin.json 或反序列化 plugin.json 时发送错误
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    private PluginDescription getPluginDescription(JarFile pluginFile) throws IOException {
        JarEntry pluginDescriptionFile = pluginFile.getJarEntry(Constants.PLUGIN_DESCRIPTION_FILE_NAME);
        AssertUtils.expression(
                pluginDescriptionFile != null,
                new FileNotFoundException("plugin.json cannot be found in the plugin jar")
        );

        InputStream pluginDescriptionFileInputStream = pluginFile.getInputStream(pluginDescriptionFile);
        return JacksonUtils.readInputStream(
                pluginDescriptionFileInputStream,
                PluginDescription.class
        );
    }
}

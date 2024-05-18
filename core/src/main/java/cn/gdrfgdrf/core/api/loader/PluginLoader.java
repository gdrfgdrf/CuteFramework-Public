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

package cn.gdrfgdrf.core.api.loader;

import cn.gdrfgdrf.core.api.PluginManager;
import cn.gdrfgdrf.core.api.base.Plugin;
import cn.gdrfgdrf.core.api.common.PluginDescription;
import cn.gdrfgdrf.core.api.event.PluginEvent;
import cn.gdrfgdrf.core.api.exception.*;
import cn.gdrfgdrf.core.common.Constants;
import cn.gdrfgdrf.core.common.VersionEnum;
import cn.gdrfgdrf.core.event.EventManager;
import cn.gdrfgdrf.core.utils.FileUtils;
import cn.gdrfgdrf.core.utils.StringUtils;
import cn.gdrfgdrf.core.utils.asserts.AssertUtils;
import cn.gdrfgdrf.core.utils.asserts.exception.AssertArrayLengthMismatchException;
import cn.gdrfgdrf.core.utils.asserts.exception.AssertNotNullException;
import cn.gdrfgdrf.core.utils.jackson.JacksonUtils;
import cn.gdrfgdrf.core.utils.stack.StackUtils;
import cn.gdrfgdrf.core.utils.stack.exception.StackIllegalArgumentException;
import cn.gdrfgdrf.core.utils.stack.exception.StackIllegalOperationException;
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
 * @Description 插件加载器，该类不会作为 Bean 被管理，而是由程序主类调用
 * @Author gdrfgdrf
 * @Date 2024/5/4
 */
public class PluginLoader {
    private static PluginLoader INSTANCE;

    /**
     * @Description 注册该类到 {@link EventManager} 以接收事件，
     * 该构造函数仅允许 cn.gdrfgdrf.core.api.loader.PluginLoader 的 getInstance 方法调用
     *
     * @throws cn.gdrfgdrf.core.utils.stack.exception.StackIllegalOperationException
     *         当不被允许的类或方法调用该方法时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    private PluginLoader() throws AssertNotNullException, StackIllegalOperationException, StackIllegalArgumentException {
        StackUtils.onlyMethod("cn.gdrfgdrf.core.api.loader.PluginLoader", "getInstance");
    }

    /**
     * @Description 单例模式，获取 {@link PluginLoader} 实例，
     * 该方法仅允许 cn.gdrfgdrf.core.SmartCore 的 run 方法
     *
     * @return cn.gdrfgdrf.core.api.loader.PluginLoader
     *         {@link PluginLoader} 实例
     * @throws cn.gdrfgdrf.core.utils.stack.exception.StackIllegalOperationException
     *         当不被允许的类或方法调用该方法时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    public static PluginLoader getInstance() throws
            AssertNotNullException,
            AssertArrayLengthMismatchException,
            StackIllegalOperationException,
            StackIllegalArgumentException
    {
        StackUtils.onlyMethod("cn.gdrfgdrf.core.SmartCore", "run");
        if (INSTANCE == null) {
            INSTANCE = new PluginLoader();
        }
        return INSTANCE;
    }

    /**
     * @Description 开始加载插件，当插件加载发生错误时将抛出 {@link PluginLoadException}，错误实例将包含在其中，
     * 该错误不会直接抛出，而且会发布一个 {@link cn.gdrfgdrf.core.api.event.PluginEvent.LoadError} 事件，
     * 该事件将会异步发出
     *
     * @throws cn.gdrfgdrf.core.utils.stack.exception.StackIllegalOperationException
     *         当不被允许的类或方法调用该方法时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    public void startLoading() throws
            AssertNotNullException,
            StackIllegalOperationException,
            StackIllegalArgumentException
    {
        StackUtils.onlyMethod("cn.gdrfgdrf.core.SmartCore", "run");

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
     * @Description 加载插件，
     * 会首先获取插件的 plugin.json 文件并反序列化为 {@link PluginDescription}，
     * 之后会检查其中的 api-version，
     * 完成之后将会加载 main-class，
     * 加载完成后会注册到 {@link PluginManager}
     *
     * @param pluginFile
	 *        插件文件
     * @throws IOException
     *         插件文件无法被解析为 {@link JarFile}
     * @Author gdrfgdrf
     * @Date 2024/5/5
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
     * @Description 加载插件的类
     * @param pluginDescription
	 *        插件描述文件
     * @return cn.gdrfgdrf.core.api.base.Plugin
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
     * @Author gdrfgdrf
     * @Date 2024/5/5
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
     * @Description 检查插件描述文件中的开发时所使用的核心版本 api-version
     * @param pluginDescription
	 *        插件描述
     * @throws UnsupportedPluginException
     *         插件开发所使用的 api-version 在当前版本的 {@link VersionEnum} 中被解析为 {@link VersionEnum#UNAVAILABLE} 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/5
     */
    private void checkPluginCoreVersion(PluginDescription pluginDescription) throws UnsupportedPluginException {
        int compareResult = VersionEnum.CURRENT.compare(pluginDescription.getApiVersion());
        AssertUtils.expression(
                compareResult <= 0 && compareResult != -2,
                new UnsupportedPluginException(pluginDescription.getPluginFile(), pluginDescription.getRawApiVersion())
        );
    }

    /**
     * @Description 检查插件描述文件在代码中的表示是否正确
     * @param pluginFile
	 *        插件文件
	 * @param pluginDescription
	 *        插件描述文件在代码中的表示
     * @throws PluginUndefinedPropertyException
     *         pluginDescription 中有必需的字段未定义时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/5
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
     * @Description 解析插件文件并从中反序列出 {@link PluginDescription}
     * @param pluginFile
	 *        插件文件
     * @return cn.gdrfgdrf.core.api.common.PluginDescription
     *         插件描述文件在代码中的表示
     * @throws IOException
     *         无法找到 plugin.json 或反序列化 plugin.json 时发送错误
     * @Author gdrfgdrf
     * @Date 2024/5/5
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

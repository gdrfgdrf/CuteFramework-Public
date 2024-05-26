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

package io.github.gdrfgdrf.cuteframework.config;

import io.github.gdrfgdrf.cuteframework.common.Constants;
import io.github.gdrfgdrf.cuteframework.config.common.Config;
import io.github.gdrfgdrf.cuteframework.config.event.ConfigEvent;
import io.github.gdrfgdrf.cuteframework.event.EventManager;
import io.github.gdrfgdrf.cuteframework.utils.FileUtils;
import io.github.gdrfgdrf.cuteframework.utils.asserts.exception.AssertNotNullException;
import io.github.gdrfgdrf.cuteframework.utils.jackson.JacksonUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 配置管理器，可以将配置文件反序列化为配置类，
 * 该配置类必须拥有一个名叫 reset 的方法，
 * 且入参必须有且仅有一个该配置类的实例
 * 该构造函数仅用于无法找到配置文件时新建一个配置类
 *
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
@Getter
@Setter
public class ConfigManager {
    private static ConfigManager INSTANCE;

    /**
     * 框架的配置类实例
     */
    private Config config;

    private ConfigManager() {}

    public static ConfigManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConfigManager();
        }
        return INSTANCE;
    }

    /**
     * 加载配置文件
     * @param owner
     *        配置文件所有者
     * @param configFileName
	 *        配置文件的文件名
     * @param clazz
     *        需要将配置文件反序列到的类
     * @throws IOException
     *         配置文件 IO 流错误
     * @throws NoSuchMethodException
     *         无法找到配置类的无参构造函数 或 reset 方法
     * @throws InvocationTargetException
     *         配置类的构造函数或 reset 方法抛出错误
     * @throws InstantiationException
     *         配置类的构造函数或 reset 方法抛出错误
     * @throws IllegalAccessException
     *         因为访问权限而无法调用构造函数或 reset 方法
     * @return java.lang.Object
     *         配置文件实例
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    @SuppressWarnings("unchecked")
    public <T> T load(String owner, String configFileName, Class<?> clazz) throws
            IOException,
            AssertNotNullException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException
    {
        EventManager.getInstance().post(new ConfigEvent.Load.Pre());

        File folder = new File(Constants.CONFIG_FOLDER + owner);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        Object result;
        File file = new File(Constants.CONFIG_FOLDER + owner + "/" + configFileName);
        if (!file.exists()) {
            file.createNewFile();

            result = clazz.getDeclaredConstructor().newInstance();

            Method reset = clazz.getDeclaredMethod("reset", clazz);
            reset.invoke(null, result);

            save(owner, configFileName, result);
        } else {
            result = JacksonUtils.readFile(file, clazz);
        }

        EventManager.getInstance().post(new ConfigEvent.Load.Post(result, file));

        return (T) result;
    }

    /**
     * 保存 {@link ConfigManager#config} 到文件
     * @param owner
     *        配置文件所有者
     * @param targetFileName
	 *        需要保存到的配置文件的文件名
     * @param config
     *        需要序列化的配置文件实例
     * @throws IOException
     *         配置文件 IO 流错误
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public void save(String owner, String targetFileName, Object config) throws IOException, AssertNotNullException {
        if (config == null) {
            return;
        }

        File folder = new File(Constants.CONFIG_FOLDER + owner);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(Constants.CONFIG_FOLDER + owner + "/" + targetFileName);
        if (!file.exists()) {
            file.createNewFile();
        }

        EventManager.getInstance().post(new ConfigEvent.Save.Pre(config, file));

        Writer writer = FileUtils.getWriter(file);
        writer.write(JacksonUtils.writeJsonString(config));
        writer.close();

        EventManager.getInstance().post(new ConfigEvent.Save.Post(config, file));
    }

}

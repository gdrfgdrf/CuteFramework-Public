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

package io.github.gdrfgdrf.cuteframework.locale;

import io.github.gdrfgdrf.cuteframework.common.Constants;
import io.github.gdrfgdrf.cuteframework.locale.base.LanguageBlock;
import io.github.gdrfgdrf.cuteframework.locale.base.LanguageCollect;
import io.github.gdrfgdrf.cuteframework.locale.exception.NotFoundLanguagePackageException;
import io.github.gdrfgdrf.cuteframework.utils.ClassUtils;
import io.github.gdrfgdrf.cuteframework.utils.FileUtils;
import io.github.gdrfgdrf.cuteframework.utils.StringUtils;
import io.github.gdrfgdrf.cuteframework.utils.asserts.AssertUtils;
import io.github.gdrfgdrf.cuteframework.utils.asserts.exception.AssertNotNullException;
import io.github.gdrfgdrf.cuteframework.utils.jackson.JacksonUtils;
import io.github.gdrfgdrf.cuteframework.utils.jackson.SuperJsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @Description 语言加载器，翻译提供者请仔细查看该包的 package-info
 * @Author gdrfgdrf
 * @Date 2024/4/11
 */
@Slf4j
public class LanguageLoader {
    private static LanguageLoader INSTANCE;

    private final List<String> OWNER_LIST = new ArrayList<>();

    private LanguageLoader() {}

    /**
     * @Description 单例模式，获取 {@link LanguageLoader} 实例
     * @return io.github.gdrfgdrf.cuteframework.locale.LanguageLoader
     *         {@link LanguageLoader} 实例
     * @Author gdrfgdrf
     * @Date 2024/4/11
     */
    public static LanguageLoader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LanguageLoader();
        }
        return INSTANCE;
    }

    public void registerOwner(String owner) {
        if (OWNER_LIST.contains(owner)) {
            return;
        }
        OWNER_LIST.add(owner);
    }

    /**
     * @Description 加载指定 owner 的指定语言，
     * 若语言文件不存在则从类加载语言，具体说明请查阅 package-info
     *
     * @param classLoader
	 *        能够加载语言类的类加载器
	 * @param collectPackage
	 *        语言集合所在的包名
	 * @param languagePackage
	 *        语言块所在的包名
	 * @param owner
	 *        owner
	 * @param language
	 *        语言
     * @throws AssertNotNullException
     *         当以上任何的参数为 null 时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/22
     */
    public void load(
            ClassLoader classLoader,
            String collectPackage,
            String languagePackage,
            String owner,
            String language
    ) throws
            AssertNotNullException,
            IOException,
            NotFoundLanguagePackageException,
            IllegalAccessException
    {
        AssertUtils.notNull("class loader", classLoader);
        AssertUtils.notNull("collect package", collectPackage);
        AssertUtils.notNull("language package", languagePackage);
        AssertUtils.notNull("language owner", owner);
        AssertUtils.notNull("language", language);

        File implLanguageFolder = new File(Constants.LOCALE_LANGUAGE_FOLDER + owner);
        if (!implLanguageFolder.exists()) {
            implLanguageFolder.mkdirs();
        }

        File languageFile = new File(Constants.LOCALE_LANGUAGE_FOLDER + owner + "/" + language + ".json");
        if (languageFile.exists()) {
            loadFromFile(classLoader, collectPackage, languagePackage, owner, language);
            return;
        }
        loadFromClass(classLoader, collectPackage, languagePackage, language);

        saveCollectClass(classLoader, collectPackage, languageFile);
    }

    /**
     * @Description 从语言文件加载语言
     * @param classLoader
     *        能够加载语言类的类加载器
     * @param owner
     *        owner
     * @param collectPackage
     *        语言集合类所在的包名
     * @param languagePackage
     *        语言块所在的包名
     * @param language
	 *        语言
     * @throws AssertNotNullException
     *         language 为 null 时抛出
     * @throws IOException
     *         语言文件 IO 错误
     * @Author gdrfgdrf
     * @Date 2024/4/12
     */
    @SuppressWarnings("unchecked")
    private void loadFromFile(
            ClassLoader classLoader,
            String collectPackage,
            String languagePackage,
            String owner,
            String language
    ) throws AssertNotNullException, IOException {
        AssertUtils.notNull("class loader", classLoader);
        AssertUtils.notNull("collect package", collectPackage);
        AssertUtils.notNull("language package", languagePackage);
        AssertUtils.notNull("language owner", owner);
        AssertUtils.notNull("language", language);
        collectPackage = ClassUtils.formatPackageName(collectPackage);
        languagePackage = ClassUtils.formatPackageName(languagePackage);

        File languageFile = new File(Constants.LOCALE_LANGUAGE_FOLDER + owner + "/" + language + ".json");
        if (!languageFile.exists()) {
            return;
        }

        String languagePackageString = language.replace("_", ".");
        String fullLanguagePackage = languagePackage + "." + languagePackageString;
        boolean languagePackageExists = ClassUtils.isPackageExists(classLoader, fullLanguagePackage);

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackage(collectPackage)
                .forPackage(fullLanguagePackage)
                .addClassLoaders(classLoader));
        Set<Class<? extends LanguageCollect>> allLanguageCollectClasses = reflections.getSubTypesOf(LanguageCollect.class);
        SuperJsonNode superJsonNode = JacksonUtils.readFileTree(languageFile);
        allLanguageCollectClasses.forEach(collectClass -> {
            try {
                String collectClassName = collectClass.getSimpleName();
                Field[] collectFields = Arrays.stream(collectClass.getDeclaredFields())
                        .filter(field -> field.getType() == LanguageString.class)
                        .toArray(Field[]::new);
                SuperJsonNode collectNode = new SuperJsonNode(superJsonNode.getJsonNode().get(collectClassName));

                for (Field collectField : collectFields) {
                    String fieldName = collectField.getName();

                    if (collectNode.getJsonNode() != null && collectNode.contains(fieldName)) {
                        String languageContent = collectNode.getStringOrNull(fieldName);
                        if (StringUtils.isBlank(languageContent)) {
                            return;
                        }
                        LanguageString languageString = new LanguageString(languageContent);

                        try {
                            setFromString(fieldName, languageString, collectClass);
                        } catch (Exception e) {
                            log.error("Cannot set field for collect class", e);
                        }
                        continue;
                    }
                    if (!languagePackageExists) {
                        continue;
                    }

                    try {
                        Class<? extends LanguageBlock> languageBlockClass = (Class<? extends LanguageBlock>)
                                classLoader.loadClass(fullLanguagePackage + "." + collectClassName);
                        setFromBlock(collectField, languageBlockClass.getDeclaredField(fieldName), fieldName);
                    } catch (Exception e) {
                        log.error("Load language error from class", e);
                    }
                }
            } catch (Exception e) {
                log.error("Load language error from file", e);
            }
        });
    }

    /**
     * @Description 从类加载语言
     * @param classLoader
     *        能够加载语言块类的类加载器
     * @param collectPackage
     *        语言集合所在的包名
     * @param languagePackage
     *        语言块所在的包名
     * @param language
	 *        需要加载的语言
     * @throws AssertNotNullException
     *         当任何一个参数为 null 时抛出
     * @throws NotFoundLanguagePackageException
     *         没有找到语言包时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/12
     */
    private void loadFromClass(
            ClassLoader classLoader,
            String collectPackage,
            String languagePackage,
            String language
    ) throws AssertNotNullException, NotFoundLanguagePackageException {
        AssertUtils.notNull("class loader", classLoader);
        AssertUtils.notNull("collect package", collectPackage);
        AssertUtils.notNull("language package", languagePackage);
        AssertUtils.notNull("language", language);
        ClassUtils.formatPackageName(collectPackage);
        ClassUtils.formatPackageName(languagePackage);

        String languagePackageString = language.replace("_", ".");
        String fullLanguagePackage = languagePackage + "." + languagePackageString;

        if (!ClassUtils.isPackageExists(classLoader, fullLanguagePackage)) {
            throw new NotFoundLanguagePackageException();
        }

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackage(fullLanguagePackage)
                .addClassLoaders(classLoader));
        Set<Class<? extends LanguageBlock>> allLanguageBlockClasses = reflections.getSubTypesOf(LanguageBlock.class);

        allLanguageBlockClasses.forEach(languageBlockClass -> {
            String className = languageBlockClass.getSimpleName();
            try {
                Class<?> collectClass = classLoader.loadClass(collectPackage + "." + className);
                Field[] collectFields = Arrays.stream(collectClass.getDeclaredFields())
                        .filter(field -> field.getType() == LanguageString.class)
                        .toArray(Field[]::new);

                for (Field collectField : collectFields) {
                    String fieldName = collectField.getName();
                    setFromBlock(
                            collectField,
                            languageBlockClass.getDeclaredField(fieldName),
                            fieldName
                    );
                }
            } catch (Exception e) {
                log.error("Load language error from class", e);
            }
        });
    }

    /**
     * @Description 序列化所有 {@link LanguageCollect} 类并保存到语言文件
     * @param classLoader
     *        能够加载语言集合类的类加载器
     * @param collectPackage
     *        语言集合类所在的包名
     * @param file
	 *        语言文件
     * @throws AssertNotNullException
     *         当任何一个参数为 null 时抛出
     * @throws IOException
     *         语言文件 IO 错误
     * @throws IllegalAccessException
     *         无法从语言自核类中获取语言字段
     * @Author gdrfgdrf
     * @Date 2024/4/16
     */
    private void saveCollectClass(ClassLoader classLoader, String collectPackage, File file) throws
            AssertNotNullException,
            IOException,
            IllegalAccessException
    {
        AssertUtils.notNull("class loader", classLoader);
        AssertUtils.notNull("collect package", collectPackage);
        AssertUtils.notNull("file", file);
        ClassUtils.formatPackageName(collectPackage);

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackage(collectPackage)
                .addClassLoaders(classLoader));
        Set<Class<? extends LanguageCollect>> allCollectClasses = reflections.getSubTypesOf(LanguageCollect.class);

        ObjectNode root = JacksonUtils.newTree();
        for (Class<?> collectClass : allCollectClasses) {
            ObjectNode languageCollectNode = JacksonUtils.newTree();

            Field[] collectFields = Arrays.stream(collectClass.getDeclaredFields())
                    .filter(field -> field.getType() == LanguageString.class)
                    .toArray(Field[]::new);
            for (Field collectField : collectFields) {
                String languageKey = collectField.getName();
                LanguageString languageContent = (LanguageString) collectField.get(null);

                languageCollectNode.put(languageKey, languageContent.get().getString());
            }

            root.set(collectClass.getSimpleName(), languageCollectNode);
        }

        if (!file.exists()) {
            file.createNewFile();
        }
        Writer writer = FileUtils.getWriter(file);
        writer.write(root.toString());
        writer.close();
    }

    /**
     * @Description 从 {@link LanguageBlock} 中获取字段并设置到 {@link LanguageCollect}，
     * 为了获取内容，
     * 会暂时的把 {@link LanguageBlock} 中的字段设置为可访问，设置完成后再设置回去
     *
     * @param collectField
     *        {@link LanguageCollect} 中需要设置的字段
     * @param blockField
     *        从 {@link LanguageBlock} 中获取的字段
     * @param fieldName
     *        字段名
     * @Author gdrfgdrf
     * @Date 2024/4/19
     */
    private void setFromBlock(Field collectField, Field blockField, String fieldName) {
        ClassUtils.accessibleField(null, blockField, accessibleBlockField -> {
            try {
                LanguageString languageString = (LanguageString) accessibleBlockField.get(null);
                collectField.set(null, languageString);
            } catch (IllegalAccessException e) {
                log.error("Cannot set field for collect class", e);
            }
        });
    }

    /**
     * @Description 将语言内容设置到语言集合中
     * @param languageKey
	 *        语言键
	 * @param languageContent
	 *        语言内容
	 * @param languageCollect
	 *        语言集合
     * @throws NoSuchFieldException
     *         无法在语言集合中根据语言键获取语言字段
     * @throws RuntimeException
     *         可以找到语言键，但无法为其设置语言内容，该异常的内容为 {@link IllegalAccessException}
     * @Author gdrfgdrf
     * @Date 2024/4/12
     */
    private void setFromString(
            String languageKey,
            LanguageString languageContent,
            Class<? extends LanguageCollect> languageCollect
    ) throws NoSuchFieldException {
        if (StringUtils.isBlank(languageKey)) {
            return;
        }

        Field languageField = languageCollect.getDeclaredField(languageKey);
        ClassUtils.accessibleField(null, languageField, accessibleLanguageField -> {
            try {
                accessibleLanguageField.set(null, languageContent);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }


}

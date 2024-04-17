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

package cn.gdrfgdrf.smartuploader.locale;

import cn.gdrfgdrf.smartuploader.common.Constants;
import cn.gdrfgdrf.smartuploader.locale.base.LanguageBlock;
import cn.gdrfgdrf.smartuploader.locale.base.LanguageCollect;
import cn.gdrfgdrf.smartuploader.locale.collect.SystemLanguage;
import cn.gdrfgdrf.smartuploader.locale.exception.NotFoundLanguagePackageException;
import cn.gdrfgdrf.smartuploader.utils.ClassUtils;
import cn.gdrfgdrf.smartuploader.utils.FileUtils;
import cn.gdrfgdrf.smartuploader.utils.StringUtils;
import cn.gdrfgdrf.smartuploader.utils.asserts.AssertUtils;
import cn.gdrfgdrf.smartuploader.utils.jackson.JacksonUtils;
import cn.gdrfgdrf.smartuploader.utils.jackson.SuperJsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description 语言加载器，翻译提供者请仔细查看该包的 package-info
 * @Author gdrfgdrf
 * @Date 2024/4/11
 */
@Slf4j
public class LanguageLoader {
    private static LanguageLoader INSTANCE;

    private LanguageLoader() {}

    /**
     * @Description 单例模式，获取 {@link LanguageLoader} 实例
     * @return cn.gdrfgdrf.smartuploader.locale.LanguageLoader
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

    /**
     * @Description 加载语言，若语言文件不存在则从类加载语言，具体说明请查阅 package-info
     * @param language
	 *        语言
     * @throws cn.gdrfgdrf.smartuploader.utils.asserts.exception.AssertNotNullException
     *         language 为 null 时抛出
     * @throws IOException
     *         语言文件 IO 错误
     * @throws NotFoundLanguagePackageException
     *         当没有找到语言文件，尝试从类加载语言时，无法通过 language 找到对应的语言包时抛出
     * @throws IllegalAccessException
     *         序列化语言汇总类时，因为访问权限而无法从语言汇总类获取字符串
     * @Author gdrfgdrf
     * @Date 2024/4/12
     */
    public void load(String language) throws IOException, NotFoundLanguagePackageException, IllegalAccessException {
        AssertUtils.notNull("language", language);

        File languageFolder = new File(Constants.LOCALE_LANGUAGE_FOLDER);
        if (!languageFolder.exists()) {
            languageFolder.mkdirs();
        }

        File languageFile = new File(Constants.LOCALE_LANGUAGE_FOLDER + language + ".json");
        if (languageFile.exists()) {
            loadFromFile(languageFile);
            return;
        }
        loadFromClass(language);

        saveCollectClass(languageFile);
    }

    /**
     * @Description 从语言文件加载语言
     * @param languageFile
	 *        语言文件
     * @throws cn.gdrfgdrf.smartuploader.utils.asserts.exception.AssertNotNullException
     *         languageFile 为 null 时抛出
     * @throws IOException
     *         语言文件 IO 错误
     * @Author gdrfgdrf
     * @Date 2024/4/12
     */
    @SuppressWarnings("unchecked")
    private void loadFromFile(File languageFile) throws IOException {
        AssertUtils.notNull("language file", languageFile);
        if (!languageFile.exists()) {
            return;
        }

        SuperJsonNode superJsonNode = JacksonUtils.readFileTree(languageFile);
        Iterator<String> collectClassKeySet = superJsonNode.keySet();
        collectClassKeySet.forEachRemaining(collectClassName -> {
            try {
                Class<? extends LanguageCollect> collectClass =
                        (Class<? extends LanguageCollect>) Class.forName(
                                Constants.LOCALE_COLLECT_PACKAGE + "." + collectClassName
                        );
                SuperJsonNode collectNode = new SuperJsonNode(superJsonNode.getJsonNode().get(collectClassName));

                collectNode.keySet().forEachRemaining(languageKey -> {
                    String languageContent = collectNode.getStringOrNull(languageKey);
                    if (StringUtils.isBlank(languageContent)) {
                        return;
                    }

                    try {
                        setLanguageContent(languageKey, languageContent, collectClass);
                    } catch (Exception e) {
                        log.error("Cannot set language field", e);
                    }
                });
            } catch (Exception e) {
                log.error("load language error from file", e);
            }
        });

    }

    /**
     * @Description 从类加载语言
     * @param targetLanguage
	 *        需要加载的语言
     * @throws NotFoundLanguagePackageException
     *         没有找到语言包时抛出
     * @Author gdrfgdrf
     * @Date 2024/4/12
     */
    private void loadFromClass(String targetLanguage) throws NotFoundLanguagePackageException {
        String targetLanguagePackageString = targetLanguage.replace("_", ".");
        String fullTargetLanguagePackage = Constants.LOCALE_LANGUAGE_PACKAGE + "." + targetLanguagePackageString;

        if (!ClassUtils.isPackageExists(fullTargetLanguagePackage)) {
            throw new NotFoundLanguagePackageException();
        }

        Set<Class<?>> allLanguageBlockClasses = new HashSet<>();
        ClassUtils.searchJar(fullTargetLanguagePackage, clazz -> {
            try {
                if (clazz.asSubclass(LanguageBlock.class) != null) {
                    return true;
                }
            } catch (Exception ignored) {
            }
            return false;
        }, allLanguageBlockClasses);

        allLanguageBlockClasses.forEach(languageBlockClass -> {
            String className = languageBlockClass.getSimpleName();
            try {
                Class<?> collectClass = Class.forName(Constants.LOCALE_COLLECT_PACKAGE + "." + className);
                Field[] collectFields = Arrays.stream(collectClass.getDeclaredFields())
                        .filter(field -> field.getType() == String.class)
                        .toArray(Field[]::new);

                for (Field collectField : collectFields) {
                    String fieldName = collectField.getName();
                    Field languageField = languageBlockClass.getDeclaredField(fieldName);

                    ClassUtils.accessibleField(null, languageField, accessibleLanguageField -> {
                        try {
                            String languageString = (String) accessibleLanguageField.get(null);
                            collectField.set(null, languageString);
                        } catch (IllegalAccessException e) {
                            log.error("Cannot set field for collect class", e);
                        }
                    });

                }
            } catch (Exception e) {
                log.error("Load language error from class", e);
            }
        });
    }

    /**
     * @Description 序列化所有 {@link LanguageCollect} 类并保存到语言文件
     * @param file
	 *        语言文件
     * @throws IOException
     *         语言文件 IO 错误
     * @Author gdrfgdrf
     * @Date 2024/4/16
     */
    private void saveCollectClass(File file) throws IOException, IllegalAccessException {
        Set<Class<?>> allCollectClasses = new HashSet<>();
        ClassUtils.searchJar(Constants.LOCALE_COLLECT_PACKAGE, clazz -> {
            try {
                if (clazz.asSubclass(LanguageCollect.class) != null) {
                    return true;
                }
            } catch (Exception ignored) {
            }
            return false;
        }, allCollectClasses);

        ObjectNode root = JacksonUtils.newTree();
        for (Class<?> collectClass : allCollectClasses) {
            ObjectNode languageCollectNode = JacksonUtils.newTree();

            Field[] collectFields = Arrays.stream(collectClass.getDeclaredFields())
                    .filter(field -> field.getType() == String.class)
                    .toArray(Field[]::new);
            for (Field collectField : collectFields) {
                String languageKey = collectField.getName();
                String languageContent = (String) collectField.get(null);

                languageCollectNode.put(languageKey, languageContent);
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
    private void setLanguageContent(
            String languageKey,
            String languageContent,
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

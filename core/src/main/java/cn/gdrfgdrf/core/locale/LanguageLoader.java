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

package cn.gdrfgdrf.core.locale;

import cn.gdrfgdrf.core.common.Constants;
import cn.gdrfgdrf.core.locale.base.LanguageBlock;
import cn.gdrfgdrf.core.locale.base.LanguageCollect;
import cn.gdrfgdrf.core.locale.exception.NotFoundLanguagePackageException;
import cn.gdrfgdrf.core.utils.ClassUtils;
import cn.gdrfgdrf.core.utils.FileUtils;
import cn.gdrfgdrf.core.utils.StringUtils;
import cn.gdrfgdrf.core.utils.asserts.AssertUtils;
import cn.gdrfgdrf.core.utils.jackson.JacksonUtils;
import cn.gdrfgdrf.core.utils.jackson.SuperJsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
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
     * @throws cn.gdrfgdrf.core.utils.asserts.exception.AssertNotNullException
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
            loadFromFile(language);
            return;
        }
        loadFromClass(language);

        saveCollectClass(languageFile);
    }

    /**
     * @Description 从语言文件加载语言
     * @param language
	 *        语言
     * @throws cn.gdrfgdrf.core.utils.asserts.exception.AssertNotNullException
     *         language 为 null 时抛出
     * @throws IOException
     *         语言文件 IO 错误
     * @Author gdrfgdrf
     * @Date 2024/4/12
     */
    @SuppressWarnings("unchecked")
    private void loadFromFile(String language) throws IOException {
        AssertUtils.notNull("language", language);

        File languageFile = new File(Constants.LOCALE_LANGUAGE_FOLDER + language + ".json");
        if (!languageFile.exists()) {
            return;
        }

        String targetLanguagePackageString = language.replace("_", ".");
        String fullTargetLanguagePackage = Constants.LOCALE_LANGUAGE_PACKAGE + "." + targetLanguagePackageString;
        boolean languagePackageExists = ClassUtils.isPackageExists(fullTargetLanguagePackage);

        Set<Class<?>> allLanguageCollectClasses = new HashSet<>();
        ClassUtils.searchJar(Constants.LOCALE_COLLECT_PACKAGE, clazz -> {
            try {
                clazz.asSubclass(LanguageCollect.class);
                return true;
            } catch (Exception ignored) {
            }
            return false;
        }, allLanguageCollectClasses);

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
                            setFromString(fieldName, languageString, (Class<? extends LanguageCollect>) collectClass);
                        } catch (Exception e) {
                            log.error("Cannot set field for collect class", e);
                        }
                        continue;
                    }
                    if (!languagePackageExists) {
                        continue;
                    }

                    try {
                        Class<? extends LanguageBlock> languageBlockClass =
                                (Class<? extends LanguageBlock>) Class.forName(
                                        fullTargetLanguagePackage + "." + collectClassName
                                );
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
                clazz.asSubclass(LanguageBlock.class);
                return true;
            } catch (Exception ignored) {
            }
            return false;
        }, allLanguageBlockClasses);

        allLanguageBlockClasses.forEach(languageBlockClass -> {
            String className = languageBlockClass.getSimpleName();
            try {
                Class<?> collectClass = Class.forName(Constants.LOCALE_COLLECT_PACKAGE + "." + className);
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
                clazz.asSubclass(LanguageCollect.class);
                return true;
            } catch (Exception ignored) {
            }
            return false;
        }, allCollectClasses);

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

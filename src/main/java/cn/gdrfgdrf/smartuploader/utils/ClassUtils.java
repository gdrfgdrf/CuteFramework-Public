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

package cn.gdrfgdrf.smartuploader.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Description 类对象工具类
 * @Author gdrfgdrf
 * @Date 2024/4/6
 */
public class ClassUtils {
    private ClassUtils() {}

    public static void accessibleField(Object obj, Field field, Consumer<Field> consumer) {
        boolean changeAccessible = false;
        if (!field.canAccess(obj)) {
            changeAccessible = true;
            field.setAccessible(true);
        }

        consumer.accept(field);

        if (changeAccessible) {
            field.setAccessible(false);
        }
    }

    public static boolean isPackageExists(String packageName) {
        ClassLoader classLoader = ClassUtils.class.getClassLoader();
        String packagePath = packageName.replace(".", "/");
        URL packageUrl = classLoader.getResource(packagePath);
        return packageUrl != null;
    }

    public static void search(
            File searchRoot,
            String packageName,
            Predicate<Class<?>> predicate,
            Set<Class<?>> result
    ) {
        searchInternal(searchRoot, packageName, predicate, result, true);
    }

    private static void searchInternal(
            File searchRoot,
            String packageName,
            Predicate<Class<?>> predicate,
            Set<Class<?>> result,
            boolean flag
    ) {
        if (searchRoot.isDirectory()) {
            File[] files = searchRoot.listFiles();
            if (files == null) {
                return;
            }
            if (!flag) {
                packageName = packageName + "." + searchRoot.getName();
            }

            String finalPackageName = packageName;
            Arrays.stream(files).forEach(file -> {
                searchInternal(file, finalPackageName, predicate, result, false);
            });

            return;
        }
        if (searchRoot.getName().endsWith(".class")) {
            try {
                Class<?> clazz = Class.forName(packageName + "." +
                        searchRoot.getName().substring(
                                0,
                                searchRoot.getName().lastIndexOf(".")
                        ));
                if (predicate == null || predicate.test(clazz)) {
                    result.add(clazz);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void searchJar(
            String packageName,
            Predicate<Class<?>> predicate,
            Set<Class<?>> result
    ) {
        try {
            Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader()
                    .getResources(packageName.replace(".", "/"));

            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();
                String protocol = url.getProtocol();

                if (!"jar".equalsIgnoreCase(protocol)) {
                    if ("file".equalsIgnoreCase(protocol)) {
                        String classpath = url.getPath().replace(
                                packageName.replace(".", "/"),
                                ""
                        );
                        String packagePath = packageName.replace(".", "/");
                        File searchRoot = new File(classpath + packagePath);

                        search(searchRoot, packageName, predicate, result);
                    }
                    continue;
                }
                JarURLConnection connection = (JarURLConnection) url.openConnection();
                if (connection == null) {
                    continue;
                }
                JarFile jarFile = connection.getJarFile();
                if (jarFile == null) {
                    continue;
                }

                Enumeration<JarEntry> entryEnumeration = jarFile.entries();
                while (entryEnumeration.hasMoreElements()) {
                    JarEntry entry = entryEnumeration.nextElement();
                    String entryName = entry.getName();
                    if (!entryName.contains(".class") ||
                            !entryName.replaceAll("/", ".").startsWith(packageName)) {
                        continue;
                    }
                    String className = entryName.substring(0, entryName.lastIndexOf("."))
                            .replace("/", ".");
                    Class<?> clazz = Class.forName(className);

                    if (predicate == null || predicate.test(clazz)) {
                        result.add(clazz);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

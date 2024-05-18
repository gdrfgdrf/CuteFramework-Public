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

package cn.gdrfgdrf.cuteframework.utils;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.processing.Generated;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
@Slf4j
public class ClassUtils {
    private ClassUtils() {}

    public static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> targetAnnotation) {
        Annotation annotation = getAnnotation(clazz, targetAnnotation);
        return annotation != null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<? extends T> targetAnnotation) {
        Annotation[] annotations = clazz.getAnnotations();

        for (Annotation annotation : annotations) {
            if (annotation.annotationType() != Deprecated.class &&
                    annotation.annotationType() != SuppressWarnings.class &&
                    annotation.annotationType() != Override.class &&
                    annotation.annotationType() != Generated.class &&
                    annotation.annotationType() != Target.class &&
                    annotation.annotationType() != Retention.class &&
                    annotation.annotationType() != Documented.class &&
                    annotation.annotationType() != Inherited.class
            ) {
                if (annotation.annotationType() == targetAnnotation) {
                    return (T) annotation;
                } else {
                    T deeperAnnotation = getAnnotation(annotation.annotationType(), targetAnnotation);
                    if (deeperAnnotation != null) {
                        return deeperAnnotation;
                    }
                }
            }
        }

        return null;
    }

    @SuppressWarnings("all")
    public static Object safetyInvoke(Object obj, Method method, Object... arguments) {
        try {
            return method.invoke(obj, arguments);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

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
            ClassLoader classLoader,
            Set<Class<?>> result
    ) {
        searchInternal(searchRoot, packageName, predicate, result, classLoader, true);
    }

    private static void searchInternal(
            File searchRoot,
            String packageName,
            Predicate<Class<?>> predicate,
            Set<Class<?>> result,
            ClassLoader classLoader,
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
            Arrays.stream(files).forEach(file -> searchInternal(file, finalPackageName, predicate, result, classLoader, false));

            return;
        }
        if (searchRoot.getName().endsWith(".class")) {
            try {
                Class<?> clazz = Class.forName(
                        packageName + "." + searchRoot
                                .getName()
                                .substring(0, searchRoot.getName().lastIndexOf(".")),
                        true,
                        classLoader
                );
                if (predicate == null || predicate.test(clazz)) {
                    result.add(clazz);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void searchJar(
            ClassLoader classLoader,
            String packageName,
            Predicate<Class<?>> predicate,
            Set<Class<?>> result
    ) {
        try {
            Enumeration<URL> urlEnumeration = classLoader
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

                        search(searchRoot, packageName, predicate, classLoader, result);
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
                    Class<?> clazz = Class.forName(className, true, classLoader);

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

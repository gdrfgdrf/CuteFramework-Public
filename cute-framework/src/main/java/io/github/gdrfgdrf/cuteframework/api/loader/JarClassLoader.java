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

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Jar 包加载器
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
public class JarClassLoader extends URLClassLoader {
    private final JarFile jarFile;
    private final URL url;

    private final ClassLoader parent;

    public JarClassLoader(File file) throws IOException {
        this(file, Thread.currentThread().getContextClassLoader());
    }

    public JarClassLoader(File file, ClassLoader parent) throws IOException {
        super(new URL[]{file.toURI().toURL()}, parent);
        this.parent = parent;
        this.jarFile = new JarFile(file);
        this.url = file.toURI().toURL();
    }

    public String classNameToJarEntry(String name) {
        String s = name.replaceAll("\\.", "/");
        return s + ".class";
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            Class<?> c = null;

            if (null != jarFile) {
                String jarEntryName = classNameToJarEntry(name);
                JarEntry entry = jarFile.getJarEntry(jarEntryName);

                if (null != entry) {
                    InputStream is = jarFile.getInputStream(entry);
                    int availableLen = is.available();

                    int len = 0;
                    byte[] bt1 = new byte[availableLen];
                    while (len < availableLen) {
                        len += is.read(bt1, len, availableLen - len);
                    }

                    CodeSigner[] signers = entry.getCodeSigners();
                    CodeSource source = new CodeSource(url, signers);

                    c = defineClass(name, bt1, 0, bt1.length, source);
                } else {
                    if (parent != null) {
                        return parent.loadClass(name);
                    }
                }
            }

            return c;
        } catch (IOException e) {
            throw new ClassNotFoundException("Class " + name + " not found.");
        }
    }

    @Nullable
    @Override
    public URL getResource(String name) {
        return findResource(name);
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        return findResources(name);
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        InputStream is = null;
        try {
            if (null != jarFile) {
                JarEntry entry = jarFile.getJarEntry(name);
                if (entry != null) {
                    is = jarFile.getInputStream(entry);
                }
                if (is == null) {
                    is = super.getResourceAsStream(name);
                }
            }
        } catch (IOException e) {
            return null;
        }
        return is;
    }
}


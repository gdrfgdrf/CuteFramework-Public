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

package cn.gdrfgdrf.cuteframework.api.loader;

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
 * @Description Jar 包加载器
 * @Author gdrfgdrf
 * @Date 2024/5/6
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


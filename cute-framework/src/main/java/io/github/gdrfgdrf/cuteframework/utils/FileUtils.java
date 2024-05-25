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

package io.github.gdrfgdrf.cuteframework.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 文件工具类
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
public class FileUtils {
    private FileUtils() {}

    /**
     * 获取文件夹内所有的文件，若提供的文件不是一个文件夹则返回 null
     * @param folder
	 *        文件夹
	 * @param fileFilter
	 *        文件过滤器，若返回 true 则放入结果，返回 false 则跳过
     * @return java.io.File[]
     *        文件夹内的文件，若提供的文件不是一个文件夹则返回 null
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public static File[] getFiles(File folder, FileFilter fileFilter) {
        if (!folder.isDirectory()) {
            return null;
        }
        if (fileFilter == null) {
            return folder.listFiles();
        }
        return folder.listFiles(fileFilter);
    }

    /**
     * 获取文件后缀名
     * @param file
	 *        文件
     * @return java.lang.String
     *         文件后缀名，若提供的文件是一个文件夹则返回 null
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public static String getExtension(File file) {
        if (!file.isFile()) {
            return null;
        }
        return file.getName().substring(
                file.getName().lastIndexOf(".")
        );
    }

    /**
     * 以 UTF-8 编码获取文件读取器
     * @param file
	 *        文件
     * @return java.io.Reader
     *        UTF-8 编码的文件读取器
     * @throws FileNotFoundException
     *         没有找到文件
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public static Reader getReader(File file) throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
    }

    /**
     * 以 UTF-8 编码获取文件写入器
     * @param file
	 *        文件
     * @return java.io.Writer
     *         UTF-8 编码的文件写入器
     * @throws FileNotFoundException
     *         没有找到文件
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public static Writer getWriter(File file) throws FileNotFoundException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
    }

    /**
     * 填充路径，保证一个路径的结尾一定是斜杠
     * @param path
	 *        路径
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public static String formatPath(String path) {
        if (path.endsWith("/") || path.endsWith("\\")) {
            return path;
        }
        return path + "/";
    }

}

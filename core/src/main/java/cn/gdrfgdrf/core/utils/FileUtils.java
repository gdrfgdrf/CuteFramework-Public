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

package cn.gdrfgdrf.core.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @Description 文件工具类
 * @Author gdrfgdrf
 * @Date 2024/4/6
 */
public class FileUtils {
    private FileUtils() {}

    /**
     * @Description 获取文件夹内所有的文件，若提供的文件不是一个文件夹则返回 null
     * @param folder
	 *        文件夹
	 * @param fileFilter
	 *        文件过滤器，若返回 true 则放入结果，返回 false 则跳过
     * @return java.io.File[]
     *        文件夹内的文件，若提供的文件不是一个文件夹则返回 null
     * @Author gdrfgdrf
     * @Date 2024/5/4
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
     * @Description 获取文件后缀名
     * @param file
	 *        文件
     * @return java.lang.String
     *         文件后缀名，若提供的文件是一个文件夹则返回 null
     * @Author gdrfgdrf
     * @Date 2024/5/4
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
     * @Description 以 UTF-8 编码获取文件读取器
     * @param file
	 *        文件
     * @return java.io.Reader
     *        UTF-8 编码的文件读取器
     * @throws FileNotFoundException
     *         没有找到文件
     * @Author gdrfgdrf
     * @Date 2024/4/6
     */
    public static Reader getReader(File file) throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
    }

    /**
     * @Description 以 UTF-8 编码获取文件写入器
     * @param file
	 *        文件
     * @return java.io.Writer
     *         UTF-8 编码的文件写入器
     * @throws FileNotFoundException
     *         没有找到文件
     * @Author gdrfgdrf
     * @Date 2024/4/6
     */
    public static Writer getWriter(File file) throws FileNotFoundException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
    }

}

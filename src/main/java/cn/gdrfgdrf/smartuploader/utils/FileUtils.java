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

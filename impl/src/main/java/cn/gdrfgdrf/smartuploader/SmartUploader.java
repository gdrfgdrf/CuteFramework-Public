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

package cn.gdrfgdrf.smartuploader;

/**
 * @Description 程序主类
 * @Author gdrfgdrf
 * @Date 2024/5/1
 */
public class SmartUploader {
    private static SmartUploader INSTANCE;

    private SmartUploader() {}

    /**
     * @Description 单例模式，获取 {@link SmartUploader} 实例
     * @return cn.gdrfgdrf.smartuploader.SmartUploader
     *         {@link SmartUploader} 实例
     * @Author gdrfgdrf
     * @Date 2024/5/1
     */
    public static SmartUploader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SmartUploader();
        }
        return INSTANCE;
    }

    public static void main(String[] args) {
        SmartUploader.getInstance().run();
    }

    public void run() {

    }

}

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

package cn.gdrfgdrf.core.bean;

import cn.gdrfgdrf.core.utils.stack.StackUtils;
import org.reflections.Reflections;

/**
 * @Description Bean 管理器，对 Bean 进行创建，移除等操作
 * @Author gdrfgdrf
 * @Date 2024/4/6
 */
public class BeanManager {
    private static BeanManager INSTANCE;

    private BeanManager() {}

    /**
     * @Description 单例模式，获取 {@link BeanManager} 实例
     * @return cn.gdrfgdrf.smartuploader.bean.BeanManager
     *         {@link BeanManager} 实例
     * @Author gdrfgdrf
     * @Date 2024/4/20
     */
    public static BeanManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BeanManager();
        }
        return INSTANCE;
    }

    /**
     * @Description 开始创建 Bean，该方法仅允许 cn.gdrfgdrf.smartuploader.SmartUploader 调用
     * @throws cn.gdrfgdrf.core.utils.stack.exception.StackIllegalOperationException
     *         当不被允许的类或方法调用该方法时抛出
     * @Author gdrfgdrf
     * @Date 2024/5/1
     */
    public void createByScanning() {
        StackUtils.onlyMethod("cn.gdrfgdrf.smartuploader.SmartUploader", "run");

        // create core beans
        Reflections reflections = new Reflections("cn.gdrfgdrf.core");



    }


}

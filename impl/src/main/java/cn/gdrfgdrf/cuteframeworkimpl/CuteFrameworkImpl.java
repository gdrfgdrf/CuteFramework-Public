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

package cn.gdrfgdrf.cuteframeworkimpl;

import cn.gdrfgdrf.cuteframework.CuteFramework;
import cn.gdrfgdrf.cuteframework.api.event.PluginEvent;
import cn.gdrfgdrf.cuteframework.event.EventManager;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 程序主类
 * @Author gdrfgdrf
 * @Date 2024/5/1
 */
@Slf4j
public class CuteFrameworkImpl {
    private static CuteFrameworkImpl INSTANCE;

    private CuteFrameworkImpl() {}

    /**
     * @Description 单例模式，获取 {@link CuteFrameworkImpl} 实例
     * @return cn.gdrfgdrf.smartuploader.SmartUploader
     *         {@link CuteFrameworkImpl} 实例
     * @Author gdrfgdrf
     * @Date 2024/5/1
     */
    public static CuteFrameworkImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CuteFrameworkImpl();
        }
        return INSTANCE;
    }

    public static void main(String[] args) throws Exception {
        CuteFrameworkImpl.getInstance().run();
    }

    /**
     * @Description 程序开始运行
     * @throws Exception
     *         程序出现未被捕获的错误，此时程序将会直接崩溃
     * @Author gdrfgdrf
     * @Date 2024/5/4
     */
    public void run() throws Exception {
        EventManager.getInstance().registerAsynchronous(this);
        try {
            CuteFramework.run();
        } catch (Exception e) {
            log.error("Error when initialize smart core", e);
        }

        throw new NullPointerException("test");
    }

    @Subscribe
    public static void onPluginLoadExceptionEvent(PluginEvent.LoadError loadError) {
        System.out.println("4、插件 " + loadError.getException().getPluginFile() + " 加载错误：" + loadError.getException().getMessage());
    }

    @Subscribe
    public static void onPluginStateChange(PluginEvent.StateChange.Pre post) {
        System.out.println("Plugin " + post.getPlugin().getPluginDescription().getName() + " state change from " + post.getPreviousPluginState() + " to " + post.getTargetPluginState());
    }

}

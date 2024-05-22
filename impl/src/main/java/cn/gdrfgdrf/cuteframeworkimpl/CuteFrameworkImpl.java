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

package cn.gdrfgdrf.cuteframeworkimpl;

import cn.gdrfgdrf.cuteframework.CuteFramework;
import cn.gdrfgdrf.cuteframework.api.event.PluginEvent;
import cn.gdrfgdrf.cuteframework.event.EventManager;
import cn.gdrfgdrf.cuteframework.locale.LanguageLoader;
import cn.gdrfgdrf.cuteframeworkimpl.locale.collect.TestLanguage;
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
            log.error("Error when initialize cute framework core", e);
        }

        try {
            LanguageLoader.getInstance().load(
                    CuteFrameworkImpl.class.getClassLoader(),
                    "cn.gdrfgdrf.cuteframeworkimpl.locale.collect",
                    "cn.gdrfgdrf.cuteframeworkimpl.locale.language",
                    "cute-framework-impl",
                    "chinese"
            );
        } catch (Exception e) {
            log.error("Error when initialize language", e);
        }

        System.out.println(TestLanguage.TEST_LANGUAGE.get());

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

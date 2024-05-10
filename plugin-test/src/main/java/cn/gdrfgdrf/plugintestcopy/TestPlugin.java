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

package cn.gdrfgdrf.plugintestcopy;

import cn.gdrfgdrf.core.api.base.Plugin;

/**
 * @Description
 * @Author gdrfgdrf
 * @Date 2024/5/5
 */
public class TestPlugin extends Plugin {
    public TestPlugin() {
        System.out.println("TestPlugin Copy initialize");
        throw new NullPointerException();
    }

    @Override
    public void onEnable() {
        System.out.println(getPluginDescription());
        System.out.println("onEnable");
    }

    @Override
    public void onLoad() {
        System.out.println("onLoad");
    }

    @Override
    public void onStop() {
        System.out.println("onStop");
    }

    @Override
    public void onDisable() {
        System.out.println("onDisable");
    }
}

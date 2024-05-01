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

package cn.gdrfgdrf.core.bean.resolver.base;

import cn.gdrfgdrf.core.bean.resolver.BeanResolverManager;

/**
 * @Description Bean 解析器，Bean 由 {@link cn.gdrfgdrf.core.bean.BeanManager} 实例化完成后，
 * 将会使用 {@link BeanResolverManager} 分发到指定的 {@link BeanResolver}，
 * 该类可对 Bean 进行初始化，
 * 比如说获取所有带有 {@link cn.gdrfgdrf.core.exceptionhandler.annotation.ExceptionHandler} 注解的方法，
 * 并注册到 {@link cn.gdrfgdrf.core.exceptionhandler.handler.manager.ExceptionHandlerManager}。
 * 建议该类只进行例如解析注解并注册到管理器之类的操作，不推荐进行设置 Bean 的字段之类的操作
 * 设置 Bean 字段的操作应该在构造函数内进行，也就是被 {@link cn.gdrfgdrf.core.bean.BeanManager} 实例化时
 *
 * @Author gdrfgdrf
 * @Date 2024/4/29
 */
public interface BeanResolver {
    /**
     * @Description 对 Bean 进行解析，
     * @param bean
	 *        Bean 实例
     * @Author gdrfgdrf
     * @Date 2024/4/29
     */
    void resolve(Object bean);
}

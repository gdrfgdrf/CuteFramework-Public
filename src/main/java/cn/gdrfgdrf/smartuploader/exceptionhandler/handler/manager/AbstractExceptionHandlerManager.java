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

package cn.gdrfgdrf.smartuploader.exceptionhandler.handler.manager;

import cn.gdrfgdrf.smartuploader.base.BaseManager;
import cn.gdrfgdrf.smartuploader.classinjector.ClassInjector;
import cn.gdrfgdrf.smartuploader.exceptionhandler.ExceptionDispatcher;
import cn.gdrfgdrf.smartuploader.exceptionhandler.annotation.ExceptionSupport;
import cn.gdrfgdrf.smartuploader.exceptionhandler.base.ExceptionHandler;
import cn.gdrfgdrf.smartuploader.exceptionhandler.handler.annotation.ExceptionHandlerInstantiate;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 管理异常处理器，
 * 对 {@link cn.gdrfgdrf.smartuploader.exceptionhandler.base.ExceptionHandler} 进行创建，
 * 并注册到 {@link cn.gdrfgdrf.smartuploader.exceptionhandler.ExceptionDispatcher}
 *
 * @Author gdrfgdrf
 * @Date 2024/4/17
 */
@Slf4j
public abstract class AbstractExceptionHandlerManager extends BaseManager<ExceptionHandler, ExceptionHandlerInstantiate> {
    /**
     * @Description 实例化 {@link ExceptionHandler} 并注册到 {@link cn.gdrfgdrf.smartuploader.exceptionhandler.ExceptionDispatcher}
     * @param clazz
	 *        {@link ExceptionHandler} 类
     * @Author gdrfgdrf
     * @Date 2024/4/17
     */
    @Override
    public void instantiate(Class<? extends ExceptionHandler> clazz) {
        try {
            ExceptionSupport exceptionSupport = clazz.getAnnotation(ExceptionSupport.class);
            if (exceptionSupport == null) {
                return;
            }

            ExceptionHandler exceptionHandler = createInstance(clazz);
            Class<? extends Throwable>[] support = exceptionSupport.support();

            for (Class<? extends Throwable> throwableType : support) {
                ExceptionDispatcher.getInstance().registerExceptionHandler(throwableType, exceptionHandler);
            }
        } catch (Exception e) {
            log.error("Error when instantiate a exception handler", e);
        }
    }
}

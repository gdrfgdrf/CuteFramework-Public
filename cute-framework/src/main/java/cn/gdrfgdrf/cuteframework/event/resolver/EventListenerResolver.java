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

package cn.gdrfgdrf.cuteframework.event.resolver;

import cn.gdrfgdrf.cuteframework.bean.resolver.clazz.annotation.BeanClassResolverAnnotation;
import cn.gdrfgdrf.cuteframework.bean.resolver.clazz.base.BeanClassResolver;
import cn.gdrfgdrf.cuteframework.event.EventManager;
import cn.gdrfgdrf.cuteframework.event.annotation.EventListener;
import cn.gdrfgdrf.cuteframework.event.enums.SubscriberType;

/**
 * @Description 接收所有拥有 {@link cn.gdrfgdrf.cuteframework.exceptionhandler.annotation.ExceptionHandler} 的类
 * @Author gdrfgdrf
 * @Date 2024/5/6
 */
@BeanClassResolverAnnotation(targetClassAnnotation = EventListener.class)
public class EventListenerResolver implements BeanClassResolver {
    @Override
    public void resolve(Object bean) throws Exception {
        EventListener eventListener = bean.getClass().getAnnotation(EventListener.class);
        SubscriberType subscriberType = eventListener.type();

        if (subscriberType == SubscriberType.SYNC || subscriberType == SubscriberType.ALL) {
            EventManager.getInstance().register(bean);
        }
        if (subscriberType == SubscriberType.ASYNC  || subscriberType == SubscriberType.ALL) {
            EventManager.getInstance().registerAsynchronous(bean);
        }
    }
}

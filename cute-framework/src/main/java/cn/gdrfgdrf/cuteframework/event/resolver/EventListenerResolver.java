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

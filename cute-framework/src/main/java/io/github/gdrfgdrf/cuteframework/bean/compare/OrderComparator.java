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

package io.github.gdrfgdrf.cuteframework.bean.compare;

import io.github.gdrfgdrf.cuteframework.bean.annotation.Order;
import io.github.gdrfgdrf.cuteframework.utils.ClassUtils;

import java.util.Comparator;

/**
 * 分析所有 Bean 类的 {@link Order} 注解并进行排序以开始创建 Bean，
 * 当 Bean 类没有 {@link Order} 注解时，将按照原本的顺序安排进入
 * @author gdrfgdrf
 * @since v1_0_0_20240525_RELEASE
 */
public class OrderComparator implements Comparator<Class<?>> {
    private static OrderComparator INSTANCE;

    private OrderComparator() {}

    /**
     * 单例模式，获取 {@link OrderComparator} 实例
     * @return io.github.gdrfgdrf.cuteframework.bean.compare.OrderComparator
     *         {@link OrderComparator} 实例
     * @author gdrfgdrf
     * @since v1_0_0_20240525_RELEASE
     */
    public static OrderComparator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OrderComparator();
        }
        return INSTANCE;
    }

    @Override
    public int compare(Class<?> o1, Class<?> o2) {
        if (o1 == o2) {
            return 0;
        }

        Order order1 = ClassUtils.getAnnotation(o1, Order.class);
        Order order2 = ClassUtils.getAnnotation(o2, Order.class);

        if (order1 == null && order2 != null) {
            return 1;
        }
        if (order1 != null && order2 == null) {
            return -1;
        }
        if (order1 == null) {
            return 0;
        }

        int orderValue1 = order1.value();
        int orderValue2 = order2.value();
        return Integer.compare(orderValue1, orderValue2);
    }
}

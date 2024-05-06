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

package cn.gdrfgdrf.core.bean.compare;

import cn.gdrfgdrf.core.bean.annotation.Order;
import cn.gdrfgdrf.core.utils.ClassUtils;

import java.util.Comparator;

/**
 * @Description 分析所有 Bean 类的 {@link cn.gdrfgdrf.core.bean.annotation.Order} 注解并进行排序以开始创建 Bean，
 * 当 Bean 类没有 {@link cn.gdrfgdrf.core.bean.annotation.Order} 注解时，将按照原本的顺序安排进入
 * @Author gdrfgdrf
 * @Date 2024/5/6
 */
public class OrderComparator implements Comparator<Class<?>> {
    private static OrderComparator INSTANCE;

    private OrderComparator() {}

    /**
     * @Description 单例模式，获取 {@link OrderComparator} 实例
     * @return cn.gdrfgdrf.core.bean.compare.OrderComparator
     *         {@link OrderComparator} 实例
     * @Author gdrfgdrf
     * @Date 2024/5/6
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

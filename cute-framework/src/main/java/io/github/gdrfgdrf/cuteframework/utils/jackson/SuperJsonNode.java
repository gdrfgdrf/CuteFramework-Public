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

package io.github.gdrfgdrf.cuteframework.utils.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.Iterator;

/**
 * @description 定制 json 节点
 * @author gdrfgdrf
 * @since 2024/4/12
 */
@Getter
public class SuperJsonNode {
    /**
     * 根节点
     */
    private final JsonNode jsonNode;

    public SuperJsonNode(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    /**
     * @description 获取根节点下对应索引的内容，若找不到则抛出错误
     * @param i
	 *        索引
     * @return java.lang.String
     *         内容
     * @author gdrfgdrf
     * @since 2024/5/25
     */
    public String getString(int i) {
        return jsonNode.get(i).asText();
    }

    /**
     * @description 获取根节点下对应键的值，若找不到则抛出错误
     * @param key
	 *        键
     * @return java.lang.String
     *         值
     * @author gdrfgdrf
     * @since 2024/5/25
     */
    public String getString(String key) {
        return jsonNode.get(key).asText();
    }

    /**
     * @description 获取根节点下对应索引的内容，若找不到则返回 null
     * @param i
	 *        索引
     * @return java.lang.String
     *         内容
     * @author gdrfgdrf
     * @since 2024/5/25
     */
    public String getStringOrNull(int i) {
        if (jsonNode.has(i)) {
            return jsonNode.get(i).asText();
        }
        return null;
    }

    /**
     * @description 获取根节点下对应键的值，若找不到则返回 null
     * @param key
     *        键
     * @return java.lang.String
     *         值
     * @author gdrfgdrf
     * @since 2024/5/25
     */
    public String getStringOrNull(String key) {
        if (jsonNode.has(key)) {
            return jsonNode.get(key).asText();
        }
        return null;
    }

    /**
     * @description 检查根节点是否存在某个键
     * @param key
	 *        键
     * @return boolean
     *         true 存在，false 不存在
     * @author gdrfgdrf
     * @since 2024/5/25
     */
    public boolean contains(String key) {
        return jsonNode.has(key);
    }

    /**
     * @description 获取根节点的大小
     * @return int
     *         大小
     * @author gdrfgdrf
     * @since 2024/5/25
     */
    public int size() {
        return jsonNode.size();
    }

    /**
     * @description 获取根节点的所有键的迭代器
     * @return java.util.Iterator<java.lang.String>
     *         所有键的迭代器
     * @author gdrfgdrf
     * @since 2024/5/25
     */
    public Iterator<String> keySet() {
        return jsonNode.fieldNames();
    }
}

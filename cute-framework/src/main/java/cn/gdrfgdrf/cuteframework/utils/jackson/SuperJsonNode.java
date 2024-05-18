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

package cn.gdrfgdrf.cuteframework.utils.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.Iterator;

/**
 * @Description 定制 json 节点
 * @Author gdrfgdrf
 * @Date 2024/4/12
 */
@Getter
public class SuperJsonNode {
    private final JsonNode jsonNode;

    public SuperJsonNode(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    public String getString(int i) {
        return jsonNode.get(i).asText();
    }

    public String getString(String key) {
        return jsonNode.get(key).asText();
    }

    public String getStringOrNull(int i) {
        if (jsonNode.has(i)) {
            return jsonNode.get(i).asText();
        }
        return null;
    }

    public String getStringOrNull(String key) {
        if (jsonNode.has(key)) {
            return jsonNode.get(key).asText();
        }
        return null;
    }

    public boolean contains(String key) {
        return jsonNode.has(key);
    }

    public int size() {
        return jsonNode.size();
    }

    public Iterator<String> keySet() {
        return jsonNode.fieldNames();
    }
}

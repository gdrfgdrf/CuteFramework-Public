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

package cn.gdrfgdrf.cuteframework.utils.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description jackson 的 json 序列化工具类
 * @Author gdrfgdrf
 * @Date 2024/4/12
 */
@SuppressWarnings("unchecked")
public class JacksonUtils {
    private static final ObjectMapper MAPPER_INSTANCE = new ObjectMapper();

    private JacksonUtils() {}

    /**
     * @Description 将 Json 字符串反序列化为对象
     * @param jsonString
	 *        Json 字符串
	 * @param type
	 *        对象类型
     * @return T
     *         对象实例
     * @throws JsonProcessingException
     *         反序列化错误
     * @Author gdrfgdrf
     * @Date 2024/5/25
     */
    public static <T> T readString(String jsonString, Class<?> type) throws JsonProcessingException {
        return (T) MAPPER_INSTANCE.readValue(jsonString, type);
    }

    /**
     * @Description 将 Json 文件反序列化为对象
     * @param file
     *        Json 文件
     * @param type
     *        对象类型
     * @return T
     *         对象实例
     * @throws JsonProcessingException
     *         反序列化错误
     * @Author gdrfgdrf
     * @Date 2024/5/25
     */
    public static <T> T readFile(File file, Class<?> type) throws IOException {
        return (T) MAPPER_INSTANCE.readValue(file, type);
    }

    /**
     * @Description 将输入流反序列化为对象
     * @param inputStream
     *        输入流
     * @param type
     *        对象类型
     * @return T
     *         对象实例
     * @throws JsonProcessingException
     *         反序列化错误
     * @Author gdrfgdrf
     * @Date 2024/5/25
     */
    public static <T> T readInputStream(InputStream inputStream, Class<?> type) throws IOException {
        return (T) MAPPER_INSTANCE.readValue(inputStream, type);
    }

    /**
     * @Description 将字节反序列化为对象
     * @param bytes
     *        字节
     * @param type
     *        对象类型
     * @return T
     *         对象实例
     * @throws JsonProcessingException
     *         反序列化错误
     * @Author gdrfgdrf
     * @Date 2024/5/25
     */
    public static <T> T readBytes(byte[] bytes, Class<?> type) throws IOException {
        return (T) MAPPER_INSTANCE.readValue(bytes, type);
    }

    /**
     * @Description 新建一个可操作的 Json 对象实例
     * @return com.fasterxml.jackson.databind.node.ObjectNode
     *         可操作的 Json 对象实例
     * @throws JsonProcessingException
     *         反序列化错误
     * @Author gdrfgdrf
     * @Date 2024/5/25
     */
    public static ObjectNode newTree() throws JsonProcessingException {
        return (ObjectNode) MAPPER_INSTANCE.readTree("{}");
    }

    /**
     * @Description 反序列化 Json 字符串为 {@link SuperJsonNode}
     * @param jsonString
     *        Json 字符串
     * @return cn.gdrfgdrf.cuteframework.utils.jackson.SuperJsonNode
     *         {@link SuperJsonNode} 实例
     * @throws JsonProcessingException
     *         反序列化错误
     * @Author gdrfgdrf
     * @Date 2024/5/25
     */
    public static SuperJsonNode readStringTree(String jsonString) throws JsonProcessingException {
        return new SuperJsonNode(MAPPER_INSTANCE.readTree(jsonString));
    }

    /**
     * @Description 反序列化 Json 文件为 {@link SuperJsonNode}
     * @param file
     *        Json 文件
     * @return cn.gdrfgdrf.cuteframework.utils.jackson.SuperJsonNode
     *         {@link SuperJsonNode} 实例
     * @throws JsonProcessingException
     *         反序列化错误
     * @Author gdrfgdrf
     * @Date 2024/5/25
     */
    public static SuperJsonNode readFileTree(File file) throws IOException {
        return new SuperJsonNode(MAPPER_INSTANCE.readTree(file));
    }

    /**
     * @Description 反序列化输入流为 {@link SuperJsonNode}
     * @param inputStream
     *        输入流
     * @return cn.gdrfgdrf.cuteframework.utils.jackson.SuperJsonNode
     *         {@link SuperJsonNode} 实例
     * @throws JsonProcessingException
     *         反序列化错误
     * @Author gdrfgdrf
     * @Date 2024/5/25
     */
    public static SuperJsonNode readInputStreamTree(InputStream inputStream) throws IOException {
        return new SuperJsonNode(MAPPER_INSTANCE.readTree(inputStream));
    }

    /**
     * @Description 反序列化字节为 {@link SuperJsonNode}
     * @param bytes
     *        字节
     * @return cn.gdrfgdrf.cuteframework.utils.jackson.SuperJsonNode
     *         {@link SuperJsonNode} 实例
     * @throws JsonProcessingException
     *         反序列化错误
     * @Author gdrfgdrf
     * @Date 2024/5/25
     */
    public static SuperJsonNode readBytes(byte[] bytes) throws IOException {
        return new SuperJsonNode(MAPPER_INSTANCE.readTree(bytes));
    }

    /**
     * @Description 将数组类型的 Json 字符串转为列表对象
     * @param jsonString
	 *        数组类型的 Json 字符串
	 * @param E
	 *        列表的元素类型
     * @return java.util.List<E>
     *         列表
     * @throws JsonProcessingException
     *         反序列化错误
     * @Author gdrfgdrf
     * @Date 2024/5/25
     */
    public static <E> List<E> toList(String jsonString, Class<E> E) throws JsonProcessingException {
        List<E> result = new ArrayList<>();
        SuperJsonNode jsonNode = JacksonUtils.readStringTree(jsonString);
        if (jsonNode.getJsonNode().isArray()) {
            for (int i = 0; i < jsonNode.size(); i++) {
                E e = JacksonUtils.readString(jsonNode.getString(i), E);
                result.add(e);
            }
        }

        return result;
    }

    /**
     * @Description 将对象转为 Json 字符串
     * @param obj
	 *        对象
     * @return java.lang.String
     *         Json 字符串
     * @throws JsonProcessingException
     *         序列化错误
     * @Author gdrfgdrf
     * @Date 2024/5/25
     */
    public static String writeJsonString(Object obj) throws JsonProcessingException {
        return MAPPER_INSTANCE.writeValueAsString(obj);
    }


}

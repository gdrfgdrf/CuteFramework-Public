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

package cn.gdrfgdrf.cuteframework.common;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 核心版本列表，枚举名必须遵守以下规则
 * 版本排列为升序，越底下的版本越大，
 * 版本命名遵守以下规则
 * v 为 固定符号，所有版本前都必须带有该符号，该符号必须小写
 * major_version 为 主版本号，程序有重大更新或重大变更时 +1，
 * minor_version 为 副版本号，程序功能有了一定地增加或修改时 +1，
 * patch_version 为 修订版本号，bug 修复或一些小的改动时 +1，
 * publish_date 为 发布日期，
 * 日期必须为纯数字，年份、月份、日期之间无需用任何符号分隔，比如 20231231，当月份、日期不够两位数时补零处理，比如 20240502
 * _ 为 分隔符号，仅用作分割处理
 * publish_channel 为 发布渠道，必须为希腊字母表的 Alpha、Beta、RC、Release，它们之间的大小关系为左小右大
 * <p>
 * 遵守这样的排列顺序
 * V + major_version + minor_version + patch_version + publish_date + _ + publish_channel
 * 例子：
 * v1.1.1.20231231_Beta
 * v1.0.0.20240502_Release
 * <p>
 * 版本大小的对比为版本在该类中的位置决定。
 * 版本在该类中作为枚举则全部应该把全部的 "." 替换为 "_"，
 * 版本在该类中作为枚举时，除开头的 v 符号外，其他全部字符都应该大写
 *
 * @Author gdrfgdrf
 * @Date 2024/5/2
 */
@Getter
public enum VersionEnum {
    v1_0_0_UNDEFINED_RELEASE(
            "1",
            "0",
            "0",
            "undefined",
            PublishChannel.UNAVAILABLE
    ),
    /**
     * 不可用的版本，当插件所定义的核心版本在该枚举类中找不到时将返回该类
     */
    UNAVAILABLE(
            "unavailable",
            "unavailable",
            "unavailable",
            "unavailable",
            PublishChannel.UNAVAILABLE
    );

    /**
     * {@link VersionEnum#version} 字段到具体 {@link VersionEnum} 的映射
     */
    private static final Map<String, VersionEnum> VERSION_MAP = new HashMap<>();
    /**
     * 当前的核心版本
     */
    public static final VersionEnum CURRENT = VersionEnum.v1_0_0_UNDEFINED_RELEASE;
    /**
     * 完整的版本字符串
     */
    private final String version;
    /**
     * 主版本号
     */
    private final String majorVersion;
    /**
     * 副版本号
     */
    private final String minorVersion;
    /**
     * 修订版本号
     */
    private final String patchVersion;
    /**
     * 发布日期
     */
    private final String publishDate;
    /**
     * 发布渠道
     */
    private final PublishChannel channel;

    static {
        for (VersionEnum value : VersionEnum.values()) {
            VERSION_MAP.put(value.name(), value);
        }
        VERSION_MAP.remove(VersionEnum.UNAVAILABLE.name());
    }

    VersionEnum(
            String majorVersion,
            String minorVersion,
            String patchVersion,
            String publishDate,
            PublishChannel channel
    ) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.patchVersion = patchVersion;
        this.publishDate = publishDate;
        this.channel = channel;

        this.version = String.format(
                "v%s.%s.%s.%s_%s",
                majorVersion,
                minorVersion,
                patchVersion,
                publishDate,
                channel.getChannel()
        );
    }

    public int compare(VersionEnum version2) {
        return VersionEnum.compare(this, version2);
    }

    /**
     * @Description 对比两个版本之间的大小，
     * 返回 -1 说明 version1 < version2，
     * 返回 0 说明 version1 = version2，
     * 返回 1 说明 version1 > version2，
     * 两个版本中有一个或全部为 {@link VersionEnum#UNAVAILABLE} 时返回 -2
     *
     * @param version1
	 *        用来对比的第一个版本
     * @param version2
     *        用来对比的第二个版本
     * @return int
     *         对比结果
     * @Author gdrfgdrf
     * @Date 2024/5/2
     */
    public static int compare(VersionEnum version1, VersionEnum version2) {
        if (version1 == VersionEnum.UNAVAILABLE || version2 == VersionEnum.UNAVAILABLE) {
            return -2;
        }

        int ordinal1 = version1.ordinal();
        int ordinal2 = version2.ordinal();

        return Integer.compare(ordinal1, ordinal2);
    }

    public static VersionEnum get(String versionStr) {
        VersionEnum versionEnum = VERSION_MAP.get(versionStr);
        if (versionEnum == null) {
            return VersionEnum.UNAVAILABLE;
        }
        return versionEnum;
    }

    /**
     * @Description 发布渠道
     * @Author gdrfgdrf
     * @Date 2024/5/8
     */
    @Getter
    public enum PublishChannel {
        ALPHA("Alpha"),
        BETA("Beta"),
        RC("RC"),
        RELEASE("Release"),
        UNAVAILABLE("Unavailable");

        /**
         * 渠道名
         */
        private final String channel;

        PublishChannel(String channel) {
            this.channel = channel;
        }
    }
}

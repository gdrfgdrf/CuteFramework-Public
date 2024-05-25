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

/**
 * @description
 * !!! 所有翻译者必须仔细阅读该注释 !!! <p>
 * !!! All translators must read this note carefully !!! <p>
 * <p>
 * 该包为语言模块，语言模块过于复杂，特此该文件注释，<p>
 * 为了适配同一语言不同地区方言的情况，比如汉语下有简体中文和繁体中文，<p>
 * 该模块的采取了多个语言实现，一个语言汇总的方式来实现语言支持，<p>
 * 包 io.github.gdrfgdrf.cuteframework.locale.language 下为多个语言翻译，<p>
 * 包 io.github.gdrfgdrf.cuteframework.locale.language 下除语言大类的包的命名外必须全部遵守 ISO 639-3 标准，<p>
 * 若有 ISO 639-3 标准没有收录的语言，则必须翻译为英语作为包名，<p>
 * <p>
 * 比如汉语有二简字这种 ISO 639-3 标准没有的东西 <p>
 * 则需要使用 chinese.simplified 作为简体中文的实现，<p>
 * 使用 chinese.traditional 作为繁体中文的实现，<p>
 * 这种情况下包的命名同样无需遵守 ISO 639-3 标准，但必须经过英语翻译，<p>
 * <p>
 * 包之间具有继承关系，比如 <p>
 * chinese 包下必须是汉语的翻译，<p>
 * chinese.simplified 下必须为简体中文的翻译，<p>
 * 不能在 chinese.traditional 下进行简体中文的翻译。<p>
 * <p>
 * io.github.gdrfgdrf.cuteframework.locale.language 包下的第一层包必须为语言大类包，<p>
 * 汉语为 chinese 包，英语为 english 包，法语为 french 包，<p>
 * 为了方便开发者定位语言大类包，语言大类无需遵守 ISO 639-3 标准，<p>
 * 但必须是语言名在英语下的翻译，比如 <p>
 * 汉语不能用 "汉语" 作为包名，而必须用 "chinese"，<p>
 * 法语不能用 "français" 作为包名，而必须用 "french"。<p>
 * <p>
 * 语言大类包的下一层级必须是语言翻译的具体实现，比如 <p>
 * 简体中文则为 simplified，繁体中文则为 traditional，<p>
 * 比如汉语的情况下，简体中文 和 繁体中文 不能必须出现任何的地区性文字，比如粤语的 “冇”。<p>
 * <p>
 * 若语言翻译的包下还有其他的语言方言或为了更好地实现翻译，可进行迭代操作，比如 <p>
 * 要在 chinese.simplified 下支持更好的粤语简体中文翻译，则可新建包 yue，<p>
 * 组成 chinese.simplified.yue，<p>
 * yue 包下可进行粤语的简体中文翻译，<p>
 * 命名为 yue 是因为在 ISO 639-3 标准下粤语为 yue。<p>
 * <p>
 * 为了代码的简洁，语言翻译的包的迭代操作最多可迭代 5 层，<p>
 * 语言大类包不算层级，算语言翻译包，<p>
 * 假设粤语有如下的语言分化关系 aaa -> bbb -> ccc -> ddd -> eee 四个分支 <p>
 * 那么最多只能进行到 ccc 的翻译，不能再继续往下，<p>
 * 也就是说包最大只能到 chinese.simplified.yue.aaa.bbb.ccc。<p>
 * <p>
 * io.github.gdrfgdrf.cuteframework.locale.language 下的所有包的命名、类命名、翻译内容都不能有任何的违法词汇，包括但不限于：<p>
 * 侮辱性词汇，<p>
 * 性骚扰词汇，<p>
 * 性暗示词汇，<p>
 * 种族歧视词汇，<p>
 * 挑起阶级对立词汇，<p>
 * 等等。 <p>
 * <p>
 * 包的命名规则仅和 ISO 639-3 标准 相关，<p>
 * 翻译者必须查阅 ISO 639-3 标准，<p>
 * 每一个翻译类的基础信息都必须有所有翻译者的署名、创建日期，以竖杠 "|" 分割，在上一行必须写上 "Basic:" <p>
 * 之后若有修改则必须和基础信息隔开一行并写上 修改日期、修改内容、修改者署名，以 "|" 分割，第一条修改信息的上一行必须写上 "Change:" <p>
 * 修改内容的格式为 xxx -> xxx，<p>
 * 修改信息之间无需空行，<p>
 * 日期格式为 YYYY-MM-DD，日期遵循协调时间时，即 UTC 时间，若数字仅个位数但格式有多位数，则补零处理，比如 1 月要写成 01，<p>
 * 例子：<p>
 *      Basic: <p>
 *      gdrfgdrf | 2024-04-09 <p>
 * <p>
 *      Change: <p>
 *      2024-05-01 | xxx -> xxx | test-person-1 <p>
 *      2024-06-01 | xxx -> xxx | test-person-2 <p>
 * <p>
 *      public class ExampleLanguage {} <p>
 * <p>
 * 翻译问题由翻译提供者承担责任，任何人不得在未经翻译提供者的允许下擅自修改翻译内容。<p>
 * 包 io.github.gdrfgdrf.cuteframework.locale.language 下都会成为官方翻译，开发者有权添加或删除语言，但无权修改语言内容。<p>
 * 开发者添加或删除语言必须和其他开发者进行讨论，知道添加（删除）语言会有什么影响。<p>
 *
 * ===================================================================================================================== <p>
 * 以下为语言加载规则 <p>
 * 语言的加载将会由 {@link io.github.gdrfgdrf.cuteframework.locale.LanguageLoader} 完成。<p>
 * 所有语言汇总类必须实现 {@link io.github.gdrfgdrf.cuteframework.locale.base.LanguageCollect}，<p>
 * 所有语言翻译类必须实现 {@link io.github.gdrfgdrf.cuteframework.locale.base.LanguageBlock}，<p>
 * <p>
 * 程序会先从配置文件中获取到设置的语言，<p>
 * 之后检查该语言文件是否存在，不存在则从语言实现类中读取语言并写入文件，存在则从文件中读取语言，<p>
 * 读取到语言之后将会通过反射赋值到汇总类。<p>
 * 所有实现了 {@link io.github.gdrfgdrf.cuteframework.locale.base.LanguageBlock} 的类的所有字段 <p>
 * 的类型都必须是 {@link io.github.gdrfgdrf.cuteframework.locale.LanguageString}，<p>
 * 并且字段名必须存在于汇总类中。<p>
 * 以下为语言文件的格式 <p>
 *      语言汇总类名: { <p>
 *          "键1": "值1", <p>
 *          "键2": "值2", <p>
 *          "键3": "值3" <p>
 *      } <p>
 * <p>
 * 引入一个新概念：owner，<p>
 * 该值指定某个程序，比如说当加载框架语言时，该值是 cute-framework，<p>
 * 加载使用了该框架的程序时，该值可以是程序名，<p>
 * 当出现重名时，如果提供的类加载器能获取语言类，那么后来的将会覆盖先来的，<p>
 * 同时，<p>
 * 保存语言将会保存在 {@link io.github.gdrfgdrf.cuteframework.common.Constants#LOCALE_LANGUAGE_FOLDER} 下的 owner 的值的文件夹， <p>
 * 比如说框架的 owner 为 cute-framework，<p>
 * 那么保存的语言文件则在 {@link io.github.gdrfgdrf.cuteframework.common.Constants#LOCALE_LANGUAGE_FOLDER}/cute-framework <p>
 * <p>
 * 例子：<p>
 *      假设当前配置文件中设置的语言为 chinese_simplified，<p>
 *      那么程序将检查 chinese_simplified.json 文件是否存在，<p>
 *      有以下两种情况：<p>
 *          文件存在，<p>
 *                  从中读取出语言，假设读取到的 语言汇总类名 是 "SystemLanguage"，<p>
 *                  那么程序将会获取 io.github.gdrfgdrf.cuteframework.locale.collect 下，<p>
 *                  实现了 {@link io.github.gdrfgdrf.cuteframework.locale.base.LanguageCollect} 的 "SystemLanguage" 类，<p>
 *                  之后获取其中所有类型为 {@link io.github.gdrfgdrf.cuteframework.locale.LanguageString} 的字段并进行循环，<p>
 *                  获取到字段名，并到文件读取 语言汇总类名 "SystemLanguage" 节点下的和字段名相同的值，<p>
 *                  之后反射设置给字段，<p>
 *                  若 {@link io.github.gdrfgdrf.cuteframework.locale.base.LanguageCollect} 的 "SystemLanguage" 类中 <p>
 *                  有值是 语言汇总类名 "SystemLanguage" 节点下没有的，<p>
 *                  则会从当前语言下对应 {@link io.github.gdrfgdrf.cuteframework.locale.base.LanguageBlock} 的 "SystemLanguage" 类中获取。<p>
 *                  若找不到则直接设置为 null，<p>
 *                  如此往复，语言文件读取完成。<p>
 *          文件不存在，<p>
 *                  将 chinese_simplified 的 "_" 全部替换为 "." 得到 chinese.simplified <p>
 *                  并添加到 io.github.gdrfgdrf.cuteframework.locale.language 之后得到 <p>
 *                  io.github.gdrfgdrf.cuteframework.locale.language.chinese.simplified，<p>
 *                  如果 io.github.gdrfgdrf.cuteframework.locale.language.chinese.simplified 这个包不存在，<p>
 *                  则抛出 {@link io.github.gdrfgdrf.cuteframework.locale.exception.NotFoundLanguagePackageException}，<p>
 *                  若存在，则获取该包下所有实现了 {@link io.github.gdrfgdrf.cuteframework.locale.base.LanguageBlock} 的类，<p>
 *                  假设获取到实现了 {@link io.github.gdrfgdrf.cuteframework.locale.base.LanguageBlock} 的 "SystemLanguage" 类，<p>
 *                  那么也将会去 io.github.gdrfgdrf.cuteframework.locale.collect 下获取 <p>
 *                  实现了 {@link io.github.gdrfgdrf.cuteframework.locale.base.LanguageCollect} 的 "SystemLanguage" 类，<p>
 *                  之后通过反射 {@link java.lang.Class#getDeclaredFields()} <p>
 *                  获取到 {@link io.github.gdrfgdrf.cuteframework.locale.base.LanguageCollect} 的 "SystemLanguage" 类 的所有字段，<p>
 *                  然后开始对这些字段循环，获取到字段名，<p>
 *                  并前往 {@link io.github.gdrfgdrf.cuteframework.locale.base.LanguageBlock} 的 "SystemLanguage" 类 获取到 <p>
 *                  拥有相同名字的字段，并获取该字段的内容，<p>
 *                  然后赋值给 {@link io.github.gdrfgdrf.cuteframework.locale.base.LanguageCollect} 的 "SystemLanguage" 类 的对应字段，<p>
 *                  如此往复，类文件读取完成，<p>
 *                  完成读取后将会把所有 {@link io.github.gdrfgdrf.cuteframework.locale.base.LanguageCollect} 的类序列化到语言文件。<p>
 *
 * @author gdrfgdrf
 * @since 2024/4/9
 */
package io.github.gdrfgdrf.cuteframework.locale;


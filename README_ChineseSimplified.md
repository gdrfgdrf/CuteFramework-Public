CuteFramework
===
__[English](https://github.com/gdrfgdrf/CuteFramework-Public/blob/master/README.md)__ | [简体中文](https://github.com/gdrfgdrf/CuteFramework-Public/blob/master/README_ChineseSimplified.md)
- 一个开发框架.

<!-- TOC -->
* [CuteFramework](#cuteframework)
  * [模块](#模块)
  * [特性](#特性)
    * [BeanManager](#beanmanager)
      * [BeanResolver](#beanresolver)
    * [Config](#config)
    * [ExceptionHandler](#exceptionhandler)
    * [Event](#event)
    * [Locale](#locale)
    * [Plugin](#plugin)
    * [Utils](#utils)
      * [Asserts](#asserts)
      * [Jackson](#jackson)
      * [Stack](#stack)
    * [Thread](#thread)
    * [Class](#class)
  * [依赖](#依赖)
    * [复制的代码](#复制的代码)
  * [开源协议](#开源协议)
<!-- TOC -->

模块
------------------------
- BeanManager
    - BeanResolver
- Config
- ExceptionHandler
- Event
- Locale
- Plugin
- Utils

特性
------------------------
### BeanManager
和 SpringBoot 的 Bean 容器类似，  
全部被标记为 Bean 的类将会被自动创建。

#### BeanResolver
BeanManager 的子模块，  
Bean 在实例化完成之后将会发送到该模块进行初始化。  
该模块支持对类和方法的初始化，

### Config
自动序列化配置文件。

### ExceptionHandler
拥有一个全局异常捕获器      
可以捕获全部没有被 try...catch 捕获的异常，  
然后会将异常发送到指定的异常处理器进行处理。  
同时该模块也应用自定义异常类，可以获取本地化的字符串。

### Event
该模块使用 Guava 的 Event Bus 模块实现。  
支持同步事件和异步事件。  
同时还支持一个依赖于 BeanManager 的特性，  
如果有一个类想要成为事件订阅者，  
只需要对其使用 EventListener 注解，
这将会让其成为一个 Bean，  
然后它将会在 Bean 加载时被自动注册到 EventManager。

### Locale
这是一个本地化语言字符串的模块。   
拥有完整我语言规则。   
支持自动的语言文件序列化。  
想要添加一个语言信息，  
只需将语言键添加到指定的集合类，  
并在语言块中添加一个具体字符串。

### Plugin
框架中预留了许多的 API。  
同时，框架还有几乎完整的 wiki信息。  
还有着安全错误以防止某些插件的违规操作。

### Utils
#### Asserts
检查变量， 如果检查结果错误，   
将会抛出一个异常，其中拥有本地化的语言信息
#### Jackson
使用 Jackson 对 Json 文件进行序列化.
#### Stack
在堆栈层面对某个方法进行保护.  
只允许某个类或某个方法执行方法.
### Thread
线程池支持.
### Class
对类文件进行一些操作,  
比如说进行类扫描.

依赖
------------------------
| Name                                                  | Using          |
|-------------------------------------------------------|----------------|
| [Reflections](https://github.com/ronmamo/reflections) | 类扫描            |
| [Guava](https://github.com/google/guava)              | 对事件模块的实现       |
| [Jackson](https://github.com/FasterXML/jackson)       | 对 Json 文件进行序列化 |
| [SLF4J](https://github.com/qos-ch/slf4j)              | 日志框架           |
| [Logback](https://github.com/qos-ch/logback)          | 对日志框架的实现       |
| [Lombok](https://github.com/projectlombok/lombok)     | 代码简化           |

### 复制的代码
modified:
- io.github.gdrfgdrf.cuteframework.utils.TypeParameterMatcher
    - io.netty.util.internal.TypeParameterMatcher of Netty

开源协议
------------------------
我们使用 Apache License 2.0。
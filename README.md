CuteFramework 
===
__[English](https://github.com/gdrfgdrf/CuteFramework-Public/blob/master/README.md)__ | [简体中文](https://github.com/gdrfgdrf/CuteFramework-Public/blob/master/README_ChineseSimplified.md)
- It's a development framework.

<!-- TOC -->
* [CuteFramework](#cuteframework-)
  * [Modules](#modules)
  * [Features](#features)
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
  * [Dependencies](#dependencies)
    * [Copied code](#copied-code)
  * [License](#license)
<!-- TOC -->

Modules
------------------------
- BeanManager
  - BeanResolver 
- Config
- ExceptionHandler
- Event
- Locale
- Plugin
- Utils

Features
------------------------
### BeanManager
Just like SpringBoot's bean,  
All Bean classes can be automatically scanned and created.  

#### BeanResolver
It's a submodule of BeanManager,  
After the Bean is instantiated, it is passed to this module for initialization.  
Class resolver and method resolver is available.

### Config
Automatically serialize configuration files.  

### ExceptionHandler
There is a global exception handler that catches exceptions    
that are not caught by try...catch,  
then it is then dispatched to the specified exception handler.  
It also has custom exception classes which can provide a message that has been localization.

### Event
This module is implemented using Guava's Event Bus module.  
This module supports both synchronous and asynchronous events.
Also, it has a feature which is depended on BeanManager module,  
if a class wants to be an event subscriber, simply use the EventListener annotation on it  
this will make it a bean, then it will be registered to the EventManager automatically  
when the BeanManager is creating beans.

### Locale
This is a localized language string module,   
it has complete language rules,   
support for automatic serialization operations.  
Adding a message, simply add the language key to the specified collection class  
and add a concrete string to the language block.  

### Plugin
There are a number of reserved apis in the framework.  
Also, the framework has an almost complete wiki.  
There are also security measures to prevent the illegal operation of certain plugins.  

### Utils
#### Asserts
It can check some arguments, if the check result is false,   
then it will throw an exception which has a localized message.  
#### Jackson
Serialization of Json, which is using Jackson.  
#### Stack
Protect a method from the stack level.  
For example, only a certain class or method is allowed to execute that method.
### Thread
Thread pool support.  
### Class
Perform some operations on the class file,  
such as scanning the classes under the specified package.  

Dependencies
------------------------
| Name                                                  | Using                                     |
|-------------------------------------------------------|-------------------------------------------|
| [Reflections](https://github.com/ronmamo/reflections) | Class scan                                |
| [Guava](https://github.com/google/guava)              | Event module support                      |
| [Jackson](https://github.com/FasterXML/jackson)       | Serialize the Json file                   |
| [SLF4J](https://github.com/qos-ch/slf4j)              | Log framework                             |
| [Logback](https://github.com/qos-ch/logback)          | Implementation of logging framework SLF4J |
| [Lombok](https://github.com/projectlombok/lombok)     | Code simplification                       |

### Copied code
modified:
- io.github.gdrfgdrf.cuteframework.utils.TypeParameterMatcher
  - io.netty.util.internal.TypeParameterMatcher of Netty

License
------------------------
We are using the Apache License 2.0
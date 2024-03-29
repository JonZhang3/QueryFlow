# 简介

QueryFlow 是一款持久层框架，旨在使用者可以像水流一样流畅、简单、快速地编写数据库操作代码。

>  该文档适用于 QueryFlow V1.0.5

## 特性
- **方便自由**：既可以直接编写复杂 SQL，也可以使用条件构造器或直接传入一个 Entity 来对数据库进行 CRUD 操作
- **Druid连接池**：内部采用 Druid 数据库连接池，可以直接使用 Druid 提供的强大的监控等功能
- **对象映射**：查询结果自动映射到基本数据类型（以及其包装类型）、Java Bean、Map，方便后续操作。当然也可以自由地操作查询结果
- **简单分页**：分页方法中传入查询 SQL、page、limit 即可快速分页查询，兼容 MySQL、Oracle、H2等数据库
- **注解 SQL**：在抽象方法或接口方法上使用 `@Query` 或 `@Update` 注解，并在抽象类或接口上使用 `@Mapper` 注解
- **数据库字典映射**：在查询时不必再关联代码表获取代码所对应的值，一个注解搞定
- **多数据源**：支持配置多数据源，且配置使用方便
- **存储过程**：方便的存储过程调用，快捷注册输出参数

## 安装（Maven）
```xml
<dependency>
    <groupId>com.github.jonzhang3</groupId>
    <artifactId>queryflow</artifactId>
    <version>1.0.5</version>
</dependency>
```

## 文档
[文档](https://jonzhang3.github.io/QueryFlow)

## 一起来

如果你发现该项目中存在 BUG，希望可以及时反馈给我，我会在第一时间进行修复。如果你希望有什么好用的功能或改进建议，也可以提供给我，万分感谢！

也欢迎你参与进来！！！

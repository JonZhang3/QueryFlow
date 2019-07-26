# 通用 CRUD

你可以自己编写 SQL，从而使用 Java 代码进行更加灵活的逻辑控制，然后使用 `Accessor` API 去执行。

你也可以使用单表操作的 API，像写SQL一样编写 Java 代码。

你还可以在接口或者抽象类上使用 `@Mapper` 注解，在方法上使用 `@Execute` 或 `@Select` 执行 CRUD 操作。

## 实体注解说明

> @Table

表示该类是个表实体类，并标明表名

|  值   | 描述                                                         |
| :---: | ------------------------------------------------------------ |
| value | 表名，如果为空的话，则使用实体类名作为表名，例：User -> user，RoleAndUser -> roleAndUser |

注：当然并不一定是被 `@Table` 注解的类才可以用来接受查询的返回结果，任何 Java Bean 都可以。

> @Id

表示该字段为表中的主键

| 值           | 描述                                                 |
| ------------ | ---------------------------------------------------- |
| value        | 指定字段的名称，如果未指定，则直接使用该成员变量名称 |
| keyGenerator | 指定主键生成策略，默认为 Snowflake 算法              |

你也可以自定义主键生成策略：

1. 实现 `KeyGenerator<T>` 接口
2. 然后在  `KeyGenerateUtil` 注册你的实现类，`KeyGenerateUtil.registerKeyGenerator(class)`
3. 为主键指定 `keyGenerator`，或调用 `KeyGenerateUtil.generateId(CustomKeyGenerator.class)` 方法生成唯一键。

> @Column

字段注解

| 值              | 描述                                                         |
| --------------- | ------------------------------------------------------------ |
| value           | 指定字段名称。如果未指定，则直接使用该成员变量名称           |
| exists          | 指定该字段是否在表中存在。如果不存在，在你直接 insert(实体对象)时，则不会解析该字段 |
| isDictionaryKey | 指定该字段是否是一个数据字典字段，true 表示存在              |
| dictionaryClass | 如果该字段是一个数据字典字段，指定数据字典的枚举类           |

### 数据字典

如果使用数据字典功能，需要指定字段的 `isDictionaryKey` 为 `true`

并且编写枚举类同时实现 `DictionaryEnum` 接口。

如果该数据字典字段为 `DictionaryEnum` 类型，并且指定 `isDictionaryKey` 为 `true`，则无需再指定 `dictionaryClass`。在处理查询返回结果时，则会填充相应的枚举值。

如果字段的 Java 类型为 `String` 类型，并且指定 `isDictionaryKey` 为 `true`，则必须指定 `dictionaryClass`。在填充时会填充上相应枚举值 的 `getValue()` 方法返回结果。

如果是其他类型，则不会按照数据字典字段的逻辑来填充值。

### 其他注解

> @DataSource

在使用 `@Select` 或 `@Execute` 注解的方法上使用该注解，指定操作使用的数据源。

| 值    | 描述               |
| ----- | ------------------ |
| value | 添加数据源时的 tag |
# 添加数据源

### 配置文件

在使用配置文件配置数据源时，如果不主动提供配置文件路径，
默认会依次项目部署目录（SpringBoot）、项目部署目录下的 conf 目录、 classpath 路径下寻找配置文件，默认的配置文件名为 `queryflow.yaml` 或 `queryflow.yml` 或 `queryflow.properties` ，优先寻找 `queryflow.yaml` 。

配置文件可采用 YAML 格式，也可以采用 Properties 格式。如果采用 Properties 格式配置文件，限制是只能配置一个数据源。

##### YAML

```yaml
# 单数据源示例
defaultPageLimit: 10
camelCaseToSnake: true
url: jdbc:h2:mem:test
username: root
password: root
maxActive: 100
initialSize: 10

#-------------------------------------------------
# 多数据源示例
defaultPageLimit: 10
camelCaseToSnake: true
datasources:
  - tag: db1
    url: jdbc:h2:mem:test
    username: root
    password: root
    maxActive: 100
    initialSize: 10
  - tag: db2
    url: jdbc:h2:mem:test2
    username: root
    password: root
    maxActive: 100
    initialSize: 10
```

##### Properties

```properties
defaultPageLimit=10
camelCaseToSnake=true
url=jdbc:h2:mem:test
username=root
password=root
maxActive=100
initialSize=10
```

具体的配置文件可配置项：

```yaml
workerId: <int> 全局配置。用于 Snowflake ID 生成算法。默认为 1
dataCenterId: <int> 全局配置。用于 Snowflake ID 生成算法。默认为 1
camelCaseToSnake: <boolean> 全局配置。是否将小驼峰命名法转化为下划线命名法，将在实体映射，处理返回结果时起作用。默认为 true
debug: <boolean> 全局配置。是否启用调试模式。默认为 false
defaultPageLimit: <int> 全局配置。设置默认分页大小。默认为 10
queryTimeout: <int> 全局配置。查询超时时间，>=0。如果设置为0，表示没有限制。默认为 0
queryMaxRows: <int> 全局配置。查询返回的最大行数，>=0。如果设置为0，表示没有限制。默认为 0
maxFieldSize: <int> 全局配置。字段的最大大小，>=0。如果设置为0，表示没有限制。默认为 0
closeAfterExecuted: <boolean> 全局配置。指定是否在每次执行后关闭数据库连接。false 表示不关闭，你需要自己手动关闭数据库连接。默认为 false
datasources:
  - tag: <string> 标志
    url: <string> 数据库连接
    username: <string> 数据库用户名
    password: <string> 数据库密码
    maxActive: <int> 连接池保存的最大连接数
    initialSize: <int> 连接池初始连接数
    minIdle: <int>
    maxWait: <int>
    timeBetweenEvictionRunsMillis: <long> 设置多久进行一次空闲连接的检测，单位 ms
    minEvictableIdleTimeMillis: <long> 设置一个连接在连接池中的最小生存时间，单位 ms
    validationQuery: <string> 验证 SQL，验证数据库是否可以连接
    filters: <string> 配置 Druid 过滤器
```



```java
AccessorFactoryBuilder builder = new AccessorFactoryBuilder();

// 从配置文件中构建数据源
builder.fromFile();

// 指定配置文件路径
builder.fromFile(path);

// 你可以不使用默认的 Druid 连接池，添加自己的数据源
builder.addDatabase(dataSource);

// 使用详细数据源配置来添加数据源（默认采用 Druid数据源）
builder.addDatabase(DatabaseConfig);

// 使用连接池的默认配置，只提供 url、username、password
builder.addDatabase(url, username, password);

// 如果你使用了 @Mapper 注解来使用注解 SQL，需要提供需要扫描的包，来寻找这些被注解的类。
builder.scanPackage(packageNames...);

// 最后调用该方法完成构建
builder.build();
```

在你构建完成数据源之后，默认的数据源即你首次添加的数据源，或配置文件中的第一个数据源。

# 获取 Accessor

```java
// 获取可操作数据库的 Accessor。不带参数，表示获取按顺序添加数据源后的第一个数据源
AccessorFactory.accessor();

// 获取添加顺序指定位置的 Accessor，从 1 开始。
AccessorFactory.accessor(index);

// 获取指定标签的 Accessor
AccessorFactory.accessor(tag);

// 当然你也可以使用接口类 A 进行数据库操作，减少代码的输入
A.tag(tagName);// 使用指定的 数据源
A.update(sql);// 使用默认数据源操作数据库
```


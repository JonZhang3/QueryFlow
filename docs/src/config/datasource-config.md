# DatabaseConfig

> tag

为该数据源设置标签，方便配置多数据源后，选取指定数据源。

> url

数据库 `url`

> username

数据库用户名

> password

数据库密码

> maxActive

设置最大连接数。默认为 `8`

> initialSize

初始化连接数量。默认为 `0`

> minIdle

设置最小空闲连接数量。默认为 `0`

> maxWait

设置获取连接等待超时时间，单位： `ms`。默认 `-1`，没有限制

> timeBetweenEvictionRunsMillis

设置间隔多久进行一次检测需要关闭的空闲连接，单位：`ms`。默认为 `60000`，60秒

> minEvictableIdleTimeMillis

设置一个连接在池中最小生存时间，单位：`ms`。默认为 `1800000`，半小时

> validationQuery

用来检测连接连接是否有效的sql，要求是一个查询语句，常用 `select 'x'`。默认为 `null`

> validationQueryTimeout

检测连接是否有效的超时时间，单位 `s`。默认为 `0`，表示没有限制

> poolPreparedStatements

设置是否开启 PrepardStatement 缓存。默认为 `false`

> maxPoolPreparedStatementPerConnectionSize

设置每个连接上 PreparedStatement 缓存的大小。默认为 `10`

> filters

设置扩展插件

由于连接池默认为采用 Druid，所以你可以查看 [Druid 相关文档](https://github.com/alibaba/druid)来了解连接池配置信息.


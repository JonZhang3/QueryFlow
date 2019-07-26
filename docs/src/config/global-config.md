# GlobalConfig

> workerId

如果使用 `Snowflake` 算法，指定 worker id。默认为 `1`

> dataCenterId

如果使用 `Snowflake` 算法，指定 data center id。默认为 `1`

> camlCaseToSnake

如果为 `true`，在数据库查询操作返回结果后，填充 JavaBean 时会尝试将 Bean 中的成员变量转换为下划线的命名方式，从而与数据库返回的列名正确匹配并填充值。默认为 `true`

> debug

如果为 `true`，在执行 SQL 时，会将执行的 SQL 已经 填充之后 SQL 语句打印到日志中。默认为 `false`

> defaultPageLimit

指定默认的分页大小，分页查询时使用。默认为 `10`

> queryTimeout

设置 `Connection` 执行 SQL 语句时的超时时间，如果为 `0` 表示没有限制。具体可以查看 `Statement#setQueryTimeout` 方法。默认为 `0`

> queryMaxRows

为查询操作设置结果最大行数限制，如果查询结果超出此限制，剩余的行数将被删除，如果为 `0` 表示没有限制。具体可以查看 `Statement#setMaxRows` 方法。默认为 `0`

> maxFieldSize

设置查询返回结果中每列的大小限制，如果超出该限制，多余的数据将被丢弃。如果为 `0` 表示没有限制。具体可以查看 `Statement#setMaxFieldSize` 方法。默认为 `0`

> closeAfterExecuted

设置在进行增删改查是是否立即关闭数据库连接，`true` 表示立即关闭。默认为 `false`，即同一个线程使用同一个 连接，除非你主动调用了关闭方法。
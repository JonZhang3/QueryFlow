# Accessor API

> ```java
> DataSource getDataSource();
> ```

获取数据源

> ```java
> UpdateStatement createUpdate(String sql);
> ```

执行 DDL，DML语句

> ```java
> int update(String sql, Object... params);
> ```

执行 DDL，DML语句

> ```java
> int update(String sql, List<Object> values);
> ```

执行 DDL，DML语句

> ```java
> BatchStatement createBatch();
> ```

创建批量操作表达式，批量执行不需要预编译的 SQL 语句

> ```java
> int[] batch(String... sqls);
> ```

批量执行 SQL 语句

> ```java
> int[] batch(List<String> sqls);
> ```

批量执行 SQL 语句

> ```java
> PreparedBatchStatement prepareBatch(String sql);
> ```

创建预编译批量执行表达式，批量执行预编译 SQL 语句

> ```java
> int[] batch(String sql, List<List<Object>> values);
> ```

批量执行需要预编译的 SQL 语句

> ```java
> SelectStatement createQuery(String sql);
> ```

创建查询表达式

> ```java
> SelectStatement query(String sql, Object... values);
> ```

执行查询语句，操作查询结果可以使用 `SelectStatement` 类中的 `one`，`oneMap`，`list`，`listMap`，`result` 方法。

> ```java
> SelectStatement query(String sql, List<Object> values);
> ```

执行查询语句

> ```java
> <T> Pager<T> page(String sql, int page, int limit, Class<T> requiredType, Object... values);
> ```

分页查询，指定页码，每页的大小即可

> ```java
> <T> Pager<T> page(String sql, List<Object> values, int page, int limit, Class<T> requiredType);
> ```

分页查询

> ```java
> <T> Pager<T> page(String sql, List<Object> values, int page, Class<T> requiredType);
> ```

分页查询，指定页码即可。采用全局设置 limit，`GlobalConfig.defaultPageLimit(limit)`

> ```java
> Pager<ResultMap> pageToMap(String sql, List<Object> values, int page, int limit);
> ```

分页查询，将查询结果存入到 `ResultMap` 中

> ```java
> Pager<ResultMap> pageToMap(String sql, List<Object> values, int page);
> ```

分页查询

> ```java
> int count(String sql, Object... values);
> ```

统计查询

> ```java
> int count(String sql, List<Object> values);
> ```

统计查询

> ```java
> CallStatement createCall(String sql);
> ```

创建存储过程调用表达式

> ```java
> CallStatement call(String sql, Object... values);
> ```

调用存储过程

> ```java
> CallStatement call(String sql, List<Object> values);
> ```

调用存储过程

> ```java
> void openTransaction();
> ```

开启事务

> ```java
> void openTransaction(TransactionLevel level);
> ```

开启事务并指定事务级别

> ```java
> void commit();
> ```

提交事务并主动关闭数据库连接

> ```java
> void commit(boolean close);
> ```

提交事务，指定是否关闭数据库连接

> ```java
> void rollback();
> ```

回滚事务并关闭数据库连接

> ```java
> void rollback(boolean close);
> ```

回滚事务，指定是否关闭数据库连接

> ```java
> void close();
> ```

关闭数据库连接

> ```java
> <T> T getMapper(Class<T> clazz);
> ```

获取扫描的 `@Mapper` 注解类
# 快速开始

## 简单示例

> 假设有一张 User 表，以及其对应的实体类 User

首先要配置数据库
```java
AccessorFactoryBuilder builder = new AccessorFactoryBuilder();
DatabaseConfig config = new DatabaseConfig(url, user, pass).setMaxWait(3000).setTag(AccessorFactory.DEFAULT_TAG);
builder.addDatabse(config);
builder.build();
```

```java
@Table("user")
public class User {
    @Id
    private long id;
    @Column("userName")
    private String userName;
    @Column
    private String password;
    @Column
    private int age;
    
    // setter and getter
    
}
```

> 手写 SQL，手动填充参数（灵活，编写复杂 SQL）

``` java
// 插入
String sql = "INSERT INTO user VALUES (?, ?, ?, ?)";
Object[] params = {KeyGenerateUtil.generateId(), "张三", "123456", 20};
// 更少的代码可以使用 A.update(sql, params);
int row = AccessorFactory.accessor().update(sql, params);

// 删除
String sql = "DELETE FROM user WHERE id = ?";
row = A.update(sql, id);

// 更新
String sql = "UPDATE user SET userName = ? WHERE id = ?";
Object[] params = {id};
row = A.createUpdate(sql)
  .bind(userName)
  .bindArray(params)
  .execute();

// 查询单条记录
String sql = "SELECT * FROM user WHERE id = ?";
Object[] params = {id};
// 该方法中的第三个参数，不一定是使用 @Table 注解的类，可以是任意 Java Bean
User user = A.query(sql, params).one(User.class);
// 你也可以将查询的结果封装到一个 Map 中
// ResultMap map = A.query(sql, params).oneMap();

// 查询多条记录
String sql = "SELECT * FROM user";
List<User> users = A.query(sql).list(User.class);
```

> 注解

```java
// 也可以是抽象类
@Mapper
public interface UserMapper {
    
    @Update("INSERT INTO user VALUES (${user.id}, ${user.userName}, ${user.password}, ${user.age})")
    public void insert(@Bind("user") User user);
    
    @Select("SELECT * FROM user WHERE id = ${id}")
    public User select(@Bind("id") long userId);
    
}

// 使用
UserMapper mapper = A.getMapper(UserMapper.class);
mapper.insert(user);
mapper.select(id);
```

> 单表简单执行 （简便，单表操作）

```java
// 插入
User user = new User();
user.setId(KeyGenerateUtil.generateId());
user.setUserName("张三");
user.setPassword("123");
user.setAge(20);
// 直接提供已经赋值的实体类，要求实体类使用 @Table 注解
SqlBox.insert(user);
SqlBox.insert("user")
    .column("id", id)
    .column("userName", userName)
    .column(age, age).execute();

// 更新
SqlBox.update("user")
    .set("userName", userName)
    .where().eq("id", id).execute();

// 删除
SqlBox.deleteById(User.class, "123");
SqlBox.delete("user")
    .where().eq("id", "123").execute();

// 查询
SqlBox.select(User.class)
    .where().eq("id", id)
    .query(User.class);
Sqlbox.select("userName", "age")
  .from("user")
  .where().eq("id", id)
  .query(User.class);
```

> 分页查询

```java
Pager<User> users = A.fetchPage(sql, params, page, limit, User.class);
// 你也可以不提供 limit 参数，使用默认的 limit (10)，也可以在 GlobalConfig 中或配置文件进行配置
Pager<User> users = A.fetchPage(sql, params, page,  User.class);

// Pager 中的参数说明

```

> 注：QueryFlow 的所有有关数据库的操作，都不会主动关闭数据库连接，需要在手动关闭数据库连接。也可以在全局配置中配置没执行一个操作立即关闭数据库连接 `GlobalConfig.closeAfterExecuted(true)` 。



> 批量操作

```java
BatchStatement batch = A.createBatch()
int[] rows = batch.add(sql)// 添加要执行的SQL
  .add(sql)
  .execute();
// 批量执行需要预编译的 SQL
PreparedBatchStatement pbs = A.prepareBatch("sql");
// 添加参数，最后使用 add 方法，将参数集合添加到 PreparedStatement
pbs.bind("a").bind("b").add();
// 为了方便操作，也可以使用下面的方法直接添加参数
pbs.add(Object... params);
// 或者
pbs.add(List<Object> params);
```

> 存储过程调用

```java
// 只有输入参数
A.call("call test(?,?)", "A", "B");
// 或者
List<Object> params = new ArrayList<>();
params.add("A");
params.add("B");
A.call("call test(?,?)", params);

// 包含输出参数
OutParameters outParameters = A.createCall()
  .bind("A")// 输入参数
  .registerOutParameter("sum", DataType.INTEGER)// 注册输出参数
  execute();
// 获取注册的输出参数的值
int sum = outParameters.getInteger("sum");
```


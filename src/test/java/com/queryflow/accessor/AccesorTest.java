package com.queryflow.accessor;

import com.queryflow.accessor.statement.OutParameters;
import com.queryflow.common.DataType;
import com.queryflow.config.DatabaseConfig;
import com.queryflow.entity.*;
import com.queryflow.key.KeyGenerateUtil;
import static org.junit.Assert.*;

import com.queryflow.sql.SqlBox;
import com.queryflow.common.ResultMap;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AccesorTest {

    private static final String URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root";

    @BeforeClass
    public static void beforeClass() {
        AccessorFactoryBuilder builder = new AccessorFactoryBuilder();
        DatabaseConfig config = new DatabaseConfig();
        config.setTag("mysql");
        config.setUrl(URL);
        config.setUsername(USER_NAME);
        config.setPassword(PASSWORD);
        builder.addDatabase(config)
            .scanPackage("com.queryflow")
            .build(true);
        createTables();
    }

    @After
    public void after() {
        AccessorFactory.accessor().close();
    }

    private static void createTables() {
        String userSql = "CREATE TABLE user(id bigint(20) NOT NULL, username varchar(255) DEFAULT NULL, password varchar(255) DEFAULT NULL, aget int(11) DEFAULT NULL, PRIMARY KEY(id))";
        AccessorFactory.accessor().update(userSql);
        String orderSql = "CREATE TABLE orders (id bigint(20) NOT NULL,name varchar(255) DEFAULT NULL,status int(11) DEFAULT NULL,settlementStatus int(11) DEFAULT NULL,PRIMARY KEY(id))";
        AccessorFactory.accessor().update(orderSql);
        String goodsSql = "CREATE TABLE goods(id int(11) NOT NULL AUTO_INCREMENT,name varchar(255) DEFAULT NULL,PRIMARY KEY(id))";
        AccessorFactory.accessor().update(goodsSql);
    }

    private void insertUser(String tag, int num, String username) {
        for(int i = 0; i < num; i++) {
            User user = new User();
            user.setId(KeyGenerateUtil.generateId());
            user.setUsername(username + i);
            user.setPassword("123456");
            user.setAge(20 + i);
            if(tag != null) {
                SqlBox.insert(user, tag);
            } else {
                SqlBox.insert(user);
            }
        }
    }

    @Test
    public void testInsert() {
        String sql = "INSERT INTO user (id, username, password, aget) VALUES (?, ?, ?, ?)";
        long id = KeyGenerateUtil.generateId();
        Object[] params = {id, "zhangsan", "123456", "20"};
        int row = A.update(sql, params);
        assertEquals(1, row);
        User zhangsanUser = A.query("select * from user where id = ?", id).one(User.class);
        assertEquals("zhangsan", zhangsanUser.getUsername());
        assertEquals("123456", zhangsanUser.getPassword());
        assertEquals(20, zhangsanUser.getAge());

        User user = new User();
        user.setId(KeyGenerateUtil.generateId());
        user.setUsername("lisi");
        user.setPassword("123456");
        user.setAge(25);
        TestMapper testMapper = A.getMapper(TestMapper.class);
        assertNotNull(testMapper);
        testMapper.insert(user);

        User wangwu = new User();
        wangwu.setUsername("wangwu");
        wangwu.setPassword("123456");
        wangwu.setAge(26);
        row = SqlBox.insert(wangwu);
        assertEquals(1, row);

        id = KeyGenerateUtil.generateId();
        row = SqlBox.insert("user")
            .column("id", id).column("username", "阿达")
            .column("password", "1212143423")
            .column("aget", 30)
            .execute();
        assertEquals(1, row);
        user = SqlBox.select("username", "password").from("user").where().eq("id", id).query(User.class);
        assertNotNull(user);
        assertEquals("阿达", user.getUsername());
        assertEquals("1212143423", user.getPassword());

        id = KeyGenerateUtil.generateId();
        row = SqlBox.insert("user").values(id, "test", "test1", "40").execute();
        assertEquals(1, row);
        user = SqlBox.selectFrom(User.class).where().eq("id", id).query(User.class);
        assertNotNull(user);
        assertEquals("test", user.getUsername());
        assertEquals("test1", user.getPassword());
        assertEquals(40, user.getAge());
    }

    @Test
    public void testUpdate() {
        String sql = "INSERT INTO goods (name) VALUES (?)";
        Object[] params = {"土豆"};
        List<ResultMap> keys = A.createUpdate(sql).bindArray(params).executeAndReturnGeneratedKeys("id", "name");
        long key = keys.get(0).getLong("id");
        ResultMap resultMap1 = A.query("select * from goods where id = ?", key).oneMap();
        assertEquals("土豆", resultMap1.getString("NAME"));

        String updateSql = "UPDATE goods SET name = ? WHERE id = ?";
        params = new Object[]{"西红柿", key};
        int row = A.update(updateSql, params);
        assertEquals(1, row);
        ResultMap resultMap2 = A.query("select * from goods where id = ?", key).oneMap();
        assertEquals("西红柿", resultMap2.getString("NAME"));

        row = SqlBox.update("goods").set("name", "白菜").where().eq("id", key).execute();
        assertEquals(1, row);

        String name = SqlBox.select("name").from("goods").where().eq("id", key).query(String.class);
        assertEquals("白菜", name);
    }

    @Test
    public void testDelete() {
        long id = KeyGenerateUtil.generateId();
        User user = new User();
        user.setId(id);
        user.setUsername("zhanghong");
        user.setPassword("123456");
        user.setAge(26);
        SqlBox.insert(user);

        String sql = "DELETE FROM user WHERE id = ?";
        Object[] params = {id};
        int row = A.update(sql, params);
        assertEquals(1, row);

        SqlBox.insert(user);
        row = SqlBox.delete("user").where().eq("id", id).execute();
        assertEquals(1, row);

        SqlBox.insert(user);
        row = SqlBox.deleteById(User.class, id);
        assertEquals(1, row);
    }

    @Test
    public void testQuery() {
        int num = 10;
        String username = "张三fetch";
        insertUser(null, num, username);

        String sql = "SELECT * FROM user WHERE username = ?";
        Object[] params = {username + 2};
        User user = A.query(sql, params).one(User.class);
        assertNotNull(user);
        assertEquals(username + 2, user.getUsername());
        assertEquals(22, user.getAge());

        String listSql = "SELECT * FROM user WHERE username LIKE ?";
        Object[] values = {username + "%"};
        List<User> users = A.query(listSql, values).list(User.class);
        assertEquals(num, users.size());
    }

    @Test
    public void testTransaction() {
        for(int i = 0; i < 10; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    User user = new User();
                    user.setId(KeyGenerateUtil.generateId());
                    user.setUsername("lisit" + finalI);
                    user.setPassword("123456");
                    user.setAge(30 + finalI);
                    A.openTransaction();
                    SqlBox.insert(user);
                    A.commit();
                }
            }).start();
        }
        try {
            Thread.sleep(30 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDictionaryEnum() {
        long id = KeyGenerateUtil.generateId();
        Order order = new Order();
        order.setId(id);
        order.setName("彩电");
        order.setStatus(OrderStatus.OPEN);
        order.setSettlementStatus(SettlementStatus.NO.getValue());
        int row = SqlBox.insert(order);
        assertEquals(1, row);

        Order order2 = SqlBox.selectFrom(Order.class).where().eq("id", id).query(Order.class);
        assertEquals(id, order2.getId());
        assertEquals(OrderStatus.OPEN, order2.getStatus());
        assertEquals(SettlementStatus.NO.getValue(), order.getSettlementStatus());
    }

    @Test
    public void testCall() {
        String dropUserTable = "DROP TABLE IF EXISTS `user`";
        A.tag("mysql").update(dropUserTable);
        String createUserSql = "CREATE TABLE `user` ( `id` bigint(20) NOT NULL,`username` varchar(255) DEFAULT NULL,`password` varchar(255) DEFAULT NULL,`aget` int(11) DEFAULT NULL,PRIMARY KEY (`id`))";
        A.tag("mysql").update(createUserSql);
        insertUser("mysql", 10, "abc");

        String dropProcedureSql = "DROP PROCEDURE IF EXISTS count_user";
        A.tag("mysql").update(dropProcedureSql);
        String procedureSql = "CREATE PROCEDURE `count_user`(OUT `sum` int) BEGIN select count(1) INTO sum FROM `user`; END";
        A.tag("mysql").update(procedureSql);

        OutParameters parameters = A.tag("mysql").createCall("call count_user(?)")
            .registerOutParameter("sum", DataType.INTEGER)
            .execute();
        assertEquals(10, parameters.getInteger("sum").intValue());
    }

    @Test
    public void testBatch() {
        SqlBox.delete("user").execute();

        String sql = "INSERT INTO user VALUES (?,?,?,?)";
        List<List<Object>> values = new ArrayList<>();
        values.add(Arrays.asList(KeyGenerateUtil.generateId(), "张三", "123", 1));
        values.add(Arrays.asList(KeyGenerateUtil.generateId(), "zhangsan", "123", 2));
        values.add(Arrays.asList(KeyGenerateUtil.generateId(), "李四", "123", 3));
        values.add(Arrays.asList(KeyGenerateUtil.generateId(), "lisi", "123", 4));
        int[] batch = A.batch(sql, values);
        assertEquals(4, A.count("select * from user"));

        SqlBox.delete("user").execute();

        A.prepareBatch("INSERT INTO user VALUES (?,?,?,?)")
            .bind(KeyGenerateUtil.generateId()).bind("张三").bind("123").bind(1).add()
            .add(Arrays.asList(KeyGenerateUtil.generateId(), "zhangsan", "123", 2))
            .add(KeyGenerateUtil.generateId(), "lisi", "123", 3)
            .execute();
        assertEquals(3, A.count("select * from user"));

        SqlBox.delete("user").execute();

        List<User> users = new LinkedList<>();
        users.add(new User(KeyGenerateUtil.generateId(), "张三", "123", 1));
        users.add(new User(KeyGenerateUtil.generateId(), "zhangsan", "123", 2));
        users.add(new User(KeyGenerateUtil.generateId(), "李四", "123", 3));
        users.add(new User(KeyGenerateUtil.generateId(), "lisi", "123", 4));
        SqlBox.batchInsert(User.class, users);
        List<User> userList = SqlBox.selectFrom(User.class).queryList(User.class);
        assertEquals(4, userList.size());
    }

    @Test
    public void testTypeHandlerAndFill() {
        String entitySql = "CREATE TABLE IF NOT EXISTS entity (id varchar(20) NOT NULL, is_exists char(1) DEFAULT NULL, create_time varchar(14) DEFAULT NULL, update_time varchar(14) DEFAULT NULL, PRIMARY KEY(id))";
        A.update(entitySql);
        String id = KeyGenerateUtil.generateId() + "";
        Entity entity = new Entity();
        entity.setId(id);
        entity.setExists(true);
        int column = SqlBox.insert(entity);
        assertEquals(1, column);
        ResultMap resultMap = A.query("SELECT * FROM entity").oneMap();
        assertEquals("1", resultMap.getString("is_exists"));
        String createTime = resultMap.getString("create_time");
        String updateTime = resultMap.getString("update_time");
        System.out.println(createTime);
        System.out.println(updateTime);
        Entity result = SqlBox.selectFrom(Entity.class).where().eq("id", id).query(Entity.class);
        assertTrue(result.isExists());
        assertEquals(createTime, result.getCreateTime());
        assertEquals(updateTime, result.getUpdateTime());

        entity.setExists(false);
        column = SqlBox.update(entity, Entity.UpdateGroupExists.class).where().eq("id", id).execute();
        assertEquals(1, column);
        result = SqlBox.selectFrom(Entity.class).where().eq("id", id).query(Entity.class);
        assertFalse(result.isExists());
        assertEquals(createTime, result.getCreateTime());
    }

}

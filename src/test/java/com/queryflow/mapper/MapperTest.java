package com.queryflow.mapper;

import com.queryflow.entity.User;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.*;

public class MapperTest {

    private Class<?> clazz = TestMapper.class;

    @Test
    public void test1() {
        try {
            Method updateUser = clazz.getMethod("updateUser", String.class, String.class);
            MapperMethod mapperMethod = MapperMethodBuilder.getMapperMethod(updateUser);

            assertEquals(MapperMethod.SqlType.EXECUTE, mapperMethod.getSqlType());
            assertNull(mapperMethod.getReturnClass());
            assertEquals("test", mapperMethod.getDataSourceTag());
            assertEquals("UPDATE user SET username = ? WHERE id = ?",
                    mapperMethod.getPreparedSql());
            assertNull(mapperMethod.getReturnType());

            List<SqlValue> sqlValues = mapperMethod.getSqlValues();
            assertEquals(2, sqlValues.size());

            SqlValue usernameSqlValue = sqlValues.get(0);
            assertEquals("username", usernameSqlValue.getName());
            assertEquals(0, usernameSqlValue.getIndex());
            assertEquals(SqlValue.Type.VALUE, usernameSqlValue.getType());
            assertNull(usernameSqlValue.getBeanClass());

            SqlValue idSqlValue = sqlValues.get(1);
            assertEquals("id", idSqlValue.getName());
            assertEquals(1, idSqlValue.getIndex());
            assertEquals(SqlValue.Type.VALUE, idSqlValue.getType());
            assertNull(idSqlValue.getBeanClass());
        } catch (NoSuchMethodException e) {
            assertNull(e);
        }
    }

    @Test
    public void test2() {
        try {
            Method selectUser = clazz.getMethod("selectUser", User.class);
            MapperMethod mapperMethod = MapperMethodBuilder.getMapperMethod(selectUser);

            assertEquals(MapperMethod.SqlType.QUERY, mapperMethod.getSqlType());
            assertNull(mapperMethod.getDataSourceTag());
            assertEquals(MapperMethod.ReturnType.BEAN, mapperMethod.getReturnType());
            assertEquals(User.class, mapperMethod.getReturnClass());
            assertEquals("SELECT * FROM user WHERE username = ?", mapperMethod.getPreparedSql());

            List<SqlValue> sqlValues = mapperMethod.getSqlValues();
            assertEquals(1, sqlValues.size());

            SqlValue beanSqlValue = sqlValues.get(0);
            assertEquals(SqlValue.Type.BEAN_VALUE, beanSqlValue.getType());
            assertEquals(0, beanSqlValue.getIndex());
            assertEquals(User.class, beanSqlValue.getBeanClass());
            assertEquals("username", beanSqlValue.getName());
        } catch (NoSuchMethodException e) {
            assertNull(e);
        }
    }

}

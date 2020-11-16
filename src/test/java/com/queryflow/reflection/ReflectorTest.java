package com.queryflow.reflection;

import com.queryflow.annotation.Column;
import com.queryflow.annotation.Table;
import com.queryflow.common.FillType;
import com.queryflow.reflection.entity.EntityReflector;
import com.queryflow.sql.Insert;
import com.queryflow.sql.SqlBox;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class ReflectorTest {

    @Test
    public void testNormalReflector() {
        Reflector reflector = new Reflector(Cat.class, true, false);
        assertEquals(1, reflector.fieldSize());
        assertNotNull(reflector.getFieldInvoker("eat"));
        reflector = new Reflector(Cat.class, true, true);
        assertEquals(3, reflector.fieldSize());
        Cat cat = new Cat();
        cat.setName("cat");
        cat.setAge(20);
        cat.setEat("meat");
        assertEquals("cat", reflector.getFieldValue("name", cat));
        reflector.setFieldValue("age", cat, 40);
        assertEquals(40, reflector.getFieldValue("age", cat));
    }

    @Test
    public void testEntityReflector() {
        EntityReflector reflector = ReflectionUtil.forEntityClass(AUser.class);
        assertEquals("aUser", reflector.getTableName());
        assertEquals(8, reflector.fieldSize());
        assertNotNull(reflector.getFieldByColumnName("uname"));
        assertNotNull(reflector.getFieldByColumnName("nick_name"));
        assertNull(reflector.getFieldByColumnName("nickName"));
        AUser user = (AUser) reflector.newInstance();
        user.setUsername("Jon");
        user.setAge(20);
        user.setGender("1");
        Insert insert = SqlBox.insertInstance(user);
        System.out.println(insert.buildSql());
        System.out.println(insert.getValues());

        EntityReflector peopleReflector = ReflectionUtil.forEntityClass(People.class);
        assertTrue(peopleReflector.isNormalBean());
    }

    static class Animal {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    static class Cat extends Animal {
        private final String test = "test";
        private String eat;

        public String getEat() {
            return eat;
        }

        public void setEat(String eat) {
            this.eat = eat;
        }

        public String getTest() {
            return test;
        }
    }

    static class People {
        @Column("uname")
        private String username;
        private int age;
        private String gender;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }

    @Table
    static class AUser extends People {

        @Column(value = "pass")
        private String password;

        private String nickName;

        private String idcard;

        @Column(fillType = FillType.INSERT_UPDATE, fillDatePattern = "yyyyMMdd")
        private String updateTime;

        @Column(fillType = FillType.INSERT_UPDATE)
        private Date date;


        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

}

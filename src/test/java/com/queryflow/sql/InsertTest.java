package com.queryflow.sql;

import org.junit.Test;
import static org.junit.Assert.*;

public class InsertTest {

    @Test
    public void test() {
        Insert insert = new Insert("test")
                .column("A", "a")
                .column("B", 1)
                .column("C", "c");
        assertEquals("INSERT INTO test (A,B,C) VALUES (?,?,?)", insert.buildSql());
        assertArrayEquals(new Object[]{"a", 1, "c"}, insert.getValues().toArray());

        insert = new Insert("test")
                .columns("A", "B", "C", "D")
                .values("a", 1, "c", "d");
        assertEquals("INSERT INTO test (A,B,C,D) VALUES (?,?,?,?)",
                insert.buildSql());
        assertArrayEquals(new Object[]{"a", 1, "c", "d"}, insert.getValues().toArray());

        insert = new Insert("test")
                .values("a", "b", "c", "d");
        assertEquals("INSERT INTO test VALUES (?,?,?,?)", insert.buildSql());
        assertArrayEquals(new Object[]{"a", "b", "c", "d"}, insert.getValues().toArray());

        Select select = new Select().from("user_b").where().eq("type", "1");
        insert = new Insert("user_a").select(select);
        assertEquals("INSERT INTO user_a SELECT * FROM user_b WHERE (type = ?)", insert.buildSql());
        assertArrayEquals(new Object[]{"1"}, insert.getValues().toArray());

        select = new Select("username", "age").from("user_b").where().eq("type", "1");
        insert = new Insert("user_a").columns("username", "age").select(select);
        assertEquals("INSERT INTO user_a (username,age) SELECT username, age FROM user_b WHERE (type = ?)", insert.buildSql());
    }

}

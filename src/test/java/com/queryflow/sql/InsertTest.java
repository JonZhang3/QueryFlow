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
    }

}

package com.queryflow.sql;

import org.junit.Test;
import static org.junit.Assert.*;

public class SelectTest {

    @Test
    public void test() {
        Select select = new Select("SUM(num)")
                .from("test")
                .where()
                .eq("A", "a")
                .and().in("B", 1, 2, 3)
                .orNew()
                  .notEq("C", "c")
                  .and().between("D", 0, 10)
                  .or().le("E", 100)
                .groupBy("F").having("G = 1");
        assertEquals("SELECT SUM(num) FROM test WHERE (A = ? AND B IN (?, ?, ?)) OR (C <> ? AND D BETWEEN ? AND ? OR E <= ?) GROUP BY F HAVING G = 1",
                select.buildSql());
        assertArrayEquals(new Object[]{"a", 1, 2, 3, "c", 0, 10, 100}, select.getValues().toArray());
    }

}

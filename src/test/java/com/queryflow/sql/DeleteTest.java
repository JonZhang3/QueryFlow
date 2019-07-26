package com.queryflow.sql;

import org.junit.Test;
import static org.junit.Assert.*;

public class DeleteTest {

    @Test
    public void test() {
        Delete delete = new Delete("test").where()
                .eq("A", "a")
                .and().notEq("B", "b")
                .or().like("C", "%c%");
        assertEquals("DELETE FROM test WHERE (A = ? AND B <> ? OR C LIKE ?)",
                delete.buildSql());
        assertArrayEquals(new Object[]{"a", "b", "%c%"},
                delete.getValues().toArray());

        delete = new Delete("test")
                .where()
                .eq("A", "a")
                .and().between("B", 1, 10)
                .orNew()
                  .eq("C", "c")
                  .and().in("D", "a", "b", "c");
        assertEquals("DELETE FROM test WHERE (A = ? AND B BETWEEN ? AND ?) OR (C = ? AND D IN (?, ?, ?))",
                delete.buildSql());
        assertArrayEquals(new Object[]{"a", 1, 10, "c", "a", "b", "c"}, delete.getValues().toArray());
    }

}

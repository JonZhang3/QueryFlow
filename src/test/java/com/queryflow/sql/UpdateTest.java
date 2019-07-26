package com.queryflow.sql;

import org.junit.Test;
import static org.junit.Assert.*;

public class UpdateTest {

    @Test
    public void test() {
        Update update = new Update("test")
                .set("A", "b")
                .where().eq("A", "a");
        assertEquals("UPDATE test SET A = ? WHERE (A = ?)", update.buildSql());
        assertArrayEquals(new Object[]{"b", "a"}, update.getValues().toArray());
    }

}

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
                .and().in(false, "B", 1, 2, 3)
                .orNew()
                  .notEq("C", "c")
                  .and().between("D", 0, 10)
                  .or().le("E", 100)
                .groupBy("F").having("G = 1");
        assertEquals("SELECT SUM(num) FROM test WHERE (A = ?) OR (C <> ? AND D BETWEEN ? AND ? OR E <= ?) GROUP BY F HAVING G = 1",
                select.buildSql());
        assertArrayEquals(new Object[]{"a", "c", 0, 10, 100}, select.getValues().toArray());

        select = new Select("Websites.id", "Websites.name", "access_log.count", "access_log.date")
            .from("Websites")
            .join("access_log").on("Websites.id", "access_log.site_id");
        assertEquals("SELECT Websites.id, Websites.name, access_log.count, access_log.date FROM Websites INNER " +
            "JOIN access_log ON Websites.id = access_log.site_id", select.buildSql());


    }

}

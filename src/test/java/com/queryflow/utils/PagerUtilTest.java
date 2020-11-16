package com.queryflow.utils;

import com.queryflow.common.DbType;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PagerUtilTest {

    @Test
    public void test() {
        String sql = "SELECT username FROM sys_user WHERE username LIKE ? ORDER BY " +
            "(SELECT user_id  FROM sys_users_jobs WHERE user_id = ?) ";
        String countSql = PagerUtil.count(sql, DbType.MYSQL.value().toLowerCase());
        assertEquals("SELECT COUNT(*) FROM sys_user WHERE username LIKE ?", replaceWhiteSpace(countSql));
        sql = "SELECT COUNT(1) FROM (" + sql + ")";
        countSql = PagerUtil.count(sql, DbType.MYSQL.value().toLowerCase());
        assertEquals("SELECT COUNT(*) FROM ( SELECT username FROM sys_user WHERE username LIKE ? ORDER BY ( SELECT user_id FROM sys_users_jobs WHERE user_id = ? ) )", replaceWhiteSpace(countSql));
        List<Object> values = new ArrayList<>(2);
        values.add("zhangsan%");
        values.add("1");
        sql = "SELECT username FROM sys_user WHERE username LIKE ? ORDER BY " +
            "(SELECT user_id  FROM sys_users_jobs WHERE user_id = ?) ";
        countSql = PagerUtil.count(sql, DbType.MYSQL.value().toLowerCase(), values);
        assertEquals("SELECT COUNT(*) FROM sys_user WHERE username LIKE ?", replaceWhiteSpace(countSql));
        assertArrayEquals(new Object[]{"zhangsan%"}, values.toArray());
    }

    private static String replaceWhiteSpace(String src) {
        char[] chars = src.toCharArray();
        boolean lastHasWhiteSpace = false;
        StringBuilder result = new StringBuilder();
        for (char ch : chars) {
            if(Character.isWhitespace(ch)) {
                if(!lastHasWhiteSpace) {
                    lastHasWhiteSpace = true;
                    result.append(' ');
                }
            } else {
                lastHasWhiteSpace = false;
                result.append(ch);
            }
        }
        return result.toString();
    }

}

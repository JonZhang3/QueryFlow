package com.queryflow.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SqlInterpolationTest {

    private SqlInterpolation interpolation = new SqlInterpolation();

    @Test
    public void test() {
        String sql = "SELECT * FROM test WHERE A = ? AND B = ? OR C IN (?, ?, ?)";
        List<Object> values = new ArrayList<>();
        values.add("a");
        values.add("b");
        values.add(1);
        values.add(2);
        values.add(3);
        String result = interpolation.convert(sql, values);
        Assert.assertEquals("SELECT * FROM test WHERE A = 'a' AND B = 'b' OR C IN (1, 2, 3)",
                result);
    }

}

package com.queryflow.utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 将 SQL 语句中的值插入到 SQL 语句中, 形成完整的 SQL
 * <p>
 * 例：
 * <blockquote><pre>
 *     String sql = "SELECT * FROM test WHERE A = ? AND B = ? OR C IN (?, ?, ?)";
 *     Object[] values = {"a", "b", 1, ,2 ,3};
 *     String result = interpolation.convert(sql, values);
 *     System.out.println(result);
 *     // Output: SELECT * FROM test WHERE A = 'a' AND B = 'b' OR C IN (1, 2, 3)
 * </pre></blockquote>
 *
 * @author Jon
 * @since 1.0.0
 */
public class SqlInterpolation {

    private static final Pattern PATTERN = Pattern.compile("(\\\\?\\?)");

    public String convert(String template, Object[] values) {
        List<Object> list = Arrays.asList(values);
        return convert(template, list);
    }

    public String convert(String template, List<Object> values) {
        if (Utils.isEmpty(template)) {
            return "";
        }
        StringBuffer sb = new StringBuffer(template.length());
        Matcher matcher = PATTERN.matcher(template);
        int index = 0;
        while (matcher.find()) {
            String placeholder = matcher.group(1);
            if (placeholder.length() > 1 && placeholder.startsWith("\\")) {
                continue;
            }
            Object value = values.get(index);
            if (value != null) {
                if (value instanceof String) {
                    value = "'" + value + "'";
                }
                matcher.appendReplacement(sb, value.toString());
            } else {
                matcher.appendReplacement(sb, "null");
            }
            index++;
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}

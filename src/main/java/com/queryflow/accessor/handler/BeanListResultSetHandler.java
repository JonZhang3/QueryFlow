package com.queryflow.accessor.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将查询结果数据封装成 Java Bean 列表
 *
 * @author Jon
 * @since 1.0.0
 */
public class BeanListResultSetHandler<T> implements ResultSetHandler<List<T>> {

    private BeanResultSetHandler<T> beanHandler;

    @SuppressWarnings("unchecked")
    public BeanListResultSetHandler(Class<T> type) {
        beanHandler = BeanResultSetHandler.newBeanHandler(type);
    }

    @Override
    public List<T> handle(ResultSet rs) throws SQLException {
        List<T> result = new ArrayList<>();
        while (rs.next()) {
            result.add(beanHandler.handle(rs, false));
        }
        return result;
    }

}

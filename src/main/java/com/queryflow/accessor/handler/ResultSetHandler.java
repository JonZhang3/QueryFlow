package com.queryflow.accessor.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 返回结果处理接口
 *
 * @author Jon
 * @since 1.0.0
 */
@FunctionalInterface
public interface ResultSetHandler<T> {

    T handle(ResultSet rs) throws SQLException;

}

package com.queryflow.accessor;

import com.queryflow.accessor.statement.*;
import com.queryflow.common.TransactionLevel;
import com.queryflow.page.Pager;
import com.queryflow.common.ResultMap;

import javax.sql.DataSource;
import java.util.List;

/**
 * 数据库访问者接口
 *
 * @author Jon
 * @since 1.0.0
 */
public interface Accessor {

    /**
     * 获取数据源
     *
     * @return DataSource
     */
    DataSource getDataSource();

    /**
     * 创建 Update 表达式，执行增删改操作
     *
     * @param sql 要执行的 SQL
     */
    UpdateStatement createUpdate(String sql);

    /**
     * 执行增删改，DDL 操作
     *
     * @param sql    要执行的 SQL
     * @param values 参数
     * @return 受影响的行数
     */
    int update(String sql, Object... values);

    /**
     * 执行增删改，DDL操作
     *
     * @param sql    要执行的 SQL
     * @param values 参数
     * @return 受影响的行数
     */
    int update(String sql, List<Object> values);

    /**
     * 创建批量操作表达式
     */
    BatchStatement createBatch();

    /**
     * 批量执行给定的 SQL
     *
     * @param sqls SQL 列表
     * @return 每条 SQL 执行后，受影响的行数
     */
    int[] batch(String... sqls);

    int[] batch(List<String> sqls);

    PreparedBatchStatement prepareBatch(String sql);

    int[] batch(String sql, List<List<Object>> values);

    /**
     * 创建查询表达式
     *
     * @param sql 要执行的 SQL
     */
    SelectStatement createQuery(String sql);

    SelectStatement query(String sql, Object... values);

    SelectStatement query(String sql, List<Object> values);

    /**
     * 分页查询，将查询结果封装到 {@code ResultMap} 列表中
     *
     * @param sql    查询 SQL
     * @param values 参数
     * @param page   页码
     * @param limit  一页的大小
     */
    Pager<ResultMap> pageToMap(String sql, List<Object> values, int page, int limit);

    /**
     * 分页查询
     *
     * @param sql    查询 SQL
     * @param values 参数
     * @param page   页码
     */
    Pager<ResultMap> pageToMap(String sql, List<Object> values, int page);

    /**
     * 分页查询，将查询结果封装到指定的 Java Bean 中
     *
     * @param sql          查询 SQL
     * @param page         页码
     * @param limit        一页的大小
     * @param requiredType Java Bean Class
     * @param values       参数
     */
    <T> Pager<T> page(String sql, int page, int limit, Class<T> requiredType, Object... values);

    <T> Pager<T> page(String sql, List<Object> values, int page, int limit, Class<T> requiredType);

    <T> Pager<T> page(String sql, List<Object> values, int page, Class<T> requiredType);

    /**
     * 对 SQL 查询的结果执行 Count 操作
     *
     * @param sql    查询 SQL
     * @param values 参数
     */
    int count(String sql, Object... values);

    int count(String sql, List<Object> values);

    CallStatement createCall(String sql);

    CallStatement call(String sql, Object... values);

    CallStatement call(String sql, List<Object> values);

    /**
     * 开启数据库事务
     */
    void openTransaction();

    /**
     * 开启数据库事务，可指定事务级别
     *
     * @param level 事务级别
     * @see java.sql.Connection#setTransactionIsolation(int)
     */
    void openTransaction(TransactionLevel level);

    /**
     * 提交数据库事务，事务提交后，会关闭数据库连接
     */
    void commit();

    /**
     * 提交数据库事务
     *
     * @param close 指定在提交完事务后，是否关闭数据库连接。{@code true} 表示关闭数据库连接。
     */
    void commit(boolean close);

    /**
     * 回滚数据库事务
     */
    void rollback();

    /**
     * 回滚数据库事务，回滚后，会主动关闭数据库连接
     *
     * @param close 指定在回滚完事务后，是否关闭数据库连接。{@code true} 表示关闭数据库连接。
     */
    void rollback(boolean close);

    <T> T getMapper(Class<T> clazz);

    /**
     * 关闭数据库连接
     */
    void close();

}

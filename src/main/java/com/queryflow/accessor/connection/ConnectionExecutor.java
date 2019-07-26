package com.queryflow.accessor.connection;

import com.queryflow.accessor.handler.ResultSetHandler;
import com.queryflow.accessor.interceptor.Interceptors;
import com.queryflow.common.ResultMap;

import java.util.List;

/**
 * JDBC 数据库操作执行接口
 * 负责执行 SQL 的增删改查
 *
 * @author Jon
 * @since 1.0.0
 */
public interface ConnectionExecutor {

    /**
     * 执行 UPDATE，DELETE，INSERT，DDL 语句
     *
     * @param sql          要执行的 SQL
     * @param params       预编译 SQL 中对应占位符数据
     * @param interceptors 拦截器
     * @return 返回受影响的记录条数
     */
    int update(String sql, List<Object> params, Interceptors interceptors);

    /**
     * 插入数据同时获取数据库自动生成的主键
     *
     * @param sql            要执行的 SQL
     * @param params         参数
     * @param keyColumnNames 主键所在列名称
     * @param interceptors   拦截器
     * @param handler        返回结果处理函数
     * @param <T>            结果的实例类型
     * @return 操作结果
     */
    <T> T insertGetKey(String sql, List<Object> params, String[] keyColumnNames,
                       Interceptors interceptors, ResultSetHandler<T> handler);

    /**
     * 批量执行插入操作，同时返回数据库自动生成的主键值
     *
     * @param sql            要执行的 SQL
     * @param params         参数
     * @param keyColumnNames 主键所在列名称
     * @param interceptors   拦截器
     * @param handler        返回结果处理函数
     * @param <T>            结果的实例类型
     * @return 操作结果
     */
    <T> T batchInsertGetKes(String sql, List<List<Object>> params,
                            String[] keyColumnNames, Interceptors interceptors,
                            ResultSetHandler<T> handler);

    /**
     * 批量执行增删改操作
     *
     * @param sqls         要执行的 SQL 列表
     * @param interceptors 拦截器
     * @return 要执行的 SQL 列表中每条 SQL 执行后影响的行数
     */
    int[] batchUpdate(List<String> sqls, Interceptors interceptors);

    /**
     * 批量执行增删改预编译 SQL
     *
     * @param sql          要执行的 SQL
     * @param params       参数
     * @param interceptors 拦截器
     * @return 执行的SQL影响的行数
     */
    int[] batchUpdate(String sql, List<List<Object>> params, Interceptors interceptors);

    /**
     * 执行数据库查询操作
     *
     * @param sql          查询 SQL
     * @param params       参数
     * @param interceptors 拦截器
     * @param handler      查询结果处理函数
     * @param <T>          结果的实例类型
     * @return 返回数据处理结果
     */
    <T> T query(String sql, List<Object> params, Interceptors interceptors,
                ResultSetHandler<T> handler);

    /**
     * 执行数据库查询操作，返回结果为一行，并将查询结果封装成 Java Bean
     *
     * @param sql          查询 SQL
     * @param params       参数
     * @param interceptors 拦截器
     * @param beanClass    Java Bean Class
     * @param <T>          结果的实例类型
     * @return Java Bean
     */
    <T> T queryForBean(String sql, List<Object> params, Interceptors interceptors, Class<T> beanClass);

    /**
     * 执行数据库查询操作，返回结果为多行，并将查询结果封装成 Java Bean
     *
     * @param sql          查询 SQL
     * @param params       参数
     * @param interceptors 拦截器
     * @param beanClass    Java Bean Class
     * @param <T>          结果的实例类型
     * @return Java Bean List
     */
    <T> List<T> queryForListBean(String sql, List<Object> params, Interceptors interceptors, Class<T> beanClass);

    /**
     * 执行数据库查询操作，返回结果为一行，并将查询结果封装到 Map 中
     *
     * @param sql          查询 SQL
     * @param params       参数
     * @param interceptors 拦截器
     * @return ResultMap
     * @see ResultMap
     */
    ResultMap queryForMap(String sql, List<Object> params, Interceptors interceptors);

    /**
     * 执行数据库查询操作，返回结果为多行，并将查询结果封装到 Map 中
     *
     * @param sql          查询 SQL
     * @param params       参数
     * @param interceptors 拦截器
     * @return ResultMap List
     * @see ResultMap
     */
    List<ResultMap> queryForListMap(String sql, List<Object> params, Interceptors interceptors);

    /**
     * 执行存储过程
     *
     * @param sql          要执行的 SQL
     * @param params       参数
     * @param interceptors 拦截器
     * @return 受影响的行数
     */
    int call(String sql, List<Object> params, Interceptors interceptors);

    /**
     * 执行存储过程
     *
     * @param sql          要执行的 SQL
     * @param params       参数
     * @param interceptors 拦截器
     * @param handler      返回数据处理函数
     * @param <T>          结果的实例类型
     * @return 结果
     */
    <T> T call(String sql, List<Object> params, Interceptors interceptors,
               ResultSetHandler<T> handler);
}

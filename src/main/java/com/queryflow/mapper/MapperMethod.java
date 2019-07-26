package com.queryflow.mapper;

import com.queryflow.mapper.executor.ExecuteMethodExecutor;
import com.queryflow.mapper.executor.ListResultSelectMethodExecutor;
import com.queryflow.mapper.executor.MapperMethodExecutor;
import com.queryflow.mapper.executor.OneResultSelectMethodExecutor;

import java.util.LinkedList;
import java.util.List;

public class MapperMethod {

    private Class<?> returnClass;
    private ReturnType returnType;
    private String dataSourceTag;
    private String preparedSql;
    private MapperMethodExecutor executor;
    private SqlType sqlType;
    private List<SqlValue> sqlValues = new LinkedList<>();

    public MapperMethod(SqlType sqlType) {
        this.sqlType = sqlType;
    }

    public SqlType getSqlType() {
        return sqlType;
    }

    public void setPreparedSql(String preparedSql) {
        this.preparedSql = preparedSql;
    }

    public String getPreparedSql() {
        return this.preparedSql;
    }

    public void setReturnType(ReturnType returnType) {
        this.returnType = returnType;
    }

    public ReturnType getReturnType() {
        return this.returnType;
    }

    public void setReturnClass(Class<?> returnClass) {
        this.returnClass = returnClass;
    }

    public Class<?> getReturnClass() {
        return returnClass;
    }

    public void addSqlValue(SqlValue sqlValue) {
        this.sqlValues.add(sqlValue);
    }

    public List<SqlValue> getSqlValues() {
        return sqlValues;
    }

    public void setDataSourceTag(String dataSourceTag) {
        this.dataSourceTag = dataSourceTag;
    }

    public String getDataSourceTag() {
        return dataSourceTag;
    }

    public void buildExecutor() {
        if (sqlType == SqlType.EXECUTE) {
            executor = new ExecuteMethodExecutor(dataSourceTag, preparedSql, sqlValues);
        } else {
            switch (returnType) {
                case LIST_BEAN:
                case LIST_MAP:
                    executor = new ListResultSelectMethodExecutor(dataSourceTag, preparedSql, sqlValues, returnClass);
                    break;
                case BEAN:
                case MAP:
                    executor = new OneResultSelectMethodExecutor(dataSourceTag, preparedSql, sqlValues, returnClass);
                    break;
            }
        }
    }

    public Object execute(Object target, Object[] args) {
        return executor.execute(target, args);
    }

    public enum SqlType {
        QUERY,
        EXECUTE
    }

    public enum ReturnType {
        BEAN,
        MAP,
        LIST_BEAN,
        LIST_MAP
    }

}

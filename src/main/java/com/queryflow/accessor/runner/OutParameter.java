package com.queryflow.accessor.runner;

import com.queryflow.common.DataType;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class OutParameter<T> {

    private DataType dataType;
    private T value;

    public OutParameter(DataType dataType) {
        this.dataType = dataType;
    }

    public OutParameter(DataType dataType, T value) {
        this.dataType = dataType;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public DataType getDataType() {
        return dataType;
    }

    void setValue(CallableStatement cs, int index) throws SQLException {
        Object obj = cs.getObject(index);
        value = (T) obj;
    }

    void setTo(CallableStatement statement, int index) throws SQLException {
        statement.registerOutParameter(index, dataType.value());
        if (value != null) {
            statement.setObject(index, value);
        }
    }

}

package com.queryflow.mapper.executor;

import com.queryflow.accessor.AccessorFactory;
import com.queryflow.common.QueryFlowException;
import com.queryflow.accessor.Accessor;
import com.queryflow.mapper.SqlValue;
import com.queryflow.reflection.entity.EntityReflector;
import com.queryflow.reflection.ReflectionUtil;
import com.queryflow.utils.Utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class MapperMethodExecutor {

    protected List<SqlValue> sqlValues;
    protected String sql;
    protected String dataSourceTag;

    public MapperMethodExecutor(String dataSourceTag, String sql, List<SqlValue> sqlValues) {
        this.dataSourceTag = dataSourceTag;
        this.sql = sql;
        if (sqlValues == null) {
            sqlValues = Collections.emptyList();
        }
        this.sqlValues = sqlValues;
    }

    public abstract Object execute(Object target, Object[] args);

    protected Accessor getAccessor() {
        Accessor accessor;
        if (Utils.isEmpty(dataSourceTag)) {
            accessor = AccessorFactory.accessor();
        } else {
            accessor = AccessorFactory.accessor(dataSourceTag);
        }
        if (accessor == null) {
            throw new QueryFlowException("cannot found the accessor by datasource tag:" + dataSourceTag);
        }
        return accessor;
    }

    protected Object[] fillValues(Object[] args) {
        Object[] values = new Object[sqlValues.size()];
        SqlValue sqlValue;
        for (int i = 0, size = sqlValues.size(); i < size; i++) {
            sqlValue = sqlValues.get(i);
            int index = sqlValue.getIndex();
            switch (sqlValue.getType()) {
                case VALUE:
                    values[i] = args[index];
                    break;
                case MAP_VALUE:
                    values[i] = ((Map) args[index]).get(sqlValue.getName());
                    break;
                case BEAN_VALUE:
                    EntityReflector reflector = ReflectionUtil.forEntityClass(sqlValue.getBeanClass());
                    values[i] = reflector.getFieldValue(sqlValue.getName(), args[index]);
                    break;
            }
        }
        return values;
    }

}

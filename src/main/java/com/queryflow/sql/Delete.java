package com.queryflow.sql;

import com.queryflow.accessor.Accessor;
import com.queryflow.accessor.AccessorFactory;
import com.queryflow.utils.Utils;

public class Delete extends Where<Delete> {

    public Delete(String table) {
        appender.append("DELETE FROM ").append(table);
    }

    public String buildSql() {
        if (hasWhere) {
            return appender + ")";
        }
        return appender.toString();
    }

    public int execute() {
        Accessor accessor = AccessorFactory.accessor();
        return accessor.update(buildSql(), Utils.toArray(values));
    }

    public int execute(String dataSourceTag) {
        Accessor accessor = AccessorFactory.accessor(dataSourceTag);
        return accessor.update(buildSql(), Utils.toArray(values));
    }

}

package com.queryflow.sql;

import com.queryflow.accessor.Accessor;
import com.queryflow.accessor.AccessorFactory;
import com.queryflow.utils.Utils;

public final class Delete extends Where<Delete> {

    public Delete(String table) {
        stack.push("DELETE FROM ").push(table);
    }

    @Override
    public String buildSql() {
        StringBuilder sql = stack.toStr();
        if (hasWhere) {
            return sql + ")";
        }
        return sql.toString();
    }

    public int execute() {
        Accessor accessor = AccessorFactory.accessor();
        return accessor.update(buildSql(), getValues());
    }

    public int execute(String dataSourceTag) {
        Accessor accessor = AccessorFactory.accessor(dataSourceTag);
        return accessor.update(buildSql(), getValues());
    }

}

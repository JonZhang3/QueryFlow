package com.queryflow.sql;

import com.queryflow.common.QueryFlowException;
import com.queryflow.key.KeyGenerateUtil;
import com.queryflow.reflection.entity.EntityField;
import com.queryflow.reflection.entity.EntityReflector;
import com.queryflow.reflection.ReflectionUtil;
import com.queryflow.reflection.invoker.FieldInvoker;
import com.queryflow.utils.Assert;

import java.io.Serializable;
import java.util.Iterator;

public final class SqlBox {

    private SqlBox() {}

    public static int insert(Object entity) {
        return insert(entity, "");
    }

    public static int insert(Object entity, String dataSourceTag) {
        if (entity != null) {
            EntityReflector reflector = ReflectionUtil.forEntityClass(entity.getClass());
            if (reflector.isNormalBean()) {
                throw new QueryFlowException("the bean is not a table entity: " + entity.getClass().getName());
            }
            Iterator<FieldInvoker> iterator = reflector.fieldIterator();
            EntityField field;
            Object value;
            Insert insert = new Insert(reflector.getTableName());
            while (iterator.hasNext()) {
                field = (EntityField) iterator.next();
                if (field.isIdField()) {
                    value = field.getValue(entity);
                    if (field.isZeroValue(entity)) {
                        value = KeyGenerateUtil.generateId(field.getKeyGeneratorClass());
                    }
                    if (value != null) {
                        insert.column(field.getColumnName(), value);
                    }
                } else if (field.exists()) {
                    value = field.getValue(entity);
                    insert.column(field.getColumnName(), value);
                }
            }
            return insert.execute(dataSourceTag);
        }
        return 0;
    }

    public static Insert insert(String table) {
        return new Insert(table);
    }

    public static Update update(String table) {
        return new Update(table);
    }

    public static Delete delete(String table) {
        return new Delete(table);
    }

    public static int deleteById(String table, String idColumn, Serializable id) {
        return deleteById(table, idColumn, id, "");
    }

    public static int deleteById(String table, String idColumn, Serializable id, String dataSourceTag) {
        return new Delete(table).where().eq(idColumn, id).execute(dataSourceTag);
    }

    public static int deleteById(Class<?> entityClass, Serializable id) {
        return deleteById(entityClass, id, "");
    }

    public static int deleteById(Class<?> entityClass, Serializable id, String dataSourceTag) {
        Assert.notNull(entityClass);

        EntityReflector reflector = ReflectionUtil.forEntityClass(entityClass);
        if (reflector.isNormalBean()) {
            throw new QueryFlowException("the class is not a table entity: " + entityClass.getName());
        }
        String table = reflector.getTableName();
        EntityField idField = reflector.getIdField();
        if (idField == null) {
            throw new QueryFlowException("the table entity not contains id column: " + entityClass.getName());
        }

        return deleteById(table, idField.getColumnName(), id, dataSourceTag);
    }

    public static Select select(String... columns) {
        return new Select(columns);
    }

    public static Select select(Class<?> entityClass) {
        Assert.notNull(entityClass);

        EntityReflector reflector = ReflectionUtil.forEntityClass(entityClass);
        if (reflector.isNormalBean()) {
            throw new QueryFlowException("the class is not a table entity: " + entityClass.getName());
        }
        Iterator<FieldInvoker> iterator = reflector.fieldIterator();
        String[] columns = new String[reflector.fieldSize()];
        int index = 0;
        EntityField field;
        while (iterator.hasNext()) {
            field = (EntityField) iterator.next();
            columns[index] = field.getColumnName();
            index++;
        }
        return new Select(columns).from(reflector.getTableName());
    }

    public static Select select(String table, Class<?> requiredType) {
        EntityReflector reflector = ReflectionUtil.forEntityClass(requiredType);
        Iterator<FieldInvoker> iterator = reflector.fieldIterator();
        FieldInvoker field;
        String[] columns = new String[reflector.fieldSize()];
        int index = 0;
        while (iterator.hasNext()) {
            field = iterator.next();
            if (reflector.isNormalBean()) {
                columns[index] = field.getName();
            } else {
                columns[index] = ((EntityField) field).getColumnName();
            }
            index++;
        }
        return new Select(columns).from(table);
    }

}

package com.queryflow.sql;

import com.queryflow.accessor.AccessorFactory;
import com.queryflow.common.Operation;
import com.queryflow.common.QueryFlowException;
import com.queryflow.key.KeyGenerateUtil;
import com.queryflow.reflection.entity.EntityField;
import com.queryflow.reflection.entity.EntityReflector;
import com.queryflow.reflection.ReflectionUtil;
import com.queryflow.reflection.invoker.FieldInvoker;
import com.queryflow.utils.Assert;
import com.queryflow.utils.Utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class SqlBox {

    private SqlBox() {
    }

    /**
     * 插入数据，需指定一个使用 {@code Table} 注解的类
     *
     * @param entity 实体类
     * @return 数据库中影响的行数
     */
    public static int insert(Object entity) {
        return insert(entity, "");
    }

    /**
     * 插入数据，需指定一个使用 {@code Table} 注解的类
     *
     * @param entity 实体类
     * @param dataSourceTag 数据源，多数据源情况下使用
     * @return 数据库中影响的行数
     */
    public static int insert(Object entity, String dataSourceTag) {
        if (entity != null) {
            EntityReflector reflector = ReflectionUtil.forEntityClass(entity.getClass());
            if (reflector.isNormalBean()) {
                throwNotTableBeanException(entity.getClass().getName());
            }
            Iterator<FieldInvoker> iterator = reflector.fieldIterator();
            EntityField field;
            Insert insert = new Insert(reflector.getTableName());
            while (iterator.hasNext()) {
                field = (EntityField) iterator.next();
                Object value;
                if (field.isIdField()) {
                    value = field.getValue(entity);
                    if (Utils.isZeroValue(field.getType(), value)) {
                        value = KeyGenerateUtil.generateId(field.getKeyGeneratorClass());
                    }
                    if (value != null) {
                        insert.column(field.getColumnName(), value);
                    }
                } else if (field.exists()) {
                    value = field.getValue(entity, Operation.INSERT);
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

    /**
     * 批量插入指定类型实体类的数据
     *
     * @param clazz 实体类的 {@code Class}
     * @param entities 实体类数据列表
     * @param <T> 实体类泛型参数
     * @since 1.2.0
     */
    public static <T> void batchInsert(Class<T> clazz, Collection<T> entities) {
        batchInsert(clazz, entities, "");
    }

    /**
     * 批量插入指定类型实体类的数据
     *
     * @param clazz 实体类的 {@code Class}
     * @param entities 实体类数据列表
     * @param dataSourceTag 数据源，多数据源情况下使用
     * @param <T> 实体类泛型参数
     * @since 1.2.0
     */
    public static <T> void batchInsert(Class<T> clazz, Collection<T> entities, String dataSourceTag) {
        Assert.notNull(clazz);
        if (entities != null && entities.size() > 0) {
            EntityReflector reflector = ReflectionUtil.forEntityClass(clazz);
            if (reflector.isNormalBean()) {
                throwNotTableBeanException(clazz.getName());
            }
            Iterator<FieldInvoker> iterator = reflector.fieldIterator();
            EntityField field;
            Insert insert = new Insert(reflector.getTableName());
            while (iterator.hasNext()) {
                field = (EntityField) iterator.next();
                insert.column(field.getColumnName(), null);
            }
            String sql = insert.buildSql();
            List<List<Object>> values = new LinkedList<>();
            for (T entity : entities) {
                List<Object> valueList = new LinkedList<>();
                iterator = reflector.fieldIterator();
                while (iterator.hasNext()) {
                    field = (EntityField) iterator.next();
                    Object value;
                    if (field.isIdField()) {
                        value = field.getValue(entity);
                        if (Utils.isZeroValue(field.getType(), value)) {
                            value = KeyGenerateUtil.generateId(field.getKeyGeneratorClass());
                        }
                        if (value != null) {
                            valueList.add(value);
                        }
                    } else if (field.exists()) {
                        value = field.getValue(entity, Operation.INSERT);
                        valueList.add(value);
                    }
                }
                values.add(valueList);
            }
            AccessorFactory.accessor(dataSourceTag).batch(sql, values);
        }
    }

    public static Update update(String table) {
        return new Update(table);
    }

    /**
     * 更新指定实体类对应表的数据，可指定更新分组
     *
     * @param entity 实体类
     * @param updateGroupClass 更新分组
     * @return {@code Where}，可自定义更新条件
     * @see com.queryflow.annotation.Column#updateGroups()
     * @since 1.2.0
     */
    public static Where<Update> update(Object entity, Class<?> updateGroupClass) {
        Assert.notNull(entity);
        Assert.notNull(updateGroupClass);
        EntityReflector reflector = ReflectionUtil.forEntityClass(entity.getClass());
        if (reflector.isNormalBean()) {
            throwNotTableBeanException(entity.getClass().getName());
        }
        Update update = new Update(reflector.getTableName());
        Iterator<FieldInvoker> iterator = reflector.fieldIterator();
        EntityField field;
        while (iterator.hasNext()) {
            field = (EntityField) iterator.next();
            if(field.containsUpdateGroupClass(updateGroupClass)) {
                updateSet(entity, field, update, false);
            }
        }
        return update;
    }

    /**
     * 更新指定实体类对应表的指定列
     *
     * @param entity 实体类
     * @param columns 指定的列名
     * @return {@code Where} 可自定义条件
     */
    public static Where<Update> update(Object entity, String... columns) {
        return update(entity, false, columns);
    }

    /**
     * 更新表，可以指定要更新的列
     *
     * @param entity     包含更新内容的实体类，需要使用{@code @Table} 注解
     * @param ignoreNull 是否忽略为 null 的字段
     * @param columns    要更新的列名（tips：这里是列名而不是字段名）
     * @return {@code Where} 可自定义条件
     */
    public static Where<Update> update(Object entity, boolean ignoreNull, String... columns) {
        Assert.notNull(entity);
        if (columns == null) {
            columns = new String[0];
        }
        EntityReflector reflector = ReflectionUtil.forEntityClass(entity.getClass());
        if (reflector.isNormalBean()) {
            throwNotTableBeanException(entity.getClass().getName());
        }
        EntityField field = null;
        Update update = new Update(reflector.getTableName());
        if (columns.length == 0) {
            Iterator<FieldInvoker> iterator = reflector.fieldIterator();
            while (iterator.hasNext()) {
                field = (EntityField) iterator.next();
                updateSet(entity, field, update, ignoreNull);
            }
        } else {
            for (String column : columns) {
                if (Utils.isNotEmpty(column)) {
                    field = reflector.getFieldByColumnName(column);
                }
                if (field == null) {
                    throw new QueryFlowException("unknow column name [" + column + "]");
                }
                updateSet(entity, field, update, ignoreNull);
            }
        }
        return update;
    }

    private static void updateSet(Object entity, EntityField field, Update update, boolean ignoreNull) {
        Object value;
        if(!field.isIdField() && field.exists()) {
            value = field.getValue(entity, Operation.UPDATE);
            if (ignoreNull) {
                if (value != null) {
                    update.set(field.getColumnName(), value);
                }
            } else {
                update.set(field.getColumnName(), value);
            }
        }
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
            throwNotTableBeanException(entityClass.getName());
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

    public static Select selectFrom(Class<?> entityClass) {
        Assert.notNull(entityClass);

        EntityReflector reflector = ReflectionUtil.forEntityClass(entityClass);
        if (reflector.isNormalBean()) {
            throwNotTableBeanException(entityClass.getName());
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

    /**
     * 查询指定类中的所有字段数据，注意：注定类中的所有字段在表中必须存在
     *
     * @param table 查询的表名称
     * @param requiredType 包含要查询的字段的类
     * @return {@code Select}
     */
    public static Select selectFrom(String table, Class<?> requiredType) {
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

    private static void throwNotTableBeanException(String className) {
        throw new QueryFlowException("the bean is not a table entity: " + className + ". Table entity must use @Table annotation.");
    }

}

package com.queryflow.reflection.entity;

import com.queryflow.annotation.Table;
import com.queryflow.config.GlobalConfig;
import com.queryflow.reflection.FieldReflector;
import com.queryflow.reflection.invoker.FieldInvoker;
import com.queryflow.utils.Utils;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * 反射获取实体类信息
 */
public class EntityReflector extends FieldReflector {

    private String tableName;// 表名

    // 是否是普通的 Java Bean，即没有使用 @Table 注解的类
    // 从而不用再去解析 @Id、@Column 注解
    private boolean isNormalBean = false;

    private EntityField idField;

    public EntityReflector(Class<?> clazz) {
        this(clazz, true);
    }

    public EntityReflector(Class<?> clazz, boolean skipFinalField) {
        super(clazz, skipFinalField);
        parseTableName(clazz);
    }

    @Override
    protected FieldInvoker createFieldInvoker(Field field) {
        if (isNormalBean) {
            return super.createFieldInvoker(field);
        }
        EntityField entityField = new EntityField(field, GlobalConfig.isCamelCaseToSnake());
        if (entityField.isIdField()) {
            idField = entityField;
        }
        return entityField;
    }

    public String getTableName() {
        return tableName;
    }

    public boolean isNormalBean() {
        return isNormalBean;
    }

    public EntityField getField(String name) {
        return fieldInvokers == null ? null : (EntityField) fieldInvokers.get(name);
    }

    public EntityField getIdField() {
        return idField;
    }

    private void parseTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if (table != null) {
            isNormalBean = false;
            tableName = table.value();
            if (Utils.isBlank(tableName)) {
                tableName = formatTableName(clazz.getSimpleName());
            }
        } else {
            isNormalBean = true;
        }
    }

    private String formatTableName(String tableName) {
        int length = tableName.length();
        if (length == 1) {
            return tableName.toLowerCase(Locale.ENGLISH);
        }
        return tableName.substring(0, 1).toLowerCase(Locale.ENGLISH) + tableName.substring(1);
    }

}

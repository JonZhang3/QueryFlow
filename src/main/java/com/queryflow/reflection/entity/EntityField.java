package com.queryflow.reflection.entity;

import com.queryflow.common.ColumnFillStrategy;
import com.queryflow.common.FillType;
import com.queryflow.common.DictionaryEnum;
import com.queryflow.annotation.Column;
import com.queryflow.annotation.Id;
import com.queryflow.key.KeyGenerator;
import com.queryflow.reflection.invoker.Property;
import com.queryflow.utils.Utils;

import java.lang.reflect.Field;

public class EntityField extends Property {

    private boolean exists = true;

    private boolean isIdField = false;
    private Class<? extends KeyGenerator> keyGeneratorClass;

    private String columnName;

    private boolean isDictionaryKey = false;
    private Class<? extends DictionaryEnum> dicClass;
    private FillType fillType;
    private Class<? extends ColumnFillStrategy> fillStrategy;

    public EntityField(Field field) {
        this(field, false);
    }

    public EntityField(Field field, boolean camelCaseToSnake) {
        super(field);
        init();
        if (Utils.isBlank(columnName)) {
            columnName = field.getName();
            if (camelCaseToSnake) {
                columnName = Utils.camelCaseToSnake(columnName);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void init() {
        Id id = field.getAnnotation(Id.class);
        if (id != null) {
            isIdField = true;
            columnName = id.value();
            keyGeneratorClass = id.keyGenerator();
            return;
        }

        Column column = field.getAnnotation(Column.class);
        if (column != null) {
            isIdField = false;
            columnName = column.value();
            exists = column.exists();
            isDictionaryKey = column.isDictionaryKey();
            if (isDictionaryKey) {
                dicClass = column.dictionaryClass();
                if (dicClass == DictionaryEnum.class && DictionaryEnum.class.isAssignableFrom(getType())) {
                    dicClass = (Class<? extends DictionaryEnum>) getType();
                }
            }
            fillType = column.fillType();
            fillStrategy = column.fillStrategy();
            return;
        }

        isIdField = false;
        exists = true;
        isDictionaryKey = false;
    }

    public boolean exists() {
        return exists;
    }

    public boolean isIdField() {
        return isIdField;
    }

    @Override
    public String getName() {
        return columnName;
    }

    @SuppressWarnings("unchecked")
    public <T> Class<? extends KeyGenerator<T>> getKeyGeneratorClass() {
        return (Class<? extends KeyGenerator<T>>) keyGeneratorClass;
    }

    public String getColumnName() {
        return getName();
    }

    public boolean isDictionaryKey() {
        return isDictionaryKey;
    }

    public Class<? extends DictionaryEnum> getDicClass() {
        return dicClass;
    }

    @Override
    public void setValue(Object target, Object value) {
        if (isDictionaryKey()) {
            if (this.getType() == String.class) {
                DictionaryEnum[] constants = dicClass.getEnumConstants();
                for (DictionaryEnum constant : constants) {
                    if (constant.getCode().equals(value)) {
                        super.setValue(target, constant.getValue());
                        break;
                    }
                }
            } else if (DictionaryEnum.class.isAssignableFrom(this.getType())) {
                DictionaryEnum[] constants = dicClass.getEnumConstants();
                for (DictionaryEnum constant : constants) {
                    if (constant.getCode().equals(value)) {
                        super.setValue(target, constant);
                        break;
                    }
                }
            }
        } else {
            super.setValue(target, value);
        }
    }

    @Override
    public Object getValue(Object target) {
        Object value = super.getValue(target);
        if (isDictionaryKey()) {
            if (this.getType() == String.class) {
                DictionaryEnum[] constants = dicClass.getEnumConstants();
                for (DictionaryEnum constant : constants) {
                    if (constant.getValue().equals(value)) {
                        return constant.getCode();
                    }
                }
            } else if (DictionaryEnum.class.isAssignableFrom(this.getType())) {
                return ((DictionaryEnum) value).getCode();
            }
        }
        return value;
    }

    public Object getValue(Object target, FillType type) {
        Object value = getValue(target);
        if(this.fillType == FillType.NONE || type == null) {
            return value;
        }
        if(this.fillType == FillType.INSERT && type == FillType.INSERT) {
            return Utils.instantiate(this.fillStrategy).fill(getType(), value);
        }
        if(this.fillType == FillType.UPDATE && type == FillType.UPDATE) {
            return Utils.instantiate(this.fillStrategy).fill(getType(), value);
        }
        if(this.fillType == FillType.INSERT_UPDATE) {
            return Utils.instantiate(this.fillStrategy).fill(getType(), value);
        }
        return value;
    }

}

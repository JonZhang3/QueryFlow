package com.queryflow.reflection.entity;

import com.queryflow.common.ColumnFillStrategy;
import com.queryflow.common.Common;
import com.queryflow.common.DefaultColumnFillStrategy;
import com.queryflow.common.FillType;
import com.queryflow.common.DictionaryEnum;
import com.queryflow.annotation.Column;
import com.queryflow.annotation.Id;
import com.queryflow.common.Operation;
import com.queryflow.common.type.DefaultTypeHandler;
import com.queryflow.common.type.TypeHandler;
import com.queryflow.key.KeyGenerator;
import com.queryflow.reflection.invoker.Property;
import com.queryflow.utils.Utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class EntityField extends Property {

    private boolean exists = true;

    private boolean isIdField = false;
    private Class<? extends KeyGenerator> keyGeneratorClass;

    private String columnName;

    private boolean isDictionaryKey = false;
    private Class<? extends DictionaryEnum> dicClass;
    private FillType fillType = FillType.NONE;
    private Class<? extends ColumnFillStrategy> fillStrategy = DefaultColumnFillStrategy.class;
    private String fillPattern = DefaultColumnFillStrategy.DEFAULT_FILL_PATTERN;
    private final Map<Class<?>, Short> updateGroupClasses = new HashMap<>();
    private Class<? extends TypeHandler> typeHandler = DefaultTypeHandler.class;

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
        } else {
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
                typeHandler = column.typeHandler();
                fillType = column.fillType();
                fillStrategy = column.fillStrategy();
                fillPattern = column.fillDatePattern();
                Class<?>[] classes = column.updateGroups();
                if(classes.length > 0) {
                    for (Class<?> clazz : classes) {
                        updateGroupClasses.put(clazz, (short) 1);
                    }
                }
            }
        }
    }

    public boolean exists() {
        return exists;
    }

    public boolean isIdField() {
        return isIdField;
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @SuppressWarnings("unchecked")
    public <T> Class<? extends KeyGenerator<T>> getKeyGeneratorClass() {
        return (Class<? extends KeyGenerator<T>>) keyGeneratorClass;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public boolean isDictionaryKey() {
        return isDictionaryKey;
    }

    public Class<? extends DictionaryEnum> getDicClass() {
        return dicClass;
    }

    public Class<? extends TypeHandler> getTypeHandler() {
        return typeHandler;
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

    @SuppressWarnings("unchecked")
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
        } else {
            if(fillType == FillType.NONE) {
                value = Common.getTypeHandler(this.typeHandler).setToStatement(value);
            }
        }
        return value;
    }

    /**
     * 增加自动填充功能
     *
     * @param target 实体类
     * @param operation 当前的操作，插入或更新
     * @return 字段值
     * @since 1.2.0
     */
    public Object getValue(Object target, Operation operation) {
        Object value = getValue(target);
        if(this.fillType == FillType.NONE || !Operation.isInsertOrUpdate(operation)) {
            return value;
        }
        if(this.fillType == FillType.INSERT && operation == Operation.INSERT) {
            return Common.getFillStrategy(this.fillStrategy).fill(getType(), value, this.fillPattern, operation);
        }
        if(this.fillType == FillType.UPDATE && operation == Operation.UPDATE) {
            return Common.getFillStrategy(this.fillStrategy).fill(getType(), value, this.fillPattern, operation);
        }
        if(this.fillType == FillType.INSERT_UPDATE) {
            return Common.getFillStrategy(this.fillStrategy).fill(getType(), value, this.fillPattern, operation);
        }
        return value;
    }

    public boolean containsUpdateGroupClass(Class<?> clazz) {
        return updateGroupClasses.containsKey(clazz);
    }

}

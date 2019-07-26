package com.queryflow.reflection.invoker;

public interface FieldInvoker extends Invoker {

    /**
     * Returns the field type.
     *
     * @return {@code Class} object of this field.
     */
    Class<?> getType();

    /**
     * Checks if the field is readable. If the field is accessible or the
     * getter method is not null, returns {@code true}.
     *
     * @return {@code true} if the field is readable
     */
    boolean readable();

    /**
     * Checks if the field is writeable. If the field is not final,
     * and the field is accessible or the setter method is not null,
     * returns {@code true}.
     *
     * @return {@code true} if the field is writeable
     */
    boolean writeable();

    Object getValue(Object target);

    void setValue(Object target, Object value);

}

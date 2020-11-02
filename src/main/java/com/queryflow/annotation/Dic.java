package com.queryflow.annotation;

import java.lang.annotation.*;

/**
 *
 * code name status           sort
 * 代码 名称  状态1-有效;0-无效  排序
 *
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dic {

    /**
     * 指定使用到的字典表的表明
     */
    String table();

    /**
     * 指定类中代表字典表中名称的字段名
     */
    String nameField();

}

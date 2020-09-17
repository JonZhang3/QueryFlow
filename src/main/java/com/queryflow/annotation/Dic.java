package com.queryflow.annotation;

import java.lang.annotation.*;

/**
 *
 * code name status
 * 代码 名称  状态1-有效;0-无效
 *
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dic {

    String table();

    String field();

}

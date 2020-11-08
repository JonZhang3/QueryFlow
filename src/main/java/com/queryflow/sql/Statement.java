package com.queryflow.sql;

import java.util.List;

/**
 * Statement
 *
 * @since 1.2.0
 * @author Jon
 */
public interface Statement {

    String buildSql();

    List<Object> getValues();

}

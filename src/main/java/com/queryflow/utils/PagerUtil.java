package com.queryflow.utils;

import com.alibaba.druid.sql.PagerUtils;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLSetQuantifier;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.expr.SQLAggregateOption;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.expr.SQLSequenceExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2SelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerSelectQueryBlock;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * page utils, 参考 Druid 源码，重写 count 方法，可在优化分页 sql之后，去除多余的参数值
 * https://github.com/alibaba/druid/blob/master/src/main/java/com/alibaba/druid/sql/PagerUtils.java
 *
 * @author Jon
 * @since 1.2.0
 */
public final class PagerUtil {

    private PagerUtil() {
    }

    public static String limit(String sql, String dbType, int offset, int count) {
        return PagerUtils.limit(sql, dbType, offset, count);
    }

    public static String count(String sql, String dbType) {
        return PagerUtils.count(sql, dbType);
    }

    public static String count(String sql, String dbtype, List<Object> values) {
        List<SQLStatement> statements = SQLUtils.parseStatements(sql, dbtype);
        if (statements.size() != 1) {
            return null;
        }
        SQLStatement statement = statements.get(0);
        if (!(statement instanceof SQLSelectStatement)) {
            return null;
        }
        SQLSelectStatement selectStatement = (SQLSelectStatement) statement;
        SQLSelect select = (selectStatement).getSelect();
        SQLVariantRefExprVisitor visitor = new SQLVariantRefExprVisitor(values);
        return count(select, visitor, dbtype);
    }

    public static String count(SQLSelect select, SQLASTVisitor visitor, String dbType) {
        clearOrderBy(select, visitor);
        SQLSelectQuery query = select.getQuery();
        clearOrderBy(query, visitor);

        if(query instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) query;
            if (queryBlock.getGroupBy() != null
                && queryBlock.getGroupBy().getItems().size() > 0) {
                return "SELECT COUNT(1) FROM (" + SQLUtils.toSQLString(select, dbType) + ")";
            }
            List<SQLSelectItem> selectList = queryBlock.getSelectList();
            int option = queryBlock.getDistionOption();
            if (option == SQLSetQuantifier.DISTINCT
                && selectList.size() > 0) {
                SQLAggregateExpr countExpr = new SQLAggregateExpr("COUNT", SQLAggregateOption.DISTINCT);
                Iterator<SQLSelectItem> iterator = selectList.iterator();
                while (iterator.hasNext()) {
                    SQLSelectItem item = iterator.next();
                    item.accept(visitor);
                    countExpr.addArgument(item.getExpr());
                    iterator.remove();
                }
                queryBlock.setDistionOption(0);
                queryBlock.addSelectItem(countExpr);
            } else {
                selectList.clear();
                selectList.add(createCountItem(dbType));
            }
            return SQLUtils.toSQLString(select, dbType);
        } else if(query instanceof SQLUnionQuery) {
            return "SELECT COUNT(1) FROM (" + SQLUtils.toSQLString(select, dbType) + ")";
        }
        return null;
    }

    private static void clearOrderBy(SQLSelect select, SQLASTVisitor visitor) {
        SQLOrderBy orderBy = select.getOrderBy();
        if (orderBy != null) {
            orderBy.accept(visitor);
            select.setOrderBy(null);
        }
    }

    private static SQLSelectItem createCountItem(String dbType) {
        SQLAggregateExpr countExpr = new SQLAggregateExpr("COUNT");
        countExpr.addArgument(new SQLAllColumnExpr());
        return new SQLSelectItem(countExpr);
    }

    private static void clearOrderBy(SQLSelectQuery query, SQLASTVisitor visitor) {
        if (query instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock block = (SQLSelectQueryBlock) query;
            SQLOrderBy orderBy = block.getOrderBy();
            if (orderBy != null) {
                orderBy.accept(visitor);
                block.setOrderBy(null);
            }
            return;
        }
        if (query instanceof SQLUnionQuery) {
            SQLUnionQuery unionQuery = (SQLUnionQuery) query;
            SQLOrderBy orderBy = unionQuery.getOrderBy();
            if (orderBy != null) {
                orderBy.accept(visitor);
                unionQuery.setOrderBy(null);
            }
            clearOrderBy(unionQuery.getLeft(), visitor);
            clearOrderBy(unionQuery.getRight(), visitor);
        }
    }

    private static SQLSelectQueryBlock createQueryBlock(String dbType) {
        if (JdbcConstants.MYSQL.equals(dbType)
            || JdbcConstants.MARIADB.equals(dbType)
            || JdbcConstants.ALIYUN_ADS.equals(dbType)) {
            return new MySqlSelectQueryBlock();
        }

        if (JdbcConstants.H2.equals(dbType)) {
            return new MySqlSelectQueryBlock();
        }

        if (JdbcConstants.ORACLE.equals(dbType)) {
            return new OracleSelectQueryBlock();
        }

        if (JdbcConstants.POSTGRESQL.equals(dbType)) {
            return new PGSelectQueryBlock();
        }

        if (JdbcConstants.SQL_SERVER.equals(dbType) || JdbcUtils.JTDS.equals(dbType)) {
            return new SQLServerSelectQueryBlock();
        }

        if (JdbcConstants.DB2.equals(dbType)) {
            return new DB2SelectQueryBlock();
        }

        return new SQLSelectQueryBlock();
    }

    private static class SQLVariantRefExprVisitor extends SQLASTVisitorAdapter {

        private final List<Object> values;

        protected SQLVariantRefExprVisitor(List<Object> values) {
            this.values = values;
        }

        @Override
        public boolean visit(SQLVariantRefExpr expr) {
            if ("?".equals(expr.getName())) {
                values.remove(expr.getIndex());
            }
            return true;
        }
    }

}

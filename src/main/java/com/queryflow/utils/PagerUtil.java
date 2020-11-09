package com.queryflow.utils;

import com.alibaba.druid.sql.PagerUtils;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.expr.SQLSequenceExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;

import java.util.List;

/**
 * @author Jon
 * @since 1.2.0
 */
public final class PagerUtil {

    private PagerUtil() {
    }

    public static String limit(String sql, String dbType, int offset, int count) {
        return PagerUtils.limit(sql, dbType, offset, count);
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
        return count(select, visitor);
    }

    public static String count(SQLSelect select, SQLASTVisitor visitor) {
        clearOrderBy(select, visitor);
        SQLSelectQuery query = select.getQuery();
        clearOrderBy(query, visitor);


        return "";
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

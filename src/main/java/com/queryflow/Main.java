package com.queryflow;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class Main {

    @Transactional
    public static void main(String[] args) {
        String sql = "SELECT g.*" +
            "    , t.agentname, t.agentlogo, t.compaddress " +
            "FROM t_bas_integral_goods g " +
            "    LEFT JOIN t_bas_agent t ON g.agentid = t.AGENTID " +
            "WHERE t.AGENTTYPE = '2' " +
            "    AND t.pass = '?'" +
            "    AND t.dl_type = ?" +
            "    AND g.type = ? " +
            "ORDER BY (SELECT id FROM user where g.a=b and name = ?) ASC";
        List<SQLStatement> statements = SQLUtils.parseStatements(sql, "mysql");
        Test test = new Test();
        SQLSelectStatement statement = (SQLSelectStatement) statements.get(0);
        SQLSelect select = statement.getSelect();
        SQLOrderBy orderBy = select.getOrderBy();
        if(orderBy != null) {
            System.out.println(orderBy);
            orderBy.accept(test);
        }
        SQLSelectQueryBlock query = (SQLSelectQueryBlock) select.getQuery();
        orderBy = query.getOrderBy();
        if(orderBy != null) {
            orderBy.accept(test);
        }
//        System.out.println(statement);
    }

    public static class Test extends SQLASTVisitorAdapter {
        public boolean visit(SQLVariantRefExpr expr) {
            System.out.println(expr.getIndex() + ":" + expr);
            return true;
        }
    }

}

package com.queryflow.sql;

public class Join {

    private final JoinType type;
    private final Select select;


    protected Join(Select select, JoinType type) {
        this.type = type;
        this.select = select;
    }

    public Select on(String right, String left, String... rightAndLeft) {

        return select;
    }

    enum JoinType {
        JOIN(" JOIN "),
        LEFT_JOIN(" LEFT JOIN "),
        RIGHT_JOIN(" RIGHT JOIN "),
        FULL_JOIN(" FULL JOIN ");

        private final String type;

        JoinType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

}

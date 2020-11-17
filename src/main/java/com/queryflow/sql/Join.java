package com.queryflow.sql;

public final class Join {

    private final JoinType type;
    private final Select select;
    private final String sentence;

    protected Join(Select select, JoinType type, String sentence) {
        this.type = type;
        this.select = select;
        this.sentence = sentence;
    }

    public Select on(String left, String right, String... leftAndRight) {
        select.stack.push(type.getType()).push(sentence).push(" ON ")
            .push(left).push(" = ").push(right);
        if (leftAndRight != null && leftAndRight.length > 0) {
            for (int i = 0, len = leftAndRight.length; i < len - 1; i += 2) {
                if(i < len - 1) {
                    select.stack.push(" AND ");
                }
                select.stack.push(leftAndRight[i]).push(" = ").push(leftAndRight[i + 1]);
            }
        }
        return select;
    }

    enum JoinType {
        JOIN(" INNER JOIN "),
        LEFT_JOIN(" LEFT JOIN "),
        RIGHT_JOIN(" RIGHT JOIN "),
        FULL_JOIN(" FULL OUTER JOIN ");

        private final String type;

        JoinType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

}

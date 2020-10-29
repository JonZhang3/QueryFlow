package com.queryflow.sql;

public class Join {

    private final JoinType type;
    private final Select select;
    private final String sentence;

    protected Join(Select select, JoinType type, String sentence) {
        this.type = type;
        this.select = select;
        this.sentence = sentence;
    }

    public Select on(String left, String right, String... leftAndRight) {
        select.appender.append(type.getType()).append(sentence).append(" on ")
            .append(left).append(" = ").append(right);
        if (leftAndRight != null && leftAndRight.length > 0) {
            for (int i = 0, len = leftAndRight.length; i < len - 1; i += 2) {
                select.appender.append(leftAndRight[i]).append(" = ").append(leftAndRight[i + 1]);
            }
        }
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

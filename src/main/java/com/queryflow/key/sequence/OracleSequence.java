package com.queryflow.key.sequence;

import com.queryflow.utils.Assert;

public class OracleSequence implements Sequence<String> {

    private final String sequesceName;

    public OracleSequence(String sequenceName) {
        Assert.notEmpty(sequenceName);
        this.sequesceName = sequenceName;
    }

    @Override
    public String next() {
        return sequesceName + ".NEXTVAL";
    }

    public String getSequesceName() {
        return sequesceName;
    }


}

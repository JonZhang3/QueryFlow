package com.queryflow.key;

import com.queryflow.key.sequence.OracleSequence;

public class OracleSequenceKeyGenerator implements KeyGenerator<String> {

    private static final OracleSequence SEQUENCE = new OracleSequence("test");

    @Override
    public String generate() {
        return SEQUENCE.next();
    }
}

package com.queryflow.key;

import com.queryflow.key.sequence.Sequence;
import com.queryflow.key.sequence.SimpleSequence;

public class SimpleKeyGenerator implements KeyGenerator<String> {

    private Sequence<String> sequence = new SimpleSequence();

    @Override
    public String generate() {
        return sequence.next();
    }

}

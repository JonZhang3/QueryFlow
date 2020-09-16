package com.queryflow.key;

import com.queryflow.key.sequence.Sequence;
import com.queryflow.key.sequence.SimpleSequence;

@Deprecated
public class SimpleKeyGenerator implements KeyGenerator<String> {

    private final Sequence<String> sequence = new SimpleSequence();

    @Override
    public String generate() {
        return sequence.next();
    }

}

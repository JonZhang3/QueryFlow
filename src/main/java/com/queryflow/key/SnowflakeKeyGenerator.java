package com.queryflow.key;

import com.queryflow.config.GlobalConfig;
import com.queryflow.key.sequence.Sequence;
import com.queryflow.key.sequence.SnowflakeSequence;

public class SnowflakeKeyGenerator implements KeyGenerator<Long> {

    private Sequence<Long> sequence = new SnowflakeSequence(
        GlobalConfig.getWorkerId(), GlobalConfig.getDataCenterId()
    );

    @Override
    public Long generate() {
        return sequence.next();
    }
}

package com.queryflow.key;

import java.util.UUID;

public class UUIDKeyGenerator implements KeyGenerator<String> {

    @Override
    public String generate() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}

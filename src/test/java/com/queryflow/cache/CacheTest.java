package com.queryflow.cache;

import static org.junit.Assert.*;

import com.queryflow.cache.impl.LFUCache;
import org.junit.Test;

public class CacheTest {

    @Test
    public void testLFUCache() {
        Cache<String, String> cache = new LFUCache<>(5);
        cache.putValue("A", "a");
        cache.putValue("B", "b");
        cache.putValue("C", "c");
        cache.putValue("D", "d");
        cache.putValue("E", "e");
        cache.putValue("F", "f");
        assertNull(cache.getValue("A"));
        assertEquals("b", cache.getValue("B"));
        cache.putValue("G", "g");
        assertNull(cache.getValue("C"));
    }

}

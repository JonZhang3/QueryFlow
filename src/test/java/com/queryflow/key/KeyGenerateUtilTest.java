package com.queryflow.key;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class KeyGenerateUtilTest {

    @BeforeClass
    public static void registerKeyGenerator() {
        KeyGenerateUtil.registerKeyGenerator(OracleSequenceKeyGenerator.class);
    }

    @Test
    public void testRegisterKeyGenerator() {
        assertEquals(3, KeyGenerateUtil.keyGeneratorSize());
        assertTrue(KeyGenerateUtil.containKeyGenerator(OracleSequenceKeyGenerator.class));
    }

    @Test
    public void testGenerateId() {
        assertNotNull(KeyGenerateUtil.generateId());

        assertNull(KeyGenerateUtil.generateId(AutoIncrementKeyGenerator.class));

        assertEquals("test.NEXTVAL", KeyGenerateUtil.generateId(OracleSequenceKeyGenerator.class));
    }

//    @Test
//    public void testSimpleKeyGenerator() {
//        Set<Object> sets = new LinkedHashSet<>();
//        long start = System.currentTimeMillis();
//        for(int i = 0; i < 100; i++) {
//            sets.add(KeyGenerateUtil.generateId(SimpleKeyGenerator.class));
//        }
//        System.out.println(sets.size());
//        System.out.println((System.currentTimeMillis() - start) + "ms");
//        sets.clear();
//        start = System.currentTimeMillis();
//        for(int i = 0; i < 100; i++) {
//            sets.add(KeyGenerateUtil.generateId(SnowflakeKeyGenerator.class));
//        }
//        System.out.println(sets.size());
//        System.out.println((System.currentTimeMillis() - start) + "ms");
//    }

}

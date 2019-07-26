package com.queryflow.utils;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Date;

public class ConvertsTest {

    @Test
    public void testToStr() {
        assertNull(Converts.toStr(null, null));
        assertEquals("abc", Converts.toStr("abc", null));
    }

    @Test
    public void testToCharacter() {
        assertEquals('\0', Converts.toCharacter(null, '\0').charValue());
        assertEquals('a', Converts.toCharacter('a', '\0').charValue());
        assertEquals('\0', Converts.toCharacter("abc", '\0').charValue());
    }

    @Test
    public void testToBoolean() {
        assertFalse(Converts.toBoolean(null, false));
        assertTrue(Converts.toBoolean(true, false));
        assertFalse(Converts.toBoolean("123", false));
        assertTrue(Converts.toBoolean("true", false));
        assertFalse(Converts.toBoolean("falsse", true));
    }

    @Test
    public void testToByte() {
        assertEquals(10, Converts.toByte(10, (byte) 0).byteValue());
        assertEquals(11, Converts.toByte("11", (byte) 0).byteValue());
        assertEquals(0, Converts.toByte("abc", (byte) 0).byteValue());
    }

    @Test
    public void testToShort() {
        assertEquals(10, Converts.toShort(10, (short) 0).shortValue());
        assertEquals(11, Converts.toShort("11", (short) 0).shortValue());
        assertEquals(0, Converts.toShort("abc", (short) 0).shortValue());
    }

    @Test
    public void testToInt() {
        assertEquals(10, Converts.toInteger(10, 0).intValue());
        assertEquals(11, Converts.toInteger("11", 0).intValue());
        assertEquals(0, Converts.toInteger("abc", 0).intValue());
    }

    @Test
    public void testToLong() {
        assertEquals(10, Converts.toLong(10, 0L).longValue());
        assertEquals(11, Converts.toLong("11", 0L).longValue());
        assertEquals(0, Converts.toLong("abc", 0L).longValue());
    }

    @Test
    public void testToFloat() {
        assertEquals(10, Converts.toFloat(10, 0.0F), 0.00001);
        assertEquals(11.2, Converts.toFloat("11.2", 0.0F), 0.00001);
        assertEquals(0.0F, Converts.toFloat("abc", 0.0F), 0.00001);
    }

    @Test
    public void testToDouble() {
        assertEquals(10, Converts.toDouble(10, 0.0D), 0.00001);
        assertEquals(11.2, Converts.toDouble("11.2", 0.0D), 0.00001);
        assertEquals(0.0F, Converts.toDouble("abc", 0.0D), 0.00001);
    }

    @Test
    public void testToBytes() {
        byte[] emptyBytes = new byte[0];
        assertArrayEquals(emptyBytes, Converts.toBytes(null, emptyBytes));
        assertArrayEquals(new byte[]{1, 2, 3}, Converts.toBytes(new byte[]{1, 2, 3}, emptyBytes));
        assertArrayEquals(new byte[]{1, 2, 3}, Converts.toBytes(new Byte[]{1, 2, 3}, emptyBytes));
        assertArrayEquals(new byte[]{1}, Converts.toBytes((byte) 1, emptyBytes));
        assertArrayEquals(new byte[]{1}, Converts.toBytes(new Byte((byte) 1), emptyBytes));
        assertArrayEquals(new byte[]{97, 98, 99}, Converts.toBytes("abc", emptyBytes));
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{1, 2, 3});
        assertArrayEquals(new byte[]{1, 2, 3}, Converts.toBytes(bais, emptyBytes));
    }

    @Test
    public void testToBigDecimal() {

    }

    @Test
    public void testTo() {
        assertEquals(false, Converts.to(boolean.class, null));
        assertEquals(0, (int) Converts.to(int.class, null));
        assertEquals(0, (int) Converts.to(Integer.class, null));
        assertEquals("1", Converts.to(String.class, 1));
    }

}

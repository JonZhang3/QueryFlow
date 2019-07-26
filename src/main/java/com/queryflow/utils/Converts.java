package com.queryflow.utils;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public final class Converts {

    private Converts() {
    }

    public static final String DATE_PATTERN = "yyyyMMddHHmmss";

    public static String toStr(Object value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value.toString();
    }

    public static Character toCharacter(Object value, Character defaultValue) {
        if(value == null) {
            return defaultValue;
        }
        String strValue = value.toString();
        if(strValue.length() == 1) {
            return Character.valueOf(strValue.charAt(0));
        }
        return defaultValue;
    }

    public static Boolean toBoolean(final Object value, Boolean defaultValue) {
        if(value == null) {
            return defaultValue;
        }
        if(value instanceof Boolean) {
            return (Boolean) value;
        }
        try {
            return Boolean.valueOf(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    static Number toNumber(final Object value, final Class<?> targetClass) {
        if(value instanceof Number) {
            return (Number) value;
        }
        final String str = value.toString();
        if(str.startsWith("0x")) {
            try {
                return new BigInteger(str.substring("0x".length()), 16);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        if(str.startsWith("0b")) {
            try {
                return new BigInteger(str.substring("0b".length()), 2);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        try {
            Constructor<?> constructor = targetClass.getConstructor(String.class);
            return (Number) constructor.newInstance(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static Byte toByte(Object value, Byte defaultValue) {
        if(value == null) {
            return defaultValue;
        }
        if(value instanceof Byte) {
            return (Byte) value;
        }
        Number number = toNumber(value, Byte.class);
        if(number == null) {
            return defaultValue;
        }
        return number.byteValue();
    }

    public static Short toShort(Object value, Short defaultValue) {
        if(value == null) {
            return defaultValue;
        }
        if(value instanceof Short) {
            return (Short) value;
        }
        Number number = toNumber(value, Short.class);
        if(number == null) {
            return defaultValue;
        }
        return number.shortValue();
    }

    public static Integer toInteger(Object value, Integer defaultValue) {
        if(value == null) {
            return defaultValue;
        }
        if(value instanceof Integer) {
            return (Integer) value;
        }
        Number number = toNumber(value, Integer.class);
        if(number == null) {
            return defaultValue;
        }
        return number.intValue();
    }

    public static Long toLong(Object value, Long defaultValue) {
        if(value == null) {
            return defaultValue;
        }
        if(value instanceof Long) {
            return (Long) value;
        }
        Number number = toNumber(value, Long.class);
        if(number == null) {
            return defaultValue;
        }
        return number.longValue();
    }

    public static Float toFloat(Object value, Float defaultValue) {
        if(value == null) {
            return defaultValue;
        }
        if(value instanceof Float) {
            return (Float) value;
        }
        Number number = toNumber(value, Float.class);
        if(number == null) {
            return defaultValue;
        }
        return number.floatValue();
    }

    public static Double toDouble(Object value, Double defaultValue) {
        if(value == null) {
            return defaultValue;
        }
        if(value instanceof Double) {
            return (Double) value;
        }
        Number number = toNumber(value, Double.class);
        if(number == null) {
            return defaultValue;
        }
        return number.doubleValue();
    }

    public static byte[] toBytes(Object value, byte[] defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        Class<?> valueClass = value.getClass();
        if (Byte.class.equals(valueClass)) {
            return new byte[]{(Byte) value};
        }
        if(value instanceof byte[]) {
            return (byte[]) value;
        }
        if(value instanceof Byte[]) {
            Byte[] bytes = (Byte[]) value;
            byte[] result = new byte[bytes.length];
            for(int i = 0, len = bytes.length; i < len; i++) {
                result[i] = bytes[i].byteValue();
            }
            return result;
        }
        if (value instanceof String) {
            try {
                return ((String) value).getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defaultValue;
            }
        }
        if (value instanceof InputStream) {
            byte[] result;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream in = (InputStream) value;
            Utils.copy(in, baos);
            result = baos.toByteArray();
            Utils.close(baos);
            return result;
        }
        return defaultValue;
    }

    public static BigDecimal toBigDecimal(Object value, BigDecimal defaultValue) {
        if(value == null) {
            return defaultValue;
        }
        if(value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        Number number = toNumber(value, BigDecimal.class);
        if(number == null) {
            return defaultValue;
        }
        return new BigDecimal(number.doubleValue());
    }

    public static Date toDate(Object value, Date defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Date) {
            return (Date) value;
        }
        if (value instanceof java.util.Date) {
            return new Date(((java.util.Date) value).getTime());
        }
        return defaultValue;
    }

    public static Time toTime(Object value, Time defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Time) {
            return (Time) value;
        }
        if (value instanceof java.util.Date) {
            return new Time(((java.util.Date) value).getTime());
        }
        return defaultValue;
    }

    public static Timestamp toTimestamp(Object value, Timestamp defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Timestamp) {
            return (Timestamp) value;
        }
        if (value instanceof java.util.Date) {
            return new Timestamp(((java.util.Date) value).getTime());
        }
        return defaultValue;
    }

    public static java.util.Date toUtilDate(Object value, String pattern, java.util.Date defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (java.util.Date.class == value.getClass()) {
            return (java.util.Date) value;
        }
        if (value instanceof java.util.Date) {
            return new java.util.Date(((java.util.Date) value).getTime());
        }
        if (value instanceof String) {
            if (Utils.isEmpty(pattern)) {
                pattern = DATE_PATTERN;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            try {
                return sdf.parse((String) value);
            } catch (ParseException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static Clob toClob(Object value, Clob defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Clob) {
            return (Clob) value;
        }
        if (value instanceof String) {
            try {
                return new SerialClob(((String) value).toCharArray());
            } catch (SQLException ignore) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static Blob toBlob(Object value, Blob defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Blob) {
            return (Blob) value;
        }
        if (value instanceof byte[]) {
            try {
                return new SerialBlob((byte[]) value);
            } catch (SQLException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static <T> T to(Class<T> clazz, Object value) {
        Object result = null;
        if (value == null && clazz.isPrimitive()) {
            if (boolean.class.equals(clazz)) {
                result = false;
            } else {
                result = 0;
            }
        }
        if (clazz.isInstance(value)) {
            result = value;
        } else if (String.class.equals(clazz)) {
            result = toStr(value, null);
        } else if (Boolean.class.equals(clazz) || Boolean.TYPE.equals(clazz)) {
            result = toBoolean(value, false);
        } else if (Character.class.equals(clazz) || Character.TYPE.equals(clazz)) {
            result = toCharacter(value, '\0');
        } else if (Number.class.isAssignableFrom(clazz) || clazz.isPrimitive()) {
            if (Integer.class.equals(clazz) || Integer.TYPE.equals(clazz)) {
                result = toInteger(value, 0);
            } else if (Long.class.equals(clazz) || Long.TYPE.equals(clazz)) {
                result = toLong(value, 0L);
            } else if (Byte.class.equals(clazz) || Byte.TYPE.equals(clazz)) {
                result = toByte(value, (byte) 0);
            } else if (Short.class.equals(clazz) || Short.TYPE.equals(clazz)) {
                result = toShort(value, (short) 0);
            } else if (Float.class.equals(clazz) || Float.TYPE.equals(clazz)) {
                result = toFloat(value, 0.0F);
            } else if (Double.class.equals(clazz) || Double.TYPE.equals(clazz)) {
                result = toDouble(value, 0.0D);
            } else if (BigDecimal.class.equals(clazz)) {
                result = toBigDecimal(value, new BigDecimal(0));
            }
        } else {
            result = clazz.cast(value);
        }
        return (T) result;
    }

}

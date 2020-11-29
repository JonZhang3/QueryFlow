package com.queryflow.utils;

import com.queryflow.common.QueryFlowException;
import com.queryflow.common.function.Action2;

import java.io.*;
import java.lang.reflect.*;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.jar.JarFile;

/**
 * 通用工具类
 *
 * @author Jon
 * @since 1.0.0
 */
public final class Utils {

    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_MAP = new HashMap<>(8);

    static {
        PRIMITIVE_WRAPPER_MAP.put(Boolean.class, boolean.class);
        PRIMITIVE_WRAPPER_MAP.put(Byte.class, byte.class);
        PRIMITIVE_WRAPPER_MAP.put(Character.class, char.class);
        PRIMITIVE_WRAPPER_MAP.put(Short.class, short.class);
        PRIMITIVE_WRAPPER_MAP.put(Integer.class, int.class);
        PRIMITIVE_WRAPPER_MAP.put(Long.class, long.class);
        PRIMITIVE_WRAPPER_MAP.put(Float.class, float.class);
        PRIMITIVE_WRAPPER_MAP.put(Double.class, double.class);
    }

    private Utils() {
    }

    /**
     * 获取默认的 {@code ClassLoader}
     *
     * @return 默认的 {@code ClassLoader}
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ignore) {
        }
        if (cl == null) {
            cl = Utils.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ignore) {
                }
            }
        }
        return cl;
    }

    /**
     * 判断指定的 Class 是否是基本数据类型或其包装类型
     *
     * @param clazz Class
     * @return 如果是基本数据类型或其包装类型，返回 {@code true}。否则返回 {@code false}
     */
    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return (clazz.isPrimitive()) || (PRIMITIVE_WRAPPER_MAP.containsKey(clazz));
    }

    /**
     * 实例化一个指定类的对象，该类不应该是一个接口或抽象类，
     * 该类必须有一个不包含参数的 public 构造方法
     *
     * @param clazz 指定类
     * @param <T>   类型
     * @return 指定类的一个对象，如果参数为空，返回 {@code null}
     * @throws ClassInstantiationException 如果指定类是一个接口或者抽象类，或者不包含公共的默认构造方法，抛出该异常
     */
    public static <T> T instantiate(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        if (clazz.isInterface()) {
            throw new ClassInstantiationException(clazz, "the class is interface.");
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new ClassInstantiationException(clazz, "Is it an abstract class?", e);
        } catch (IllegalAccessException e) {
            throw new ClassInstantiationException(clazz, "Is the constructor accessible?", e);
        }
    }

    /**
     * 获取指定类的无参数默认构造方法
     *
     * @param clazz 指定类的 Class 对象
     * @param <T>   类型
     * @return 如果传入 {@code null}，返回 {@code null}。如果没有默认构造方法，返回 {@code null}。
     * 如果默认构造方法是私有的，且不可以通过反射访问类的私有方法，返回 {@code null}。
     */
    public static <T> Constructor<T> getDefaultConstructor(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        Constructor<T> constructor;
        try {
            constructor = clazz.getDeclaredConstructor();
            if (constructor != null) {
                if (canAccessPrivateMethod()) {
                    constructor.setAccessible(true);
                }
                if (constructor.isAccessible()) {
                    return constructor;
                }
            }
        } catch (NoSuchMethodException e) {
            return null;
        }
        return null;
    }

    /**
     * 获取指定字段的 getter 方法
     *
     * @param field 类的成员变量
     * @return 字段的 getter 方法
     * @deprecated 从 1.2.0 开始, 不再使用该方法获取属性的 getter 方法，改用 {@link java.beans.PropertyDescriptor}
     */
    @Deprecated
    public static Method getterMethod(Field field) {
        Assert.notNull(field);

        Class<?> declaringClass = field.getDeclaringClass();// 获取字段所在类的类型
        Class<?> fieldType = field.getType();// 获取字段声明的类型
        String methodName = capitalize(field.getName());
        if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
            methodName = "is" + methodName;
        } else {
            methodName = "get" + methodName;
        }
        Method method = getMethod(declaringClass, methodName);
        if (method == null) {
            methodName = "" + methodName;
            method = getMethod(declaringClass, methodName);
        }
        if (method != null && method.getReturnType().equals(fieldType)) {
            if (!Modifier.isPublic(method.getModifiers())) {
                method.setAccessible(true);
            }
            return method;
        }
        return null;
    }

    /**
     * 获取指定字段的 setter 方法
     *
     * @param field 类的成员变量
     * @return 字段的 setter 方法
     * @deprecated 从 1.2.0 开始, 不再使用该方法获取属性的 setter 方法，改用 {@link java.beans.PropertyDescriptor}
     */
    @Deprecated
    public static Method setterMethod(Field field) {
        Assert.notNull(field);

        Class<?> declaringClass = field.getDeclaringClass();
        Class<?> fieldType = field.getType();
        String methodName = "set" + capitalize(field.getName());
        Method method = getMethod(declaringClass, methodName, fieldType);
        if (method != null) {
            if (!Modifier.isPublic(method.getModifiers())) {
                method.setAccessible(true);
            }
            return method;
        }
        return null;
    }

    /**
     * 获取指定类中指定方法名的方法
     *
     * @param clazz      指定类
     * @param methodName 方法名
     * @param paramTypes 方法的参数列表
     * @return 字段的 getter 方法
     */
    public static Method getMethod(Class<?> clazz, String methodName,
                                   Class<?>... paramTypes) {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(methodName, paramTypes);
        } catch (NoSuchMethodException ignore) {
        }
        if (method == null) {
            Method[] methods = clazz.getDeclaredMethods();
            if (methods.length > 0) {
                Set<Method> candidates = new HashSet<>(methods.length);
                for (Method expect : methods) {
                    if (methodName.equals(expect.getName())) {
                        candidates.add(expect);
                    }
                }
                if (candidates.size() == 1) {
                    method = candidates.iterator().next();
                } else if (candidates.size() > 1 && (paramTypes == null || paramTypes.length == 0)) {
                    throw new IllegalStateException("No unique method found: " + clazz.getName() + '.' + methodName);
                }
            }
        }
        return method;
    }

    /**
     * 将指定参数转换为数组类型
     * 如果参数为 {@code null}，返回 {@code null}。
     * 如果参数本身就是个数组，直接返回。
     * 如果是 {@code List}，转换为数组返回。
     * 否则，将参数存入一个 Object 数组犯规
     *
     * @param value 参数
     * @return {@code null} or object array
     */
    @SuppressWarnings("unchecked")
    public static Object[] toArray(Object value) {
        if (value == null) {
            return null;
        }
        Object[] values;
        if (value.getClass().isArray()) {
            values = (Object[]) value;
        } else if (value instanceof List) {
            List<Object> list = (List<Object>) value;
            values = list.toArray();
        } else {
            values = new Object[]{value};
        }
        return values;
    }

    /**
     * 判断给定的字符串是否为 {@code null} 或空。
     *
     * @param src 指定的字符串
     * @return 如果指定的字符串为 {@code null} 或空，返回 {@code true}，否则返回 {@code false}
     */
    public static boolean isEmpty(String src) {
        return (null == src || src.isEmpty());
    }

    /**
     * 判断给定的字符串是否不为 {@code null} 并且不为空
     *
     * @param src 指定的字符串
     * @return 如果指定的字符串不为 {@code null} 并且不为空，返回 {@code true}，否则返回 {@code false}
     */
    public static boolean isNotEmpty(String src) {
        return !isEmpty(src);
    }

    /**
     * 判断给定的字符串是否为空，或者仅包含空白字符
     *
     * @param src 要判断的字符串
     * @return 不为空，且不仅包含空白字符，返回 {@code false}
     */
    public static boolean isBlank(String src) {
        if (isEmpty(src)) {
            return true;
        }
        for (int i = 0, len = src.length(); i < len; i++) {
            if (!Character.isWhitespace(src.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String src) {
        return !isBlank(src);
    }

    /**
     * 将小驼峰命名方式的字符串转换为 _ 命名方式
     * userName 输出 user_name
     *
     * @param src 小驼峰命名方式的字符串
     * @return 以 _ 命名方式的字符串。如果给定字符串为空，返回一个空字符串
     */
    public static String camelCaseToSnake(String src) {
        if (isEmpty(src)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        char ch;
        result.append(src.charAt(0));
        for (int i = 1, len = src.length(); i < len; i++) {
            ch = src.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                result.append('_').append((char) (ch + 32));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    /**
     * 拼接字符串，可指定间隔符，以及后缀。
     * 如果间隔符为空，则默认为空字符串
     * <code>
     * join(",", {"a", "b", "c"}, "!")
     * 输出 "a!,b!,c!"
     * </code>
     *
     * @param separator 间隔符
     * @param values    字符串列表
     * @param suffix    后缀
     * @param <T>       集合中对象的类型，默认会调用 {@code toString} 方法
     * @return 拼接后的字符串
     */
    public static <T> String join(String separator, Collection<T> values, String suffix) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        if (separator == null) {
            separator = "";
        }
        StringBuilder result = new StringBuilder();
        Object value;
        String str;
        int i = 0;
        int size = values.size();
        Iterator<T> iterator = values.iterator();
        while (iterator.hasNext()) {
            value = iterator.next();
            if (value != null) {
                str = value.toString();
                if (!"".equals(str)) {
                    result.append(str);
                    if (suffix != null) {
                        result.append(suffix);
                    }
                    if (i != size - 1) {
                        result.append(separator);
                    }
                }
            }
            i++;
        }
        return result.toString();
    }

    /**
     * 拼接字符串
     *
     * @param separator 间隔符
     * @param values    字符串列表
     * @param <T>       默认会调用集合中对象的 {@code toString} 方法
     * @return 拼接后的字符串
     */
    public static <T> String join(String separator, Collection<T> values) {
        return join(separator, values, null);
    }

    /**
     * 遍历指定目录中的所有文件
     *
     * @param root    指定目录
     * @param action2 传入参数：(File：文件，String：相对与指定个根目录的路径)
     */
    public static void listFiles(File root, Action2<File, String> action2) {
        if (root != null && root.exists()) {
            if (root.isFile()) {
                if (action2 != null) {
                    action2.apply(root, "");
                }
            } else if (root.isDirectory()) {
                recursiveListFiles(root, "", action2);
            }
        }
    }

    /**
     * 从输入流读取数据，写入到输出流
     *
     * @param in  输入流
     * @param out 输出流
     * @throws IORuntimeException 如果发生 IO 异常，抛出该非受检异常
     */
    public static void copy(InputStream in, OutputStream out) {
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            close(in);
        }
    }

    public static void close(Closeable... closeables) {
        if (closeables == null || closeables.length == 0) {
            return;
        }
        try {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        } catch (IOException ignore) {
        }
    }

    /**
     * 将 {@code Properties} 中的所有属性存入到一个 Map 中
     *
     * @param properties Properties
     * @return key 为 String 类型，value 为 Object 类型的 Map
     */
    public static Map<String, Object> propertiesToMap(Properties properties) {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            result.put((String) entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static File findHomeDir() {
        try {
            URL location = Utils.class.getProtectionDomain().getCodeSource().getLocation();
            URLConnection connection = location.openConnection();
            File source;
            if (connection instanceof JarURLConnection) {
                source = getRootJarFile(((JarURLConnection) connection).getJarFile());
            } else {
                source = new File(location.toURI());
            }
            source = source.getAbsoluteFile();
            if (source.isFile()) {
                source = source.getParentFile();
            }
            return source.getAbsoluteFile();
        } catch (IOException | URISyntaxException e) {
            throw new QueryFlowException(e);
        }
    }

    public static boolean isZeroValue(Class<?> clazz, Object value) {
        if (boolean.class == clazz) {
            return Boolean.FALSE.equals(value);
        }
        if (clazz.isPrimitive()) {
            Number num = (Number) value;
            return num.doubleValue() == 0;
        } else {
            return value == null;
        }
    }

    public static Map<String, Field> getFields(Class<?> type) {
        Map<String, Field> result = new LinkedHashMap<>();
        getFields(type, result);
        return result;
    }

    /**
     * 获取指定类中的字段，包括其祖先类中的字段，如果子类与祖先类中存在相同名称的字段，则保留子类中的字段
     *
     * @param type     指定的类
     * @param fieldMap 包含的字段
     */
    public static void getFields(Class<?> type, Map<String, Field> fieldMap) {
        Field[] fields = type.getDeclaredFields();
        if (fields.length > 0) {
            for (Field field : fields) {
                fieldMap.putIfAbsent(field.getName(), field);
            }
        }
        Class<?> superclass = type.getSuperclass();
        if (superclass != Object.class) {
            getFields(superclass, fieldMap);
        }
    }

    public static Byte[] toObjectArray(byte[] arrays) {
        final Byte[] result = new Byte[arrays.length];
        for (int i = 0, len = arrays.length; i < len; i++) {
            result[i] = arrays[i];
        }
        return result;
    }

    private static File getRootJarFile(JarFile jarFile) {
        String name = jarFile.getName();
        int separator = name.indexOf("!/");
        if (separator > 0) {
            name = name.substring(0, separator);
        }
        return new File(name);
    }

    private static void recursiveListFiles(File dir, String relativePath, Action2<File, String> action2) {
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            if (relativePath.startsWith("/")) {
                relativePath = relativePath.substring(1);
            }
            for (File file : files) {
                if (file.isFile()) {
                    if (action2 != null) {
                        action2.apply(file, relativePath);
                    }
                } else if (file.isDirectory()) {
                    recursiveListFiles(file, relativePath + "/" + file.getName(), action2);
                }
            }
        }
    }

    /**
     * 将属性名的首字母转换为大小
     * userName -> UserName
     *
     * @param propertyName 属性名
     * @return 转换后的字符串
     */
    private static String capitalize(String propertyName) {
        return propertyName.substring(0, 1).toUpperCase(Locale.ENGLISH)
            + propertyName.substring(1);
    }

    /**
     * 判断运行时是否可以方位私有方法
     */
    private static boolean canAccessPrivateMethod() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}

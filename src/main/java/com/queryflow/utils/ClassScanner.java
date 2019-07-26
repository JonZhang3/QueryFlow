package com.queryflow.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类扫描工具类，可指定扫描的包，可设置过滤器
 * <code>
 *     ClassScanner.newScanner()
 *         .setFilter((clazz) -> true)
 *         .scan();
 * </code>
 *
 * @author Jon
 * @since 1.0.0
 */
public class ClassScanner {

    private static final String DOT = ".";
    private static final String CLASS_FILE_SUFFIX = ".class";

    private List<String> packageNames = new LinkedList<>();
    private Filter<Class<?>> filter = new SampleClassFilter();
    private ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    private ClassScanner() {
    }

    public static ClassScanner newScanner() {
        return new ClassScanner();
    }

    public static ClassScanner newScanner(String... packageNames) {
        return new ClassScanner().packageName(packageNames);
    }

    private ClassScanner packageName(String... packageNames) {
        if (packageNames != null && packageNames.length > 0) {
            for (String packageName : packageNames) {
                if (Utils.isNotEmpty(packageName)) {
                    if (!packageName.endsWith(DOT)) {
                        packageName += DOT;
                    }
                    if (!this.packageNames.contains(packageName)) {
                        this.packageNames.add(packageName);
                    }
                }
            }
        }
        return this;
    }

    public ClassScanner setFilter(Filter<Class<?>> filter) {
        if (filter != null) {
            this.filter = filter;
        }
        return this;
    }

    public Set<Class<?>> scan() {
        final Set<Class<?>> classes = new HashSet<>();
        for (String packageName : packageNames) {
            List<URL> pathUrls = getClassPathUrls(packageName);
            for (URL url : pathUrls) {
                fillClasses(packageName, url, classes);
            }
        }
        return classes;
    }

    private void fillClasses(String packageName, URL url, Set<Class<?>> classes) {
        String classPath = decodeClassPath(url.getPath());
        if (!classPath.equals("")) {
            File file = new File(classPath);
            if (url.getProtocol().equals("file")) {
                if (file.isFile()) {
                    processClassFile(packageName, file, classes);
                } else if (file.isDirectory()) {
                    processDri(packageName, file, classes);
                }
            } else if (url.getProtocol().equals("jar")) {
                processJarFile(packageName, file, classes);
            }
        }
    }

    private void processClassFile(String packageName, File file, Set<Class<?>> classes) {
        String className = file.getName();
        if (isClass(className)) {
            className = packageName + className.replace(CLASS_FILE_SUFFIX, "");
            fillClass(packageName, className, classes);
        }
    }

    private void processDri(final String packageName, File file, final Set<Class<?>> classes) {
        Utils.listFiles(file, (child, relativePath) -> {
            String className = child.getName();
            if (isClass(className)) {
                relativePath = relativePath.replace('/', '.');
                if (!relativePath.endsWith(".")) {
                    relativePath += ".";
                }
                className = packageName + relativePath + className.replace(CLASS_FILE_SUFFIX, "");
                fillClass(packageName, className, classes);
            }
        });
    }

    private void processJarFile(String packageName, File file, Set<Class<?>> classes) {
        try (JarFile jarFile = new JarFile(file)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            JarEntry entry;
            while (entries.hasMoreElements()) {
                entry = entries.nextElement();
                if (isClass(entry.getName())) {
                    final String className = entry.getName().replace("/", ".")
                        .replace(CLASS_FILE_SUFFIX, "");
                    fillClass(packageName, className, classes);
                }
            }
        } catch (Exception ignore) {
        }
    }

    private void fillClass(String packageName, String className, Set<Class<?>> classes) {
        if (className.startsWith(packageName)) {
            try {
                final Class<?> clazz = Class.forName(className, false, classLoader);
                if (filter.accept(clazz)) {
                    classes.add(clazz);
                }
            } catch (Exception ignore) {
            }
        }
    }

    private boolean isClass(String name) {
        return name.endsWith(CLASS_FILE_SUFFIX);
    }

    private List<URL> getClassPathUrls(String packageName) {
        String packagePath = packageName.replace('.', '/');
        try {
            return Collections.list(classLoader.getResources(packagePath));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private String decodeClassPath(String classPath) {
        try {
            return URLDecoder.decode(classPath, Charset.defaultCharset().name());
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    private static class SampleClassFilter implements Filter<Class<?>> {
        @Override
        public boolean accept(Class<?> aClass) {
            return true;
        }
    }

}

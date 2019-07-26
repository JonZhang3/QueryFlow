package com.queryflow.config.parser;

import java.io.*;
import java.net.URL;

/**
 * 配置文件解析器抽象类
 *
 * @author Jon
 * @since 1.0.0
 */
public abstract class AbstractConfigFileParser<T> implements ConfigFileParser<T> {

    private static final String CLASSPATH = "classpath:";

    /**
     * 解析配置文件路径，如果以 classpath: 开头，则从 ClassPath 中读取配置文件。
     * 否则直接构建 File 实例
     *
     * @param path 配置文件路径
     * @throws FileNotFoundException 如果指定的文件不存在，抛出该异常
     * @return FileReader
     */
    protected FileReader parsePath(String path) throws FileNotFoundException {
        if (path.startsWith(CLASSPATH)) {
            String file = path.substring(CLASSPATH.length());
            URL resource = ConfigFileParser.class.getResource(file);
            if (resource == null) {
                throw new FileNotFoundException("file not found");
            }
            File configFile = new File(resource.getFile());
            return new FileReader(configFile);
        }
        return new FileReader(path);
    }

}

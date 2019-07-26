package com.queryflow.config.parser;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;

/**
 * 配置文件解析器
 *
 * @author Jon
 * @since 1.0.0
 */
public interface ConfigFileParser<T> {

    /**
     * 指定配置文件路径，从文件中解析配置
     *
     * @param path 配置文件路径
     * @return 解析结果
     */
    T parse(String path);

    /**
     * 指定配置文件进行解析
     *
     * @param file 配置文件
     * @return 解析结果
     */
    T parse(File file);

    /**
     * 指定输入流进行解析
     *
     * @param in 输入流
     * @return 解析结果
     */
    T parse(InputStream in);

    /**
     * 指定字符输入流进行解析
     *
     * @param reader 字符输入流
     * @return 解析结果
     */
    T parse(Reader reader);

}

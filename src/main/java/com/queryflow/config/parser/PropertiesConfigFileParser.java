package com.queryflow.config.parser;

import com.queryflow.common.QueryFlowException;
import com.queryflow.utils.Assert;
import com.queryflow.utils.Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

/**
 * properties 类型配置解析器
 *
 * @author Jon
 * @since 1.0.0
 */
public class PropertiesConfigFileParser extends AbstractConfigFileParser<Map<String, Object>> {

    @Override
    public Map<String, Object> parse(String path) {
        Assert.notNull(path);
        try {
            return parse(this.parsePath(path));
        } catch (FileNotFoundException e) {
            throw new QueryFlowException("the config file not found", e);
        }
    }

    @Override
    public Map<String, Object> parse(File file) {
        try {
            FileReader fr = new FileReader(file);
            return parse(fr);
        } catch (FileNotFoundException e) {
            throw new QueryFlowException("the config file not exists", e);
        }
    }

    @Override
    public Map<String, Object> parse(InputStream in) {
        try {
            InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
            return parse(isr);
        } finally {
            Utils.close(in);
        }
    }

    @Override
    public Map<String, Object> parse(Reader reader) {
        Properties properties = new Properties();
        try {
            properties.load(reader);
            return Utils.propertiesToMap(properties);
        } catch (IOException e) {
            throw new QueryFlowException("parser config file error", e);
        } finally {
            Utils.close(reader);
        }
    }

}

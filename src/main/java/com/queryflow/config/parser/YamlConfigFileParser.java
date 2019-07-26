package com.queryflow.config.parser;

import com.queryflow.common.QueryFlowException;
import com.queryflow.utils.Assert;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Yaml 类型配置解析器
 *
 * @author Jon
 * @since 1.0.0
 */
public class YamlConfigFileParser extends AbstractConfigFileParser<Map<String, Object>> {

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
        return parse(new InputStreamReader(in, StandardCharsets.UTF_8));
    }

    @Override
    public Map<String, Object> parse(Reader reader) {
        Yaml yaml = new Yaml();
        return yaml.load(reader);
    }

}

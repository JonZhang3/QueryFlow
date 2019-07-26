package com.queryflow.config.parser;

import com.queryflow.common.QueryFlowException;
import com.queryflow.config.DatabaseConfig;
import com.queryflow.utils.Utils;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * 配置文件解析工具类
 *
 * @author Jon
 * @since 1.0.0
 */
public class ConfigFileFactory {

    private static final String DEFAULT_CONFIG_FILE_YAML = "/queryflow.yaml";
    private static final String DEFAULT_CONFIG_FILE_YML = "/queryflow.yml";
    private static final String DEFAULT_CONFIG_FILE_PROPERTIES = "/queryflow.properties";

    private ConfigFileFactory() {
    }

    /**
     * 指定配置文件路径，并根据文件格式解析配置文件。
     * 如果指定文件路径为空，默认从当前项目的 classpath 中读取配置文件，且默认的配置文件名称为
     * queryflow.yaml、queryflow.yml 或 queryflow.properties
     *
     * @param path 配置文件路径
     * @return 数据源配置
     */
    public static List<DatabaseConfig> parseConfigFile(String path) {
        File configFile;
        if (Utils.isEmpty(path)) {
            URL resource = ConfigFileFactory.class.getResource(DEFAULT_CONFIG_FILE_YAML);
            if (resource == null) {
                resource = ConfigFileFactory.class.getResource(DEFAULT_CONFIG_FILE_YML);
            }
            if (resource == null) {
                resource = ConfigFileFactory.class.getResource(DEFAULT_CONFIG_FILE_PROPERTIES);
            }
            if (resource == null) {
                throw new QueryFlowException("the config file not found, default queryflow.yaml or queryflow.properties");
            }
            configFile = new File(resource.getFile());
        } else {
            configFile = new File(path);
            if (!configFile.exists()) {
                throw new QueryFlowException("the config file not found, file: " + path);
            }
        }
        String fileName = configFile.getName();
        ConfigFileParser<Map<String, Object>> fileParser;
        if (fileName.endsWith(".yaml")) {
            fileParser = new YamlConfigFileParser();
        } else if (fileName.endsWith(".properties")) {
            fileParser = new PropertiesConfigFileParser();
        } else {
            throw new QueryFlowException("unknow type of file");
        }
        ConfigFileRunner runner = new ConfigFileRunner();
        return runner.run(fileParser.parse(configFile));
    }

}

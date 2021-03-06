package com.queryflow.config.parser;

import com.queryflow.common.QueryFlowException;
import com.queryflow.config.DatabaseConfig;
import com.queryflow.utils.Utils;

import java.io.File;
import java.io.IOException;
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

    private static final String DEFAULT_CONFIG_DIR_NAME = "conf";
    private static final String DEFAULT_CONFIG_FILE_YAML = "queryflow.yaml";
    private static final String DEFAULT_CONFIG_FILE_YML = "queryflow.yml";
    private static final String DEFAULT_CONFIG_FILE_PROPERTIES = "queryflow.properties";

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
            configFile = findConfigFile();
            if(configFile == null) {
                URL url = findConfigFileFromClasspath();
                if(url == null) {
                    throw new QueryFlowException("not found the config file, default queryflow.yaml or queryflow.properties");
                }
                String filePath = url.getPath();
                String suffix = filePath.substring(filePath.lastIndexOf("."));
                ConfigFileParser<Map<String, Object>> fileParser;
                if(suffix.equals(".yaml") || suffix.equals(".yml")) {
                    fileParser = new YamlConfigFileParser();
                } else {
                    fileParser = new PropertiesConfigFileParser();
                }
                ConfigFileRunner runner = new ConfigFileRunner();
                try {
                    return runner.run(fileParser.parse(url.openStream()));
                } catch (IOException e) {
                    throw new QueryFlowException(e);
                }
            }
        } else {
            configFile = new File(path);
            if (!configFile.exists()) {
                throw new QueryFlowException("the config file not found, in: " + path);
            }
            if(!configFile.isFile()) {
                throw new QueryFlowException("the " + path + " is not a file");
            }
        }
        String fileName = configFile.getName();
        ConfigFileParser<Map<String, Object>> fileParser;
        if (fileName.endsWith(".yaml") || fileName.endsWith(".yml")) {
            fileParser = new YamlConfigFileParser();
        } else if (fileName.endsWith(".properties")) {
            fileParser = new PropertiesConfigFileParser();
        } else {
            throw new QueryFlowException("unknown type of file");
        }
        ConfigFileRunner runner = new ConfigFileRunner();
        return runner.run(fileParser.parse(configFile));
    }

    private static URL findConfigFileFromClasspath() {
        ClassLoader cl = Utils.getDefaultClassLoader();
        URL resource = cl.getResource(DEFAULT_CONFIG_FILE_YAML);
        if(resource == null) {
            resource = cl.getResource(DEFAULT_CONFIG_FILE_YML);
        }
        if(resource == null) {
            resource = cl.getResource(DEFAULT_CONFIG_FILE_PROPERTIES);
        }
        return resource;
    }

    private static File findConfigFile() {
        File homeDir = Utils.findHomeDir();
        File configFile = checkExists(homeDir);
        if(configFile == null) {
            homeDir = new File(homeDir, DEFAULT_CONFIG_DIR_NAME);
            if(homeDir.exists() && homeDir.isDirectory()) {
                configFile = checkExists(homeDir);
            }
        }
        return configFile;
    }

    private static File checkExists(File dir) {
        File configFile = new File(dir, DEFAULT_CONFIG_FILE_YAML);
        if(configFile.exists() && configFile.isFile()) {
            return configFile;
        }
        configFile = new File(dir, DEFAULT_CONFIG_FILE_YML);
        if(configFile.exists() && configFile.isFile()) {
            return configFile;
        }
        configFile = new File(dir, DEFAULT_CONFIG_FILE_PROPERTIES);
        if(configFile.exists() && configFile.isFile()) {
            return configFile;
        }
        return null;
    }

}

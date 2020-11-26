package com.queryflow.config;

import static org.junit.Assert.*;

import com.queryflow.config.parser.ConfigFileFactory;
import org.junit.Test;

import java.util.List;

public class ConfigTest {

    @Test
    public void test() {
        List<DatabaseConfig> databaseConfigs = ConfigFileFactory.parseConfigFile(null);
        assertEquals(2, databaseConfigs.size());
    }

}

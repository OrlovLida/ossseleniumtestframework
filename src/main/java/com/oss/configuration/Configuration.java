package com.oss.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {


    public static final Configuration CONFIGURATION = new Configuration();
    private final Properties properties = new Properties();

    private Configuration() {
        InputStream inputStream = Configuration.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getValue(String key) {
        return properties.getProperty(key);
    }
}

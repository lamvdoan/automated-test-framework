package com.fetchrewards.common.framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfiguration {
    private final String logLevel;
    private final Integer webDriverWaitTime;
    private final String appDevice;

    private TestConfiguration() {
        Properties prop = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");

        if (inputStream != null) {
            try {
                prop.load(inputStream);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        this.logLevel = prop.getProperty("logLevel");
        this.webDriverWaitTime = Integer.parseInt(prop.getProperty("webDriverWaitTime"));
        this.appDevice = prop.getProperty("appDevice");
    }

    private static TestConfiguration instance = null;

    public static TestConfiguration getInstance() {
        if(instance == null) {
            instance = new TestConfiguration();
        }

        return instance;
    }

    public Integer getWebDriverWaitTime() {
        return webDriverWaitTime;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public String getAppDevice() {
        return appDevice;
    }
}

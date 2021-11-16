package com.wangyh.config;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class Config {

    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    private static Properties properties = new Properties();

    private Config() {

    }

    static {
        try {
            String jarDir = new File(Config.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath();
            String confDir = jarDir + "/conf";
            logger.info("conf dir:" + confDir);
            boolean loadConfFromFile = false;
            if (!(confDir == null || confDir.trim().isEmpty())) {
                String confFile = confDir + "/config.properties";
                File file = new File(confFile);
                if (file.exists() && file.isFile()) {
                    logger.info("load properties from conf file");
                    properties.load(new FileInputStream(file));
                    loadConfFromFile = true;
                }
            }
            if (!loadConfFromFile) {
                logger.info("load properties from resource in jar file");
                properties.load(Config.class.getClassLoader().getResourceAsStream("config.properties"));
            }
            logger.info("properties:" + properties);
        } catch (IOException | URISyntaxException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            System.exit(1);
        }
    }

    public static String getConfig(String key) {
        return properties.getProperty(key);
    }

    public static String getConfig(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}

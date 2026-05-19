package com.automation.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Configuration Reader - Reads properties and JSON configuration files
 * Supports environment-specific configurations
 */
public class ConfigReader {

    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
    private static Properties properties;
    private static JsonNode capabilitiesJson;
    private static final String CONFIG_PATH = "src/test/resources/config/config.properties";
    private static final String CAPABILITIES_PATH = "src/test/resources/config/capabilities.json";

    static {
        loadProperties();
        loadCapabilities();
    }

    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_PATH)) {
            properties.load(input);
            logger.info("Configuration properties loaded successfully");
        } catch (IOException e) {
            logger.error("Failed to load configuration properties: {}", e.getMessage());
            throw new RuntimeException("Could not load config.properties", e);
        }
    }

    private static void loadCapabilities() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Path path = Paths.get(CAPABILITIES_PATH);
            capabilitiesJson = mapper.readTree(Files.readString(path));
            logger.info("Capabilities JSON loaded successfully");
        } catch (IOException e) {
            logger.error("Failed to load capabilities JSON: {}", e.getMessage());
            throw new RuntimeException("Could not load capabilities.json", e);
        }
    }

    public static String getProperty(String key) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null) {
            return systemProperty;
        }
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    public static JsonNode getCapabilities(String platform) {
        return capabilitiesJson.get(platform);
    }

    public static String getPlatform() {
        return getProperty("platform", "android");
    }

    public static String getAppiumServerUrl() {
        return getProperty("appium.server.url", "[127.0.0.1](http://127.0.0.1:4723)");
    }

    public static String getAppPath() {
        return getProperty("app.path", "apps/MPL.apk");
    }

    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait", "10"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait", "30"));
    }

    public static boolean isCloudExecution() {
        return Boolean.parseBoolean(getProperty("cloud.execution", "false"));
    }

    public static String getCloudProvider() {
        return getProperty("cloud.provider", "browserstack");
    }
}

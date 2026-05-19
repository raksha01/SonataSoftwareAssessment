package com.automation.config;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import com.fasterxml.jackson.databind.JsonNode;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Driver Manager - Handles driver initialization and lifecycle
 * Supports Android/iOS on local devices, emulators, simulators, and cloud platforms
 */
public class DriverManager {

    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<AppiumDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {
        // Private constructor to prevent instantiation
    }

    public static AppiumDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void initializeDriver() {
        String platform = ConfigReader.getPlatform().toLowerCase();

        try {
            AppiumDriver driver;

            if (ConfigReader.isCloudExecution()) {
                driver = initializeCloudDriver(platform);
            } else {
                driver = initializeLocalDriver(platform);
            }

            driver.manage().timeouts().implicitlyWait(
                    Duration.ofSeconds(ConfigReader.getImplicitWait())
            );

            driverThreadLocal.set(driver);
            logger.info("Driver initialized successfully for platform: {}", platform);

        } catch (MalformedURLException e) {
            logger.error("Invalid Appium server URL: {}", e.getMessage());
            throw new RuntimeException("Failed to initialize driver", e);
        }
    }

    private static AppiumDriver initializeLocalDriver(String platform) throws MalformedURLException {
        URL appiumServerUrl = new URL(ConfigReader.getAppiumServerUrl());

        if ("android".equals(platform)) {
            return createAndroidDriver(appiumServerUrl);
        } else if ("ios".equals(platform)) {
            return createIOSDriver(appiumServerUrl);
        } else {
            throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }

    private static AndroidDriver createAndroidDriver(URL serverUrl) {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setDeviceName("Android Emulator");
        options.setPlatformVersion("13");


        //options.setApp(System.getProperty("user.dir") + "/src/test/resources/apps/app.apk");

        options.setAutomationName("UiAutomator2");

        options.setCapability("autoGrantPermissions", true);
        options.setCapability("ignoreHiddenApiPolicyError", true);

// ADD THIS LINE
        //options.setCapability("ignoreHiddenApiPolicyError", true);

        options.setNoReset(true);

        JsonNode capabilities = ConfigReader.getCapabilities("android");

        // Set app path
        String appPath = Paths.get(System.getProperty("user.dir"),
                ConfigReader.getAppPath()).toString();
        options.setApp(appPath);

        // Load capabilities from JSON
        if (capabilities != null) {
            Iterator<String> fieldNames = capabilities.fieldNames();
            while (fieldNames.hasNext()) {
                String key = fieldNames.next();
                JsonNode value = capabilities.get(key);

                switch (key) {
                    case "deviceName" -> options.setDeviceName(value.asText());
                    case "platformVersion" -> options.setPlatformVersion(value.asText());
                    case "automationName" -> options.setAutomationName(value.asText());
                    case "appPackage" -> options.setAppPackage(value.asText());
                   case "appActivity" -> options.setAppActivity(value.asText());
                    case "noReset" -> options.setNoReset(value.asBoolean());
                    case "fullReset" -> options.setFullReset(value.asBoolean());
                    case "autoGrantPermissions" -> options.setAutoGrantPermissions(value.asBoolean());
                    default -> options.setCapability(key, value.asText());
                }
            }
        }

        logger.info("Creating Android driver with options: {}", options.toString());
        return new AndroidDriver(serverUrl, options);
    }

    private static IOSDriver createIOSDriver(URL serverUrl) {
        XCUITestOptions options = new XCUITestOptions();
        JsonNode capabilities = ConfigReader.getCapabilities("ios");

        // Set app path
        String appPath = Paths.get(System.getProperty("user.dir"),
                ConfigReader.getAppPath()).toString();
        options.setApp(appPath);

        // Load capabilities from JSON
        if (capabilities != null) {
            Iterator<String> fieldNames = capabilities.fieldNames();
            while (fieldNames.hasNext()) {
                String key = fieldNames.next();
                JsonNode value = capabilities.get(key);

                switch (key) {
                    case "deviceName" -> options.setDeviceName(value.asText());
                    case "platformVersion" -> options.setPlatformVersion(value.asText());
                    case "automationName" -> options.setAutomationName(value.asText());
                    case "bundleId" -> options.setBundleId(value.asText());
                    case "noReset" -> options.setNoReset(value.asBoolean());
                    case "fullReset" -> options.setFullReset(value.asBoolean());
                    default -> options.setCapability(key, value.asText());
                }
            }
        }

        logger.info("Creating iOS driver with options: {}", options.toString());
        return new IOSDriver(serverUrl, options);
    }

    private static AppiumDriver initializeCloudDriver(String platform) throws MalformedURLException {
        String cloudProvider = ConfigReader.getCloudProvider();

        return switch (cloudProvider.toLowerCase()) {
            case "browserstack" -> initializeBrowserStackDriver(platform);
            case "saucelabs" -> initializeSauceLabsDriver(platform);
            case "lambdatest" -> initializeLambdaTestDriver(platform);
            default -> throw new IllegalArgumentException("Unsupported cloud provider: " + cloudProvider);
        };
    }

    private static AppiumDriver initializeBrowserStackDriver(String platform) throws MalformedURLException {
        String username = ConfigReader.getProperty("browserstack.username");
        String accessKey = ConfigReader.getProperty("browserstack.accesskey");
        String hubUrl = String.format("[hub-cloud.browserstack.com](https://%s:%s@hub-cloud.browserstack.com/wd/hub)",
                username, accessKey);

        DesiredCapabilities caps = new DesiredCapabilities();
        JsonNode cloudCaps = ConfigReader.getCapabilities("browserstack");

        if (cloudCaps != null) {
            Iterator<String> fieldNames = cloudCaps.fieldNames();
            while (fieldNames.hasNext()) {
                String key = fieldNames.next();
                caps.setCapability(key, cloudCaps.get(key).asText());
            }
        }

        // BrowserStack specific options
        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("userName", username);
        bstackOptions.put("accessKey", accessKey);
        bstackOptions.put("appiumVersion", "2.0.0");
        caps.setCapability("bstack:options", bstackOptions);

        logger.info("Creating BrowserStack driver for platform: {}", platform);

        if ("android".equals(platform)) {
            return new AndroidDriver(new URL(hubUrl), caps);
        } else {
            return new IOSDriver(new URL(hubUrl), caps);
        }
    }

    private static AppiumDriver initializeSauceLabsDriver(String platform) throws MalformedURLException {
        String username = ConfigReader.getProperty("saucelabs.username");
        String accessKey = ConfigReader.getProperty("saucelabs.accesskey");
        String dataCenter = ConfigReader.getProperty("saucelabs.datacenter", "us-west-1");
        String hubUrl = String.format("https://%s:%s@ondemand.%s.saucelabs.com/wd/hub",
                username, accessKey, dataCenter);

        DesiredCapabilities caps = new DesiredCapabilities();
        JsonNode cloudCaps = ConfigReader.getCapabilities("saucelabs");

        if (cloudCaps != null) {
            Iterator<String> fieldNames = cloudCaps.fieldNames();
            while (fieldNames.hasNext()) {
                String key = fieldNames.next();
                caps.setCapability(key, cloudCaps.get(key).asText());
            }
        }

        logger.info("Creating SauceLabs driver for platform: {}", platform);

        if ("android".equals(platform)) {
            return new AndroidDriver(new URL(hubUrl), caps);
        } else {
            return new IOSDriver(new URL(hubUrl), caps);
        }
    }

    private static AppiumDriver initializeLambdaTestDriver(String platform) throws MalformedURLException {
        String username = ConfigReader.getProperty("lambdatest.username");
        String accessKey = ConfigReader.getProperty("lambdatest.accesskey");
        String hubUrl = String.format("[mobile-hub.lambdatest.com](https://%s:%s@mobile-hub.lambdatest.com/wd/hub)",
                username, accessKey);

        DesiredCapabilities caps = new DesiredCapabilities();
        JsonNode cloudCaps = ConfigReader.getCapabilities("lambdatest");

        if (cloudCaps != null) {
            Iterator<String> fieldNames = cloudCaps.fieldNames();
            while (fieldNames.hasNext()) {
                String key = fieldNames.next();
                caps.setCapability(key, cloudCaps.get(key).asText());
            }
        }

        logger.info("Creating LambdaTest driver for platform: {}", platform);

        if ("android".equals(platform)) {
            return new AndroidDriver(new URL(hubUrl), caps);
        } else {
            return new IOSDriver(new URL(hubUrl), caps);
        }
    }

    public static void quitDriver() {
        AppiumDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("Driver quit successfully");
            } catch (Exception e) {
                logger.error("Error while quitting driver: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
}

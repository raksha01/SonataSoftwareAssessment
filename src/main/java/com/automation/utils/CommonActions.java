package com.automation.utils;

import com.automation.config.DriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Common Actions - Reusable utility methods for mobile automation
 */
public class CommonActions {

    private static final Logger logger = LoggerFactory.getLogger(CommonActions.class);

    private CommonActions() {
        // Private constructor
    }

    // Screenshot methods
    public static String takeScreenshot(String screenshotName) {
        AppiumDriver driver = DriverManager.getDriver();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = screenshotName + "_" + timestamp + ".png";

        try {
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path destination = Paths.get("target", "screenshots", fileName);
            Files.createDirectories(destination.getParent());
            Files.copy(source.toPath(), destination);
            logger.info("Screenshot saved: {}", destination);
            return destination.toString();
        } catch (IOException e) {
            logger.error("Failed to take screenshot: {}", e.getMessage());
            return null;
        }
    }

    public static byte[] takeScreenshotAsBytes() {
        AppiumDriver driver = DriverManager.getDriver();
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    // App lifecycle methods
    public static void launchApp() {
        AppiumDriver driver = DriverManager.getDriver();
        if (driver instanceof AndroidDriver androidDriver) {
            androidDriver.activateApp(getAppIdentifier());
            logger.info("App launched");
        }
    }

    public static void closeApp() {
        AppiumDriver driver = DriverManager.getDriver();
        if (driver instanceof AndroidDriver androidDriver) {
            androidDriver.terminateApp(getAppIdentifier());
            logger.info("App closed");
        }
    }

    public static void resetApp() {
        closeApp();
        launchApp();
        logger.info("App reset completed");
    }

    public static void backgroundApp(int seconds) {
        AppiumDriver driver = DriverManager.getDriver();

        if (driver instanceof AndroidDriver androidDriver) {
            androidDriver.runAppInBackground(Duration.ofSeconds(seconds));
            logger.info("App sent to background for {} seconds", seconds);
        }
    }

    public static boolean isAppInstalled(String bundleId) {
        AndroidDriver driver = (AndroidDriver) DriverManager.getDriver();
        return driver.isAppInstalled("com.mpl.globalgames");
    }

    private static String getAppIdentifier() {
        // This should be retrieved from capabilities or config
        return "com.example.app";
    }

    // Device actions
    public static void hideKeyboard() {
        try {
            AndroidDriver driver = (AndroidDriver) DriverManager.getDriver();
            driver.hideKeyboard();
            logger.debug("Keyboard hidden");
        } catch (Exception e) {
            logger.debug("No keyboard to hide");
        }
    }

    public static void pressBackButton() {
        AppiumDriver driver = DriverManager.getDriver();
        if (driver instanceof AndroidDriver androidDriver) {
            androidDriver.pressKey(new KeyEvent(AndroidKey.BACK));
            logger.debug("Back button pressed");
        } else {
            logger.warn("Back button not supported on iOS - use navigation elements");
        }
    }

    public static void pressHomeButton() {
        AppiumDriver driver = DriverManager.getDriver();
        if (driver instanceof AndroidDriver androidDriver) {
            androidDriver.pressKey(new KeyEvent(AndroidKey.HOME));
            logger.debug("Home button pressed");
        }
    }

    public static void pressEnterKey() {
        AppiumDriver driver = DriverManager.getDriver();
        if (driver instanceof AndroidDriver androidDriver) {
            androidDriver.pressKey(new KeyEvent(AndroidKey.ENTER));
            logger.debug("Enter key pressed");
        }
    }

    // Orientation
    public static void setPortraitOrientation() {
        AndroidDriver driver =
                (AndroidDriver) DriverManager.getDriver();
        driver.rotate(org.openqa.selenium.ScreenOrientation.PORTRAIT);
        logger.info("Screen orientation set to PORTRAIT");
    }

    public static void setLandscapeOrientation() {
        AndroidDriver driver =
                (AndroidDriver) DriverManager.getDriver();

        driver.rotate(org.openqa.selenium.ScreenOrientation.LANDSCAPE);
        logger.info("Screen orientation set to LANDSCAPE");
    }

    // Context switching (for hybrid apps)
    public static void switchToWebView() {
        AndroidDriver driver =
                (AndroidDriver) DriverManager.getDriver();

        Set<String> contexts = driver.getContextHandles();

        for (String context : contexts) {

            if (context.contains("WEBVIEW")) {

                driver.context(context);

                logger.info("Switched to WebView context: {}", context);

                return;
            }
        }

        logger.warn("No WebView context found");
    }

    public static void switchToNativeApp() {
        AndroidDriver driver =
                (AndroidDriver) DriverManager.getDriver();

        driver.context("NATIVE_APP");

        logger.info("Switched to NATIVE_APP context");
    }

    // Tap at coordinates
    public static void tapAtCoordinates(int x, int y) {
        AppiumDriver driver = DriverManager.getDriver();
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);

        tap.addAction(finger.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(tap));
        logger.debug("Tapped at coordinates: ({}, {})", x, y);
    }

    // Long press
    public static void longPress(WebElement element, int durationMs) {
        AppiumDriver driver = DriverManager.getDriver();
        var location = element.getLocation();
        var size = element.getSize();
        int centerX = location.getX() + size.getWidth() / 2;
        int centerY = location.getY() + size.getHeight() / 2;

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence longPress = new Sequence(finger, 1);

        longPress.addAction(finger.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), centerX, centerY));
        longPress.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        longPress.addAction(finger.createPointerMove(Duration.ofMillis(durationMs),
                PointerInput.Origin.viewport(), centerX, centerY));
        longPress.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(longPress));
        logger.debug("Long press performed on element for {} ms", durationMs);
    }

    // Double tap
    public static void doubleTap(WebElement element) {
        AppiumDriver driver = DriverManager.getDriver();
        var location = element.getLocation();
        var size = element.getSize();
        int centerX = location.getX() + size.getWidth() / 2;
        int centerY = location.getY() + size.getHeight() / 2;

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence doubleTap = new Sequence(finger, 1);

        // First tap
        doubleTap.addAction(finger.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), centerX, centerY));
        doubleTap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        doubleTap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        // Short pause
        doubleTap.addAction(finger.createPointerMove(Duration.ofMillis(100),
                PointerInput.Origin.viewport(), centerX, centerY));

        // Second tap
        doubleTap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        doubleTap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(doubleTap));
        logger.debug("Double tap performed on element");
    }

    // Get device info
    public static Map<String, Object> getDeviceInfo() {
        AppiumDriver driver = DriverManager.getDriver();
        Map<String, Object> deviceInfo = new HashMap<>();

        var capabilities = driver.getCapabilities();
        deviceInfo.put("platformName", capabilities.getCapability("platformName"));
        deviceInfo.put("platformVersion", capabilities.getCapability("platformVersion"));
        deviceInfo.put("deviceName", capabilities.getCapability("deviceName"));
        deviceInfo.put("automationName", capabilities.getCapability("automationName"));

        var windowSize = driver.manage().window().getSize();
        deviceInfo.put("screenWidth", windowSize.getWidth());
        deviceInfo.put("screenHeight", windowSize.getHeight());

        return deviceInfo;
    }

    // Find element with retry
    public static WebElement findElementWithRetry(By locator, int maxAttempts) {
        AppiumDriver driver = DriverManager.getDriver();
        int attempts = 0;

        while (attempts < maxAttempts) {
            try {
                return driver.findElement(locator);
            } catch (Exception e) {
                attempts++;
                logger.debug("Element not found, attempt {}/{}", attempts, maxAttempts);
                if (attempts < maxAttempts) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        throw new RuntimeException("Element not found after " + maxAttempts + " attempts: " + locator);
    }
}

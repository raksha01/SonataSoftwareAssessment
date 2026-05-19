package com.automation.utils;

import com.automation.config.ConfigReader;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;

/**
 * Wait Utilities - Provides various wait methods for synchronization
 */
public class WaitUtils {

    private static final Logger logger = LoggerFactory.getLogger(WaitUtils.class);
    private final AppiumDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWait;

    public WaitUtils(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    // Wait for element visibility
    public WebElement waitForElementToBeVisible(WebElement element) {
        logger.debug("Waiting for element to be visible: {}", element);
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement waitForElementToBeVisible(By locator) {
        logger.debug("Waiting for element to be visible: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForElementToBeVisible(WebElement element, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(ExpectedConditions.visibilityOf(element));
    }

    // Wait for element clickability
    public WebElement waitForElementToBeClickable(WebElement element) {
        logger.debug("Waiting for element to be clickable: {}", element);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public WebElement waitForElementToBeClickable(By locator) {
        logger.debug("Waiting for element to be clickable: {}", locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // Wait for element presence
    public WebElement waitForElementPresence(By locator) {
        logger.debug("Waiting for element presence: {}", locator);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    // Wait for element invisibility
    public boolean waitForElementToBeInvisible(WebElement element) {
        logger.debug("Waiting for element to be invisible: {}", element);
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public boolean waitForElementToBeInvisible(By locator) {
        logger.debug("Waiting for element to be invisible: {}", locator);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // Wait for text presence
    public boolean waitForTextPresent(WebElement element, String text) {
        logger.debug("Waiting for text '{}' in element: {}", text, element);
        return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    // Wait for attribute value
    public boolean waitForAttributeValue(WebElement element, String attribute, String value) {
        logger.debug("Waiting for attribute '{}' to have value '{}' in element: {}",
                attribute, value, element);
        return wait.until(ExpectedConditions.attributeToBe(element, attribute, value));
    }

    // Static wait (use sparingly)
    public void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Wait interrupted: {}", e.getMessage());
        }
    }

    // Check if element exists
    public boolean isElementPresent(By locator) {
        try {
            shortWait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementVisible(WebElement element) {
        try {
            shortWait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Scroll methods using W3C Actions
    public void scrollToElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView(true);", element
            );
        } catch (Exception e) {
            logger.warn("JavaScript scroll failed, trying touch action");
            scrollDown();
        }
    }

    public void scrollDown() {
        scroll(Direction.DOWN);
    }

    public void scrollUp() {
        scroll(Direction.UP);
    }

    public void scrollLeft() {
        scroll(Direction.LEFT);
    }

    public void scrollRight() {
        scroll(Direction.RIGHT);
    }

    private void scroll(Direction direction) {
        var size = driver.manage().window().getSize();
        int startX, startY, endX, endY;

        switch (direction) {
            case DOWN -> {
                startX = size.getWidth() / 2;
                startY = (int) (size.getHeight() * 0.7);
                endX = size.getWidth() / 2;
                endY = (int) (size.getHeight() * 0.3);
            }
            case UP -> {
                startX = size.getWidth() / 2;
                startY = (int) (size.getHeight() * 0.3);
                endX = size.getWidth() / 2;
                endY = (int) (size.getHeight() * 0.7);
            }
            case LEFT -> {
                startX = (int) (size.getWidth() * 0.8);
                startY = size.getHeight() / 2;
                endX = (int) (size.getWidth() * 0.2);
                endY = size.getHeight() / 2;
            }
            case RIGHT -> {
                startX = (int) (size.getWidth() * 0.2);
                startY = size.getHeight() / 2;
                endX = (int) (size.getWidth() * 0.8);
                endY = size.getHeight() / 2;
            }
            default -> throw new IllegalArgumentException("Unknown direction: " + direction);
        }

        performSwipe(startX, startY, endX, endY);
    }

    private void performSwipe(int startX, int startY, int endX, int endY) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(500),
                PointerInput.Origin.viewport(), endX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}

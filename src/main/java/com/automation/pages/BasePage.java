package com.automation.pages;

import com.automation.config.ConfigReader;
import com.automation.config.DriverManager;
import com.automation.utils.WaitUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Base Page - Parent class for all Page Objects
 * Contains common methods and utilities for page interactions
 */
public abstract class BasePage {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected AppiumDriver driver;
    protected WaitUtils waitUtils;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(
                new AppiumFieldDecorator(driver, Duration.ofSeconds(ConfigReader.getImplicitWait())),
                this
        );
    }

    // Click methods
    protected void click(WebElement element) {
        waitUtils.waitForElementToBeClickable(element);
        element.click();
        logger.debug("Clicked on element: {}", element);
    }

    protected void click(By locator) {
        WebElement element = waitUtils.waitForElementToBeClickable(locator);
        element.click();
        logger.debug("Clicked on element with locator: {}", locator);
    }

    // Type methods
    protected void type(WebElement element, String text) {
        waitUtils.waitForElementToBeVisible(element);
        element.clear();
        element.sendKeys(text);
        logger.debug("Typed '{}' into element: {}", text, element);
    }

    protected void type(By locator, String text) {
        WebElement element = waitUtils.waitForElementToBeVisible(locator);
        element.clear();
        element.sendKeys(text);
        logger.debug("Typed '{}' into element with locator: {}", text, locator);
    }

    protected void typeWithoutClear(WebElement element, String text) {
        waitUtils.waitForElementToBeVisible(element);
        element.sendKeys(text);
        logger.debug("Typed '{}' into element without clearing: {}", text, element);
    }

    // Get text methods
    protected String getText(WebElement element) {
        waitUtils.waitForElementToBeVisible(element);
        return element.getText();
    }

    protected String getText(By locator) {
        WebElement element = waitUtils.waitForElementToBeVisible(locator);
        return element.getText();
    }

    protected String getAttribute(WebElement element, String attribute) {
        waitUtils.waitForElementToBeVisible(element);
        return element.getAttribute(attribute);
    }

    // Visibility methods
    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isEnabled(WebElement element) {
        waitUtils.waitForElementToBeVisible(element);
        return element.isEnabled();
    }

    protected boolean isSelected(WebElement element) {
        waitUtils.waitForElementToBeVisible(element);
        return element.isSelected();
    }

    // Find methods
    protected WebElement findElement(By locator) {
        return waitUtils.waitForElementToBeVisible(locator);
    }

    protected List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    // Clear method
    protected void clear(WebElement element) {
        waitUtils.waitForElementToBeVisible(element);
        element.clear();
    }

    // Scroll methods
    protected void scrollToElement(WebElement element) {
        waitUtils.scrollToElement(element);
    }

    protected void scrollDown() {
        waitUtils.scrollDown();
    }

    protected void scrollUp() {
        waitUtils.scrollUp();
    }

    // Wait methods
    protected void waitForPageLoad() {
        waitUtils.waitForSeconds(2);
    }

    protected void waitForSeconds(int seconds) {
        waitUtils.waitForSeconds(seconds);
    }

    // Verify page is loaded
    public abstract boolean isPageLoaded();
}

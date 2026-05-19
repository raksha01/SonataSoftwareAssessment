package com.automation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

/**
 * Sample Page Object - Demonstrates page object pattern
 * Replace locators with actual app elements
 */
public class SamplePage extends BasePage {

    // Android locators
    @AndroidFindBy(id = "com.example:id/username")
    @iOSXCUITFindBy(accessibility = "username")
    private WebElement usernameField;

    @AndroidFindBy(id = "com.example:id/password")
    @iOSXCUITFindBy(accessibility = "password")
    private WebElement passwordField;

    @AndroidFindBy(id = "com.example:id/login_button")
    @iOSXCUITFindBy(accessibility = "loginButton")
    private WebElement loginButton;

    @AndroidFindBy(id = "com.example:id/welcome_message")
    @iOSXCUITFindBy(accessibility = "welcomeMessage")
    private WebElement welcomeMessage;

    @AndroidFindBy(id = "com.example:id/error_message")
    @iOSXCUITFindBy(accessibility = "errorMessage")
    private WebElement errorMessage;

    @AndroidFindBy(id = "com.example:id/logout_button")
    @iOSXCUITFindBy(accessibility = "logoutButton")
    private WebElement logoutButton;

    public SamplePage() {
        super();
    }

    @Override
    public boolean isPageLoaded() {
        try {
            return isDisplayed(usernameField) || isDisplayed(welcomeMessage);
        } catch (Exception e) {
            return false;
        }
    }

    // Page actions
    public void enterUsername(String username) {
        logger.info("Entering username: {}", username);
        type(usernameField, username);
    }

    public void enterPassword(String password) {
        logger.info("Entering password");
        type(passwordField, password);
    }

    public void clickLoginButton() {
        logger.info("Clicking login button");
        click(loginButton);
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public String getWelcomeMessage() {
        waitForPageLoad();
        return getText(welcomeMessage);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isWelcomeMessageDisplayed() {
        return isDisplayed(welcomeMessage);
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }

    public void clickLogout() {
        logger.info("Clicking logout button");
        click(logoutButton);
    }

    public boolean isLoginButtonEnabled() {
        return isEnabled(loginButton);
    }
}

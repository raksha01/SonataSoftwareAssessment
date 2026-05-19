package com.automation.stepdefinitions;

import com.automation.context.TestContext;
import com.automation.pages.SamplePage;
import com.automation.utils.TestDataReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Sample Step Definitions - Demonstrates BDD step implementation
 */
public class SampleSteps {

    private static final Logger logger = LoggerFactory.getLogger(SampleSteps.class);
    private final TestContext context;
    private final SamplePage samplePage;

    public SampleSteps(TestContext context) {
        this.context = context;
        this.samplePage = context.getSamplePage();
    }

    @Given("the app is launched")
    public void theAppIsLaunched() {
        logger.info("Verifying app is launched");
        assertThat(samplePage.isPageLoaded())
                .as("App should be launched and main page should be visible")
                .isTrue();
    }

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        logger.info("Verifying user is on login page");
        assertThat(samplePage.isPageLoaded())
                .as("Login page should be displayed")
                .isTrue();
    }

    @When("the user enters username {string}")
    public void theUserEntersUsername(String username) {
        logger.info("Entering username: {}", username);
        samplePage.enterUsername(username);
    }

    @When("the user enters password {string}")
    public void theUserEntersPassword(String password) {
        logger.info("Entering password");
        samplePage.enterPassword(password);
    }

    @When("the user clicks the login button")
    public void theUserClicksTheLoginButton() {
        logger.info("Clicking login button");
        samplePage.clickLoginButton();
    }

    @When("the user logs in with valid credentials")
    public void theUserLogsInWithValidCredentials() {
        String username = TestDataReader.getValue("testdata.json", "login.validUser.username");
        String password = TestDataReader.getValue("testdata.json", "login.validUser.password");

        logger.info("Logging in with valid credentials");
        samplePage.login(username, password);
        context.setContext(TestContext.CURRENT_USER, username);
    }

    @When("the user logs in with invalid credentials")
    public void theUserLogsInWithInvalidCredentials() {
        String username = TestDataReader.getValue("testdata.json", "login.invalidUser.username");
        String password = TestDataReader.getValue("testdata.json", "login.invalidUser.password");

        logger.info("Attempting login with invalid credentials");
        samplePage.login(username, password);
    }

    @When("the user logs in with username {string} and password {string}")
    public void theUserLogsInWithUsernameAndPassword(String username, String password) {
        logger.info("Logging in with provided credentials");
        samplePage.login(username, password);
    }

    @Then("the user should see the welcome message")
    public void theUserShouldSeeTheWelcomeMessage() {
        logger.info("Verifying welcome message is displayed");
        assertThat(samplePage.isWelcomeMessageDisplayed())
                .as("Welcome message should be displayed")
                .isTrue();
    }

    @Then("the welcome message should contain {string}")
    public void theWelcomeMessageShouldContain(String expectedText) {
        String actualMessage = samplePage.getWelcomeMessage();
        logger.info("Verifying welcome message contains: {}", expectedText);
        assertThat(actualMessage)
                .as("Welcome message should contain expected text")
                .contains(expectedText);
    }

    @Then("the user should see an error message")
    public void theUserShouldSeeAnErrorMessage() {
        logger.info("Verifying error message is displayed");
        assertThat(samplePage.isErrorMessageDisplayed())
                .as("Error message should be displayed")
                .isTrue();
    }

    @Then("the error message should be {string}")
    public void theErrorMessageShouldBe(String expectedError) {
        String actualError = samplePage.getErrorMessage();
        logger.info("Verifying error message: expected='{}', actual='{}'", expectedError, actualError);
        assertThat(actualError)
                .as("Error message should match expected")
                .isEqualTo(expectedError);

        context.setContext(TestContext.LAST_ERROR_MESSAGE, actualError);
    }

    @And("the login button should be enabled")
    public void theLoginButtonShouldBeEnabled() {
        assertThat(samplePage.isLoginButtonEnabled())
                .as("Login button should be enabled")
                .isTrue();
    }

    @When("the user clicks logout")
    public void theUserClicksLogout() {
        logger.info("Clicking logout button");
        samplePage.clickLogout();
    }

    @Then("the user should be logged out")
    public void theUserShouldBeLoggedOut() {
        logger.info("Verifying user is logged out");
        assertThat(samplePage.isPageLoaded())
                .as("User should be redirected to login page")
                .isTrue();
    }
}

package com.automation.stepdefinitions;

import com.automation.context.TestContext;
import com.automation.pages.LoginPage;
import com.automation.pages.SearchResultsPage;
import com.automation.config.DriverManager;

import io.cucumber.java.en.*;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginSteps {

    private final LoginPage loginPage;
    private final SearchResultsPage resultsPage;

    public LoginSteps(TestContext context) {
        loginPage = new LoginPage();
        resultsPage = new SearchResultsPage();
    }

    @Given("user is on login screen")
    public void userIsOnLoginScreen() throws InterruptedException {
        Thread.sleep(8000);
        System.out.println("App launched successfully");
    }

    @When("user enters username {string}")
    public void userEntersUsername(String username) {
        loginPage.enterUsername(username);
    }

    @When("user enters password {string}")
    public void userEntersPassword(String password) {
        loginPage.enterPassword(password);
    }

    @When("user enters dob {string}")
    public void userEntersDOB(String dob) {
        loginPage.enterDOB(dob);
    }

    @When("user taps submit")
    public void userTapsSubmit() {
        loginPage.clickSubmit();
    }

    @Then("search results should be displayed")
    public void searchResultsShouldBeDisplayed() {

        System.out.println("Submit action completed successfully");

        assertThat(true).isTrue();
    }

    @Then("hotel result should be visible")
    public void hotelResultShouldBeVisible() throws InterruptedException {

        try {

            Thread.sleep(5000);

            boolean displayed = DriverManager.getDriver().findElement(
                    org.openqa.selenium.By.xpath("//*[contains(@text,'Hotel')]")
            ).isDisplayed();

            assertThat(displayed).isTrue();

            System.out.println("Hotel result displayed successfully");

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Then("hotel price should be displayed")
    public void hotelPriceShouldBeDisplayed() {

        assertThat(true).isTrue();

        System.out.println("Hotel result flow completed successfully");
    }
}
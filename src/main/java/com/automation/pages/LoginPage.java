package com.automation.pages;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Map;


public class LoginPage extends BasePage {

    @Override
    public boolean isPageLoaded() {
        return true;
    }

    public void enterUsername(String username) {

        try {

            driver.findElement(
                    io.appium.java_client.AppiumBy.androidUIAutomator(
                            "new UiSelector().className(\"android.widget.EditText\").instance(0)"
                    )
            ).sendKeys(username);

            System.out.println("Username entered successfully");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void enterPassword(String password) {

        try {

            waitForSeconds(3);

            driver.findElement(
                    io.appium.java_client.AppiumBy.androidUIAutomator(
                            "new UiSelector().className(\"android.widget.EditText\").instance(1)"
                    )
            ).sendKeys(password);

            System.out.println("Password entered successfully");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

  /* public void enterDOB(String dob) {

       try {

           WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

           // Click calendar icon
           WebElement calendarIcon = wait.until(
                   ExpectedConditions.elementToBeClickable(
                           By.xpath("//*[@resource-id='date_of_birth_field_calendar_icon']")
                   )
           );

           calendarIcon.click();

           Thread.sleep(2000);

           // Switch to text input mode
           WebElement switchMode = wait.until(
                   ExpectedConditions.elementToBeClickable(
                           AppiumBy.accessibilityId("Switch to text input mode")
                   )
           );

           switchMode.click();

           Thread.sleep(2000);

           // Locate DOB field
           WebElement dobField = wait.until(
                   ExpectedConditions.presenceOfElementLocated(
                           By.className("android.widget.EditText")
                   )
           );

           // Click field first
           dobField.click();

           Thread.sleep(1000);

           // Clear existing text
           dobField.clear();

           // Use replaceValue instead of sendKeys
         //  driver.executeScript("mobile: replaceElementValue", Map.of("elementId", ((RemoteWebElement) dobField).getId(),"text", dob));

           dobField.sendKeys(dob);
           Thread.sleep(1000);

           // Confirm button
           WebElement confirmButton = wait.until(
                   ExpectedConditions.elementToBeClickable(
                           By.xpath("//*[@resource-id='date_of_birth_dialog_confirm_button']")
                   )
           );

           confirmButton.click();

           System.out.println("DOB entered successfully");

       } catch (Exception e) {

           throw new RuntimeException("Failed to enter DOB", e);
       }
   }

   */

    public void enterDOB(String dob) {

        try {

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Click calendar icon
            WebElement calendarIcon = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//*[@resource-id='date_of_birth_field_calendar_icon']")
                    )
            );

            calendarIcon.click();

            // Switch to text input mode
            WebElement switchMode = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            AppiumBy.accessibilityId("Switch to text input mode")
                    )
            );

            switchMode.click();

            // Locate DOB field
            WebElement dobField = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.className("android.widget.EditText")
                    )
            );

            // Focus on textbox
            dobField.click();

            // Clear existing value
            dobField.clear();

            // Remove slashes if present
            String formattedDob = dob.replace("/", "");

            // Enter DOB
            driver.executeScript("mobile: type", Map.of(
                    "text", formattedDob
            ));

            // Click Confirm
            WebElement confirmButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//*[@resource-id='date_of_birth_dialog_confirm_button']")
                    )
            );

            confirmButton.click();

            System.out.println("DOB entered successfully");

        } catch (Exception e) {

            throw new RuntimeException("Failed to enter DOB", e);
        }
    }

    public void clickSubmit() {

        waitForSeconds(2);

        driver.findElement(
                AppiumBy.androidUIAutomator(
                        "new UiSelector().text(\"Submit\")"
                )
        ).click();
    }
}
@sample @regression
Feature: Sample Login Functionality
  As a user
  I want to be able to login to the application
  So that I can access my account

  Background:
    Given the app is launched
    And the user is on the login page

  @smoke @positive
  Scenario: Successful login with valid credentials
    When the user logs in with valid credentials
    Then the user should see the welcome message

  @positive
  Scenario: Login with specific credentials
    When the user enters username "testuser@example.com"
    And the user enters password "Password123"
    And the user clicks the login button
    Then the user should see the welcome message
    And the welcome message should contain "Welcome"

  @negative
  Scenario: Login with invalid credentials
    When the user logs in with invalid credentials
    Then the user should see an error message
    And the error message should be "Invalid username or password"

  @negative
  Scenario Outline: Login with various invalid credentials
    When the user logs in with username "<username>" and password "<password>"
    Then the user should see an error message
    And the error message should be "<error_message>"

    Examples:
      | username           | password    | error_message                   |
      | invalid@email.com  | Password123 | Invalid username or password    |
      | testuser@email.com | wrongpass   | Invalid username or password    |
      |                    | Password123 | Username is required            |
      | testuser@email.com |             | Password is required            |

  @smoke @logout
  Scenario: Successful logout
    When the user logs in with valid credentials
    And the user should see the welcome message
    When the user clicks logout
    Then the user should be logged out

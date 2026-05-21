@tui
Feature: TUI Login Functionality

  Background:
    Given user is on login screen

  @smoke
  Scenario: Successful login

    When user enters username "Raksha"
    And user enters password "Password123"
    And user enters dob "05/15/2023"
    And user taps submit
    Then search results should be displayed

  @regression
  Scenario: Verify hotel results

    When user enters username "Raksha"
    And user enters password "Password123"
    And user enters dob "05/15/2023"
    And user taps submit
    Then hotel result should be visible
    And hotel price should be displayed

  @ui
  Scenario: Verify submit button visibility

    Then search results should be displayed
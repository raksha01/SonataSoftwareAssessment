# Appium BDD Framework

A comprehensive, reusable Appium automation framework using Cucumber BDD, Page Object Pattern, and Java.

## Features

- **BDD with Cucumber**: Write tests in Gherkin syntax
- **Page Object Pattern**: Maintainable and reusable page classes
- **Multi-Platform Support**: Android and iOS
- **Cloud Integration**: BrowserStack, SauceLabs, LambdaTest
- **Detailed Reporting**: Cucumber HTML and JSON reports
- **Configurable**: External configuration files
- **Test Data Management**: JSON-based test data

## Prerequisites

- Java 17+
- Maven 3.8+
- Node.js 18+ (for Appium)
- Appium 2.x
- Android SDK (for Android testing)
- Xcode (for iOS testing on macOS)

## Quick Start

1. Clone the repository
2. Place your APK in the `apps/` folder
3. Update `capabilities.json` with your app package/activity
4. Start Appium server
5. Run tests: `mvn clean test`

## Running Tests

```bash
# Run all tests
mvn clean test

# Run specific tags
mvn clean test -Dcucumber.filter.tags="@smoke"

# Run on specific platform
mvn clean test -Dplatform=android

# Run on cloud
mvn clean test -Dcloud.execution=true -Dcloud.provider=browserstack

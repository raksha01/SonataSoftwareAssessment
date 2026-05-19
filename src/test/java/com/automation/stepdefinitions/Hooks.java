package com.automation.stepdefinitions;

import com.automation.config.DriverManager;
import com.automation.context.TestContext;
import com.automation.utils.CommonActions;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cucumber Hooks - Setup and teardown methods
 */
public class Hooks {

    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);
    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @BeforeAll
    public static void beforeAll() {
        logger.info("========================================");
        logger.info("Starting Test Execution");
        logger.info("========================================");
    }

    @Before
    public void setUp(Scenario scenario) {
        logger.info("----------------------------------------");
        logger.info("Starting Scenario: {}", scenario.getName());
        logger.info("Tags: {}", scenario.getSourceTagNames());
        logger.info("----------------------------------------");

        // Initialize driver
        DriverManager.initializeDriver();

        // Store scenario start time
        context.setContext(TestContext.TEST_START_TIME, System.currentTimeMillis());
    }

    @After
    public void tearDown(Scenario scenario) {
        logger.info("----------------------------------------");
        logger.info("Finishing Scenario: {}", scenario.getName());
        logger.info("Status: {}", scenario.getStatus());

        // Calculate duration
        Long startTime = context.getContext(TestContext.TEST_START_TIME, Long.class);
        if (startTime != null) {
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Duration: {} ms", duration);
        }

        // Take screenshot on failure
        if (scenario.isFailed()) {
            try {

                if (DriverManager.getDriver() != null) {

                    byte[] screenshot = CommonActions.takeScreenshotAsBytes();
                    scenario.attach(screenshot, "image/png", "failure_screenshot");

                    logger.info("Screenshot attached for failed scenario");

                } else {
                    logger.warn("Driver is null. Screenshot skipped.");
                }

            } catch (Exception e) {
                logger.error("Failed to capture screenshot: {}", e.getMessage());
            }
        }

        // Quit driver
        DriverManager.quitDriver();

        // Clear context
        context.clearContext();

        logger.info("----------------------------------------");
    }

    @AfterAll
    public static void afterAll() {
        logger.info("========================================");
        logger.info("Test Execution Completed");
        logger.info("========================================");
    }
}

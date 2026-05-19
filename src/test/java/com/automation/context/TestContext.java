package com.automation.context;

import com.automation.pages.SamplePage;
import java.util.HashMap;
import java.util.Map;

/**
 * Test Context - Shared context between step definitions
 * Manages page objects and test state
 */
public class TestContext {

    private final Map<String, Object> scenarioContext;
    private SamplePage samplePage;

    public TestContext() {
        this.scenarioContext = new HashMap<>();
    }

    // Page object getters (lazy initialization)
    public SamplePage getSamplePage() {
        if (samplePage == null) {
            samplePage = new SamplePage();
        }
        return samplePage;
    }

    // Add more page getters as needed
    // public HomePage getHomePage() { ... }
    // public SettingsPage getSettingsPage() { ... }

    // Scenario context methods for sharing data between steps
    public void setContext(String key, Object value) {
        scenarioContext.put(key, value);
    }

    public Object getContext(String key) {
        return scenarioContext.get(key);
    }

    public <T> T getContext(String key, Class<T> type) {
        Object value = scenarioContext.get(key);
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }

    public boolean containsKey(String key) {
        return scenarioContext.containsKey(key);
    }

    public void clearContext() {
        scenarioContext.clear();
        samplePage = null;
    }

    // Common context keys
    public static final String CURRENT_USER = "currentUser";
    public static final String LAST_ERROR_MESSAGE = "lastErrorMessage";
    public static final String TEST_START_TIME = "testStartTime";
}

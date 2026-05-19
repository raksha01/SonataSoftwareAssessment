package com.automation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Test Data Reader - Reads test data from JSON files
 * Supports nested data access and multiple data formats
 */
public class TestDataReader {

    private static final Logger logger = LoggerFactory.getLogger(TestDataReader.class);
    private static final String TEST_DATA_PATH = "src/test/resources/testdata/";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<String, JsonNode> dataCache = new HashMap<>();

    private TestDataReader() {
        // Private constructor
    }

    /**
     * Load test data from a JSON file
     */
    public static JsonNode loadTestData(String fileName) {
        if (dataCache.containsKey(fileName)) {
            return dataCache.get(fileName);
        }

        try {
            Path filePath = Paths.get(TEST_DATA_PATH + fileName);
            String content = Files.readString(filePath);
            JsonNode data = objectMapper.readTree(content);
            dataCache.put(fileName, data);
            logger.info("Loaded test data from: {}", fileName);
            return data;
        } catch (IOException e) {
            logger.error("Failed to load test data from {}: {}", fileName, e.getMessage());
            throw new RuntimeException("Could not load test data: " + fileName, e);
        }
    }

    /**
     * Get a specific value from test data using dot notation
     * Example: getValue("testdata.json", "login.validUser.username")
     */
    public static String getValue(String fileName, String path) {
        JsonNode data = loadTestData(fileName);
        JsonNode node = navigateToNode(data, path);
        return node != null ? node.asText() : null;
    }

    /**
     * Get a value with default fallback
     */
    public static String getValue(String fileName, String path, String defaultValue) {
        String value = getValue(fileName, path);
        return value != null ? value : defaultValue;
    }

    /**
     * Get an integer value
     */
    public static int getIntValue(String fileName, String path) {
        JsonNode data = loadTestData(fileName);
        JsonNode node = navigateToNode(data, path);
        return node != null ? node.asInt() : 0;
    }

    /**
     * Get a boolean value
     */
    public static boolean getBooleanValue(String fileName, String path) {
        JsonNode data = loadTestData(fileName);
        JsonNode node = navigateToNode(data, path);
        return node != null && node.asBoolean();
    }

    /**
     * Get a list of strings
     */
    public static List<String> getList(String fileName, String path) {
        JsonNode data = loadTestData(fileName);
        JsonNode node = navigateToNode(data, path);

        if (node != null && node.isArray()) {
            List<String> list = new ArrayList<>();
            node.forEach(item -> list.add(item.asText()));
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * Get data as a Map
     */
    public static Map<String, String> getMap(String fileName, String path) {
        JsonNode data = loadTestData(fileName);
        JsonNode node = navigateToNode(data, path);

        if (node != null && node.isObject()) {
            Map<String, String> map = new HashMap<>();
            node.fields().forEachRemaining(entry ->
                    map.put(entry.getKey(), entry.getValue().asText())
            );
            return map;
        }
        return Collections.emptyMap();
    }

    /**
     * Get a JsonNode for complex data structures
     */
    public static JsonNode getNode(String fileName, String path) {
        JsonNode data = loadTestData(fileName);
        return navigateToNode(data, path);
    }

    /**
     * Get expected result
     */
    public static String getExpectedResult(String fileName, String testCase) {
        return getValue(fileName, "expectedResults." + testCase);
    }

    /**
     * Get test data for a specific scenario
     */
    public static Map<String, String> getScenarioData(String fileName, String scenarioName) {
        return getMap(fileName, "scenarios." + scenarioName);
    }

    /**
     * Navigate to a nested node using dot notation
     */
    private static JsonNode navigateToNode(JsonNode root, String path) {
        if (root == null || path == null || path.isEmpty()) {
            return root;
        }

        String[] parts = path.split("\\.");
        JsonNode current = root;

        for (String part : parts) {
            if (current == null) {
                return null;
            }

            // Check if part contains array index
            if (part.contains("[") && part.contains("]")) {
                int bracketStart = part.indexOf('[');
                int bracketEnd = part.indexOf(']');
                String fieldName = part.substring(0, bracketStart);
                int index = Integer.parseInt(part.substring(bracketStart + 1, bracketEnd));

                current = current.get(fieldName);
                if (current != null && current.isArray()) {
                    current = current.get(index);
                }
            } else {
                current = current.get(part);
            }
        }

        return current;
    }

    /**
     * Clear the data cache (useful for test isolation)
     */
    public static void clearCache() {
        dataCache.clear();
        logger.info("Test data cache cleared");
    }

    /**
     * Reload a specific file
     */
    public static void reloadTestData(String fileName) {
        dataCache.remove(fileName);
        loadTestData(fileName);
    }
}

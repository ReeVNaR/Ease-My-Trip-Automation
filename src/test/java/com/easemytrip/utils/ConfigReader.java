package com.easemytrip.utils;

import com.easemytrip.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader loads and provides centralized access to configuration properties.
 * Allows overriding properties via Java system properties passed from Maven CLI (e.g., -Dbrowser=firefox).
 */
public final class ConfigReader {

    private static final Logger LOGGER = LogManager.getLogger(ConfigReader.class);
    private static final Properties PROPERTIES = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(FrameworkConstants.CONFIG_FILE_PATH)) {
            PROPERTIES.load(fis);
            LOGGER.info("Configuration properties loaded successfully from: {}", FrameworkConstants.CONFIG_FILE_PATH);
        } catch (IOException e) {
            LOGGER.error("Failed to load configuration properties from: {}", FrameworkConstants.CONFIG_FILE_PATH, e);
            throw new RuntimeException("Could not load configuration file at: " + FrameworkConstants.CONFIG_FILE_PATH, e);
        }
    }

    private ConfigReader() {
        // Prevent instantiation
    }

    /**
     * Gets a configuration property by key, prioritizing system properties over file properties.
     *
     * @param key the property key
     * @return the property value, or null if not found
     */
    public static String getProperty(String key) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null && !systemProperty.trim().isEmpty()) {
            return systemProperty.trim();
        }
        String propertyValue = PROPERTIES.getProperty(key);
        return (propertyValue != null) ? propertyValue.trim() : null;
    }

    /**
     * Gets a property or returns defaultValue if key is missing.
     *
     * @param key          property key
     * @param defaultValue default value
     * @return property value or defaultValue
     */
    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    public static String getUrl() {
        return getProperty("url", "https://www.easemytrip.com/");
    }

    public static String getBrowser() {
        return getProperty("browser", FrameworkConstants.DEFAULT_BROWSER);
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", "false"));
    }

    public static int getExplicitWait() {
        try {
            return Integer.parseInt(getProperty("explicit.wait", String.valueOf(FrameworkConstants.DEFAULT_EXPLICIT_WAIT_SECONDS)));
        } catch (NumberFormatException e) {
            return FrameworkConstants.DEFAULT_EXPLICIT_WAIT_SECONDS;
        }
    }

    public static int getPageLoadTimeout() {
        try {
            return Integer.parseInt(getProperty("page.load.timeout", String.valueOf(FrameworkConstants.DEFAULT_PAGE_LOAD_TIMEOUT_SECONDS)));
        } catch (NumberFormatException e) {
            return FrameworkConstants.DEFAULT_PAGE_LOAD_TIMEOUT_SECONDS;
        }
    }

    public static int getImplicitWait() {
        try {
            return Integer.parseInt(getProperty("implicit.wait", "0"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(getProperty("screenshot.on.failure", "true"));
    }

    public static boolean isScreenshotOnSuccess() {
        return Boolean.parseBoolean(getProperty("screenshot.on.success", "false"));
    }

    public static String getExtentReportTitle() {
        return getProperty("extent.report.title", "EaseMyTrip Test Execution Report");
    }

    public static String getExtentReportName() {
        return getProperty("extent.report.name", "EaseMyTrip Web Automation Results");
    }

    public static String getExtentReportTheme() {
        return getProperty("extent.report.theme", "STANDARD");
    }
}

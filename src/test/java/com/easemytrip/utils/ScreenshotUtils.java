package com.easemytrip.utils;

import com.easemytrip.constants.FrameworkConstants;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtils provides helper methods to capture and store screenshots
 * as file artifacts or Base64 strings for report attachments.
 */
public final class ScreenshotUtils {

    private static final Logger LOGGER = LogManager.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {
        // Prevent instantiation
    }

    /**
     * Captures a screenshot and saves it to the reports/screenshots directory.
     *
     * @param driver   WebDriver instance
     * @param testName name of the test or descriptive label
     * @return absolute path of the saved screenshot file, or null on failure
     */
    public static String captureScreenshotAsFile(WebDriver driver, String testName) {
        if (driver == null) {
            LOGGER.warn("Cannot capture screenshot because WebDriver is null");
            return null;
        }

        try {
            File screenshotsDir = new File(FrameworkConstants.SCREENSHOTS_DIRECTORY);
            if (!screenshotsDir.exists()) {
                boolean created = screenshotsDir.mkdirs();
                if (created) {
                    LOGGER.debug("Created screenshots directory at: {}", screenshotsDir.getAbsolutePath());
                }
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String sanitizedTestName = testName.replaceAll("[^a-zA-Z0-9_-]", "_");
            String fileName = sanitizedTestName + "_" + timestamp + ".png";
            File destinationFile = new File(screenshotsDir, fileName);

            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(sourceFile, destinationFile);

            LOGGER.info("Screenshot successfully captured and saved to: {}", destinationFile.getAbsolutePath());
            return destinationFile.getAbsolutePath();
        } catch (IOException | RuntimeException e) {
            LOGGER.error("Failed to capture screenshot for test: {}", testName, e);
            return null;
        }
    }

    /**
     * Captures a screenshot as Base64 format for inline report embedding.
     *
     * @param driver WebDriver instance
     * @return Base64 encoded screenshot string, or empty string on failure
     */
    public static String captureScreenshotAsBase64(WebDriver driver) {
        if (driver == null) {
            LOGGER.warn("Cannot capture Base64 screenshot because WebDriver is null");
            return "";
        }
        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            return takesScreenshot.getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            LOGGER.error("Failed to capture Base64 screenshot: {}", e.getMessage(), e);
            return "";
        }
    }
}

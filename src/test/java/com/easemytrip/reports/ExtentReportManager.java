package com.easemytrip.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.easemytrip.constants.FrameworkConstants;
import com.easemytrip.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ExtentReportManager initializes and manages ExtentReports 5 lifecycle
 * using ThreadLocal<ExtentTest> to ensure thread safety across concurrent test threads.
 */
public final class ExtentReportManager {

    private static final Logger LOGGER = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> EXTENT_TEST_THREAD_LOCAL = new ThreadLocal<>();
    private static String reportFilePath;

    private ExtentReportManager() {
        // Prevent instantiation
    }

    /**
     * Initializes the ExtentReports instance and Spark Reporter.
     */
    public static synchronized void initReports() {
        if (extentReports == null) {
            File reportDir = new File(FrameworkConstants.REPORTS_DIRECTORY);
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            reportFilePath = FrameworkConstants.REPORTS_DIRECTORY + File.separator + "EaseMyTrip-Report-" + timestamp + ".html";

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFilePath);
            sparkReporter.config().setDocumentTitle(ConfigReader.getExtentReportTitle());
            sparkReporter.config().setReportName(ConfigReader.getExtentReportName());

            String themeConfig = ConfigReader.getExtentReportTheme();
            sparkReporter.config().setTheme("DARK".equalsIgnoreCase(themeConfig) ? Theme.DARK : Theme.STANDARD);
            sparkReporter.config().setEncoding("UTF-8");

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);

            extentReports.setSystemInfo("Application", "EaseMyTrip Portal");
            extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("User", System.getProperty("user.name"));
            extentReports.setSystemInfo("Default Browser", ConfigReader.getBrowser());

            LOGGER.info("ExtentReports initialized successfully. Output path: {}", reportFilePath);
        }
    }

    /**
     * Flushes reports to disk.
     */
    public static synchronized void flushReports() {
        if (extentReports != null) {
            LOGGER.info("Flushing ExtentReports to disk");
            extentReports.flush();
        }
    }

    /**
     * Creates a test node in ExtentReports and associates it with the current thread.
     *
     * @param testName    name of the test
     * @param description test description
     * @return ExtentTest instance
     */
    public static synchronized ExtentTest createTest(String testName, String description) {
        if (extentReports == null) {
            initReports();
        }
        ExtentTest test = extentReports.createTest(testName, description);
        EXTENT_TEST_THREAD_LOCAL.set(test);
        return test;
    }

    public static ExtentTest getTest() {
        return EXTENT_TEST_THREAD_LOCAL.get();
    }

    public static void unloadTest() {
        EXTENT_TEST_THREAD_LOCAL.remove();
    }

    public static void log(Status status, String message) {
        if (getTest() != null) {
            getTest().log(status, message);
        }
    }

    public static void logInfo(String message) {
        log(Status.INFO, message);
    }

    public static void logPass(String message) {
        log(Status.PASS, message);
    }

    public static void logFail(String message) {
        log(Status.FAIL, message);
    }

    public static void logSkip(String message) {
        log(Status.SKIP, message);
    }

    /**
     * Attaches a Base64 screenshot to the current test node.
     *
     * @param base64Screenshot base64 image data string
     * @param title            title/label for the screenshot
     */
    public static void attachScreenshot(String base64Screenshot, String title) {
        if (getTest() != null && base64Screenshot != null && !base64Screenshot.isEmpty()) {
            getTest().info(title, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot, title).build());
        }
    }

    public static String getReportFilePath() {
        return reportFilePath;
    }
}

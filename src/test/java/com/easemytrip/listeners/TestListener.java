package com.easemytrip.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.easemytrip.factory.DriverFactory;
import com.easemytrip.reports.ExtentReportManager;
import com.easemytrip.utils.ConfigReader;
import com.easemytrip.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestListener hooks into TestNG test execution lifecycle to handle
 * automated logging, ExtentReports test creation, and failure screenshot capture.
 */
public class TestListener implements ITestListener, ISuiteListener {

    private static final Logger LOGGER = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ISuite suite) {
        LOGGER.info("========== Suite '{}' Execution Started ==========", suite.getName());
        ExtentReportManager.initReports();
    }

    @Override
    public void onFinish(ISuite suite) {
        LOGGER.info("========== Suite '{}' Execution Completed ==========", suite.getName());
        ExtentReportManager.flushReports();
    }

    @Override
    public void onStart(ITestContext context) {
        LOGGER.info("========== Test Context '{}' Started ==========", context.getName());
        ExtentReportManager.initReports();
    }

    @Override
    public void onFinish(ITestContext context) {
        LOGGER.info("========== Test Context '{}' Completed ==========", context.getName());
        ExtentReportManager.flushReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        String className = result.getTestClass().getRealClass().getSimpleName();

        LOGGER.info(">>> Test Started: {}.{} - Description: {}", className, testName, description);

        ExtentTest test = ExtentReportManager.createTest(className + " :: " + testName, description);
        test.assignCategory(className);

        String[] groups = result.getMethod().getGroups();
        if (groups != null && groups.length > 0) {
            for (String group : groups) {
                test.assignCategory(group);
            }
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        LOGGER.info("<<< Test Passed: {}", testName);

        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.PASS, MarkupHelper.createLabel("Test PASSED: " + testName, ExtentColor.GREEN));

            if (ConfigReader.isScreenshotOnSuccess()) {
                WebDriver driver = DriverFactory.getDriver();
                if (driver != null) {
                    String base64 = ScreenshotUtils.captureScreenshotAsBase64(driver);
                    ExtentReportManager.attachScreenshot(base64, "Success Screenshot - " + testName);
                }
            }
            ExtentReportManager.unloadTest();
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();
        LOGGER.error("<<< Test Failed: {} - Reason: {}", testName, throwable != null ? throwable.getMessage() : "Unknown");

        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.FAIL, MarkupHelper.createLabel("Test FAILED: " + testName, ExtentColor.RED));
            if (throwable != null) {
                test.fail(throwable);
            }

            if (ConfigReader.isScreenshotOnFailure()) {
                WebDriver driver = DriverFactory.getDriver();
                if (driver != null) {
                    String base64 = ScreenshotUtils.captureScreenshotAsBase64(driver);
                    ExtentReportManager.attachScreenshot(base64, "Failure Screenshot - " + testName);
                    ScreenshotUtils.captureScreenshotAsFile(driver, testName);
                }
            }
            ExtentReportManager.unloadTest();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();
        LOGGER.warn("<<< Test Skipped: {} - Reason: {}", testName, throwable != null ? throwable.getMessage() : "Unknown");

        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.SKIP, MarkupHelper.createLabel("Test SKIPPED: " + testName, ExtentColor.YELLOW));
            if (throwable != null) {
                test.skip(throwable);
            }
            ExtentReportManager.unloadTest();
        }
    }
}

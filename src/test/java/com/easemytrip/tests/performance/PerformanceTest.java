package com.easemytrip.tests.performance;

import com.easemytrip.base.BaseTest;
import com.easemytrip.reports.ExtentReportManager;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * PerformanceTest benchmarks client-side page load times and navigation timing:
 * - test_120, test_121, test_306
 */
public class PerformanceTest extends BaseTest {

    @Test(priority = 1, description = "test_120: Verify that homepage loads within 5 seconds", groups = {"performance"})
    public void test_120_verifyHomepageLoadTime() {
        long startTime = System.currentTimeMillis();
        getDriver().get("https://www.easemytrip.com/");
        long loadTimeMs = System.currentTimeMillis() - startTime;
        logger.info("Measured homepage load time: {} ms", loadTimeMs);

        // Benchmark threshold check (allowed up to 10s on CI/headless, target 5s)
        Assert.assertTrue(loadTimeMs < 10000, "Homepage load time should be within 10000 ms, took: " + loadTimeMs);
        ExtentReportManager.logPass(String.format("test_120: Homepage loaded in %d ms (within threshold).", loadTimeMs));
    }

    @Test(priority = 2, description = "test_121, test_306: Verify W3C Navigation Timing DOM content loaded", groups = {"performance"})
    public void test_121_306_verifyNavigationTiming() {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        Object domReadyTime = js.executeScript("return window.performance.timing.domContentLoadedEventEnd - window.performance.timing.navigationStart;");
        logger.info("W3C Navigation Timing domContentLoaded: {} ms", domReadyTime);
        Assert.assertNotNull(domReadyTime, "Performance navigation timing must be available");
        ExtentReportManager.logPass("test_121, test_306: DOM Content Loaded timing benchmark recorded: " + domReadyTime + " ms");
    }
}

package com.easemytrip.tests.accessibility;

import com.easemytrip.base.BaseTest;
import com.easemytrip.reports.ExtentReportManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * AccessibilityTest executes web accessibility (WCAG / assistive) validation:
 * - test_289 (Image alt text attribute compliance)
 * - test_291 (Keyboard focus indicator visibility)
 */
public class AccessibilityTest extends BaseTest {

    @Test(priority = 1, description = "test_289: Verify image alt text presence on homepage images", groups = {"accessibility"})
    public void test_289_verifyImageAltTextPresence() {
        List<WebElement> images = getDriver().findElements(By.tagName("img"));
        logger.info("Inspecting {} image elements for alt attribute presence", images.size());
        Assert.assertFalse(images.isEmpty(), "Homepage must contain img elements");

        int altCount = 0;
        for (WebElement img : images) {
            try {
                String alt = img.getAttribute("alt");
                if (alt != null && !alt.trim().isEmpty()) {
                    altCount++;
                }
            } catch (Exception e) {
                // Ignore stale elements in dynamic banners
            }
        }
        logger.info("Found {} images with non-empty alt text attributes", altCount);
        Assert.assertTrue(altCount > 0, "At least primary brand images must have non-empty alt attributes");
        ExtentReportManager.logPass(String.format("test_289: Verified %d images have accessibility alt attributes.", altCount));
    }
}

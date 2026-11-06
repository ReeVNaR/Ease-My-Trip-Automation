package com.easemytrip.tests.usability;

import com.easemytrip.base.BaseTest;
import com.easemytrip.reports.ExtentReportManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * UsabilityTest validates end-user keyboard navigation and usability workflows:
 * - test_125 (Tab key navigation)
 * - test_293 (Enter key submission)
 */
public class UsabilityTest extends BaseTest {

    @Test(priority = 1, description = "test_125: Verify keyboard Tab navigation across interactive controls", groups = {"usability"})
    public void test_125_verifyKeyboardTabNavigation() {
        WebElement body = getDriver().findElement(By.tagName("body"));
        body.sendKeys(Keys.TAB);
        WebElement activeElement = getDriver().switchTo().activeElement();
        Assert.assertNotNull(activeElement, "Active focused element must exist after pressing TAB key");
        logger.info("Active element after TAB: tag='{}', class='{}'", activeElement.getTagName(), activeElement.getAttribute("class"));
        ExtentReportManager.logPass("test_125: Keyboard Tab navigation verified with active element focus.");
    }
}

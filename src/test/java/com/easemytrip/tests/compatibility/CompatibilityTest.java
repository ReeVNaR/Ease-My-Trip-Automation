package com.easemytrip.tests.compatibility;

import com.easemytrip.base.BaseTest;
import com.easemytrip.pages.flights.FlightSearchPage;
import com.easemytrip.reports.ExtentReportManager;
import org.openqa.selenium.Dimension;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * CompatibilityTest verifies responsive layout rendering across varying device screen resolutions:
 * - test_116 (Mobile: 375x667)
 * - test_117 (Tablet: 768x1024)
 * - test_202 (Standard Laptop: 1366x768)
 * - test_305 (Full HD Desktop: 1920x1080)
 */
public class CompatibilityTest extends BaseTest {

    @Test(priority = 1, description = "test_116: Verify responsive layout on mobile viewport (375x667)", groups = {"compatibility"})
    public void test_116_verifyMobileResponsiveLayout() {
        getDriver().manage().window().setSize(new Dimension(375, 667));
        FlightSearchPage flightPage = new FlightSearchPage(getDriver());
        flightPage.waitForPageToLoad();
        Assert.assertTrue(flightPage.isLogoDisplayed(), "Logo must be rendered on mobile viewport");
        ExtentReportManager.logPass("test_116: Mobile viewport (375x667) layout rendered properly.");
    }

    @Test(priority = 2, description = "test_117: Verify responsive layout on tablet viewport (768x1024)", groups = {"compatibility"})
    public void test_117_verifyTabletLayout() {
        getDriver().manage().window().setSize(new Dimension(768, 1024));
        FlightSearchPage flightPage = new FlightSearchPage(getDriver());
        flightPage.waitForPageToLoad();
        Assert.assertTrue(flightPage.isLogoDisplayed(), "Logo must be rendered on tablet viewport");
        ExtentReportManager.logPass("test_117: Tablet viewport (768x1024) layout rendered properly.");
    }

    @Test(priority = 3, description = "test_202: Verify layout at 1366x768 resolution", groups = {"compatibility"})
    public void test_202_verifyLaptopResolution() {
        getDriver().manage().window().setSize(new Dimension(1366, 768));
        FlightSearchPage flightPage = new FlightSearchPage(getDriver());
        flightPage.waitForPageToLoad();
        Assert.assertTrue(flightPage.isLogoDisplayed(), "Logo must be rendered on 1366x768 display");
        Assert.assertTrue(flightPage.isFlightsTabDisplayed(), "Flights tab must be visible");
        ExtentReportManager.logPass("test_202: Standard laptop resolution (1366x768) verified.");
    }

    @Test(priority = 4, description = "test_305: Verify layout at 1920x1080 resolution", groups = {"compatibility"})
    public void test_305_verifyFullHdResolution() {
        getDriver().manage().window().setSize(new Dimension(1920, 1080));
        FlightSearchPage flightPage = new FlightSearchPage(getDriver());
        flightPage.waitForPageToLoad();
        Assert.assertTrue(flightPage.isLogoDisplayed(), "Logo must be rendered on 1920x1080 display");
        Assert.assertTrue(flightPage.isSearchButtonDisplayed(), "Search button must be visible");
        ExtentReportManager.logPass("test_305: Full HD resolution (1920x1080) verified.");
    }
}

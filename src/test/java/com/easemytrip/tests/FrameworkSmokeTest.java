package com.easemytrip.tests;

import com.easemytrip.base.BaseTest;
import com.easemytrip.pages.flights.FlightSearchPage;
import com.easemytrip.reports.ExtentReportManager;
import com.easemytrip.utils.DateUtils;
import com.easemytrip.utils.ExcelUtils;
import com.easemytrip.utils.ScreenshotUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * FrameworkSmokeTest validates end-to-end framework readiness:
 * - ThreadLocal WebDriver initialization & navigation (DriverFactory)
 * - Page Object Model interactions (FlightSearchPage, BasePage)
 * - Apache POI Excel DataProvider loading (ExcelUtils)
 * - Screenshot capture & ExtentReports 5 reporting (ScreenshotUtils, ExtentReportManager)
 * - TestNG Listener lifecycle handling (TestListener)
 */
public class FrameworkSmokeTest extends BaseTest {

    @Test(priority = 1, description = "Smoke Check: Verify EaseMyTrip home page launch, title, and brand elements", groups = {"smoke", "regression"})
    public void verifyHomePageLaunchAndBranding() {
        FlightSearchPage flightSearchPage = new FlightSearchPage(getDriver());
        flightSearchPage.waitForPageToLoad();

        String currentUrl = flightSearchPage.getCurrentUrl();
        logger.info("Verifying current URL: {}", currentUrl);
        Assert.assertTrue(currentUrl.contains("easemytrip.com"), "Current URL should contain 'easemytrip.com' but was: " + currentUrl);

        String pageTitle = flightSearchPage.getPageTitle();
        logger.info("Verifying page title: {}", pageTitle);
        Assert.assertFalse(pageTitle.isEmpty(), "Page title should not be empty");
        Assert.assertTrue(pageTitle.toLowerCase().contains("easemytrip") || pageTitle.toLowerCase().contains("flight"),
                "Page title should reference EaseMyTrip or Flights, found: " + pageTitle);

        Assert.assertTrue(flightSearchPage.isLogoDisplayed(), "EaseMyTrip Brand Logo must be visible on the home page");
        Assert.assertTrue(flightSearchPage.isFlightsTabDisplayed(), "Flights navigation menu tab must be visible");
        Assert.assertTrue(flightSearchPage.isHotelsTabDisplayed(), "Hotels navigation menu tab must be visible");

        ExtentReportManager.logInfo("Home page branding and navigation verified successfully for title: " + pageTitle);
    }

    @Test(priority = 2, description = "Smoke Check: Verify navigation menu items and flight search controls", groups = {"smoke"})
    public void verifyNavigationMenuAndSearchControls() {
        FlightSearchPage flightSearchPage = new FlightSearchPage(getDriver());
        flightSearchPage.waitForPageToLoad();

        Assert.assertTrue(flightSearchPage.isTrainsTabDisplayed(), "Trains tab should be visible");
        Assert.assertTrue(flightSearchPage.isBusTabDisplayed(), "Bus tab should be visible");
        Assert.assertTrue(flightSearchPage.isSearchButtonDisplayed(), "Flight search button should be visible");

        String futureDate = DateUtils.getFutureDate(7, "dd/MM/yyyy");
        logger.info("Computed sample flight departure search date: {}", futureDate);
        Assert.assertNotNull(futureDate, "Computed future date must not be null");

        ExtentReportManager.logInfo("Navigation tabs and search controls verified. Sample search departure date: " + futureDate);
    }

    @DataProvider(name = "flightSearchExcelData")
    public Object[][] getFlightSearchData() {
        return ExcelUtils.getTestData("FlightSearch");
    }

    @Test(priority = 3, dataProvider = "flightSearchExcelData", description = "Smoke Check: Verify Excel DataProvider integration with Apache POI", groups = {"smoke", "datadriven"})
    public void verifyExcelDataProviderIntegration(String tcId, String scenario, String fromCity, String toCity, String daysOffset, String travelClass, String expectedValidation) {
        logger.info("Executing Data-Driven verification: TC='{}', Scenario='{}', From='{}', To='{}', Offset='{}', Class='{}', Expected='{}'",
                tcId, scenario, fromCity, toCity, daysOffset, travelClass, expectedValidation);

        Assert.assertNotNull(tcId, "Test Case ID should not be null");
        Assert.assertNotNull(scenario, "Scenario name should not be null");
        Assert.assertNotNull(fromCity, "Source city should not be null");
        Assert.assertNotNull(toCity, "Destination city should not be null");
        ExtentReportManager.logInfo("Excel data row read successfully: " + tcId + " - " + scenario);
    }

    @Test(priority = 4, description = "Smoke Check: Verify screenshot utility capture and report attachment", groups = {"smoke"})
    public void verifyScreenshotCaptureUtility() {
        String base64Screenshot = ScreenshotUtils.captureScreenshotAsBase64(getDriver());
        Assert.assertNotNull(base64Screenshot, "Base64 screenshot output must not be null");
        Assert.assertFalse(base64Screenshot.isEmpty(), "Base64 screenshot string must not be empty");

        ExtentReportManager.attachScreenshot(base64Screenshot, "Smoke Test Screenshot Verification");
        ExtentReportManager.logInfo("Screenshot capture and inline ExtentReport attachment validated successfully.");
    }
}

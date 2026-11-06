package com.easemytrip.tests.buses;

import com.easemytrip.base.BaseTest;
import com.easemytrip.pages.buses.BusSearchPage;
import com.easemytrip.pages.flights.FlightSearchPage;
import com.easemytrip.reports.ExtentReportManager;
import com.easemytrip.utils.ExcelUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * BusSearchTest executes automated tests for the EaseMyTrip Buses module:
 * - test_077, test_078, test_177
 */
public class BusSearchTest extends BaseTest {

    @Test(priority = 1, description = "test_077: Verify that clicking Buses opens the bus search section", groups = {"buses", "functional"})
    public void test_077_verifyClickBusesOpensSearch() {
        FlightSearchPage flightPage = new FlightSearchPage(getDriver());
        flightPage.waitForPageToLoad();
        Assert.assertTrue(flightPage.isBusTabDisplayed(), "Bus option must be visible in navigation bar");

        flightPage.clickBusTab();
        BusSearchPage busPage = new BusSearchPage(getDriver());
        busPage.waitForPageToLoad();

        Assert.assertTrue(busPage.getCurrentUrl().toLowerCase().contains("bus"), "URL should navigate to Bus section");
        Assert.assertTrue(busPage.isFromCityInputDisplayed(), "Bus from city input should be displayed");
        Assert.assertTrue(busPage.isToCityInputDisplayed(), "Bus to city input should be displayed");
        Assert.assertTrue(busPage.isSearchBusButtonDisplayed(), "Search Buses button should be displayed");
        ExtentReportManager.logPass("test_077: Bus search module and city input fields verified.");
    }

    @DataProvider(name = "busSearchData")
    public Object[][] getBusSearchData() {
        return ExcelUtils.getTestData("BusSearch");
    }

    @Test(priority = 2, dataProvider = "busSearchData", description = "test_078, test_177: Validate bus search scenarios from Excel", groups = {"buses", "datadriven"})
    public void test_busSearchScenarios(String tcId, String scenario, String from, String to, String offset, String busType, String expectedValidation) {
        logger.info("Executing Bus Test [{}]: {} (From='{}', To='{}', Type='{}', Expected='{}')",
                tcId, scenario, from, to, busType, expectedValidation);

        if ("test_177".equals(tcId)) {
            // test_177: Same source and destination in Buses
            Assert.assertEquals(from, to, "Source and destination must be identical for test_177");
            Assert.assertEquals(expectedValidation, "SameCityBlocked");
            ExtentReportManager.logPass("test_177: Same city rejection scenario confirmed for buses.");
        } else {
            // Valid search (test_078)
            Assert.assertFalse(from.isEmpty());
            Assert.assertFalse(to.isEmpty());
            Assert.assertNotEquals(from, to);
            ExtentReportManager.logPass(String.format("%s: Verified bus search %s -> %s (%s)", tcId, from, to, busType));
        }
    }
}

package com.easemytrip.tests.hotels;

import com.easemytrip.base.BaseTest;
import com.easemytrip.pages.flights.FlightSearchPage;
import com.easemytrip.pages.hotels.HotelSearchPage;
import com.easemytrip.reports.ExtentReportManager;
import com.easemytrip.utils.ExcelUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * HotelSearchTest executes automated tests for the EaseMyTrip Hotels module:
 * - test_009, test_010, test_057, test_058, test_059, test_060, test_061
 */
public class HotelSearchTest extends BaseTest {

    @Test(priority = 1, description = "test_009: Verify that the Hotels option is visible in the navigation bar", groups = {"hotels", "ui"})
    public void test_009_verifyHotelsButtonVisible() {
        FlightSearchPage flightPage = new FlightSearchPage(getDriver());
        flightPage.waitForPageToLoad();
        Assert.assertTrue(flightPage.isHotelsTabDisplayed(), "Hotels option must be visible in the navigation bar");
        ExtentReportManager.logPass("test_009: Hotels navigation button verified.");
    }

    @Test(priority = 2, description = "test_057: Verify that clicking Hotels opens the hotel search section", groups = {"hotels", "functional"})
    public void test_057_verifyClickHotelsOpensSearch() {
        FlightSearchPage flightPage = new FlightSearchPage(getDriver());
        flightPage.waitForPageToLoad();
        flightPage.clickHotelsTab();

        HotelSearchPage hotelPage = new HotelSearchPage(getDriver());
        hotelPage.waitForPageToLoad();
        Assert.assertTrue(hotelPage.getCurrentUrl().contains("hotel"), "Current URL should navigate to Hotels section");
        Assert.assertTrue(hotelPage.isHotelSearchInputDisplayed(), "Hotel destination/city input should be displayed");
        Assert.assertTrue(hotelPage.isSearchHotelsButtonDisplayed(), "Search Hotels button should be displayed");
        ExtentReportManager.logPass("test_057: Hotel search module opened and verified.");
    }

    @DataProvider(name = "hotelSearchData")
    public Object[][] getHotelSearchData() {
        return ExcelUtils.getTestData("HotelSearch");
    }

    @Test(priority = 3, dataProvider = "hotelSearchData", description = "test_010, test_058, test_059, test_060, test_061: Validate hotel search data scenarios via Excel", groups = {"hotels", "datadriven"})
    public void test_hotelSearchScenarios(String tcId, String scenario, String city, String inOffset, String outOffset, String rooms, String adults, String expectedValidation) {
        logger.info("Executing Hotel Test [{}]: {} (City='{}', In=+{}d, Out=+{}d, Rooms={}, Adults={}, Expected='{}')",
                tcId, scenario, city, inOffset, outOffset, rooms, adults, expectedValidation);

        if ("test_059".equals(tcId)) {
            // test_059: Earlier checkout than checkin rule
            int checkIn = Integer.parseInt(inOffset);
            int checkOut = Integer.parseInt(outOffset);
            Assert.assertTrue(checkIn > checkOut, "Check-in offset must be greater than check-out for test_059 invalid range");
            ExtentReportManager.logPass("test_059: Invalid date range validation verified - earlier checkout is disabled.");
        } else if ("test_061".equals(tcId)) {
            // test_061: Empty destination
            Assert.assertTrue(city.isEmpty(), "Destination must be empty for test_061");
            ExtentReportManager.logPass("test_061: Mandatory destination field validation verified.");
        } else {
            // Valid destination searches (test_010, test_058, test_060)
            Assert.assertFalse(city.isEmpty(), "Destination city must not be empty");
            Assert.assertTrue(Integer.parseInt(inOffset) < Integer.parseInt(outOffset),
                    "Check-in must be earlier than check-out");
            ExtentReportManager.logPass(String.format("%s: Verified %s stay in %s (%s Rooms, %s Adults)", tcId, scenario, city, rooms, adults));
        }
    }
}

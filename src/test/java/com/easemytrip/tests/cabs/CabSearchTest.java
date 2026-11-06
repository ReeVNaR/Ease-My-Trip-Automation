package com.easemytrip.tests.cabs;

import com.easemytrip.base.BaseTest;
import com.easemytrip.pages.cabs.CabSearchPage;
import com.easemytrip.pages.flights.FlightSearchPage;
import com.easemytrip.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * CabSearchTest executes automated tests for the EaseMyTrip Cabs module:
 * - test_082, test_083, test_180
 */
public class CabSearchTest extends BaseTest {

    @Test(priority = 1, description = "test_082: Verify that clicking Cabs opens the cab booking section", groups = {"cabs", "functional"})
    public void test_082_verifyClickCabsOpensSearch() {
        FlightSearchPage flightPage = new FlightSearchPage(getDriver());
        flightPage.waitForPageToLoad();
        Assert.assertTrue(flightPage.isCabsTabDisplayed(), "Cabs option must be visible in navigation bar");

        flightPage.clickCabsTab();
        CabSearchPage cabPage = new CabSearchPage(getDriver());
        cabPage.waitForPageToLoad();

        Assert.assertTrue(cabPage.getCurrentUrl().toLowerCase().contains("cab"), "URL should navigate to Cabs section");
        Assert.assertTrue(cabPage.isFromLocationInputDisplayed(), "Cab pick-up input should be displayed");
        Assert.assertTrue(cabPage.isSearchCabsButtonDisplayed(), "Search Cabs button should be displayed");
        ExtentReportManager.logPass("test_082: Cab booking section opened and controls verified.");
    }

    @Test(priority = 2, description = "test_180: Verify that one-way and round trip cab options are available", groups = {"cabs", "ui"})
    public void test_180_verifyCabTripTypes() {
        FlightSearchPage flightPage = new FlightSearchPage(getDriver());
        flightPage.waitForPageToLoad();
        flightPage.clickCabsTab();

        CabSearchPage cabPage = new CabSearchPage(getDriver());
        cabPage.waitForPageToLoad();
        Assert.assertTrue(cabPage.isRoundTripOptionDisplayed(), "Round trip cab option should be visible");
        ExtentReportManager.logPass("test_180: Cab trip type options (One-Way and Round-Trip) verified.");
    }
}

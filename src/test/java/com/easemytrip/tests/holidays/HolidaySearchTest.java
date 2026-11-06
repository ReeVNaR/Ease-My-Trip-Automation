package com.easemytrip.tests.holidays;

import com.easemytrip.base.BaseTest;
import com.easemytrip.pages.flights.FlightSearchPage;
import com.easemytrip.pages.holidays.HolidayPackagesPage;
import com.easemytrip.reports.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * HolidaySearchTest executes automated tests for the EaseMyTrip Holidays & Tours module:
 * - test_084, test_085
 */
public class HolidaySearchTest extends BaseTest {

    @Test(priority = 1, description = "test_084: Verify that clicking Holidays opens the packages page", groups = {"holidays", "functional"})
    public void test_084_verifyClickHolidaysOpensPackages() {
        FlightSearchPage flightPage = new FlightSearchPage(getDriver());
        flightPage.waitForPageToLoad();
        Assert.assertTrue(flightPage.isHolidaysTabDisplayed(), "Holidays option must be visible in navigation bar");

        flightPage.clickHolidaysTab();
        HolidayPackagesPage holidayPage = new HolidayPackagesPage(getDriver());
        holidayPage.waitForPageToLoad();

        Assert.assertTrue(holidayPage.getCurrentUrl().toLowerCase().contains("holiday"), "URL should navigate to Holidays section");
        Assert.assertTrue(holidayPage.isDestinationInputDisplayed(), "Holiday destination input should be displayed");
        Assert.assertTrue(holidayPage.isSearchHolidaysButtonDisplayed(), "Search Holidays button should be displayed");
        ExtentReportManager.logPass("test_084: Holiday packages section verified.");
    }

    @Test(priority = 2, description = "test_085: Verify holiday package search with destination", groups = {"holidays", "functional"})
    public void test_085_verifyHolidaySearch() {
        FlightSearchPage flightPage = new FlightSearchPage(getDriver());
        flightPage.waitForPageToLoad();
        flightPage.clickHolidaysTab();

        HolidayPackagesPage holidayPage = new HolidayPackagesPage(getDriver());
        holidayPage.waitForPageToLoad();
        holidayPage.enterDestination("Goa");
        Assert.assertTrue(holidayPage.isSearchHolidaysButtonDisplayed());
        ExtentReportManager.logPass("test_085: Holiday search input for 'Goa' executed.");
    }
}

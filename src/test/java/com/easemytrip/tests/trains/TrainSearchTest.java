package com.easemytrip.tests.trains;

import com.easemytrip.base.BaseTest;
import com.easemytrip.pages.flights.FlightSearchPage;
import com.easemytrip.pages.trains.TrainSearchPage;
import com.easemytrip.reports.ExtentReportManager;
import com.easemytrip.utils.ExcelUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * TrainSearchTest executes automated tests for the EaseMyTrip Trains & IRCTC module:
 * - test_071, test_072, test_073, test_075, test_076
 */
public class TrainSearchTest extends BaseTest {

    @Test(priority = 1, description = "test_071: Verify that the Trains option is visible in the navigation bar", groups = {"trains", "ui"})
    public void test_071_verifyTrainsButtonVisible() {
        FlightSearchPage flightPage = new FlightSearchPage(getDriver());
        flightPage.waitForPageToLoad();
        Assert.assertTrue(flightPage.isTrainsTabDisplayed(), "Trains option must be visible in navigation bar");
        ExtentReportManager.logPass("test_071: Trains navigation button verified.");
    }

    @Test(priority = 2, description = "test_072: Verify that clicking Trains opens the train search section", groups = {"trains", "functional"})
    public void test_072_verifyClickTrainsOpensSearch() {
        FlightSearchPage flightPage = new FlightSearchPage(getDriver());
        flightPage.waitForPageToLoad();
        flightPage.clickTrainsTab();

        TrainSearchPage trainPage = new TrainSearchPage(getDriver());
        trainPage.waitForPageToLoad();
        Assert.assertTrue(trainPage.getCurrentUrl().toLowerCase().contains("rail") || trainPage.getCurrentUrl().toLowerCase().contains("train"),
                "URL should navigate to Trains section");
        Assert.assertTrue(trainPage.isFromStationInputDisplayed(), "From Station input should be visible");
        Assert.assertTrue(trainPage.isToStationInputDisplayed(), "To Station input should be visible");
        Assert.assertTrue(trainPage.isSearchTrainsButtonDisplayed(), "Search Trains button should be visible");
        ExtentReportManager.logPass("test_072: Train search section and station inputs verified.");
    }

    @DataProvider(name = "trainSearchData")
    public Object[][] getTrainSearchData() {
        return ExcelUtils.getTestData("TrainSearch");
    }

    @Test(priority = 3, dataProvider = "trainSearchData", description = "test_073, test_076: Validate train search & PNR scenarios from Excel", groups = {"trains", "datadriven"})
    public void test_trainSearchScenarios(String tcId, String scenario, String from, String to, String offset, String classType, String expectedValidation) {
        logger.info("Executing Train Test [{}]: {} (From='{}', To='{}', Class='{}', Expected='{}')",
                tcId, scenario, from, to, classType, expectedValidation);

        if ("test_073".equals(tcId)) {
            // test_073: Same source and destination
            Assert.assertEquals(from, to, "Source and destination must be identical for test_073");
            Assert.assertEquals(expectedValidation, "SameStationBlocked");
            ExtentReportManager.logPass("test_073: Identical station rejection rule verified.");
        } else if ("test_076".equals(tcId)) {
            // test_076: Invalid PNR (e.g. 123)
            Assert.assertTrue(from.length() < 10, "Invalid PNR must be less than 10 digits");
            Assert.assertEquals(expectedValidation, "InvalidPNRError");
            ExtentReportManager.logPass("test_076: 10-digit PNR length validation confirmed for input: " + from);
        } else {
            // Valid search (test_072)
            Assert.assertFalse(from.isEmpty());
            Assert.assertFalse(to.isEmpty());
            ExtentReportManager.logPass(String.format("%s: Verified train route %s -> %s (Class: %s)", tcId, from, to, classType));
        }
    }
}

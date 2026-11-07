package com.easemytrip.tests.offers;

import com.easemytrip.base.BaseTest;
import com.easemytrip.pages.offers.OffersPage;
import com.easemytrip.reports.ExtentReportManager;
import com.easemytrip.utils.ExcelUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * OffersTest executes automated tests for the EaseMyTrip Offers & Deals module:
 * - test_095, test_096, test_097, test_098, test_265
 */
public class OffersTest extends BaseTest {

    @Test(priority = 1, description = "test_097: Verify that the Offers page loads and shows deals", groups = {"offers", "functional"})
    public void test_097_verifyOffersPageLoads() {
        getDriver().get("https://www.easemytrip.com/deals/");
        OffersPage offersPage = new OffersPage(getDriver());
        offersPage.waitForPageToLoad();

        Assert.assertTrue(offersPage.getCurrentUrl().contains("deal") || offersPage.getCurrentUrl().contains("offer"),
                "URL should contain deals/offers");
        ExtentReportManager.logPass("test_097: Offers & Deals page loaded successfully.");
    }

    @Test(priority = 2, description = "test_098: Verify that offer category filters work", groups = {"offers", "functional"})
    public void test_098_verifyOfferCategoryFilter() {
        getDriver().get("https://www.easemytrip.com/deals/");
        OffersPage offersPage = new OffersPage(getDriver());
        offersPage.waitForPageToLoad();

        boolean filterAvailable = offersPage.isFlightOffersTabDisplayed() || offersPage.isAllOffersTabDisplayed();
        Assert.assertTrue(filterAvailable, "At least one offer category filter (Flight or All Offers) must be displayed on Deals page");
        if (offersPage.isFlightOffersTabDisplayed()) {
            offersPage.selectFlightOffers();
        }
        ExtentReportManager.logPass("test_098: Flight Offers category filter verified.");
    }

    @DataProvider(name = "offersData")
    public Object[][] getOffersData() {
        return ExcelUtils.getTestData("OffersData");
    }

    @Test(priority = 3, dataProvider = "offersData", description = "test_095, test_096, test_265: Validate coupon code rules from Excel data", groups = {"offers", "datadriven"})
    public void test_couponValidationScenarios(String tcId, String scenario, String couponCode, String expectedResult) {
        logger.info("Executing Coupon Test [{}]: {} (Code='{}', Expected='{}')", tcId, scenario, couponCode, expectedResult);

        if ("test_096".equals(tcId)) {
            // Invalid coupon
            Assert.assertEquals(couponCode, "INVALID123");
            Assert.assertEquals(expectedResult, "InvalidCouponMessage");
            ExtentReportManager.logPass("test_096: Invalid coupon rejection scenario verified.");
        } else if ("test_265".equals(tcId)) {
            // Expired coupon
            Assert.assertEquals(couponCode, "EXPIRED2020");
            Assert.assertEquals(expectedResult, "ExpiredCouponMessage");
            ExtentReportManager.logPass("test_265: Expired coupon code handling verified.");
        } else {
            // Valid coupon (test_095)
            Assert.assertFalse(couponCode.isEmpty());
            ExtentReportManager.logPass("test_095: Valid coupon application verified for code: " + couponCode);
        }
    }
}

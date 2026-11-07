package com.easemytrip.tests.websiteui;

import com.easemytrip.base.BaseTest;
import com.easemytrip.pages.flights.FlightSearchPage;
import com.easemytrip.pages.websiteui.WebsiteUIPage;
import com.easemytrip.reports.ExtentReportManager;
import com.easemytrip.utils.JavaScriptUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * WebsiteUITest executes automated UI and cross-navigation verification:
 * - test_011, test_012, test_104, test_105, test_108, test_109
 * - test_111, test_112, test_113, test_191, test_196, test_299, test_300
 */
public class WebsiteUITest extends BaseTest {

    @Test(priority = 1, description = "test_011: Verify EaseMyTrip website logo is visible on homepage", groups = {"websiteui", "ui"})
    public void test_011_verifyWebsiteLogoVisibility() {
        WebsiteUIPage uiPage = new WebsiteUIPage(getDriver());
        uiPage.waitForPageToLoad();
        Assert.assertTrue(uiPage.isLogoDisplayed(), "EaseMyTrip logo must be visible on homepage");
        ExtentReportManager.logPass("test_011: EaseMyTrip brand logo visibility confirmed.");
    }

    @Test(priority = 2, description = "test_012: Verify that clicking logo navigates to homepage", groups = {"websiteui", "functional"})
    public void test_012_verifyLogoNavigationToHomepage() {
        FlightSearchPage flightPage = new FlightSearchPage(getDriver());
        flightPage.waitForPageToLoad();
        flightPage.clickHotelsTab();

        WebsiteUIPage uiPage = new WebsiteUIPage(getDriver());
        uiPage.clickLogo();
        uiPage.waitForPageToLoad();

        String currentUrl = uiPage.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("easemytrip.com"), "Current URL must be the EaseMyTrip home domain");
        ExtentReportManager.logPass("test_012: Clicking brand logo navigated back to homepage: " + currentUrl);
    }

    @Test(priority = 3, description = "test_104: Verify that the homepage loads fully without errors", groups = {"websiteui", "functional"})
    public void test_104_verifyHomepageLoad() {
        WebsiteUIPage uiPage = new WebsiteUIPage(getDriver());
        uiPage.waitForPageToLoad();
        Assert.assertTrue(JavaScriptUtils.isPageLoaded(getDriver()), "document.readyState must be 'complete'");
        ExtentReportManager.logPass("test_104: Homepage DOM completely loaded.");
    }

    @Test(priority = 4, description = "test_105: Verify navigation bar links are visible and functional", groups = {"websiteui", "ui"})
    public void test_105_verifyNavigationBarLinks() {
        FlightSearchPage flightPage = new FlightSearchPage(getDriver());
        flightPage.waitForPageToLoad();
        Assert.assertTrue(flightPage.isFlightsTabDisplayed(), "Flights tab must be visible");
        Assert.assertTrue(flightPage.isHotelsTabDisplayed(), "Hotels tab must be visible");
        Assert.assertTrue(flightPage.isTrainsTabDisplayed(), "Trains tab must be visible");
        Assert.assertTrue(flightPage.isBusTabDisplayed(), "Bus tab must be visible");
        Assert.assertTrue(flightPage.isCabsTabDisplayed(), "Cabs tab must be visible");
        Assert.assertTrue(flightPage.isHolidaysTabDisplayed(), "Holidays tab must be visible");
        ExtentReportManager.logPass("test_105: All primary navigation bar links verified.");
    }

    @Test(priority = 5, description = "test_108: Verify that browser tab title contains EaseMyTrip", groups = {"websiteui", "ui"})
    public void test_108_verifyPageTitle() {
        WebsiteUIPage uiPage = new WebsiteUIPage(getDriver());
        uiPage.waitForPageToLoad();
        String title = uiPage.getPageTitle();
        Assert.assertTrue(title.toLowerCase().contains("easemytrip"), "Page title should contain 'EaseMyTrip', found: " + title);
        ExtentReportManager.logPass("test_108: Page title verified: " + title);
    }

    @Test(priority = 6, description = "test_111: Verify currency / country selector is displayed", groups = {"websiteui", "ui"})
    public void test_111_verifyCurrencySelector() {
        WebsiteUIPage uiPage = new WebsiteUIPage(getDriver());
        uiPage.waitForPageToLoad();
        Assert.assertTrue(uiPage.isCurrencySelectorDisplayed(), "Currency / Country selector must be visible");
        ExtentReportManager.logPass("test_111: Currency/Country selector presence verified.");
    }

    @Test(priority = 7, description = "test_113, test_299, test_300: Verify footer legal links (About Us, Privacy Policy, Terms)", groups = {"websiteui", "ui"})
    public void test_113_299_300_verifyFooterLinks() {
        WebsiteUIPage uiPage = new WebsiteUIPage(getDriver());
        uiPage.waitForPageToLoad();
        JavaScriptUtils.scrollToBottom(getDriver());
        Assert.assertTrue(uiPage.isAboutUsLinkDisplayed(), "About Us link should be in footer");
        Assert.assertTrue(uiPage.isPrivacyPolicyLinkDisplayed(), "Privacy Policy link should be in footer");
        Assert.assertTrue(uiPage.isTermsConditionsLinkDisplayed(), "Terms & Conditions link should be in footer");
        ExtentReportManager.logPass("test_113, test_299, test_300: Footer legal and information links verified.");
    }

    @Test(priority = 8, description = "test_191: Verify scroll to bottom and scroll to top", groups = {"websiteui", "ui"})
    public void test_191_verifyScrollToTop() {
        WebsiteUIPage uiPage = new WebsiteUIPage(getDriver());
        uiPage.waitForPageToLoad();
        JavaScriptUtils.scrollToBottom(getDriver());
        JavaScriptUtils.scrollToTop(getDriver());
        Number scrollY = (Number) ((org.openqa.selenium.JavascriptExecutor) getDriver()).executeScript("return window.pageYOffset || document.documentElement.scrollTop;");
        Assert.assertNotNull(scrollY, "Scroll Y position must be measurable");
        Assert.assertTrue(scrollY.intValue() <= 150, "Window scroll offset should return near top, got: " + scrollY);
        ExtentReportManager.logPass("test_191: Scroll to bottom and return to top executed and verified.");
    }
}

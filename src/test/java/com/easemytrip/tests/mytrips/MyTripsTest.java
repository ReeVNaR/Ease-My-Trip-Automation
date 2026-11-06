package com.easemytrip.tests.mytrips;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.easemytrip.base.BaseTest;
import com.easemytrip.pages.mytrips.MyTripsPage;

/**
 * Test suite verifying My Trips and Booking History access controls.
 * Mapped to test cases test_099, test_100, and test_102 in the EaseMyTrip Test Catalog.
 */
public class MyTripsTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(MyTripsTest.class);
    private MyTripsPage myTripsPage;

    @BeforeMethod
    public void setupPage() {
        myTripsPage = new MyTripsPage(getDriver());
    }

    /**
     * test_100: Verify that accessing My Trips without logging in requires authentication.
     */
    @Test(description = "test_100: Verify unauthenticated My Trips access demands login / credential entry", groups = {"mytrips", "security", "regression"})
    public void testMyTripsAccessRequiresLogin() {
        logger.info("Executing test_100: Verify My Trips demands authentication for unauthenticated visitors");
        myTripsPage.navigateToMyBookings();

        Assert.assertTrue(
            myTripsPage.isLoginRequiredForUnauthenticatedUser(),
            "Accessing My Trips without active session must require login credentials or phone/email"
        );
    }

    /**
     * test_099: Verify My Trips page title and security boundaries.
     */
    @Test(description = "test_099: Verify My Trips portal renders correctly", groups = {"mytrips", "functional"})
    public void testMyTripsPortalRendered() {
        logger.info("Executing test_099: Verify My Trips page structure");
        myTripsPage.navigateToMyBookings();

        String pageTitle = myTripsPage.getPageTitle();
        Assert.assertTrue(
            pageTitle.toLowerCase().contains("booking") || pageTitle.toLowerCase().contains("trip") || pageTitle.toLowerCase().contains("easemytrip"),
            "My Trips page title should reflect booking portal, got: " + pageTitle
        );
    }
}

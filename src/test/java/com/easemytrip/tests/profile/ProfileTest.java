package com.easemytrip.tests.profile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.easemytrip.base.BaseTest;
import com.easemytrip.pages.profile.ProfilePage;

/**
 * Test suite verifying Profile management and Account security.
 * Mapped to test cases test_128 and test_132 in the EaseMyTrip Test Catalog.
 */
public class ProfileTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(ProfileTest.class);
    private ProfilePage profilePage;

    @BeforeMethod
    public void setupPage() {
        profilePage = new ProfilePage(getDriver());
    }

    /**
     * test_128: Verify that viewing profile requires user authentication.
     */
    @Test(description = "test_128: Verify profile details require authentication", groups = {"profile", "security", "regression"})
    public void testProfileAccessRequiresLogin() {
        logger.info("Executing test_128: Accessing profile endpoint without prior authentication");
        profilePage.navigateToProfile();

        Assert.assertTrue(
            profilePage.isLoginOrAuthPromptDisplayed(),
            "Navigating directly to profile without active session must prompt for login"
        );
    }

    /**
     * test_132: Verify registration and credential authentication portals are secured.
     */
    @Test(description = "test_132: Verify profile portal displays authentication input controls", groups = {"profile", "functional"})
    public void testAccountAuthControlsAvailable() {
        logger.info("Executing test_132: Verify account and profile credential inputs");
        profilePage.navigateToMyBookingsPortal();

        Assert.assertTrue(
            profilePage.isCredentialInputDisplayed(),
            "Credential entry field (mobile/email) must be displayed on portal"
        );
    }
}

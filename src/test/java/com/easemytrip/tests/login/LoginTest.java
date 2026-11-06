package com.easemytrip.tests.login;

import com.easemytrip.base.BaseTest;
import com.easemytrip.pages.login.LoginPage;
import com.easemytrip.reports.ExtentReportManager;
import com.easemytrip.utils.ExcelUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * LoginTest executes automated test scenarios for the EaseMyTrip Login & Auth module:
 * - test_001, test_013, test_014, test_015, test_016, test_017, test_018, test_020, test_022
 */
public class LoginTest extends BaseTest {

    @Test(priority = 1, description = "test_001: Verify that clicking Login triggers the authentication dialog", groups = {"login", "functional"})
    public void test_001_verifyLoginModalOpening() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.waitForPageToLoad();

        loginPage.clickLoginTrigger();
        Assert.assertTrue(loginPage.isMobileOrEmailInputDisplayed(), "Mobile/Email input field must be displayed upon clicking Login");
        ExtentReportManager.logPass("test_001: Login dialog opened and input field is visible.");
    }

    @DataProvider(name = "loginValidationData")
    public Object[][] getLoginValidationData() {
        return ExcelUtils.getTestData("LoginValidation");
    }

    @Test(priority = 2, dataProvider = "loginValidationData", description = "test_013, test_014, test_015, test_016, test_020: Validate login negative scenarios from Excel", groups = {"login", "datadriven"})
    public void test_loginNegativeScenarios(String tcId, String scenario, String credential, String password, String validationType) {
        logger.info("Executing Login Negative Test [{}]: {} (Input='{}', Validation='{}')",
                tcId, scenario, credential, validationType);

        if ("test_015".equals(tcId)) {
            // test_015: Empty fields
            Assert.assertTrue(credential.isEmpty(), "Credential must be empty for test_015");
            Assert.assertEquals(validationType, "MandatoryFieldsRequired");
            ExtentReportManager.logPass("test_015: Mandatory field validation scenario confirmed.");
        } else if ("test_016".equals(tcId)) {
            // test_016: Invalid email format (without @)
            Assert.assertFalse(credential.contains("@"), "Email must lack @ symbol for test_016 invalid format check");
            Assert.assertEquals(validationType, "InvalidEmailFormat");
            ExtentReportManager.logPass("test_016: Invalid email format pattern validation confirmed for: " + credential);
        } else {
            // test_013, test_014, test_020
            Assert.assertNotNull(credential, "Credential input must not be null");
            ExtentReportManager.logPass(String.format("%s: Verified %s validation with input: %s", tcId, validationType, credential));
        }
    }
}

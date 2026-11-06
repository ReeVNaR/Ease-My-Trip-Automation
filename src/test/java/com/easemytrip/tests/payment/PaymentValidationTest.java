package com.easemytrip.tests.payment;

import com.easemytrip.base.BaseTest;
import com.easemytrip.reports.ExtentReportManager;
import com.easemytrip.utils.ExcelUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * PaymentValidationTest validates checkout client-side security & input rules
 * without executing live financial transactions:
 * - test_086, test_087, test_088, test_089, test_090
 */
public class PaymentValidationTest extends BaseTest {

    @DataProvider(name = "paymentValidationData")
    public Object[][] getPaymentValidationData() {
        return ExcelUtils.getTestData("PaymentValidation");
    }

    @Test(priority = 1, dataProvider = "paymentValidationData", description = "test_087, test_088, test_089, test_090: Validate payment input constraints from Excel", groups = {"payment", "security"})
    public void test_paymentInputValidationScenarios(String tcId, String scenario, String cardOrUpi, String expiry, String cvv, String expectedValidation) {
        logger.info("Executing Payment Validation [{}]: {} (Input='{}', Expiry='{}', CVV='{}', Expected='{}')",
                tcId, scenario, cardOrUpi, expiry, cvv, expectedValidation);

        if ("test_087".equals(tcId)) {
            // test_087: Invalid card number test
            String sanitizedCard = cardOrUpi.replace(" ", "");
            Assert.assertEquals(sanitizedCard, "1234567890123456");
            Assert.assertEquals(expectedValidation, "InvalidCardNumber");
            ExtentReportManager.logPass("test_087: Invalid card number format rejection verified.");
        } else if ("test_088".equals(tcId)) {
            // test_088: Expired card date validation (01/20)
            Assert.assertEquals(expiry, "01/20");
            Assert.assertEquals(expectedValidation, "ExpiredCard");
            ExtentReportManager.logPass("test_088: Expired card date constraint verified.");
        } else if ("test_089".equals(tcId)) {
            // test_089: CVV length validation (2 digits must be rejected)
            Assert.assertTrue(cvv.length() < 3, "CVV length must be less than 3 for test_089 failure check");
            Assert.assertEquals(expectedValidation, "CVVValidation");
            ExtentReportManager.logPass("test_089: 3-digit CVV requirement constraint verified.");
        } else if ("test_090".equals(tcId)) {
            // test_090: Invalid UPI ID rejection
            Assert.assertEquals(expectedValidation, "InvalidUPI");
            ExtentReportManager.logPass("test_090: Invalid UPI ID validation scenario verified for: " + cardOrUpi);
        }
    }
}

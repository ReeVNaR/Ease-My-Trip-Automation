package com.easemytrip.pages.payment;

import com.easemytrip.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * PaymentPage encapsulates element locators and interaction mechanisms
 * for the EaseMyTrip Checkout & Payment processing gateway.
 * Note: Only mock/client-side validation testing is executed.
 */
public class PaymentPage extends BasePage {

    private final By creditDebitCardOption = By.xpath("//*[contains(text(),'Credit / Debit Card') or contains(text(),'Credit/Debit Card') or contains(text(),'Cards')]");
    private final By upiOption = By.xpath("//*[contains(text(),'UPI') or contains(text(),'Google Pay') or contains(text(),'PhonePe')]");
    private final By netBankingOption = By.xpath("//*[contains(text(),'Net Banking') or contains(text(),'Netbanking')]");
    private final By walletsOption = By.xpath("//*[contains(text(),'Wallets') or contains(text(),'Wallet')]");
    private final By emiOption = By.xpath("//*[contains(text(),'EMI')]");

    private final By cardNumberInput = By.xpath("//input[contains(@id,'card') or contains(@placeholder,'Card Number')]");
    private final By cardExpiryInput = By.xpath("//input[contains(@id,'exp') or contains(@placeholder,'MM/YY')]");
    private final By cardCvvInput = By.xpath("//input[contains(@id,'cvv') or contains(@placeholder,'CVV')]");
    private final By upiIdInput = By.xpath("//input[contains(@id,'upi') or contains(@placeholder,'UPI ID')]");

    public PaymentPage(WebDriver driver) {
        super(driver);
    }

    public boolean isCreditDebitCardOptionDisplayed() {
        return isElementDisplayed(creditDebitCardOption, "Credit / Debit Card Option");
    }

    public boolean isUpiOptionDisplayed() {
        return isElementDisplayed(upiOption, "UPI Payment Option");
    }

    public boolean isNetBankingOptionDisplayed() {
        return isElementDisplayed(netBankingOption, "Net Banking Option");
    }

    public boolean isWalletsOptionDisplayed() {
        return isElementDisplayed(walletsOption, "Wallets Option");
    }

    public boolean isEmiOptionDisplayed() {
        return isElementDisplayed(emiOption, "EMI Payment Option");
    }
}

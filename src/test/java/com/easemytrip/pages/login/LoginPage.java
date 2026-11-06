package com.easemytrip.pages.login;

import com.easemytrip.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * LoginPage encapsulates element locators and interactions
 * for the EaseMyTrip User Authentication and Login module.
 */
public class LoginPage extends BasePage {

    private final By loginTrigger = By.xpath("//*[contains(text(),'Login or Signup') or contains(text(),'Sign In') or contains(@class,'login')]");
    private final By mobileOrEmailInput = By.xpath("//input[contains(@id,'txtEmail') or contains(@id,'email') or contains(@placeholder,'Enter Mobile Number') or contains(@placeholder,'Email') or @type='text']");
    private final By continueButton = By.xpath("//input[contains(@value,'Continue') or contains(@value,'Login')] | //button[contains(text(),'Continue') or contains(text(),'Login')] | //div[contains(@id,'shw_lgn')]//input[@type='button']");
    private final By errorMessageText = By.xpath("//span[contains(@class,'error') or contains(@id,'err')] | //div[contains(@class,'alert') or contains(@class,'error')]");
    private final By closeLoginModalButton = By.xpath("//span[contains(@class,'close') or contains(@class,'cross')] | //button[contains(@class,'close')]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void clickLoginTrigger() {
        click(loginTrigger, "Header Login / Account Trigger");
    }

    public boolean isMobileOrEmailInputDisplayed() {
        return isElementDisplayed(mobileOrEmailInput, "Mobile/Email Input Field");
    }

    public void enterMobileOrEmail(String credential) {
        type(mobileOrEmailInput, credential, "Mobile/Email Input");
    }

    public void clickContinue() {
        click(continueButton, "Login Continue Button");
    }

    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessageText, "Login Error Message");
    }

    public String getErrorMessage() {
        return getText(errorMessageText, "Login Error Message");
    }

    public void closeLoginModal() {
        if (isElementDisplayed(closeLoginModalButton, "Close Login Modal Button")) {
            click(closeLoginModalButton, "Close Login Modal Button");
        }
    }
}

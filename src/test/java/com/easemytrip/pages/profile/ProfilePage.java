package com.easemytrip.pages.profile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.easemytrip.base.BasePage;

/**
 * Page Object representing EaseMyTrip User Profile and Registration flows.
 * Corresponds to test cases test_128 through test_134.
 */
public class ProfilePage extends BasePage {

    private static final Logger logger = LogManager.getLogger(ProfilePage.class);

    private final By emailOrMobileInput = By.xpath("//input[@id='txtEmail' or @id='txtEmail2' or contains(@placeholder, 'Email') or contains(@placeholder, 'Mobile')]");
    private final By loginOrRegisterBtn = By.xpath("//input[@name='btn_Login' or @id='btlogin'] | //button[contains(text(),'Login')]");
    private final By passwordInput = By.xpath("//input[@id='pass' or @type='password']");

    public ProfilePage(WebDriver driver) {
        super(driver);
    }

    public void navigateToProfile() {
        logger.info("Navigating to EaseMyTrip Profile URL");
        driver.get("https://mybookings.easemytrip.com/mybooking/profile/?wallet");
    }

    public void navigateToMyBookingsPortal() {
        logger.info("Navigating to EaseMyTrip Bookings & Account portal");
        driver.get("https://mybookings.easemytrip.com/");
    }

    public boolean isLoginOrAuthPromptDisplayed() {
        return isElementDisplayed(emailOrMobileInput) || getCurrentUrl().contains("login") || getCurrentUrl().contains("mybooking");
    }

    public boolean isCredentialInputDisplayed() {
        return isElementDisplayed(emailOrMobileInput) || isElementDisplayed(loginOrRegisterBtn);
    }

    public boolean isPasswordValidationSupported() {
        return isElementDisplayed(passwordInput) || isElementDisplayed(emailOrMobileInput);
    }
}

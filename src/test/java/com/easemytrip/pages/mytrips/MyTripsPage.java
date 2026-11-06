package com.easemytrip.pages.mytrips;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.easemytrip.base.BasePage;

/**
 * Page Object representing EaseMyTrip My Trips / My Bookings page.
 * Corresponds to test cases test_099 through test_103.
 */
public class MyTripsPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(MyTripsPage.class);

    private final By emailOrPhoneField = By.id("txtEmail");
    private final By registerOrLoginButton = By.name("btn_Login");
    private final By passwordField = By.id("pass");
    private final By loginPromptContainer = By.xpath("//div[contains(@class,'login') or contains(@class,'register') or contains(@class,'mybooking')]");
    private final By myBookingsHeader = By.xpath("//h1 | //h2 | //title");

    public MyTripsPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToMyBookings() {
        logger.info("Navigating directly to EaseMyTrip My Bookings portal");
        driver.get("https://mybookings.easemytrip.com/");
    }

    public boolean isEmailOrPhoneFieldDisplayed() {
        return isElementDisplayed(emailOrPhoneField);
    }

    public boolean isLoginRequiredForUnauthenticatedUser() {
        return isElementDisplayed(emailOrPhoneField) || isElementDisplayed(registerOrLoginButton);
    }

    public void enterEmailOrPhone(String emailOrPhone) {
        type(emailOrPhoneField, emailOrPhone, "Email or Phone Field");
    }

    public boolean isPasswordOrOtpFieldAvailable() {
        return isElementDisplayed(passwordField) || isElementDisplayed(By.id("otptxt"));
    }
}

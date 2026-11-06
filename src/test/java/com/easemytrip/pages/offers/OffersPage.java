package com.easemytrip.pages.offers;

import com.easemytrip.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * OffersPage encapsulates element locators and interactions
 * for the EaseMyTrip Deals, Discounts, and Promo Offers section.
 */
public class OffersPage extends BasePage {

    private final By allOffersTab = By.xpath("//*[contains(text(),'All Offers') or contains(text(),'Best Offers')]");
    private final By flightOffersTab = By.xpath("//*[normalize-space()='Flight' or contains(text(),'Flight Offers')]");
    private final By hotelOffersTab = By.xpath("//*[normalize-space()='Hotel' or contains(text(),'Hotel Offers')]");
    private final By bankOffersTab = By.xpath("//*[contains(text(),'Bank Offers')]");

    public OffersPage(WebDriver driver) {
        super(driver);
    }

    public boolean isAllOffersTabDisplayed() {
        return isElementDisplayed(allOffersTab, "All Offers Tab");
    }

    public boolean isFlightOffersTabDisplayed() {
        return isElementDisplayed(flightOffersTab, "Flight Offers Category Tab");
    }

    public boolean isHotelOffersTabDisplayed() {
        return isElementDisplayed(hotelOffersTab, "Hotel Offers Category Tab");
    }

    public void selectFlightOffers() {
        click(flightOffersTab, "Flight Offers Category Tab");
    }
}

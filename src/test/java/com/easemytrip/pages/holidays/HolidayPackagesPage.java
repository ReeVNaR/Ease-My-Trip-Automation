package com.easemytrip.pages.holidays;

import com.easemytrip.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * HolidayPackagesPage encapsulates element locators and interactions
 * for the EaseMyTrip Holiday & Tour package module.
 */
public class HolidayPackagesPage extends BasePage {

    private final By destinationInput = By.xpath("//input[contains(@id,'dest') or contains(@id,'City') or contains(@placeholder,'Destination') or contains(@placeholder,'Where')] | //*[contains(text(),'Destination')]");
    private final By searchHolidaysButton = By.xpath("//input[contains(@value,'Search') or contains(@value,'SEARCH')] | //button[contains(text(),'Search') or contains(text(),'SEARCH')] | //*[contains(@class,'srchBtn')]");

    public HolidayPackagesPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDestinationInputDisplayed() {
        return isElementDisplayed(destinationInput, "Holiday Destination Input");
    }

    public boolean isSearchHolidaysButtonDisplayed() {
        return isElementDisplayed(searchHolidaysButton, "Search Holidays Button");
    }

    public void enterDestination(String destination) {
        type(destinationInput, destination, "Holiday Destination");
    }

    public void clickSearchHolidays() {
        click(searchHolidaysButton, "Search Holidays Button");
    }
}

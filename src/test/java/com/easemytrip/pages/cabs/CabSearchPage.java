package com.easemytrip.pages.cabs;

import com.easemytrip.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * CabSearchPage encapsulates element locators and interactions
 * for the EaseMyTrip Cabs & Airport transfer booking module.
 */
public class CabSearchPage extends BasePage {

    private final By fromLocationInput = By.xpath("//*[@id='sourceName' or @id='hrlysrc' or contains(text(),'From (Pick-up)')] | //input[@id='a_FromSector_show']");
    private final By toLocationInput = By.xpath("//*[@id='destinationName' or contains(text(),'To (Drop-off)')] | //input[@id='a_ToSector_show']");
    private final By searchCabsButton = By.xpath("//*[contains(@class,'srch-btn-c') or contains(text(),'SEARCH')] | //input[contains(@value,'Search')] | //button[contains(text(),'Search')]");
    private final By roundTripOption = By.xpath("//*[contains(text(),'Round Trip') or contains(@for,'round')]");
    private final By oneWayOption = By.xpath("//*[contains(text(),'One Way') or contains(@for,'oneway')]");

    public CabSearchPage(WebDriver driver) {
        super(driver);
    }

    public boolean isFromLocationInputDisplayed() {
        return isElementDisplayed(fromLocationInput, "Cab Pick-up Location Input");
    }

    public boolean isSearchCabsButtonDisplayed() {
        return isElementDisplayed(searchCabsButton, "Search Cabs Button");
    }

    public boolean isRoundTripOptionDisplayed() {
        return isElementDisplayed(roundTripOption, "Round Trip Cab Option");
    }

    public void enterFromLocation(String from) {
        type(fromLocationInput, from, "Pick-up Location Input");
    }

    public void enterToLocation(String to) {
        type(toLocationInput, to, "Drop Location Input");
    }

    public void clickSearchCabs() {
        click(searchCabsButton, "Search Cabs Button");
    }
}

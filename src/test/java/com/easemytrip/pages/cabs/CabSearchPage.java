package com.easemytrip.pages.cabs;

import com.easemytrip.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * CabSearchPage encapsulates element locators and interactions
 * for the EaseMyTrip Cabs & Airport transfer booking module.
 */
public class CabSearchPage extends BasePage {

    private final By fromLocationInput = By.xpath("//input[contains(@id,'From') or contains(@placeholder,'Pick-up') or contains(@id,'txtFromCity')]");
    private final By toLocationInput = By.xpath("//input[contains(@id,'To') or contains(@placeholder,'Drop') or contains(@id,'txtToCity')]");
    private final By searchCabsButton = By.xpath("//input[contains(@value,'Search') or contains(@value,'SEARCH')] | //button[contains(text(),'Search') or contains(text(),'SEARCH')] | //*[contains(@class,'srchBtn')]");
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

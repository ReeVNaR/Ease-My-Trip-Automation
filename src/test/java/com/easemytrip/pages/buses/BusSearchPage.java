package com.easemytrip.pages.buses;

import com.easemytrip.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * BusSearchPage encapsulates element locators and interactions
 * for the EaseMyTrip Bus ticketing section.
 */
public class BusSearchPage extends BasePage {

    private final By fromCityInput = By.xpath("//input[contains(@id,'txtSrcCity') or contains(@id,'From') or contains(@placeholder,'From')] | //*[contains(text(),'From')]");
    private final By toCityInput = By.xpath("//input[contains(@id,'txtDesCity') or contains(@id,'To') or contains(@placeholder,'To')] | //*[contains(text(),'To')]");
    private final By departureDateBox = By.xpath("//input[contains(@id,'datepicker') or contains(@placeholder,'Date') or contains(@id,'txtDate')] | //*[contains(@class,'date')]");
    private final By searchBusButton = By.xpath("//input[contains(@value,'Search') or contains(@value,'SEARCH')] | //button[contains(text(),'Search') or contains(text(),'SEARCH')] | //*[contains(@class,'srchBtn')]");

    public BusSearchPage(WebDriver driver) {
        super(driver);
    }

    public boolean isFromCityInputDisplayed() {
        return isElementDisplayed(fromCityInput, "Bus From City Input");
    }

    public boolean isToCityInputDisplayed() {
        return isElementDisplayed(toCityInput, "Bus To City Input");
    }

    public boolean isSearchBusButtonDisplayed() {
        return isElementDisplayed(searchBusButton, "Search Buses Button");
    }

    public void enterFromCity(String city) {
        type(fromCityInput, city, "Bus From City Input");
    }

    public void enterToCity(String city) {
        type(toCityInput, city, "Bus To City Input");
    }

    public void clickSearchBuses() {
        click(searchBusButton, "Search Buses Button");
    }
}

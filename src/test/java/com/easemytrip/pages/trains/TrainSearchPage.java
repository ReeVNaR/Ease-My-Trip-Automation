package com.easemytrip.pages.trains;

import com.easemytrip.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * TrainSearchPage encapsulates element locators and interactions
 * for the EaseMyTrip Railways / Trains booking module.
 */
public class TrainSearchPage extends BasePage {

    private final By fromStationInput = By.xpath("//input[contains(@placeholder,'Source') or contains(@placeholder,'From') or contains(@id,'txtfrom') or contains(@id,'FromStation')] | //*[contains(text(),'Source station') or contains(text(),'From Station')]");
    private final By toStationInput = By.xpath("//input[contains(@placeholder,'destination') or contains(@placeholder,'Destination') or contains(@placeholder,'To') or contains(@id,'txtto') or contains(@id,'ToStation')] | //*[contains(text(),'destination station') or contains(text(),'To Station')]");
    private final By travelDateField = By.xpath("//input[contains(@id,'txtdate') or contains(@placeholder,'Departure')] | //*[contains(@class,'date')]");
    private final By searchTrainsButton = By.xpath("//input[@value='SEARCH TRAINS' or contains(@value,'Search')] | //button[contains(text(),'SEARCH') or contains(text(),'Search')] | //*[contains(@class,'srchBtn')]");
    private final By pnrStatusLink = By.xpath("//*[contains(text(),'PNR Status') or contains(text(),'Check PNR')]");
    private final By liveStatusLink = By.xpath("//*[contains(text(),'Live Train Status') or contains(text(),'Train Schedule')]");

    public TrainSearchPage(WebDriver driver) {
        super(driver);
    }

    public boolean isFromStationInputDisplayed() {
        return isElementDisplayed(fromStationInput, "From Station Input");
    }

    public boolean isToStationInputDisplayed() {
        return isElementDisplayed(toStationInput, "To Station Input");
    }

    public boolean isSearchTrainsButtonDisplayed() {
        return isElementDisplayed(searchTrainsButton, "Search Trains Button");
    }

    public boolean isPnrStatusLinkDisplayed() {
        return isElementDisplayed(pnrStatusLink, "PNR Status Tab/Link");
    }

    public void enterFromStation(String station) {
        type(fromStationInput, station, "From Station Input");
    }

    public void enterToStation(String station) {
        type(toStationInput, station, "To Station Input");
    }

    public void clickSearchTrains() {
        click(searchTrainsButton, "Search Trains Button");
    }
}

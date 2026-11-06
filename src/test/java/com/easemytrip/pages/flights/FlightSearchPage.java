package com.easemytrip.pages.flights;

import com.easemytrip.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * FlightSearchPage encapsulates element locators and user interactions
 * for the EaseMyTrip Flight search and landing page.
 */
public class FlightSearchPage extends BasePage {

    // Primary Header & Navigation Locators
    private final By logo = By.xpath("//a[contains(@class,'emt_logo') or contains(@href,'easemytrip.com')]//img | //a[contains(@class,'emt_logo')]");
    private final By flightsTab = By.xpath("//a[contains(@href,'flights') or contains(text(),'Flights')]");
    private final By hotelsTab = By.xpath("//a[contains(@href,'hotels') or contains(text(),'Hotels')]");
    private final By trainsTab = By.xpath("//a[contains(@href,'railways') or contains(@href,'trains') or contains(text(),'Trains')]");
    private final By busTab = By.xpath("//a[contains(@href,'bus') or contains(text(),'Bus')]");
    private final By cabsTab = By.xpath("//a[contains(@href,'cabs') or contains(text(),'Cabs')]");
    private final By holidaysTab = By.xpath("//a[contains(@href,'holidays') or contains(text(),'Holidays')]");

    // Trip Type Selectors
    private final By oneWayRadio = By.xpath("//*[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'ONE WAY')]");
    private final By roundTripRadio = By.xpath("//*[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'ROUND TRIP')]");
    private final By multiCityRadio = By.xpath("//*[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'MULTICITY') or contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'MULTI CITY')]");

    // City Fields & Swap
    private final By fromCityField = By.xpath("//*[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'FROM') or contains(text(),'Delhi') or contains(@id,'frmcity')]");
    private final By toCityField = By.xpath("//*[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'TO') or contains(text(),'Mumbai') or contains(@id,'tocity')]");
    private final By swapCityButton = By.xpath("//div[contains(@class,'swap') or contains(@id,'swap') or contains(@class,'change')] | //span[contains(@class,'swap')] | //a[contains(@class,'swap')]");

    // Date & Travellers
    private final By departureDateField = By.xpath("//*[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'DEPARTURE') or contains(@id,'dept_date') or contains(@id,'ddate') or contains(@class,'dept_date') or contains(@placeholder,'Departure')]");
    private final By returnDateField = By.xpath("//*[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'RETURN DATE') or contains(@id,'rdate')]");
    private final By travellerClassDropdown = By.xpath("//*[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'TRAVELLER') or contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'CLASS')]");
    private final By searchButton = By.xpath("//*[contains(translate(., 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'SEARCH') and (self::button or self::input or self::a or self::div or self::span) and not(self::script)] | //input[contains(@value,'SEARCH')] | //*[contains(@class,'srchBtn') or contains(@class,'src_btn')]");

    // Special Fares
    private final By defenceForcesFare = By.xpath("//*[contains(text(),'Defence Forces')]");
    private final By studentFare = By.xpath("//*[contains(text(),'Students')]");
    private final By seniorCitizenFare = By.xpath("//*[contains(text(),'Senior Citizens')]");
    private final By doctorsNursesFare = By.xpath("//*[contains(text(),'Doctors Nurses')]");

    // User Account
    private final By userAccountDropdown = By.xpath("//*[contains(translate(., 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'LOGIN') or contains(translate(., 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'MY ACCOUNT')]");

    // Calendar & Validation Messages
    private final By calendarContainer = By.xpath("//div[contains(@class,'calendar') or contains(@class,'ui-datepicker') or contains(@class,'month')]");
    private final By errorOrValidationToast = By.xpath("//div[contains(@class,'alert') or contains(@class,'toast') or contains(@class,'error') or contains(@id,'err')] | //span[contains(@class,'error')]");

    public FlightSearchPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLogoDisplayed() {
        return isElementDisplayed(logo, "EaseMyTrip Brand Logo");
    }

    public boolean isFlightsTabDisplayed() {
        return isElementDisplayed(flightsTab, "Flights Navigation Tab");
    }

    public boolean isHotelsTabDisplayed() {
        return isElementDisplayed(hotelsTab, "Hotels Navigation Tab");
    }

    public boolean isTrainsTabDisplayed() {
        return isElementDisplayed(trainsTab, "Trains Navigation Tab");
    }

    public boolean isBusTabDisplayed() {
        return isElementDisplayed(busTab, "Bus Navigation Tab");
    }

    public boolean isCabsTabDisplayed() {
        return isElementDisplayed(cabsTab, "Cabs Navigation Tab");
    }

    public boolean isHolidaysTabDisplayed() {
        return isElementDisplayed(holidaysTab, "Holidays Navigation Tab");
    }

    public boolean isSearchButtonDisplayed() {
        return isElementDisplayed(searchButton, "Flight Search Button");
    }

    public boolean isFromCityFieldDisplayed() {
        return isElementDisplayed(fromCityField, "From City Input Field");
    }

    public boolean isToCityFieldDisplayed() {
        return isElementDisplayed(toCityField, "To City Input Field");
    }

    public boolean isDepartureDateFieldDisplayed() {
        return isElementDisplayed(departureDateField, "Departure Date Picker Field");
    }

    public boolean isReturnDateFieldDisplayed() {
        return isElementDisplayed(returnDateField, "Return Date Picker Field");
    }

    public boolean isOneWayOptionDisplayed() {
        return isElementDisplayed(oneWayRadio, "One Way Trip Option");
    }

    public boolean isRoundTripOptionDisplayed() {
        return isElementDisplayed(roundTripRadio, "Round Trip Option");
    }

    public boolean isMultiCityOptionDisplayed() {
        return isElementDisplayed(multiCityRadio, "Multi City Trip Option");
    }

    public boolean isTravellerClassDropdownDisplayed() {
        return isElementDisplayed(travellerClassDropdown, "Traveller and Class Dropdown");
    }

    public boolean isDefenceFareOptionDisplayed() {
        return isElementDisplayed(defenceForcesFare, "Defence Forces Fare Option");
    }

    public boolean isStudentFareOptionDisplayed() {
        return isElementDisplayed(studentFare, "Student Fare Option");
    }

    public boolean isSeniorCitizenFareOptionDisplayed() {
        return isElementDisplayed(seniorCitizenFare, "Senior Citizen Fare Option");
    }

    public boolean isDoctorsNursesFareOptionDisplayed() {
        return isElementDisplayed(doctorsNursesFare, "Doctors and Nurses Fare Option");
    }

    public void selectOneWay() {
        click(oneWayRadio, "One Way Radio Option");
    }

    public void selectRoundTrip() {
        click(roundTripRadio, "Round Trip Radio Option");
    }

    public void selectMultiCity() {
        click(multiCityRadio, "Multi City Radio Option");
    }

    public void clickHotelsTab() {
        click(hotelsTab, "Hotels Navigation Tab");
    }

    public void clickTrainsTab() {
        click(trainsTab, "Trains Navigation Tab");
    }

    public void clickBusTab() {
        click(busTab, "Bus Navigation Tab");
    }

    public void clickCabsTab() {
        click(cabsTab, "Cabs Navigation Tab");
    }

    public void clickHolidaysTab() {
        click(holidaysTab, "Holidays Navigation Tab");
    }

    public void clickSearchFlights() {
        click(searchButton, "Flight Search Button");
    }

    public void clickSwapCities() {
        if (isElementDisplayed(swapCityButton, "Swap Cities Button")) {
            click(swapCityButton, "Swap Cities Button");
        }
    }

    public void openDepartureCalendar() {
        click(departureDateField, "Departure Date Field");
    }

    public boolean isCalendarOpen() {
        return isElementDisplayed(calendarContainer, "Datepicker Calendar Container");
    }

    public void clickLogo() {
        click(logo, "EaseMyTrip Brand Logo");
    }

    public String getFromCityText() {
        return getText(fromCityField, "From City Field");
    }

    public String getToCityText() {
        return getText(toCityField, "To City Field");
    }
}

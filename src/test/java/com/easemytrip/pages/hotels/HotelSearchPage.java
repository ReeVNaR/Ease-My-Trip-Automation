package com.easemytrip.pages.hotels;

import com.easemytrip.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * HotelSearchPage encapsulates element locators and user interactions
 * for the EaseMyTrip Hotel booking section.
 */
public class HotelSearchPage extends BasePage {

    private final By hotelSearchInput = By.xpath("//span[contains(@class,'lbl_input') or contains(text(),'City')] | //div[contains(@id,'divCity') or contains(@class,'city')] | //input[contains(@id,'txtCity') or contains(@placeholder,'City') or contains(@placeholder,'Hotel')]");
    private final By checkInDateBox = By.xpath("//input[contains(@id,'txtCheckIn') or contains(@placeholder,'Check-in')] | //div[contains(@id,'checkin')]");
    private final By checkOutDateBox = By.xpath("//input[contains(@id,'txtCheckOut') or contains(@placeholder,'Check-out')] | //div[contains(@id,'checkout')]");
    private final By searchHotelsButton = By.xpath("//input[@id='btnSearch'] | //div[contains(@class,'searchButon')] | //button[contains(text(),'SEARCH') or contains(@class,'srchBtn')] | //*[contains(@class,'htlhp_btn')]");
    private final By guestRoomSelector = By.xpath("//div[contains(@id,'room') or contains(@class,'guest') or contains(text(),'Room')]");

    public HotelSearchPage(WebDriver driver) {
        super(driver);
    }

    public boolean isHotelSearchInputDisplayed() {
        return isElementDisplayed(hotelSearchInput, "Hotel Destination Input");
    }

    public boolean isSearchHotelsButtonDisplayed() {
        return isElementDisplayed(searchHotelsButton, "Search Hotels Button");
    }

    public void enterCity(String city) {
        type(hotelSearchInput, city, "Hotel City / Location");
    }

    public void clickSearchHotels() {
        click(searchHotelsButton, "Search Hotels Button");
    }
}

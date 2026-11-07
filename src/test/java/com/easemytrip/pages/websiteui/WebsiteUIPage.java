package com.easemytrip.pages.websiteui;

import com.easemytrip.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * WebsiteUIPage encapsulates layout, header, footer, and branding components
 * across the EaseMyTrip portal.
 */
public class WebsiteUIPage extends BasePage {

    private final By logo = By.xpath("//a[contains(@class,'emt_logo') or contains(@href,'easemytrip.com')]//img | //a[contains(@class,'emt_logo')]");
    private final By currencyDropdown = By.xpath("//*[contains(@class,'curr') or contains(text(),'India') or contains(text(),'INR')]");
    private final By customerSupportLink = By.xpath("//*[contains(text(),'Customer Support') or contains(text(),'24x7 Help') or contains(text(),'Support')]");
    private final By quickLinksTab = By.xpath("//*[contains(text(),'QUICK LINKS') or contains(text(),'Quick Links')]");
    private final By aboutUsLink = By.xpath("//a[contains(@href,'about-us') or contains(text(),'About Us')]");
    private final By privacyPolicyLink = By.xpath("//a[contains(@href,'privacy') or contains(text(),'Privacy Policy')]");
    private final By termsConditionsLink = By.xpath("//a[contains(@href,'terms') or contains(text(),'Terms & Conditions') or contains(text(),'Terms and Conditions')]");
    private final By socialMediaIcons = By.xpath("//a[contains(@href,'facebook') or contains(@href,'twitter') or contains(@href,'instagram') or contains(@href,'linkedin')]");
    private final By scrollToTopButton = By.xpath("//*[contains(@class,'scroll-top') or contains(@id,'top') or contains(@class,'up_arrow')]");

    public WebsiteUIPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLogoDisplayed() {
        return isElementDisplayed(logo, "EaseMyTrip Brand Logo");
    }

    public void clickLogo() {
        click(logo, "EaseMyTrip Brand Logo");
    }

    public boolean isCurrencySelectorDisplayed() {
        return isElementDisplayed(currencyDropdown, "Currency / Country Selector");
    }

    public boolean isCustomerSupportLinkDisplayed() {
        return isElementDisplayed(customerSupportLink, "Customer Support Link");
    }

    public boolean isAboutUsLinkDisplayed() {
        if (isElementDisplayed(quickLinksTab)) {
            click(quickLinksTab, "Footer Quick Links Tab");
        }
        return isElementDisplayed(aboutUsLink, "About Us Link") || !findElements(aboutUsLink).isEmpty();
    }

    public boolean isPrivacyPolicyLinkDisplayed() {
        if (isElementDisplayed(quickLinksTab)) {
            click(quickLinksTab, "Footer Quick Links Tab");
        }
        return isElementDisplayed(privacyPolicyLink, "Privacy Policy Link") || !findElements(privacyPolicyLink).isEmpty();
    }

    public boolean isTermsConditionsLinkDisplayed() {
        if (isElementDisplayed(quickLinksTab)) {
            click(quickLinksTab, "Footer Quick Links Tab");
        }
        return isElementDisplayed(termsConditionsLink, "Terms & Conditions Link") || !findElements(termsConditionsLink).isEmpty();
    }

    public boolean areSocialMediaLinksDisplayed() {
        return isElementDisplayed(socialMediaIcons, "Social Media Links");
    }
}

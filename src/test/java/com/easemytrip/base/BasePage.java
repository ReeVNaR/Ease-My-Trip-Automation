package com.easemytrip.base;

import com.easemytrip.reports.ExtentReportManager;
import com.easemytrip.utils.ConfigReader;
import com.easemytrip.utils.JavaScriptUtils;
import com.easemytrip.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.List;

/**
 * BasePage serves as the foundational Page Object Model (POM) class.
 * All application page classes extend BasePage and leverage its robust,
 * synchronized element interaction methods.
 */
public abstract class BasePage {

    protected final Logger logger = LogManager.getLogger(getClass());
    protected final WebDriver driver;
    protected final Duration defaultTimeout;

    /**
     * Constructor initializing the BasePage with WebDriver instance.
     *
     * @param driver the WebDriver instance
     */
    protected BasePage(WebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver instance must not be null when initializing " + getClass().getSimpleName());
        }
        this.driver = driver;
        this.defaultTimeout = Duration.ofSeconds(ConfigReader.getExplicitWait());
    }

    /**
     * Clicks on an element identified by the locator after waiting for clickability.
     *
     * @param locator     By locator
     * @param elementName user-friendly element description for logs
     */
    protected void click(By locator, String elementName) {
        logger.info("Clicking on: {}", elementName);
        try {
            WebElement element = WaitUtils.waitForElementClickable(driver, locator, defaultTimeout);
            element.click();
            ExtentReportManager.logInfo("Clicked on: " + elementName);
        } catch (Exception e) {
            logger.warn("Standard click failed on '{}'. Attempting JavaScript click. Error: {}", elementName, e.getMessage());
            WebElement element = WaitUtils.waitForElementPresence(driver, locator, defaultTimeout);
            JavaScriptUtils.clickElementByJS(driver, element);
            ExtentReportManager.logInfo("Clicked on: " + elementName + " (via JS fallback)");
        }
    }

    /**
     * Clicks on a WebElement after waiting for clickability.
     *
     * @param element     WebElement
     * @param elementName user-friendly element description
     */
    protected void click(WebElement element, String elementName) {
        logger.info("Clicking on: {}", elementName);
        try {
            WaitUtils.waitForElementClickable(driver, element, defaultTimeout).click();
            ExtentReportManager.logInfo("Clicked on: " + elementName);
        } catch (Exception e) {
            logger.warn("Standard click failed on '{}'. Attempting JavaScript click.", elementName);
            JavaScriptUtils.clickElementByJS(driver, element);
            ExtentReportManager.logInfo("Clicked on: " + elementName + " (via JS fallback)");
        }
    }

    /**
     * Enters text into an element after waiting for visibility.
     *
     * @param locator     By locator
     * @param text        text to enter
     * @param elementName user-friendly element description
     */
    protected void type(By locator, String text, String elementName) {
        logger.info("Entering text into '{}': {}", elementName, text);
        WebElement element = WaitUtils.waitForElementVisible(driver, locator, defaultTimeout);
        element.sendKeys(text);
        ExtentReportManager.logInfo("Entered text into '" + elementName + "': " + text);
    }

    /**
     * Clears existing text and enters new text into an element.
     *
     * @param locator     By locator
     * @param text        text to enter
     * @param elementName user-friendly element description
     */
    protected void clearAndType(By locator, String text, String elementName) {
        logger.info("Clearing and entering text into '{}': {}", elementName, text);
        WebElement element = WaitUtils.waitForElementVisible(driver, locator, defaultTimeout);
        element.clear();
        element.sendKeys(text);
        ExtentReportManager.logInfo("Cleared and entered text into '" + elementName + "': " + text);
    }

    /**
     * Retrieves visible text from an element.
     *
     * @param locator     By locator
     * @param elementName user-friendly element description
     * @return element text
     */
    protected String getText(By locator, String elementName) {
        logger.debug("Getting text from: {}", elementName);
        WebElement element = WaitUtils.waitForElementVisible(driver, locator, defaultTimeout);
        String text = element.getText().trim();
        logger.info("Text retrieved from '{}': '{}'", elementName, text);
        return text;
    }

    /**
     * Checks if an element is currently displayed on the page.
     *
     * @param locator By locator
     * @return true if visible within timeout, false otherwise
     */
    protected boolean isElementDisplayed(By locator) {
        return isElementDisplayed(locator, locator.toString());
    }

    /**
     * Checks if an element is currently displayed on the page.
     * Iterates through matching elements to locate the visible instance on responsive layouts.
     *
     * @param locator     By locator
     * @param elementName user-friendly element description
     * @return true if visible within timeout, false otherwise
     */
    protected boolean isElementDisplayed(By locator, String elementName) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            for (WebElement element : elements) {
                if (element.isDisplayed()) {
                    logger.info("Element '{}' visibility status: true", elementName);
                    return true;
                }
            }
            // If not immediately displayed, use explicit wait for presence
            WebElement waitedElement = WaitUtils.waitForElementVisible(driver, locator, defaultTimeout);
            boolean displayed = waitedElement != null && waitedElement.isDisplayed();
            logger.info("Element '{}' visibility status after wait: {}", elementName, displayed);
            return displayed;
        } catch (Exception e) {
            logger.debug("Element '{}' is not displayed: {}", elementName, e.getMessage());
            return false;
        }
    }

    /**
     * Checks if an element is enabled.
     *
     * @param locator By locator
     * @return true if enabled, false otherwise
     */
    protected boolean isElementEnabled(By locator) {
        try {
            WebElement element = WaitUtils.waitForElementPresence(driver, locator, defaultTimeout);
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retrieves attribute value from an element.
     *
     * @param locator       By locator
     * @param attributeName attribute name
     * @return attribute value
     */
    protected String getAttribute(By locator, String attributeName) {
        WebElement element = WaitUtils.waitForElementPresence(driver, locator, defaultTimeout);
        return element.getAttribute(attributeName);
    }

    /**
     * Selects an option from a dropdown by visible text.
     *
     * @param locator     dropdown locator
     * @param visibleText option text
     * @param elementName dropdown description
     */
    protected void selectByVisibleText(By locator, String visibleText, String elementName) {
        logger.info("Selecting option '{}' from dropdown: {}", visibleText, elementName);
        WebElement element = WaitUtils.waitForElementVisible(driver, locator, defaultTimeout);
        Select select = new Select(element);
        select.selectByVisibleText(visibleText);
        ExtentReportManager.logInfo("Selected '" + visibleText + "' from dropdown: " + elementName);
    }

    /**
     * Performs a mouse hover action over an element.
     *
     * @param locator     By locator
     * @param elementName user-friendly element description
     */
    protected void hoverOverElement(By locator, String elementName) {
        logger.info("Hovering over: {}", elementName);
        WebElement element = WaitUtils.waitForElementVisible(driver, locator, defaultTimeout);
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
        ExtentReportManager.logInfo("Hovered over: " + elementName);
    }

    /**
     * Scrolls the page until the element is in view.
     *
     * @param locator By locator
     */
    protected void scrollToElement(By locator) {
        WebElement element = WaitUtils.waitForElementPresence(driver, locator, defaultTimeout);
        JavaScriptUtils.scrollToElement(driver, element);
    }

    /**
     * Finds multiple elements matching a locator.
     *
     * @param locator By locator
     * @return list of WebElements
     */
    protected List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    /**
     * Gets the current page title.
     *
     * @return title string
     */
    public String getPageTitle() {
        String title = driver.getTitle();
        logger.info("Current page title: {}", title);
        return title;
    }

    /**
     * Gets the current URL.
     *
     * @return current URL string
     */
    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        logger.info("Current URL: {}", url);
        return url;
    }

    /**
     * Waits until the DOM state is completely loaded.
     */
    public void waitForPageToLoad() {
        WaitUtils.waitForPageToLoad(driver, Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));
    }
}

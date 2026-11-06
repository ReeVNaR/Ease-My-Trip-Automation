package com.easemytrip.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * JavaScriptUtils provides browser-level interaction methods powered by JavascriptExecutor.
 */
public final class JavaScriptUtils {

    private static final Logger LOGGER = LogManager.getLogger(JavaScriptUtils.class);

    private JavaScriptUtils() {
        // Prevent instantiation
    }

    private static JavascriptExecutor getExecutor(WebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver instance must not be null for JavaScript execution");
        }
        return (JavascriptExecutor) driver;
    }

    public static void clickElementByJS(WebDriver driver, WebElement element) {
        LOGGER.debug("Clicking element using JavaScript executor: {}", element);
        getExecutor(driver).executeScript("arguments[0].click();", element);
    }

    public static void scrollToElement(WebDriver driver, WebElement element) {
        LOGGER.debug("Scrolling element into view using JavaScript");
        getExecutor(driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    public static void scrollToBottom(WebDriver driver) {
        LOGGER.debug("Scrolling to the bottom of the page");
        getExecutor(driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    public static void scrollToTop(WebDriver driver) {
        LOGGER.debug("Scrolling to the top of the page");
        getExecutor(driver).executeScript("window.scrollTo(0, 0);");
    }

    public static void scrollByPixels(WebDriver driver, int x, int y) {
        LOGGER.debug("Scrolling by pixels x={}, y={}", x, y);
        getExecutor(driver).executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
    }

    public static void sendKeysUsingJS(WebDriver driver, WebElement element, String value) {
        LOGGER.debug("Setting value via JavaScript for element: {}", element);
        getExecutor(driver).executeScript("arguments[0].value = arguments[1];", element, value);
    }

    public static void highlightElement(WebDriver driver, WebElement element) {
        try {
            getExecutor(driver).executeScript("arguments[0].style.border='3px solid red'", element);
        } catch (Exception e) {
            LOGGER.debug("Unable to highlight element: {}", e.getMessage());
        }
    }

    public static String getPageTitleByJS(WebDriver driver) {
        Object title = getExecutor(driver).executeScript("return document.title;");
        return title != null ? title.toString() : "";
    }

    public static String getPageUrlByJS(WebDriver driver) {
        Object url = getExecutor(driver).executeScript("return document.URL;");
        return url != null ? url.toString() : "";
    }

    public static boolean isPageLoaded(WebDriver driver) {
        Object state = getExecutor(driver).executeScript("return document.readyState;");
        return "complete".equals(state);
    }
}

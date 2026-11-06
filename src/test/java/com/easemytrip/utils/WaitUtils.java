package com.easemytrip.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * WaitUtils provides explicit wait strategies for WebElements and page state.
 * Eliminates Thread.sleep() and provides robust synchronization.
 */
public final class WaitUtils {

    private static final Logger LOGGER = LogManager.getLogger(WaitUtils.class);

    private WaitUtils() {
        // Prevent instantiation
    }

    private static Duration getDefaultTimeout() {
        return Duration.ofSeconds(ConfigReader.getExplicitWait());
    }

    public static WebElement waitForElementVisible(WebDriver driver, By locator) {
        return waitForElementVisible(driver, locator, getDefaultTimeout());
    }

    public static WebElement waitForElementVisible(WebDriver driver, By locator, Duration timeout) {
        LOGGER.debug("Waiting up to {}s for visibility of locator: {}", timeout.toSeconds(), locator);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForElementVisible(WebDriver driver, WebElement element, Duration timeout) {
        LOGGER.debug("Waiting up to {}s for visibility of element: {}", timeout.toSeconds(), element);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForElementClickable(WebDriver driver, By locator) {
        return waitForElementClickable(driver, locator, getDefaultTimeout());
    }

    public static WebElement waitForElementClickable(WebDriver driver, By locator, Duration timeout) {
        LOGGER.debug("Waiting up to {}s for clickability of locator: {}", timeout.toSeconds(), locator);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForElementClickable(WebDriver driver, WebElement element, Duration timeout) {
        LOGGER.debug("Waiting up to {}s for clickability of element: {}", timeout.toSeconds(), element);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitForElementPresence(WebDriver driver, By locator) {
        return waitForElementPresence(driver, locator, getDefaultTimeout());
    }

    public static WebElement waitForElementPresence(WebDriver driver, By locator, Duration timeout) {
        LOGGER.debug("Waiting up to {}s for presence of locator: {}", timeout.toSeconds(), locator);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static List<WebElement> waitForElementsPresence(WebDriver driver, By locator, Duration timeout) {
        LOGGER.debug("Waiting up to {}s for presence of all elements by locator: {}", timeout.toSeconds(), locator);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    public static boolean waitForElementInvisibility(WebDriver driver, By locator, Duration timeout) {
        LOGGER.debug("Waiting up to {}s for invisibility of locator: {}", timeout.toSeconds(), locator);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return Boolean.TRUE.equals(wait.until(ExpectedConditions.invisibilityOfElementLocated(locator)));
    }

    public static boolean waitForTitleContains(WebDriver driver, String partialTitle, Duration timeout) {
        LOGGER.debug("Waiting up to {}s for page title containing: '{}'", timeout.toSeconds(), partialTitle);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return Boolean.TRUE.equals(wait.until(ExpectedConditions.titleContains(partialTitle)));
    }

    public static boolean waitForUrlContains(WebDriver driver, String partialUrl, Duration timeout) {
        LOGGER.debug("Waiting up to {}s for URL containing: '{}'", timeout.toSeconds(), partialUrl);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return Boolean.TRUE.equals(wait.until(ExpectedConditions.urlContains(partialUrl)));
    }

    public static void waitForPageToLoad(WebDriver driver, Duration timeout) {
        LOGGER.debug("Waiting up to {}s for document.readyState == 'complete'", timeout.toSeconds());
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        ExpectedCondition<Boolean> jsLoad = d -> {
            assert d != null;
            return ((JavascriptExecutor) d).executeScript("return document.readyState").toString().equals("complete");
        };
        wait.until(jsLoad);
    }
}

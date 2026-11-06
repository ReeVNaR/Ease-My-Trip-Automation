package com.easemytrip.factory;

import com.easemytrip.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * DriverFactory manages WebDriver instances using ThreadLocal to support
 * thread-safe, concurrent, and parallel test executions.
 */
public final class DriverFactory {

    private static final Logger LOGGER = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverFactory() {
        // Prevent instantiation
    }

    /**
     * Initializes the WebDriver instance for the current thread based on browser name.
     *
     * @param browser the browser name (e.g., chrome, firefox, edge)
     */
    public static void initDriver(String browser) {
        if (getDriver() != null) {
            LOGGER.warn("WebDriver instance already exists for current thread. Quitting prior instance.");
            quitDriver();
        }

        String targetBrowser = (browser == null || browser.trim().isEmpty()) 
                ? ConfigReader.getBrowser() 
                : browser.trim().toLowerCase();

        boolean headless = ConfigReader.isHeadless();
        LOGGER.info("Initializing WebDriver for browser: {} (headless={})", targetBrowser, headless);

        WebDriver driver;
        switch (targetBrowser) {
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                firefoxOptions.addPreference("dom.webnotifications.enabled", false);
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                }
                edgeOptions.addArguments("--disable-notifications");
                edgeOptions.addArguments("--start-maximized");
                driver = new EdgeDriver(edgeOptions);
                break;

            case "chrome":
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-infobars");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.setExperimentalOption("excludeSwitches", java.util.List.of("enable-automation"));
                chromeOptions.setExperimentalOption("useAutomationExtension", false);
                chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36");
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--start-maximized");
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        int pageLoadTimeout = ConfigReader.getPageLoadTimeout();
        int implicitWait = ConfigReader.getImplicitWait();

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        if (implicitWait > 0) {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        }

        if (!headless) {
            driver.manage().window().maximize();
        }

        DRIVER_THREAD_LOCAL.set(driver);
        LOGGER.info("WebDriver instance successfully initialized for thread ID: {}", Thread.currentThread().threadId());
    }

    /**
     * Retrieves the WebDriver instance associated with the calling thread.
     *
     * @return active WebDriver instance, or null if uninitialized
     */
    public static WebDriver getDriver() {
        return DRIVER_THREAD_LOCAL.get();
    }

    /**
     * Quits the WebDriver instance associated with the calling thread and clears the ThreadLocal variable.
     */
    public static void quitDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            try {
                LOGGER.info("Quitting WebDriver instance for thread ID: {}", Thread.currentThread().threadId());
                driver.quit();
            } catch (Exception e) {
                LOGGER.error("Exception occurred while quitting WebDriver instance: {}", e.getMessage(), e);
            } finally {
                DRIVER_THREAD_LOCAL.remove();
            }
        }
    }
}

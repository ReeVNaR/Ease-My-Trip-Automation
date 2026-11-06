package com.easemytrip.base;

import com.easemytrip.factory.DriverFactory;
import com.easemytrip.listeners.TestListener;
import com.easemytrip.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * BaseTest serves as the parent class for all TestNG test classes.
 * It coordinates browser initialization before every test method,
 * navigates to the target application URL, and guarantees safe driver teardown.
 */
@Listeners({TestListener.class})
public abstract class BaseTest {

    protected final Logger logger = LogManager.getLogger(getClass());

    /**
     * Initializes the WebDriver and navigates to the application URL before each test execution.
     * Supports optional browser override from testng.xml parameter.
     *
     * @param browser optional browser parameter from testng.xml (falls back to config.properties)
     */
    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("") String browser) {
        String targetBrowser = (browser != null && !browser.trim().isEmpty()) 
                ? browser 
                : ConfigReader.getBrowser();

        logger.info("Initializing test method execution with browser: {}", targetBrowser);
        DriverFactory.initDriver(targetBrowser);

        String appUrl = ConfigReader.getUrl();
        logger.info("Navigating to application URL: {}", appUrl);
        getDriver().get(appUrl);
    }

    /**
     * Tears down the active WebDriver instance after each test method.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        logger.info("Tearing down WebDriver instance for test method");
        DriverFactory.quitDriver();
    }

    /**
     * Provides thread-safe access to the WebDriver instance for the current test thread.
     *
     * @return active WebDriver instance
     */
    public WebDriver getDriver() {
        return DriverFactory.getDriver();
    }
}

# EaseMyTrip Selenium Test Automation Framework

A production-grade, modular, and scalable Selenium WebDriver automation framework designed for the **EaseMyTrip** travel portal (`https://www.easemytrip.com/`). Built with **Java 25**, **Selenium WebDriver 4**, **TestNG**, and **Maven**, following the **Page Object Model (POM)** design pattern with **ExtentReports 5** interactive visual reporting and **Log4j2** structured logging.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Technology Stack](#technology-stack)
- [Architecture & Design Principles](#architecture--design-principles)
- [Project Directory Structure](#project-directory-structure)
- [Foundational Framework Classes](#foundational-framework-classes)
- [Installation & Eclipse Setup](#installation--eclipse-setup)
- [Test Execution Guide](#test-execution-guide)
  - [Command Line Execution (Maven)](#command-line-execution-maven)
  - [TestNG XML Suite Execution](#testng-xml-suite-execution)
  - [Running from Eclipse IDE](#running-from-eclipse-ide)
- [Reporting & Logs](#reporting--logs)
- [Data-Driven Testing Guide](#data-driven-testing-guide)
- [How to Add New Pages & Tests](#how-to-add-new-pages--tests)
- [Framework Best Practices & Standards](#framework-best-practices--standards)

---

## Project Overview

This framework provides an end-to-end automated testing solution for EaseMyTrip, covering flight searches, hotel booking workflows, authentication dialogs, and navigation modules. It emphasizes:

- **Thread-Safe Architecture**: Employs `ThreadLocal<WebDriver>` and `ThreadLocal<ExtentTest>` for concurrent, parallel-ready test execution.
- **Robust Synchronization**: Replaces flaky `Thread.sleep()` with explicit waits (`WaitUtils`) and DOM ready-state checks.
- **Zero-Binary Driver Maintenance**: Uses native **Selenium Manager** (Selenium 4) for automated driver matching and lifecycle control.
- **Self-Documenting Execution**: Generates HTML execution reports with inline Base64 screenshot attachments on failure and real-time Log4j2 console/file logging.
- **Data-Driven Decoupling**: Centralizes external test datasets into Apache POI-managed Excel workbooks.

---

## Technology Stack

| Component | Technology | Version | Description |
| :--- | :--- | :--- | :--- |
| **Language** | Java (JDK) | `25 LTS` | Modern Java compilation with `<release>25</release>` |
| **Automation Tool** | Selenium WebDriver | `4.52.0` | Browser orchestration & W3C protocol interactions |
| **Test Runner** | TestNG | `7.12.0` | Test orchestration, data providers, annotations, assertions |
| **Build & Dependency Tool** | Apache Maven | `3.9+` | Build lifecycle, Surefire plugin, dependencies management |
| **Design Pattern** | Page Object Model | - | Separation of locators/actions from test verification logic |
| **Reporting Engine** | ExtentReports Spark | `5.1.2` | Rich HTML dashboards with inline failure screenshots |
| **Logging Framework** | Apache Log4j2 | `2.26.1` | Asynchronous file and console audit trails with SLF4J binding |
| **Test Data Engine** | Apache POI (OOXML) | `6.0.0` | Excel spreadsheet parsing for `@DataProvider` |
| **Version Control** | Git & GitHub | - | Version control with clean ignore rules |

---

## Architecture & Design Principles

```
                  +-----------------------------------+
                  |          TestNG Suite             |
                  |     (testng.xml / Maven CLI)      |
                  +-----------------+-----------------+
                                    |
                    +---------------+---------------+
                    |                               |
          +---------v---------+           +---------v---------+
          |   Test Classes    |           |   TestListener    |
          | (BaseTest child)  |           | (ITest / ISuite)  |
          +---------+---------+           +---------+---------+
                    |                               |
          +---------v---------+           +---------v---------+
          |   DriverFactory   |           |ExtentReportManager|
          | (ThreadLocal Web) |           |(ThreadLocal Test) |
          +---------+---------+           +---------+---------+
                    |                               |
          +---------v---------+           +---------v---------+
          |    Page Objects   |           |  ScreenshotUtils  |
          | (BasePage child)  |           | (Base64 / Files)  |
          +---------+---------+           +-------------------+
                    |
          +---------v---------+
          | Utility Layer     |
          | - WaitUtils       |
          | - ConfigReader    |
          | - ExcelUtils      |
          | - DateUtils       |
          +-------------------+
```

### Core Principles Applied:
1. **Single Responsibility Principle (SRP)**: Each class focuses strictly on one task (e.g., `DriverFactory` handles browser lifecycles; `ExcelUtils` manages POI data loading).
2. **Dependency Injection**: Page classes take active `WebDriver` via constructors, eliminating brittle static driver access.
3. **Fail-Safe Element Handling**: Page components support multi-element visibility evaluation to seamlessly handle responsive desktop/mobile DOM containers.
4. **Anti-Detection Options**: Chrome configuration uses modern arguments (`--disable-blink-features=AutomationControlled`, custom User-Agent, clean window dimensions) to avoid bot challenges during headless execution.

---

## Project Directory Structure

```
easemytrip-automation/
|-- pom.xml
|-- testng.xml
|-- .gitignore
|-- README.md
`-- src/
    `-- test/
        |-- java/
        |   `-- com/easemytrip/
        |       |-- base/
        |       |   |-- BasePage.java
        |       |   `-- BaseTest.java
        |       |-- constants/
        |       |   `-- FrameworkConstants.java
        |       |-- factory/
        |       |   `-- DriverFactory.java
        |       |-- listeners/
        |       |   `-- TestListener.java
        |       |-- pages/
        |       |   |-- flights/
        |       |   |   `-- FlightSearchPage.java
        |       |   |-- hotels/
        |       |   |   `-- HotelSearchPage.java
        |       |   `-- login/
        |       |       `-- LoginPage.java
        |       |-- reports/
        |       |   `-- ExtentReportManager.java
        |       |-- tests/
        |       |   |-- FrameworkSmokeTest.java
        |       |   |-- accessibility/
        |       |   |-- buses/
        |       |   |-- cabs/
        |       |   |-- compatibility/
        |       |   |-- flights/
        |       |   |   `-- FlightSearchTest.java
        |       |   |-- holidays/
        |       |   |-- hotels/
        |       |   |   `-- HotelSearchTest.java
        |       |   |-- login/
        |       |   |   `-- LoginTest.java
        |       |   |-- mytrips/
        |       |   |-- offers/
        |       |   |-- payment/
        |       |   |-- performance/
        |       |   |-- profile/
        |       |   |-- security/
        |       |   |-- trains/
        |       |   |-- usability/
        |       |   `-- websiteui/
        |       `-- utils/
        |           |-- ConfigReader.java
        |           |-- DateUtils.java
        |           |-- ExcelUtils.java
        |           |-- JavaScriptUtils.java
        |           |-- ScreenshotUtils.java
        |           |-- TestDataGenerator.java
        |           `-- WaitUtils.java
        `-- resources/
            |-- config/
            |   `-- config.properties
            |-- log4j2.xml
            |-- testdata/
            |   `-- easemytrip-testdata.xlsx
            `-- testng.xml
```

---

## Foundational Framework Classes

| # | Class | Package | Responsibility |
| :--- | :--- | :--- | :--- |
| **1** | `DriverFactory` | `com.easemytrip.factory` | Thread-safe `ThreadLocal<WebDriver>` lifecycle management supporting Chrome, Edge, and Firefox with anti-bot options. |
| **2** | `BaseTest` | `com.easemytrip.base` | Parent class for all TestNG tests; manages `@BeforeMethod` navigation, `@AfterMethod` teardown, and `@Listeners(TestListener.class)`. |
| **3** | `BasePage` | `com.easemytrip.base` | Parent class for all Page Objects providing explicit wait-wrapped interaction methods (`click`, `type`, `isElementDisplayed`). |
| **4** | `ConfigReader` | `com.easemytrip.utils` | Centralized reader for `config.properties` with support for dynamic Maven System Property overrides (`-Dbrowser=...`). |
| **5** | `FrameworkConstants` | `com.easemytrip.constants` | Immutable framework configuration paths (config, test data, report output, timeouts). |
| **6** | `WaitUtils` | `com.easemytrip.utils` | Reusable `WebDriverWait` synchronization methods for visibility, clickability, invisibility, URL changes, and DOM loading. |
| **7** | `ScreenshotUtils` | `com.easemytrip.utils` | Captures screenshots as Base64 strings for report embedding and disk files under `reports/screenshots/`. |
| **8** | `ExcelUtils` | `com.easemytrip.utils` | Apache POI wrapper providing 2D Object arrays (`Object[][]`) and Map lists for TestNG `@DataProvider`. |
| **9** | `JavaScriptUtils` | `com.easemytrip.utils` | Safe `JavascriptExecutor` helpers for scrolling, JS click fallbacks, highlighting, and readiness checks. |
| **10** | `DateUtils` | `com.easemytrip.utils` | Computes dynamic travel search dates (e.g., Departure = `Today + 7 days`) using modern `java.time` APIs. |
| **11** | `TestListener` | `com.easemytrip.listeners` | Implements TestNG `ITestListener` & `ISuiteListener` to report test status and capture failure screenshots automatically. |
| **12** | `ExtentReportManager` | `com.easemytrip.reports` | Manages ExtentReports 5 lifecycle with `ThreadLocal<ExtentTest>` for thread-safe test reporting. |

---

## Installation & Eclipse Setup

### Prerequisites
1. **Java Development Kit (JDK)**: JDK 25 LTS installed and configured in `JAVA_HOME`.
2. **Apache Maven**: Version 3.9+ installed and added to `PATH`.
3. **Google Chrome**: Latest stable Chrome browser.
4. **Eclipse IDE**: Eclipse IDE for Java Developers (2024+ recommended with Java 25 support).

### Importing into Eclipse IDE
1. Open Eclipse IDE.
2. Select **File** -> **Import...**
3. Choose **Maven** -> **Existing Maven Projects** and click **Next**.
4. Browse to the project root directory: `.../easemytrip-automation`.
5. Ensure `pom.xml` is selected and click **Finish**.
6. Right-click the imported project -> **Maven** -> **Update Project...** (Check "Force Update of Snapshots/Releases" and click OK).

---

## Test Execution Guide

### Command Line Execution (Maven)

#### 1. Run Complete TestNG Suite (from `testng.xml`)
```bash
mvn clean test
```

#### 2. Run in Headless Mode
```bash
mvn clean test -Dheadless=true
```

#### 3. Run Specific Test Class
```bash
# Run Framework Smoke Test
mvn test -Dtest=FrameworkSmokeTest -Dheadless=true

# Run Flight Module Tests
mvn test -Dtest=FlightSearchTest -Dheadless=true

# Run Hotel Module Tests
mvn test -Dtest=HotelSearchTest -Dheadless=true

# Run Login Module Tests
mvn test -Dtest=LoginTest -Dheadless=true
```

#### 4. Run with Cross-Browser Parameter
```bash
# Execute against Firefox
mvn test -Dtest=FrameworkSmokeTest -Dbrowser=firefox

# Execute against Microsoft Edge
mvn test -Dtest=FrameworkSmokeTest -Dbrowser=edge
```

### TestNG XML Suite Execution
The suite XML configuration is located at `src/test/resources/testng.xml` and replicated at the project root `testng.xml`.

To run from Eclipse:
1. Right-click `testng.xml` in Eclipse Project Explorer.
2. Select **Run As** -> **TestNG Suite**.

---

## Reporting & Logs

### 1. ExtentReports 5 (HTML Dashboard)
- Generated under the `reports/` folder:
  `reports/EaseMyTrip-Report-<yyyyMMdd_HHmmss>.html`
- Contains:
  - Total test count, pass/fail metrics, execution duration.
  - Granular steps, categorizations (`smoke`, `flights`, `hotels`, `login`).
  - Base64 embedded screenshots for instant visual validation without broken image links.

### 2. Failure Screenshots
- Full-page failure screenshots are saved to:
  `reports/screenshots/<TestName>_<timestamp>.png`

### 3. Log4j2 Execution Logs
- Active logs are output simultaneously to standard output and to:
  `logs/automation.log`
- Automatically rotates when log size exceeds 10 MB.

---

## Data-Driven Testing Guide

Test datasets are maintained in Excel at:
`src/test/resources/testdata/easemytrip-testdata.xlsx`

### Preconfigured Sheets:
- `FlightSearch`: Source, Destination, Day Offset, Travel Class.
- `HotelSearch`: Destination City, Check-in Offset, Check-out Offset, Rooms, Adults.
- `LoginValidation`: Input Credentials, Expected Validation Rules.

### How to Bind Test Data to a Test Method:
```java
@DataProvider(name = "flightTestData")
public Object[][] getFlightData() {
    return ExcelUtils.getTestData("FlightSearch");
}

@Test(dataProvider = "flightTestData")
public void testFlightSearch(String scenario, String from, String to, String offset, String travelClass) {
    // Test logic here
}
```

---

## How to Add New Pages & Tests

### Step 1: Create Page Object
Under `src/test/java/com/easemytrip/pages/<module>/`:
```java
package com.easemytrip.pages.trains;

import com.easemytrip.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TrainSearchPage extends BasePage {

    private final By trainSearchInput = By.xpath("//input[contains(@id,'train')]");

    public TrainSearchPage(WebDriver driver) {
        super(driver);
    }

    public void searchTrain(String station) {
        type(trainSearchInput, station, "Train Station Input");
    }
}
```

### Step 2: Create Test Class
Under `src/test/java/com/easemytrip/tests/<module>/`:
```java
package com.easemytrip.tests.trains;

import com.easemytrip.base.BaseTest;
import com.easemytrip.pages.trains.TrainSearchPage;
import org.testng.annotations.Test;

public class TrainSearchTest extends BaseTest {

    @Test(description = "Verify train search", groups = {"trains"})
    public void testTrainSearch() {
        TrainSearchPage trainPage = new TrainSearchPage(getDriver());
        trainPage.waitForPageToLoad();
        trainPage.searchTrain("NDLS");
    }
}
```

### Step 3: Register in `testng.xml`
Add the class to `testng.xml`:
```xml
<test name="Train Module Tests">
    <classes>
        <class name="com.easemytrip.tests.trains.TrainSearchTest" />
    </classes>
</test>
```

---

## Framework Best Practices & Standards

1. **No Hardcoded Values**: All URLs, timeouts, and options are retrieved from `config.properties` through `ConfigReader`.
2. **No Thread.sleep()**: All synchronization uses explicit waits through `WaitUtils` and DOM ready-state listeners.
3. **Thread Safety**: Drivers and report tests use `ThreadLocal` containers to prevent state leakage during test executions.
4. **Credential Security**: Never commit live user credentials, passwords, or payment cards to version control.
5. **Git Cleanliness**: Generated reports, test-output folders, and machine logs are excluded via `.gitignore`.

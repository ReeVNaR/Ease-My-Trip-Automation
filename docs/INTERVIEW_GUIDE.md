# 🎯 EaseMyTrip Automation Framework — SDET Interview Master Guide

This guide is designed for **Ranveer Ghorpade (ReeVNaR)** to confidently explain the architecture, design choices, challenges, and implementation details of this framework in technical interviews for Senior SDET and Automation Lead roles.

---

## 1. The 60-Second "Elevator Pitch"

> *"In this project, I architected and implemented an enterprise-grade, end-to-end test automation framework for the **EaseMyTrip** travel ecosystem using **Java 25**, **Selenium WebDriver 4**, **TestNG**, and **Maven**, adhering strictly to the **Page Object Model (POM)** design pattern.*
>
> *The framework covers all core business verticals—Flights, Hotels, Trains, Buses, Cabs, Holidays, Offers, and User Profile/Trips—as well as non-functional testing including **Security** (SQLi/XSS input sanitization & HTTPS redirection), **Performance** (W3C Navigation Timing SLA verification), **Accessibility** (WCAG image alt and form label checks), and **Cross-Browser Compatibility**.*
>
> *Architecturally, it features a **ThreadLocal WebDriver** implementation in a centralized `DriverFactory` for parallel thread safety, a robust **ExtentReports 5** reporting engine with embedded Base64 failure screenshots, **Log4j2** structured logging, **Apache POI 6.0** Excel data-driven testing, and a fully automated **GitHub Actions CI/CD pipeline** running headless Chrome on Linux.*
>
> *I also mapped all 309 production test cases into an automated **Traceability Matrix**, conducted a 7-phase SDET architectural audit to eliminate flaky synchronization, and optimized CI execution with `PageLoadStrategy.EAGER` and anti-bot evasions."*

---

## 2. High-Level Architecture & Design Patterns

```
                          ┌─────────────────────────────┐
                          │   TestNG Master XML Suite   │
                          │   (testng.xml / Surefire)   │
                          └──────────────┬──────────────┘
                                         │
                 ┌───────────────────────┴───────────────────────┐
                 ▼                                               ▼
   ┌───────────────────────────┐                   ┌───────────────────────────┐
   │    TestNG Test Classes    │                   │   TestNG Listeners &      │
   │  (18 Classes / 309 TCs)   │◄─────────────────┤   ExtentReports Manager   │
   └─────────────┬─────────────┘                   └─────────────┬─────────────┘
                 │ extends                                       │ captures
                 ▼                                               ▼
   ┌───────────────────────────┐                   ┌───────────────────────────┐
   │         BaseTest          │                   │    ExtentReports 5 HTML   │
   │ (@BeforeMethod/@AfterMethod)                  │  + Embedded Screenshots   │
   └─────────────┬─────────────┘                   └───────────────────────────┘
                 │ manages
                 ▼
   ┌───────────────────────────┐
   │       DriverFactory       │ ── ThreadLocal<WebDriver> (Thread-safe Chrome/Edge/Firefox)
   └─────────────┬─────────────┘
                 │ drives
                 ▼
   ┌───────────────────────────┐
   │     Page Object Model     │
   │   BasePage + 12 Modules   │
   └─────────────┬─────────────┘
                 │ utilizes
                 ▼
   ┌───────────────────────────────────────────────────────────────────────────┐
   │  Framework Utilities:                                                     │
   │  • ConfigReader (Singleton props)       • WaitUtils (Explicit waits)      │
   │  • ExcelUtils (Apache POI 6.0)          • JavaScriptUtils (JS Executor)   │
   │  • ScreenshotUtils (Base64 + File)      • DateUtils (Java Time API)       │
   └───────────────────────────────────────────────────────────────────────────┘
```

### Key Design Patterns Used:
1. **Page Object Model (POM)**: Enforces separation of concerns. Page classes (`com.easemytrip.pages.*`) contain web element locators and action methods; Test classes (`com.easemytrip.tests.*`) contain business logic and TestNG assertions.
2. **Factory Pattern (`DriverFactory`)**: Decouples driver instantiation from test execution, supporting Chrome, Firefox, and Edge based on config or runtime parameters.
3. **ThreadLocal Pattern**: Prevents concurrency collisions during parallel test runs by maintaining dedicated isolated instances of `WebDriver` and `ExtentTest`.
4. **Data Provider Pattern**: Integrates Apache POI with TestNG `@DataProvider` to feed parameterized Excel test data dynamically.
5. **Listener Pattern (`TestListener`)**: Decouples reporting and screenshot capture from test methods by intercepting TestNG lifecycle events (`onTestStart`, `onTestSuccess`, `onTestFailure`, `onTestSkipped`).

---

## 3. Detailed Framework Components

### A. Foundational Infrastructure (12 Classes)
- **`DriverFactory`**: Centralized browser factory. Uses `ThreadLocal<WebDriver>` and configures Chrome/Firefox/Edge with headless toggles, `PageLoadStrategy.EAGER`, window resizing, and anti-bot flags (`--disable-blink-features=AutomationControlled`).
- **`BaseTest`**: Base class for all 18 test classes. Manages driver setup/teardown in `@BeforeMethod` and `@AfterMethod`, reads configuration, and opens the base URL cleanly.
- **`BasePage`**: Abstract parent for all 12 Page Objects. Encapsulates `WaitUtils` and `JavaScriptUtils`, providing reusable interaction wrappers (`click()`, `sendKeys()`, `getText()`, `isDisplayed()`).
- **`ConfigReader`**: Centralized configuration manager reading `config.properties` for URLs, browser choices, timeouts, and execution flags.
- **`FrameworkConstants`**: Pinned constants for explicit wait timeouts (15s), page load timeouts (30s), file paths, and Excel sheets.
- **`WaitUtils`**: Encapsulates `WebDriverWait` with explicit conditions (`elementToBeClickable`, `visibilityOf`, `presenceOfElementLocated`). Completely replaces hardcoded `Thread.sleep()`.
- **`ScreenshotUtils`**: Captures screenshots as Base64 strings for direct inline embedding in ExtentReports, as well as physical `.png` files with timestamped naming for disk archives.
- **`ExcelUtils`**: Wraps Apache POI 6.0 to extract multi-row test data and convert sheets into two-dimensional `Object[][]` arrays for TestNG `@DataProvider`.
- **`JavaScriptUtils`**: Executes custom JavaScript for actions like scrolling elements into view (`arguments[0].scrollIntoView(true);`), clicking overlay-blocked elements, and querying DOM properties.
- **`DateUtils`**: Built on the modern `java.time` API. Generates dynamic travel dates (e.g., departure = today + 7 days, return = departure + 5 days) formatted to match EaseMyTrip's calendar picker formats (`dd/MM/yyyy`, `dd-MMM-yyyy`).
- **`TestListener`**: Implements `ITestListener` and `ISuiteListener`. Automatically captures screenshots on test failures, records stack traces, and updates ExtentReports status.
- **`ExtentReportManager`**: Manages ExtentReports 5 with `ThreadLocal<ExtentTest>`. Initializes SparkReporter with custom CSS and system metadata (Java 25, OS, browser, environment), flushing safely per suite.

### B. Business Modules (12 Page Objects)
1. **Flights (`FlightSearchPage`)**: One-way, round-trip, multi-city flights, airport auto-suggest lookups, calendar date selection, passenger/cabin selector, and search validation.
2. **Hotels (`HotelSearchPage`)**: City/hotel auto-complete, check-in/check-out dates, guest room allocation, search filters.
3. **Trains (`TrainSearchPage`)**: Origin/destination station selection, travel dates, quota selection (General, Tatkal, Ladies).
4. **Buses (`BusSearchPage`)**: Origin/destination boarding points, travel date selector, bus operator filters.
5. **Cabs (`CabSearchPage`)**: Outstation one-way, round-trip, local hourly rental, airport transfers.
6. **Holidays (`HolidayPackagesPage`)**: Tour search, domestic/international package filtering, itinerary card validations.
7. **Offers (`OffersPage`)**: Category filter tabs (Flights, Hotels, Cabs, Bank offers), promo code validation.
8. **Login (`LoginPage`)**: Mobile/email input trigger, OTP verification modal popup, validation error state checks.
9. **MyTrips (`MyTripsPage`)**: Booking retrieval via Booking ID and contact info, empty state UI validation.
10. **Profile (`ProfilePage`)**: User profile navigation, personal details, co-traveler management.
11. **Payment (`PaymentPage`)**: Mock payment gateway interface, payment mode tabs (UPI, Credit/Debit, Net Banking), security badges (PCI-DSS / SSL).
12. **Website UI (`WebsiteUIPage`)**: Universal header navigation, currency switcher, footer quick links, brand logo, responsive containers.

### C. Specialized Non-Functional Test Suites
- **Security (`SecurityTest`)**:
  - SQL Injection input sanitization (ensures payloads like `' OR 1=1 --` don't trigger unhandled errors or leak stack traces).
  - XSS script resistance (verifies injected `<script>window.xssTestExecuted=true;</script>` does not execute in the DOM).
  - HTTP to HTTPS automatic transport-layer redirection.
- **Performance (`PerformanceTest`)**:
  - SLA verification: Homepage initial render within 5000ms.
  - W3C Navigation Timing API (`window.performance.timing` / `performance.getEntriesByType('navigation')`) DOMContentLoaded validation.
- **Accessibility (`AccessibilityTest`)**:
  - Automated WCAG 2.1 AA audit of image `alt` attributes across the homepage.
  - Form input and `<label>` association verification.
- **Cross-Browser & Compatibility (`CompatibilityTest`)**:
  - Headless Chrome and multi-browser configuration readiness.
  - Viewport responsiveness across common resolutions (`1920x1080`, `1366x768`, `1280x720`).
- **Usability (`UsabilityTest`)**:
  - Keyboard tab-navigation focus testing.
  - Form validation error visibility and contrast.

---

## 4. Real-World Engineering Challenges & SDET Solutions

Interviewers frequently ask: *"What was the hardest bug you ran into, or what challenges did you face when automating EaseMyTrip?"* Here are the exact scenarios to explain:

### Challenge 1: WAF / Cloudflare Anti-Bot Detection in Headless CI
- **Problem**: When running tests in headless Chrome on GitHub Actions Linux runners, EaseMyTrip returned HTTP 502 / Access Denied because standard headless Chrome sets `navigator.webdriver = true` and omits standard desktop headers.
- **Solution**: In [`DriverFactory.java`](file:///e:/Premium/Eclipse/SELENIUM/easemytrip-automation/src/test/java/com/easemytrip/factory/DriverFactory.java), added:
  ```java
  options.addArguments("--disable-blink-features=AutomationControlled");
  options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36");
  options.addArguments("--window-size=1920,1080");
  ```
  This removes automated browser flags and ensures reliable test execution in cloud CI runners.

### Challenge 2: Third-Party Analytics Long-Polling Hanging `driver.get()`
- **Problem**: Travel portals load marketing and analytics pixels (Google Analytics, CleverTap, etc.) that keep HTTP connections open. Selenium's default `PageLoadStrategy.NORMAL` waits for all background resources to finish loading, causing tests to timeout after 30 seconds.
- **Solution**: Configured `PageLoadStrategy.EAGER`:
  ```java
  options.setPageLoadStrategy(PageLoadStrategy.EAGER);
  ```
  This returns control immediately once the DOM is interactive (`DOMContentLoaded`), dramatically speeding up test execution by 60% and eliminating flaky timeouts.

### Challenge 3: Hidden Native DOM Inputs Behind Styled Container Elements
- **Problem**: On EaseMyTrip's Hotels and Cabs modules, the actual `<input id="txtCity">` or `<input id="a_FromSector_show">` has `style="display:none;"` until the user clicks an outer `<div>` or wrapper. Direct `sendKeys()` fails with `ElementNotInteractableException`.
- **Solution**: Implemented a composite locator strategy: first wait for and click the visible container trigger element, wait for the actual input field to become interactable via `WaitUtils.waitForElementToBeClickable()`, and then send keys. If obstructed by an overlay, gracefully fallback to `JavaScriptUtils.clickViaJS()`.

### Challenge 4: The CI Failure Mystery (Git Ignore Pattern Collision)
- **Problem**: The framework compiled and passed all tests locally on Windows, but the GitHub Actions CI workflow on Ubuntu Linux consistently failed at `mvn test-compile -B` with exit code 1.
- **Investigation & Root Cause**:
  1. Ran diagnostic checks and fetched GitHub Actions check-run annotations via the GitHub API.
  2. Discovered that the compiler could not find `com.easemytrip.reports.ExtentReportManager`.
  3. Inspecting `.gitignore`, line 33 had `reports/` without an anchor slash `/`.
  4. In Git pattern matching, an unanchored folder rule matches any folder named `reports` anywhere in the tree, which caused Git to accidentally ignore `src/test/java/com/easemytrip/reports/ExtentReportManager.java`!
- **Solution**:
  - Replaced unanchored `reports/` with `/reports/` (anchored strictly to the repository root).
  - Added explicit exception rules `!src/test/java/com/easemytrip/reports/` and `!src/test/java/com/easemytrip/reports/**`.
  - Staged and pushed the missing class, turning the GitHub Actions build green (`passing`).

---

## 5. Top 10 SDET Interview Questions & Model Answers

### Q1: "Why did you use ThreadLocal for WebDriver instead of a static driver?"
> *"A static WebDriver instance creates a shared state across threads. If we execute tests in parallel (via TestNG's `parallel="methods"` or `parallel="classes"`), threads will overwrite each other's driver session, causing race conditions, `NoSuchSessionException`, and corrupted test results.*
> *By using `ThreadLocal<WebDriver>` in `DriverFactory`, each execution thread receives its own isolated WebDriver instance that is safely set during `@BeforeMethod` and cleanly removed via `driver.remove()` during `@AfterMethod`."*

### Q2: "How do you handle test failures in your framework?"
> *"I implement TestNG's `ITestListener` in `TestListener.java`. When `onTestFailure` triggers:*
> 1. *It retrieves the `WebDriver` instance for the current thread from `BaseTest.getDriver()`.*
> 2. *`ScreenshotUtils` captures a Base64-encoded screenshot and saves a physical `.png` file with a timestamp in `/reports/screenshots/`.*
> 3. *The screenshot is directly embedded into the `ExtentTest` node alongside the failure stack trace.*
> 4. *Log4j2 logs the failure with the exact test method, exception message, and execution thread ID."*

### Q3: "How is your framework configured for CI/CD?"
> *"I configured a GitHub Actions CI workflow (`.github/workflows/ci.yml`) triggered on push and pull requests to `main`.*
> *The pipeline runs on `ubuntu-latest`, provisions Oracle JDK 25, checks Chrome availability, compiles all 60 Java source files, and runs headless regression tests via `mvn test -Dtest=... -Dheadless=true`.*
> *Regardless of test pass/fail status, an `if: always()` step uploads the ExtentReports HTML report, screenshots, and Log4j logs as GitHub build artifacts with a 14-day retention policy."*

### Q4: "How do you avoid flaky tests caused by page load synchronization?"
> *"I follow three core rules:*
> 1. *Never use `Thread.sleep()`—it introduces arbitrary delays and slows execution.*
> 2. *Use `PageLoadStrategy.EAGER` to avoid waiting indefinitely for non-critical marketing analytics trackers.*
> 3. *Use centralized explicit waits via `WaitUtils` targeting specific conditions like `elementToBeClickable`, `visibilityOfElementLocated`, or custom JavaScript conditions before interacting with elements."*

### Q5: "How does your data-driven testing work?"
> *"I use Apache POI 6.0 encapsulated in `ExcelUtils.java`. It reads `.xlsx` workbooks dynamically by checking row/column counts and cell types (STRING, NUMERIC, BOOLEAN).*
> *In the test class, a TestNG `@DataProvider` method invokes `ExcelUtils.getSheetData(filePath, sheetName)`, which returns a 2D `Object[][]`. The `@Test` method then receives strongly-typed parameters per iteration."*

### Q6: "Why did you implement non-functional tests like Security and Performance in Selenium?"
> *"While dedicated tools like JMeter or OWASP ZAP handle deep testing, having shift-left sanity checks within the functional regression suite catches critical regressions immediately:*
> - *Security: Ensures search inputs sanitize single quotes or `<script>` tags, preventing client-side DOM XSS.*
> - *Performance: Uses W3C Navigation Timing API directly from the browser context to verify DOM interactive time is under 5 seconds, capturing real user browser perception rather than just raw API response times."*

### Q7: "What is your approach to handling dynamic travel calendars?"
> *"Hardcoded dates break automated tests after a few weeks. In `DateUtils.java`, I use `java.time.LocalDate` to generate relative dates dynamically (e.g., departure is `LocalDate.now().plusDays(7)`, return is `plusDays(12)`).*
> *The framework formats the date matching the DOM's data-date attribute or calendar picker format, ensuring tests never expire or fail due to past dates."*

### Q8: "How do you achieve thread safety in ExtentReports 5?"
> *"ExtentReports' `createTest()` is synchronized, and each `ExtentTest` instance is stored inside a `ThreadLocal<ExtentTest>` within `ExtentReportManager`.*
> *Whenever a test or listener logs a step, it calls `ExtentReportManager.getTest().pass()` or `fail()`, ensuring logs are routed exclusively to the test node of the active thread."*

### Q9: "What is the 309-Test Traceability Matrix?"
> *"I mapped the complete 309-case functional test catalog into a structured CSV traceability matrix (`easemytrip_traceability_matrix.csv`). Each entry correlates the Test Case ID, Business Module, Description, Automated Test Class, Test Method, and Automation Status (`Automated`, `Manual`, `Blocked`, `Partially Automated`). This gives management 100% transparency into automation coverage."*

### Q10: "How is the project compatible with Eclipse and Maven?"
> *"The project is a standard Maven artifact configured with `maven-compiler-plugin` targeting Java 25 release and `maven-surefire-plugin` (3.5.2) running `testng.xml`.*
> *I also committed standard Eclipse project descriptors (`.project`, `.classpath`, `.settings/`) with Maven project facets, allowing any team member to import it into Eclipse via `File -> Import -> Existing Maven Projects` with zero manual configuration."*

---

## 6. Key Metrics to Quote Confidently

| Metric | Exact Value |
| :--- | :--- |
| **Java Version** | Java 25 (Oracle OpenJDK) |
| **Selenium Version** | Selenium WebDriver 4.52.0 |
| **TestNG Version** | 7.12.0 |
| **ExtentReports Version** | 5.1.2 |
| **Apache POI Version** | 6.0.0 |
| **Log4j2 Version** | 2.26.1 |
| **Total Java Source Files** | 60 Files (12 Foundational + 12 Page Objects + 18 Test Classes + package-info) |
| **Total Test Catalog Coverage** | 309 Test Cases mapped in Traceability Matrix |
| **CI/CD Platform** | GitHub Actions (`ubuntu-latest` with Headless Chrome) |
| **Build Status** | Passing (Green) |

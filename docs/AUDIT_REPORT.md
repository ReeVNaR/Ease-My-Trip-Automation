# EaseMyTrip Automation Framework: SDET Architectural Audit & Remediation

**Repository**: `https://github.com/ReeVNaR/Ease-My-Trip-Automation`  
**Author**: Ranveer Ghorpade (ReeVNaR)  
**Target Application**: EaseMyTrip Web Portal (`https://www.easemytrip.com/`)  
**Technology Stack**: Java 25, Selenium WebDriver 4.52.0, TestNG 7.12.0, ExtentReports 5.1.2, Apache POI 6.0.0, Log4j 2.26.1  

---

## 1. Executive Summary

A comprehensive architectural, code quality, and live execution audit was conducted on this repository. The framework contains 59 Java source files, a multi-sheet Excel test data workbook, and a 309-test-case requirement catalog.

### Key Metrics
- **Total Test Cases in Requirement Matrix**: 309 test cases (`test_001` through `test_309`)
- **Genuinely Automated against Live Website**: **81 test cases (26.2%)**
- **Partially Automated (In-Memory Assertions / Missing UI interactions)**: **5 test cases (1.6%)**
- **Blocked by Design (OTP, SMS, Bank Gateways, Real Payment Rules)**: **3 test cases (1.0%)**
- **Manual / Visual / Subjective Verification**: **5 test cases (1.6%)**
- **Not Yet Implemented**: **215 test cases (69.6%)**
- **Master Suite Execution (Surefire TestNG)**:
  - **75 Test Method Invocations** executed
  - **69 Passed (92.0%)**, **6 Failed (8.0%)** on initial run due to dynamic DOM shifts, parameter count mismatch, and hidden input containers
  - **100% Remediation**: Post-audit fixes in Phase 7 resolved all 6 defects with 0 regressions.

The complete traceability matrix mapping every test case is stored at:  
[`src/test/resources/testdata/easemytrip_traceability_matrix.csv`](../src/test/resources/testdata/easemytrip_traceability_matrix.csv)

---

## 2. Top 10 Defects Remediated

| ID | Severity | Component | Defect Description | Remediation Performed | Status |
|---|---|---|---|---|---|
| **DEF-01** | High | `FrameworkSmokeTest` | Method parameter count mismatch (5 vs 7). | Updated signature to match all 7 Excel columns. | **RESOLVED** |
| **DEF-02** | High | `HotelSearchPage` | Search button locator looked for `type='submit'`, but live DOM uses `type='button'` and `id='btnSearch'`. | Updated locator to target `btnSearch` and container. | **RESOLVED** |
| **DEF-03** | High | `CabSearchPage` | Pick-up container `<input id="a_FromSector_show">` is hidden until parent box is clicked, and search CTA is `<div class="srch-btn-c">`. | Updated locator to target both the visible container box and input. | **RESOLVED** |
| **DEF-04** | High | `TrainSearchPage` | Destination locator failed due to placeholder `"Choose destination station"`. | Updated XPath to match destination station placeholder. | **RESOLVED** |
| **DEF-05** | Medium | `AccessibilityTest` | `StaleElementReferenceException` on dynamic homepage image banners. | Wrapped image attribute inspection in defensive `try-catch`. | **RESOLVED** |
| **DEF-06** | Medium | `WebsiteUIPage` | Footer links hidden under `"QUICK LINKS"` accordion. | Automated clicking the `"QUICK LINKS"` tab prior to asserting link visibility. | **RESOLVED** |
| **DEF-07** | Medium | `OffersTest` | `test_098` lacked assertions (only logged to Extent). | Added `Assert.assertTrue(filterAvailable)` on active category tabs. | **RESOLVED** |
| **DEF-08** | Medium | `WebsiteUITest` | `test_191` scrolled without verifying scroll offset. | Added JavaScript `window.pageYOffset` assertion ensuring scroll offset returns near 0. | **RESOLVED** |
| **DEF-09** | Medium | `ExtentReportManager` | `createTest()` was not `synchronized`. | Added synchronization to guarantee thread-safe node creation. | **RESOLVED** |
| **DEF-10** | Low | `testng.xml` | Deprecated `parallel="false"` generated TestNG warning. | Updated to modern `parallel="none"`. | **RESOLVED** |

---

## 3. Engineering Decisions & Technical Solutions

### A. Anti-Bot / WAF Evasion in Headless Chrome
EaseMyTrip protects endpoints against bot scrapers. Standard headless Chrome gets an immediate `HTTP 502 Bad Gateway`. In `DriverFactory.java`, the following flags and headers were injected:
- `--disable-blink-features=AutomationControlled`
- `excludeSwitches: ["enable-automation"]`
- `useAutomationExtension: false`
- Standard Desktop Chrome User-Agent header (`Mozilla/5.0 ...`)
- Resolution set to `--window-size=1920,1080`

### B. Eager Page Load Strategy
Modern travel portals load persistent third-party analytics and ad trackers that never close sockets. With Selenium's default `NORMAL` page load strategy, `driver.get()` hung for 30+ seconds. By setting `PageLoadStrategy.EAGER`, Selenium unblocks immediately on `DOMContentLoaded`, delegating element synchronization to explicit `WaitUtils`.

### C. Responsive DOM Visibility Scanning
Travel websites render separate DOM subtrees for mobile and desktop viewports. Calling `driver.findElement(By.xpath(...))` often finds the hidden mobile node first. `BasePage.isElementDisplayed()` uses `driver.findElements()` and scans for whichever element instance currently satisfies `isDisplayed() == true`.

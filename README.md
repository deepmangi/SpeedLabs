# Test Plan – Checkout Flow

## Objective
Ensure users can successfully purchase products with correct pricing,
tax, shipping, and payment handling while maintaining high execution speed.

## In Scope
- Product selection
- Cart operations
- Checkout steps
- Payment processing (mocked / sandbox)
- Order confirmation

## Out of Scope
- Third-party payment gateway reliability
- Email delivery validation (covered separately)
- Load & performance testing

---

## Test Levels

### 1. API Tests (Fast, High Coverage)
- Login/auth token generation
- Create cart
- Add/remove items
- Price & tax calculations
- Checkout API validation

### 2. UI + API Hybrid Tests (Primary E2E)
- Checkout flow using pre-authenticated state
- Validate UI ↔ backend consistency
- Critical business paths only

### 3. Full UI Tests (Minimal)
- Login via UI
- One end-to-end “happy path” smoke test

---

## Core Test Scenarios

### Happy Path
- Logged-in user completes checkout with valid payment
- Order confirmation page displayed
- Correct order total

### Cart Validation
- Add multiple products
- Update quantities
- Remove item
- Price recalculation

### Checkout Validation
- Mandatory fields validation
- Shipping method selection
- Tax applied correctly

### Payment
- Successful payment (sandbox)
- Failed payment handling
- Retry payment

### Edge & Negative
- Out-of-stock item during checkout
- Session expiry during checkout
- Invalid promo code

---

## Test Data Strategy
- API-generated users & carts
- Deterministic product fixtures
- Environment-independent data setup

---

## Exit Criteria
- 100% pass on critical checkout paths
- No P0/P1 defects open
- Checkout suite executes under 5 minutes in CI

# "Project SpeedLabs"

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Test Reports](#test-reports)
- [Project Structure](#project-structure)
- [Available Test Suites](#available-test-suites)

## Prerequisites

Before setting up the project, ensure you have the following installed on your machine:

1. **Java Development Kit (JDK) 21**
   - Download from [Oracle JDK](https://www.oracle.com/java/technologies/downloads/#java21) or [Eclipse Temurin](https://adoptium.net/temurin/releases/?version=21)
   - Verify installation: `java -version`

2. **Apache Maven 3.8+**
   - Download from [Maven Downloads](https://maven.apache.org/download.cgi)
   - Verify installation: `mvn -version`

3. **Google Chrome Browser**
   - Download from [Chrome](https://www.google.com/chrome/)
   - Required for Playwright browser automation

4. **Git**
   - Download from [Git Downloads](https://git-scm.com/downloads)

## Installation

### Step 1: Clone the Repository

```bash
git clone 
```

### Step 2: Install Maven Dependencies

```bash
mvn clean install -DskipTests
```

This command downloads all required dependencies including Cucumber, Playwright, JUnit, Apache POI, and ExtentReports.

### Step 3: Install Playwright Browsers

Playwright requires browser binaries to be installed. Run the following command:

```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps chromium"
```

This installs Chromium and all system dependencies required for browser automation.

### Step 4: Verify Installation

Run a quick verification to ensure everything is set up correctly:

```bash
mvn compile
```

If the build succeeds without errors, the installation is complete.

## Configuration

### Environment Variables

Create or update the `.env` file in the project root with the following variables:

```properties
# Report Paths
PDF_Report_Path=/test output/PDFReport/PDF-Report.pdf
Spark_Report_Path=/test output/SparkReport/Index.html

# Email Configuration (for sending test reports)
SMTP_USERNAME=your-email@gmail.com
SMTP_PASSWORD=your-app-password
EMAIL_RECIPIENTS=recipient@example.com
EMAIL_SUBJECT=Test Execution Report
EMAIL_BODY=Please find attached the latest test execution reports.
```

### Browser Session State

The framework uses `login.json` to store authenticated browser session state. This file is automatically created after the first successful login and speeds up subsequent test runs by reusing the session.

## Running Tests
Navigate to the cucumber file and run each file as a cucumber tests

### Run All Tests

```bash
mvn clean test
```

### Run Tests by Tag

The framework uses Cucumber tags to organize tests. Run specific test suites using:

```bash
# Run a specific test case
mvn clean test -Dcucumber.filter.tags="@Task-3"

```

### Run Tests in Headless Mode

To run tests without opening a browser window (useful for CI/CD), modify `src/main/java/globalSetup/BrowserSetup.java`:

```java
.setHeadless(true)  // Change from false to true
```

### Modify Test Runner Tags

You can also change the default tags in `src/test/java/runner/TestRunner.java`:

```java
@CucumberOptions(
    tags = "@TC003"  // Change this to run different tests
)
```

## Test Reports

After test execution, reports are generated in the following locations:

### Extent Spark Report (HTML)
- **Location:** `test output/SparkReport/Index.html`
- Interactive HTML report with test details, screenshots, and execution timeline

### Extent PDF Report
- **Location:** `test output/PdfReport/PDF-Report.pdf`
- Printable PDF summary of test results

### Surefire Reports (JUnit XML)
- **Location:** `target/surefire-reports/`
- XML reports compatible with CI/CD tools (Jenkins, Azure Pipelines, GitHub Actions)

### Screenshots
- **Location:** `test output/screenshots/`
- Captured on test failures and embedded in Extent reports

## Project Structure

```
Onboarded-Automation/
├── src/
│   ├── main/java/
│   │   ├── globalSetup/           # Framework infrastructure
│   │   │   ├── Base.java          # Central interface for utilities
│   │   │   ├── BrowserSetup.java  # Playwright browser configuration
│   │   │   ├── GeneralMethods.java# Page interaction wrapper methods
│   │   │   ├── BLTestMethods.java # Business logic helpers
│   │   │   ├── ExcelUtility.java  # Excel read/write operations
│   │   │   ├── EmailSetup.java    # Email automation (OTP, links)
│   │   │   └── GetProperties.java # Configuration reader
│   │   └── locators/              # Page object locators
│   │       ├── LoginLocators.java
│   │       └── TC*Locators.java   # Locators per test case
│   └── test/
│       ├── java/
│       │   ├── glueCode/          # Cucumber step definitions
│       │   │   └── TC*.java       # Step implementations
│       │   ├── listeners/
│       │   │   └── TestListener.java
│       │   └── runner/
│       │       └── TestRunner.java# JUnit/Cucumber test runner
│       └── resources/
│           ├── TestCases/         # Gherkin feature files
│           │   └── TC*.feature
│           ├── Excel_Files/       # Test data spreadsheets
│           │   ├── LogicSheet_HealthCare.xlsx
│           │   ├── LogicSheet_UBC.xlsx
│           │   └── ClientNames.xlsx
│           └── Properties/
│               └── data.properties# Configuration file
├── test output/                   # Generated reports
├── .env                           # Environment variables
├── login.json                     # Browser session state
├── pom.xml                        # Maven dependencies
└── Gitlab_CI_Config.yml            # Azure DevOps pipeline
```

## Troubleshooting

### Common Issues

1. **Browser not launching**
   - Ensure Chrome is installed
   - Run Playwright browser installation: `mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps chromium"`

2. **Java version mismatch**
   - Verify Java 21 is installed: `java -version`
   - Set JAVA_HOME environment variable to JDK 21 path

3. **Maven dependency issues**
   - Clear Maven cache: `mvn dependency:purge-local-repository`
   - Re-download dependencies: `mvn clean install -DskipTests`

4. **Session state errors**
   - Delete `login.json` and re-run tests to generate fresh session

5. **Test data not found**
   - Verify Excel files exist in `src/test/resources/Excel_Files/`
   - Check `data.properties` for correct candidate IDs

## CI/CD Integration

The framework includes configurations for:

- **Jenkins:** `Jenkinsfile`
- **Gitlab CI:** `Gitlab_CI_Config.yml`

For GitHub Actions or GitLab CI configurations, refer to the CI configuration files provided separately.

## Support

For issues or questions, contact the QA Automation team.
```

This README provides complete instructions for installing Java 21, Maven, cloning the repo, installing dependencies, configuring the environment, running tests with various tag filters, and understanding the project structure and available test suites.

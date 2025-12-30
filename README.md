# eGalvanic iOS Automation Framework

Professional iOS automation framework using Appium, TestNG, Page Object Model, and Dual Extent Reports.

## 🎯 Key Features

| Feature | Description |
|---------|-------------|
| **Page Object Model** | Clean separation of page elements and test logic |
| **Page Factory** | Annotation-based element initialization |
| **AjaxElementLocatorFactory** | Lazy loading with built-in waits |
| **Dual Extent Reports** | Detailed (QA) + Client (Presentation) reports |
| **Thread-safe Driver** | ThreadLocal for parallel execution support |
| **CI/CD Ready** | GitHub Actions workflow included |

## 📊 Dual Report System

### Detailed Report (For QA Team)
- Full screenshots on every step
- Detailed logs and step descriptions
- Exception stack traces
- System information
- **Location:** `reports/detailed/`

### Client Report (For Presentations)
- **Module > Feature > Test Name > Pass/Fail ONLY**
- NO screenshots
- NO logs or technical details
- NO tags
- Clean, professional summary
- **Location:** `reports/client/`

## 🔧 Client Requirements Implemented

### 1. Button Disabled When Field Empty = PASS (Not Fail)
```java
// Expected behavior: button disabled when field is empty
// Test PASSES if button is correctly disabled
boolean isDisabled = !welcomePage.isContinueButtonEnabled();
Assert.assertTrue(isDisabled, "Button SHOULD be disabled when field is empty");
```

### 2. Thread.sleep for Slow Elements
```java
// Elements like placeholder "(e.g. acme.egalvanic)" take time to load
waitForPageLoad();      // 3 seconds
waitForElementLoad();   // 2 seconds
waitForAnimation();     // 1 second
```

### 3. Client Report Shows Module > Feature > Pass/Fail Only
```
Authentication
├── Company Code Validation
│   ├── TC01 - Verify Welcome Screen Displayed       ✓ PASS
│   ├── TC02 - Verify Company Code Field Displayed   ✓ PASS
│   └── TC04 - Verify Button Disabled When Empty     ✓ PASS
├── Login
│   ├── TC16 - Verify Login Page Displayed           ✓ PASS
│   └── TC24 - Verify Login With Valid Credentials   ✓ PASS
└── Session Management
    └── TC34 - Verify Session Timeout                - SKIP
```

## 📁 Project Structure

```
eGalvanic-Automation/
├── pom.xml
├── testng.xml
├── config/
│   └── config.properties
├── src/main/java/com/egalvanic/
│   ├── base/
│   │   └── BasePage.java
│   ├── pages/
│   │   ├── WelcomePage.java
│   │   └── LoginPage.java
│   ├── utils/
│   │   ├── DriverManager.java
│   │   ├── ExtentReportManager.java
│   │   └── ScreenshotUtil.java
│   └── constants/
│       └── AppConstants.java
├── src/test/java/com/egalvanic/
│   ├── base/
│   │   └── BaseTest.java
│   └── tests/
│       └── AuthenticationTest.java
├── reports/
│   ├── detailed/
│   └── client/
├── screenshots/
└── .github/workflows/
    └── ios-tests.yml (commented)
```

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+
- Node.js 18+
- Appium 2.x
- Xcode (for iOS Simulator)

### Installation

```bash
# 1. Extract project
unzip eGalvanic-Automation.zip
cd eGalvanic-Automation

# 2. Install dependencies
mvn clean install -DskipTests

# 3. Start Appium
appium

# 4. Boot Simulator
xcrun simctl boot "iPhone 17 Pro"
open -a Simulator

# 5. Run tests
mvn clean test
```

### Run Specific Tests

```bash
# Run all Authentication tests
mvn test -Dtest=AuthenticationTest

# Run single test
mvn test -Dtest=AuthenticationTest#TC24_verifyLoginWithValidCredentials

# Run by priority range
mvn test -Dtest=AuthenticationTest#TC01* -Dtest=AuthenticationTest#TC15*
```

## 📋 Test Coverage

| Module | Feature | Test Cases | Automated |
|--------|---------|------------|-----------|
| Authentication | Company Code Validation | TC01-TC15 | 15 |
| Authentication | Login | TC16-TC33 | 18 |
| Authentication | Session Management | TC34-TC38 | 0 (Manual) |
| **Total** | | **38** | **33** |

## ⚙️ Configuration

Update `AppConstants.java` for your environment:

```java
public static final String APP_PATH = "/path/to/your/app.app";
public static final String DEVICE_NAME = "iPhone 17 Pro";
public static final String PLATFORM_VERSION = "26.2";
public static final String UDID = "your-simulator-udid";
```

## 🔄 CI/CD Setup

1. Create GitHub repository
2. Edit `.github/workflows/ios-tests.yml`
3. Remove `#` from all lines
4. Push to GitHub

## 📞 Support

For issues or questions, contact the QA team.

---
**Version:** 1.0.0  
**Last Updated:** December 2024

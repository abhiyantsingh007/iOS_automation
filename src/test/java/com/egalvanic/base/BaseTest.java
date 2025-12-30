package com.egalvanic.base;

import com.egalvanic.constants.AppConstants;
import com.egalvanic.pages.LoginPage;
import com.egalvanic.pages.WelcomePage;
import com.egalvanic.utils.DriverManager;
import com.egalvanic.utils.ExtentReportManager;
import com.egalvanic.utils.ScreenshotUtil;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * Base Test class - All test classes extend this
 * Handles driver lifecycle, report initialization, and result handling
 */
public class BaseTest {

    protected WelcomePage welcomePage;
    protected LoginPage loginPage;

    // ================================================================
    // SUITE LEVEL SETUP/TEARDOWN
    // ================================================================

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("========================================");
        System.out.println("  eGalvanic iOS Automation - Starting");
        System.out.println("========================================");
        
        // Initialize both Extent Reports
        ExtentReportManager.initReports();
        
        // Clean up old screenshots
        ScreenshotUtil.cleanupScreenshots();
    }

    @AfterSuite
    public void afterSuite() {
        // Flush both reports
        ExtentReportManager.flushReports();
        
        System.out.println("========================================");
        System.out.println("  eGalvanic iOS Automation - Complete");
        System.out.println("========================================");
        System.out.println("Reports generated:");
        System.out.println("  - Detailed: " + AppConstants.DETAILED_REPORT_PATH);
        System.out.println("  - Client:   " + AppConstants.CLIENT_REPORT_PATH);
    }

    // ================================================================
    // TEST LEVEL SETUP/TEARDOWN
    // ================================================================

    @BeforeMethod
    public void beforeMethod() {
        // Initialize driver
        DriverManager.initDriver();
        
        // Initialize page objects
        welcomePage = new WelcomePage();
        loginPage = new LoginPage();
        
        System.out.println("✔ Test setup complete");
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        // Handle test result for reports
        handleTestResult(result);
        
        // Clean up
        ExtentReportManager.removeTests();
        DriverManager.quitDriver();
        
        System.out.println("✔ Test cleanup complete");
    }

    // ================================================================
    // RESULT HANDLING
    // ================================================================

    /**
     * Handle test result and update reports accordingly
     * 
     * CLIENT REQUIREMENT:
     * - If field is empty and button is disabled, test should PASS (expected behavior)
     * - This logic is implemented in the test methods themselves
     */
    private void handleTestResult(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        
        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                ExtentReportManager.logPass("Test PASSED: " + testName);
                System.out.println("✔ PASSED: " + testName);
                break;
                
            case ITestResult.FAILURE:
                // Capture screenshot on failure (for detailed report only)
                String screenshotPath = ScreenshotUtil.captureScreenshot(testName + "_FAILED");
                
                if (screenshotPath != null) {
                    ExtentReportManager.logFailWithScreenshot(
                        "Test FAILED: " + testName,
                        screenshotPath
                    );
                } else {
                    ExtentReportManager.logFail(
                        "Test FAILED: " + testName,
                        result.getThrowable()
                    );
                }
                System.out.println("✗ FAILED: " + testName);
                break;
                
            case ITestResult.SKIP:
                ExtentReportManager.logSkip("Test SKIPPED: " + testName);
                System.out.println("- SKIPPED: " + testName);
                break;
                
            default:
                break;
        }
    }

    // ================================================================
    // HELPER METHODS FOR TESTS
    // ================================================================

    /**
     * Navigate to Login page by entering valid company code
     */
    protected void navigateToLoginPage() {
        welcomePage.submitCompanyCode(AppConstants.VALID_COMPANY_CODE);
        // Wait for login page to load
        try {
            Thread.sleep(AppConstants.PAGE_LOAD_WAIT);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Complete full login with valid credentials
     */
    protected void performValidLogin() {
        navigateToLoginPage();
        loginPage.login(AppConstants.VALID_EMAIL, AppConstants.VALID_PASSWORD);
    }

    /**
     * Log info to detailed report only
     */
    protected void logStep(String stepDescription) {
        ExtentReportManager.logInfo(stepDescription);
    }

    /**
     * Log step with screenshot to detailed report only
     */
    protected void logStepWithScreenshot(String stepDescription) {
        String screenshotPath = ScreenshotUtil.captureScreenshot(
            stepDescription.replaceAll("[^a-zA-Z0-9]", "_")
        );
        if (screenshotPath != null) {
            ExtentReportManager.logStepWithScreenshot(stepDescription, screenshotPath);
        } else {
            ExtentReportManager.logInfo(stepDescription);
        }
    }

    /**
     * Wait utility for tests
     */
    protected void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

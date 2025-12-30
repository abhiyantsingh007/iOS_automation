package com.egalvanic.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.egalvanic.constants.AppConstants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Dual Extent Report Manager
 * 
 * DETAILED REPORT (for QA Team):
 * - All screenshots, logs, step details, stack traces
 * - Full debugging information
 * 
 * CLIENT REPORT (for Client Presentation):
 * - Module > Feature > Test Name > Pass/Fail ONLY
 * - NO screenshots, NO logs, NO tags, NO technical details
 * - Clean, professional summary view
 */
public class ExtentReportManager {

    private static ExtentReports detailedReport;
    private static ExtentReports clientReport;
    
    private static final ThreadLocal<ExtentTest> detailedTest = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> clientTest = new ThreadLocal<>();
    
    // Track module/feature nodes for client report hierarchy
    private static final Map<String, ExtentTest> clientModuleNodes = new HashMap<>();
    private static final Map<String, ExtentTest> clientFeatureNodes = new HashMap<>();
    
    private static String timestamp;

    private ExtentReportManager() {}

    /**
     * Initialize both reports
     */
    public static void initReports() {
        timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        
        // Create report directories
        new File(AppConstants.DETAILED_REPORT_PATH).mkdirs();
        new File(AppConstants.CLIENT_REPORT_PATH).mkdirs();
        
        initDetailedReport();
        initClientReport();
        
        System.out.println("✔ Dual Extent Reports initialized");
    }

    /**
     * Initialize Detailed Report (for QA Team)
     */
    private static void initDetailedReport() {
        String reportPath = AppConstants.DETAILED_REPORT_PATH + "Detailed_Report_" + timestamp + ".html";
        
        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("eGalvanic iOS - Detailed QA Report");
        spark.config().setReportName("Detailed Test Execution Report");
        spark.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
        spark.config().setEncoding("UTF-8");
        
        // Enable all features for detailed report
        spark.config().setCss(
            ".badge-primary { background-color: #007bff; }" +
            ".test-content { padding: 15px; }" +
            ".screenshot { max-width: 100%; border: 1px solid #ddd; margin: 10px 0; }"
        );
        
        detailedReport = new ExtentReports();
        detailedReport.attachReporter(spark);
        
        // System Info
        detailedReport.setSystemInfo("Application", "eGalvanic iOS");
        detailedReport.setSystemInfo("Platform", "iOS");
        detailedReport.setSystemInfo("Device", AppConstants.DEVICE_NAME);
        detailedReport.setSystemInfo("iOS Version", AppConstants.PLATFORM_VERSION);
        detailedReport.setSystemInfo("Automation Tool", "Appium + XCUITest");
        detailedReport.setSystemInfo("Framework", "TestNG + Page Object Model");
        detailedReport.setSystemInfo("Report Type", "DETAILED (QA Team)");
    }

    /**
     * Initialize Client Report (for Client Presentation)
     * Shows ONLY: Module > Feature > Test Name > Pass/Fail
     * NO screenshots, NO logs, NO tags, NO technical details
     */
    private static void initClientReport() {
        String reportPath = AppConstants.CLIENT_REPORT_PATH + "Client_Report_" + timestamp + ".html";
        
        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setDocumentTitle("eGalvanic iOS - Test Results");
        spark.config().setReportName("Test Execution Summary");
        spark.config().setTimeStampFormat("MMM dd, yyyy");
        spark.config().setEncoding("UTF-8");
        
        // Custom CSS to hide all technical details and show clean pass/fail
        spark.config().setCss(
            // Hide screenshots completely
            ".screenshot, .screen-capture, img { display: none !important; }" +
            
            // Hide log details, stack traces, exceptions
            ".exception-content, .stack-trace, pre { display: none !important; }" +
            
            // Hide tags section
            ".test-tag, .category-tags, .tag { display: none !important; }" +
            
            // Hide detailed logs and info messages
            ".log-content, .step-details { display: none !important; }" +
            
            // Hide timestamp details from steps
            ".timestamp, .test-time-info { display: none !important; }" +
            
            // Hide the details/logs panel
            ".details-col, .log-details { display: none !important; }" +
            
            // Clean styling for pass/fail badges
            ".badge-pass { background-color: #28a745 !important; color: white !important; " +
            "             padding: 5px 15px !important; border-radius: 4px !important; " +
            "             font-weight: bold !important; font-size: 14px !important; }" +
            ".badge-fail { background-color: #dc3545 !important; color: white !important; " +
            "             padding: 5px 15px !important; border-radius: 4px !important; " +
            "             font-weight: bold !important; font-size: 14px !important; }" +
            ".badge-skip { background-color: #ffc107 !important; color: #333 !important; " +
            "             padding: 5px 15px !important; border-radius: 4px !important; " +
            "             font-weight: bold !important; font-size: 14px !important; }" +
            
            // Make test names prominent
            ".test-name { font-size: 16px !important; font-weight: 500 !important; }" +
            
            // Clean node styling
            ".node { padding: 10px !important; margin: 5px 0 !important; " +
            "        border-left: 4px solid #007bff !important; }" +
            
            // Module headers styling
            ".test-detail { background-color: #f8f9fa !important; " +
            "               border-radius: 8px !important; margin: 10px 0 !important; }" +
            
            // Hide media panel
            ".media-container { display: none !important; }" +
            
            // Simple clean layout
            "body { font-family: 'Segoe UI', Arial, sans-serif !important; }" +
            ".container { max-width: 1200px !important; }" +
            
            // Hide step-by-step logs
            ".node-step, .step { display: none !important; }"
        );
        
        clientReport = new ExtentReports();
        clientReport.attachReporter(spark);
        
        // Minimal system info for client
        clientReport.setSystemInfo("Application", "eGalvanic iOS");
        clientReport.setSystemInfo("Test Date", new SimpleDateFormat("MMMM dd, yyyy").format(new Date()));
    }

    /**
     * Create test with Module > Feature hierarchy for CLIENT REPORT
     * 
     * @param moduleName   e.g., "Authentication"
     * @param featureName  e.g., "Login", "Company Code Validation"
     * @param testName     e.g., "Verify login with valid credentials"
     */
    public static void createTest(String moduleName, String featureName, String testName) {
        // === DETAILED REPORT: Simple flat test with all details ===
        ExtentTest detailed = detailedReport.createTest(testName);
        detailed.assignCategory(moduleName, featureName);
        detailedTest.set(detailed);
        
        // === CLIENT REPORT: Hierarchical Module > Feature > Test ===
        // Get or create Module node
        String moduleKey = moduleName;
        ExtentTest moduleNode = clientModuleNodes.get(moduleKey);
        if (moduleNode == null) {
            moduleNode = clientReport.createTest(moduleName);
            clientModuleNodes.put(moduleKey, moduleNode);
        }
        
        // Get or create Feature node under Module
        String featureKey = moduleName + "|" + featureName;
        ExtentTest featureNode = clientFeatureNodes.get(featureKey);
        if (featureNode == null) {
            featureNode = moduleNode.createNode(featureName);
            clientFeatureNodes.put(featureKey, featureNode);
        }
        
        // Create Test under Feature (just name, no category/tags)
        ExtentTest clientTestNode = featureNode.createNode(testName);
        clientTest.set(clientTestNode);
    }

    /**
     * Log info step - DETAILED REPORT ONLY
     */
    public static void logInfo(String message) {
        if (detailedTest.get() != null) {
            detailedTest.get().info(message);
        }
        // Client report: NO logs
    }

    /**
     * Log step with screenshot - DETAILED REPORT ONLY
     */
    public static void logStepWithScreenshot(String stepDescription, String screenshotPath) {
    if (detailedTest.get() != null) {
        try {
            // Use Base64 instead of file path (more reliable)
            String base64 = ScreenshotUtil.captureScreenshotAsBase64();
            if (base64 != null) {
                detailedTest.get().info(stepDescription)
                    .addScreenCaptureFromBase64String(base64);
            } else {
                detailedTest.get().info(stepDescription + " [Screenshot failed]");
            }
        } catch (Exception e) {
            detailedTest.get().info(stepDescription + " [Screenshot failed]");
        }
    }
}

    /**
     * Log PASS result
     * - Detailed: Full message with details
     * - Client: Just marks as PASS (no message shown due to CSS)
     */
    public static void logPass(String message) {
        if (detailedTest.get() != null) {
            detailedTest.get().pass(message);
        }
        if (clientTest.get() != null) {
            // For client, just mark pass - CSS hides the message
            clientTest.get().pass("✓");
        }
    }

    /**
     * Log FAIL result
     * - Detailed: Full message with exception details
     * - Client: Just marks as FAIL (no technical details shown)
     */
    public static void logFail(String message) {
        if (detailedTest.get() != null) {
            detailedTest.get().fail(message);
        }
        if (clientTest.get() != null) {
            // For client, just mark fail - CSS hides technical details
            clientTest.get().fail("✗");
        }
    }

    /**
     * Log FAIL with exception - DETAILED gets full stack trace, CLIENT just sees FAIL
     */
    public static void logFail(String message, Throwable throwable) {
        if (detailedTest.get() != null) {
            detailedTest.get().fail(message);
            detailedTest.get().fail(throwable);
        }
        if (clientTest.get() != null) {
            // Client just sees FAIL status, no exception details
            clientTest.get().fail("✗");
        }
    }

    /**
     * Log FAIL with screenshot - DETAILED ONLY gets screenshot
     */
    public static void logFailWithScreenshot(String message, String screenshotPath) {
        if (detailedTest.get() != null) {
            try {
                detailedTest.get().fail(message)
                    .addScreenCaptureFromPath(screenshotPath);
            } catch (Exception e) {
                detailedTest.get().fail(message + " [Screenshot failed]");
            }
        }
        if (clientTest.get() != null) {
            clientTest.get().fail("✗");
        }
    }

    /**
     * Log SKIP result
     */
    public static void logSkip(String message) {
        if (detailedTest.get() != null) {
            detailedTest.get().skip(message);
        }
        if (clientTest.get() != null) {
            clientTest.get().skip("-");
        }
    }

    /**
     * Log WARNING - DETAILED REPORT ONLY
     */
    public static void logWarning(String message) {
        if (detailedTest.get() != null) {
            detailedTest.get().warning(message);
        }
        // Client: No warnings shown
    }

    /**
     * Flush both reports - MUST be called at end of suite
     */
    public static void flushReports() {
        if (detailedReport != null) {
            detailedReport.flush();
            System.out.println("✔ Detailed Report saved to: " + AppConstants.DETAILED_REPORT_PATH);
        }
        if (clientReport != null) {
            clientReport.flush();
            System.out.println("✔ Client Report saved to: " + AppConstants.CLIENT_REPORT_PATH);
        }
    }

    /**
     * Get current detailed test (for advanced usage)
     */
    public static ExtentTest getDetailedTest() {
        return detailedTest.get();
    }

    /**
     * Get current client test (for advanced usage)
     */
    public static ExtentTest getClientTest() {
        return clientTest.get();
    }

    /**
     * Clean up thread locals
     */
    public static void removeTests() {
        detailedTest.remove();
        clientTest.remove();
    }
}

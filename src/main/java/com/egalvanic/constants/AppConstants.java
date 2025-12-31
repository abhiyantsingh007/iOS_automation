package com.egalvanic.constants;

/**
 * Application Constants - All configurable values
 * eGalvanic iOS Automation Framework
 * Supports both local and CI/CD environments
 */
public class AppConstants {

    // ============================================
    // APPIUM CONFIGURATION (Environment aware)
    // ============================================
    public static final String APPIUM_SERVER = getEnv("APPIUM_SERVER", "http://127.0.0.1:4723");
    public static final String APP_PATH = getEnv("APP_PATH", "/Users/abhiyantsingh/Downloads/Z Platform-QA.app");
    public static final String DEVICE_NAME = getEnv("DEVICE_NAME", "iPhone 17 Pro");
    public static final String PLATFORM_VERSION = getEnv("PLATFORM_VERSION", "26.2");
    public static final String UDID = getEnv("SIMULATOR_UDID", "B745C0EF-01AA-4355-8B08-86812A8CBBAA");
    public static final String PLATFORM_NAME = "iOS";
    public static final String AUTOMATION_NAME = "XCUITest";

    // ============================================
    // TEST DATA - COMPANY CODE
    // ============================================
    public static final String VALID_COMPANY_CODE = "acme.egalvanic";
    public static final String INVALID_COMPANY_CODE = "xyz123invalid";
    public static final String COMPANY_CODE_PLACEHOLDER = "(e.g. acme.egalvanic)";

    // ============================================
    // TEST DATA - LOGIN
    // ============================================
    public static final String VALID_EMAIL = "rahul+acme@egalvanic.com";
    public static final String VALID_PASSWORD = "RP@egalvanic123";
    public static final String INVALID_EMAIL = "invalidemail@";
    public static final String INVALID_PASSWORD = "wrongpassword123";

    // ============================================
    // TIMEOUTS (in seconds)
    // ============================================
    public static final int IMPLICIT_WAIT = 5;
    public static final int EXPLICIT_WAIT = 5;
    public static final int AJAX_TIMEOUT = 5;
    
    // ============================================
    // THREAD SLEEP TIMES (in milliseconds)
    // ============================================
    public static final int PAGE_LOAD_WAIT = 500;
    public static final int ELEMENT_LOAD_WAIT = 500;
    public static final int ANIMATION_WAIT = 500;
    public static final int SHORT_WAIT = 500;

    // ============================================
    // REPORT CONFIGURATION
    // ============================================
    public static final String DETAILED_REPORT_PATH = "reports/detailed/";
    public static final String CLIENT_REPORT_PATH = "reports/client/";
    public static final String SCREENSHOT_PATH = "screenshots/";
    public static final String DETAILED_REPORT_NAME = "eGalvanic_Detailed_Report.html";
    public static final String CLIENT_REPORT_NAME = "eGalvanic_Client_Report.html";

    // ============================================
    // MODULE & FEATURE NAMES (for Client Report)
    // ============================================
    public static final String MODULE_AUTHENTICATION = "Authentication";
    public static final String FEATURE_COMPANY_CODE = "Company Code Validation";
    public static final String FEATURE_LOGIN = "Login";
    public static final String FEATURE_SESSION = "Session Management";

    private AppConstants() {
        // Prevent instantiation
    }

    /**
     * Get environment variable with fallback to default value
     */
    private static String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }
}

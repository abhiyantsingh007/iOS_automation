package com.egalvanic.utils;

import com.egalvanic.constants.AppConstants;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Thread-safe Driver Manager using ThreadLocal
 * Manages IOSDriver lifecycle for parallel test execution
 */
public class DriverManager {

    private static final ThreadLocal<IOSDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {
        // Prevent instantiation
    }

    /**
     * Initialize and get IOSDriver instance
     */
    public static IOSDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            initDriver();
        }
        return driverThreadLocal.get();
    }

    /**
     * Initialize IOSDriver with XCUITest options
     */
    public static void initDriver() {
        try {
            XCUITestOptions options = new XCUITestOptions();
            
            // Device Configuration
            options.setDeviceName(AppConstants.DEVICE_NAME);
            options.setPlatformVersion(AppConstants.PLATFORM_VERSION);
            options.setUdid(AppConstants.UDID);
            options.setApp(AppConstants.APP_PATH);
            
            // Automation Settings
            options.setAutomationName(AppConstants.AUTOMATION_NAME);
            options.setNoReset(false);  // Fresh app state each test
            options.setFullReset(false);
            
            // Alert Handling
            options.setCapability("autoAcceptAlerts", true);
            options.setCapability("autoDismissAlerts", false);
            
            // Performance Settings
            options.setCapability("waitForQuiescence", true);
            options.setCapability("shouldUseSingletonTestManager", false);
            
            // Timeouts
            options.setCapability("newCommandTimeout", 300);
            options.setCapability("launchTimeout", 120000);
            
            // Create driver
            IOSDriver driver = new IOSDriver(
                new URL(AppConstants.APPIUM_SERVER), 
                options
            );
            
            // Set implicit wait
            driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(AppConstants.IMPLICIT_WAIT)
            );
            
            driverThreadLocal.set(driver);
            System.out.println("✔ IOSDriver initialized successfully");
            
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium server URL: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize driver: " + e.getMessage());
        }
    }

    /**
     * Quit driver and clean up
     */
    public static void quitDriver() {
        IOSDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("✔ IOSDriver quit successfully");
            } catch (Exception e) {
                System.err.println("Error quitting driver: " + e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }

    /**
     * Check if driver is active
     */
    public static boolean isDriverActive() {
        IOSDriver driver = driverThreadLocal.get();
        if (driver == null) {
            return false;
        }
        try {
            driver.getSessionId();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

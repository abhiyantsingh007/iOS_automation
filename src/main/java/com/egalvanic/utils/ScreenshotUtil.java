package com.egalvanic.utils;

import com.egalvanic.constants.AppConstants;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Screenshot Utility for capturing test evidence
 */
public class ScreenshotUtil {

    private ScreenshotUtil() {}

    /**
     * Capture screenshot and save to file
     * @param screenshotName Name for the screenshot
     * @return Path to saved screenshot
     */
    public static String captureScreenshot(String screenshotName) {
        try {
            IOSDriver driver = DriverManager.getDriver();
            if (driver == null) {
                System.err.println("Cannot capture screenshot - driver is null");
                return null;
            }

            // Generate unique filename
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String fileName = screenshotName.replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp + ".png";
            String filePath = AppConstants.SCREENSHOT_PATH + fileName;

            // Ensure directory exists
            new File(AppConstants.SCREENSHOT_PATH).mkdirs();

            // Capture and save
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            FileUtils.copyFile(srcFile, destFile);

            System.out.println("✔ Screenshot saved: " + filePath);
            return filePath;

        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Capture screenshot as Base64 string
     */
    public static String captureScreenshotAsBase64() {
        try {
            IOSDriver driver = DriverManager.getDriver();
            if (driver == null) {
                return null;
            }
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            System.err.println("Failed to capture Base64 screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Clean up old screenshots
     */
    public static void cleanupScreenshots() {
        try {
            File screenshotDir = new File(AppConstants.SCREENSHOT_PATH);
            if (screenshotDir.exists()) {
                File[] files = screenshotDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        file.delete();
                    }
                }
                System.out.println("✔ Screenshots cleaned up");
            }
        } catch (Exception e) {
            System.err.println("Failed to cleanup screenshots: " + e.getMessage());
        }
    }
}

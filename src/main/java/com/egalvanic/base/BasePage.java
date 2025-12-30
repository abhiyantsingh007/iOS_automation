package com.egalvanic.base;

import com.egalvanic.constants.AppConstants;
import com.egalvanic.utils.DriverManager;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Base Page class with PageFactory and AjaxElementLocatorFactory
 * All Page Objects extend this class
 * 
 * Includes Thread.sleep for elements that take extra time to load
 */
public abstract class BasePage {

    protected IOSDriver driver;
    protected WebDriverWait wait;

    /**
     * Constructor - initializes PageFactory with AjaxElementLocatorFactory
     */
    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(AppConstants.EXPLICIT_WAIT));
        
        // Initialize PageFactory with AjaxElementLocatorFactory for lazy loading
        // Elements are located only when accessed, with built-in wait
        PageFactory.initElements(
            new AppiumFieldDecorator(driver, Duration.ofSeconds(AppConstants.AJAX_TIMEOUT)), 
            this
        );
    }

    // ================================================================
    // THREAD SLEEP METHODS - For elements that need extra load time
    // ================================================================

    /**
     * Wait for page to fully load (use after navigation)
     */
    protected void waitForPageLoad() {
        try {
            Thread.sleep(AppConstants.PAGE_LOAD_WAIT);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Wait for slow element to load (use before interacting with slow elements)
     */
    protected void waitForElementLoad() {
        try {
            Thread.sleep(AppConstants.ELEMENT_LOAD_WAIT);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Wait for UI animation to complete
     */
    protected void waitForAnimation() {
        try {
            Thread.sleep(AppConstants.ANIMATION_WAIT);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Short wait for quick transitions
     */
    protected void shortWait() {
        try {
            Thread.sleep(AppConstants.SHORT_WAIT);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Custom sleep with specified milliseconds
     */
    protected void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ================================================================
    // ELEMENT INTERACTION METHODS
    // ================================================================

    /**
     * Click on element with wait
     */
    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    /**
     * Dismiss the keyboard to prevent click issues
     */
    protected void dismissKeyboard() {
        try {
            // Try to hide keyboard using Appium's built-in method
            driver.hideKeyboard();
            shortWait(); // Brief wait after hiding keyboard
        } catch (Exception e) {
            // If hideKeyboard doesn't work, try alternative methods
            try {
                // Alternative: press the 'Done' key on the keyboard
                driver.hideKeyboard("Done");
                shortWait();
            } catch (Exception ex1) {
                try {
                    // Try pressing the 'Return' key
                    driver.hideKeyboard("Return");
                    shortWait();
                } catch (Exception ex2) {
                    try {
                        // Try pressing the 'Go' key
                        driver.hideKeyboard("Go");
                        shortWait();
                    } catch (Exception ex3) {
                        try {
                            // Alternative: tap on the screen to dismiss keyboard
                            driver.executeScript("mobile: tap", 
                                java.util.Collections.singletonMap("x", 100),
                                java.util.Collections.singletonMap("y", 100));
                            shortWait();
                        } catch (Exception ex4) {
                            try {
                                // Use TouchAction as fallback
                                io.appium.java_client.TouchAction touchAction = 
                                    new io.appium.java_client.TouchAction(driver);
                                touchAction.tap(io.appium.java_client.touch.offset.PointOption.point(100, 100)).perform();
                                shortWait();
                            } catch (Exception ex5) {
                                // If all methods fail, just continue with the test
                                System.out.println("Could not dismiss keyboard, continuing test...");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Click on element with keyboard dismissal (use for buttons that may be obstructed by keyboard)
     */
    protected void clickWithKeyboardDismiss(WebElement element) {
        // Dismiss keyboard before clicking to prevent click issues
        dismissKeyboard();
        shortWait();
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    /**
     * Enter text into element with wait and clear
     */
    protected void enterText(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Get text from element with wait
     */
    protected String getText(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getText();
    }

    /**
     * Check if element is displayed
     */
    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if element is enabled
     */
    protected boolean isEnabled(WebElement element) {
        try {
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get element attribute value
     */
    protected String getAttribute(WebElement element, String attributeName) {
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getAttribute(attributeName);
    }

    // ================================================================
    // EXPLICIT WAIT METHODS
    // ================================================================

    /**
     * Wait for element to be visible
     */
    protected WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait for element to be clickable
     */
    protected WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Wait for element to be present by locator
     */
    protected WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait for element to be invisible
     */
    protected boolean waitForInvisibility(WebElement element) {
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * Wait with custom timeout
     */
    protected WebElement waitForVisibility(WebElement element, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(ExpectedConditions.visibilityOf(element));
    }

    // ================================================================
    // ABSTRACT METHODS - Must be implemented by child pages
    // ================================================================

    /**
     * Check if page is loaded
     */
    public abstract boolean isPageLoaded();

    /**
     * Get page name for logging
     */
    public abstract String getPageName();
}

package com.egalvanic.pages;

import com.egalvanic.base.BasePage;
import com.egalvanic.constants.AppConstants;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Login Page - Email and Password Entry Screen
 * Shown after valid company code is entered
 * 
 * CLIENT REQUIREMENT: Sign In button should be DISABLED when fields are empty
 * This is EXPECTED behavior - tests should PASS when button is disabled with empty fields
 */
public class LoginPage extends BasePage {

    // ================================================================
    // PAGE ELEMENTS
    // ================================================================

    // Email Text Field
    @iOSXCUITFindBy(iOSNsPredicate = "type == 'XCUIElementTypeTextField' AND (label CONTAINS 'email' OR label CONTAINS 'Email' OR placeholder CONTAINS 'email')")
    private WebElement emailField;

    // Alternative: Email field by type
    @iOSXCUITFindBy(iOSNsPredicate = "type == 'XCUIElementTypeTextField'")
    private WebElement emailFieldAlt;

    // Password Secure Text Field
    @iOSXCUITFindBy(iOSNsPredicate = "type == 'XCUIElementTypeSecureTextField'")
    private WebElement passwordField;

    // Sign In Button - Multiple locator strategies
    @iOSXCUITFindBy(accessibility = "Sign In")
    private WebElement signInButton;

    // Alternative: Sign In by predicate
    @iOSXCUITFindBy(iOSNsPredicate = "type == 'XCUIElementTypeButton' AND (label == 'Sign In' OR label == 'Login' OR label == 'Log In')")
    private WebElement signInButtonAlt;
    
    // Additional alternative locators for Sign In button
    @iOSXCUITFindBy(iOSNsPredicate = "type == 'XCUIElementTypeButton'")
    private List<WebElement> allButtons;
    
    // XPath locator as fallback
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[contains(@label, 'Sign') or contains(@label, 'Login') or contains(@label, 'Log')]")
    private WebElement signInButtonXPath;

    // Show/Hide Password Toggle
    @iOSXCUITFindBy(accessibility = "Show Password")
    private WebElement showPasswordIcon;

    // Change Company Code Link
    @iOSXCUITFindBy(iOSNsPredicate = "label CONTAINS 'Change' OR label CONTAINS 'company'")
    private WebElement changeCompanyLink;

    // Forgot Password Link
    @iOSXCUITFindBy(iOSNsPredicate = "label CONTAINS 'Forgot' OR label CONTAINS 'forgot'")
    private WebElement forgotPasswordLink;

    // Error Message
    @iOSXCUITFindBy(iOSNsPredicate = "type == 'XCUIElementTypeStaticText' AND (label CONTAINS 'error' OR label CONTAINS 'Error' OR label CONTAINS 'Invalid' OR label CONTAINS 'invalid' OR label CONTAINS 'incorrect')")
    private WebElement errorMessage;

    // Login Page Title/Header
    @iOSXCUITFindBy(iOSNsPredicate = "type == 'XCUIElementTypeStaticText' AND (label CONTAINS 'Sign In' OR label CONTAINS 'Login' OR label CONTAINS 'Log In')")
    private WebElement loginTitle;

    // ================================================================
    // CONSTRUCTOR
    // ================================================================

    public LoginPage() {
        super();
        // Wait for page to load after navigation
        waitForPageLoad();
    }

    // ================================================================
    // PAGE ACTIONS
    // ================================================================

    /**
     * Enter email address
     */
    public void enterEmail(String email) {
        waitForElementLoad();
        try {
            waitForVisibility(emailField);
            emailField.clear();
            emailField.sendKeys(email);
        } catch (Exception e) {
            // Fallback to alternative locator
            waitForVisibility(emailFieldAlt);
            emailFieldAlt.clear();
            emailFieldAlt.sendKeys(email);
        }
    }

    /**
     * Enter password
     */
    public void enterPassword(String password) {
        waitForElementLoad();
        waitForVisibility(passwordField);
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    /**
     * Tap Sign In button
     */
    public void tapSignIn() {
        // Dismiss keyboard first
        dismissKeyboard();
        shortWait();
        
        // Try multiple approaches to click the sign-in button
        // Approach 1: Use direct click with no explicit wait
        try {
            signInButton.click();
            return;
        } catch (Exception e1) {
            try {
                signInButtonAlt.click();
                return;
            } catch (Exception e2) {
                // Approach 2: Use JavaScript executor
                try {
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", signInButton);
                    return;
                } catch (Exception e3) {
                    try {
                        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", signInButtonAlt);
                        return;
                    } catch (Exception e4) {
                        // Approach 3: Find and click by XPath
                        try {
                            WebElement button = driver.findElement(org.openqa.selenium.By.xpath("//XCUIElementTypeButton[contains(@label, 'Sign') or contains(@label, 'Login') or contains(@label, 'Log') or contains(@name, 'Sign') or contains(@name, 'Login') or contains(@name, 'Log')]")
                            );
                            button.click();
                            return;
                        } catch (Exception e5) {
                            // Approach 4: Tap by coordinates
                            try {
                                // Look for any button that might be the sign-in button
                                List<WebElement> allButtons = driver.findElements(org.openqa.selenium.By.xpath("//XCUIElementTypeButton"));
                                for (WebElement button : allButtons) {
                                    String label = button.getAttribute("label");
                                    String name = button.getAttribute("name");
                                    if (label != null && (label.toLowerCase().contains("sign") || 
                                        label.toLowerCase().contains("login") || 
                                        label.toLowerCase().contains("log"))) {
                                        int centerX = button.getLocation().getX() + (button.getSize().getWidth() / 2);
                                        int centerY = button.getLocation().getY() + (button.getSize().getHeight() / 2);
                                        io.appium.java_client.TouchAction touchAction = 
                                            new io.appium.java_client.TouchAction(driver);
                                        touchAction.tap(io.appium.java_client.touch.offset.PointOption.point(centerX, centerY)).perform();
                                        return;
                                    }
                                }
                            } catch (Exception e6) {
                                // If all else fails, try coordinate tap on any button
                                try {
                                    List<WebElement> allButtons = driver.findElements(org.openqa.selenium.By.xpath("//XCUIElementTypeButton"));
                                    if (!allButtons.isEmpty()) {
                                        WebElement firstButton = allButtons.get(0);
                                        int centerX = firstButton.getLocation().getX() + (firstButton.getSize().getWidth() / 2);
                                        int centerY = firstButton.getLocation().getY() + (firstButton.getSize().getHeight() / 2);
                                        io.appium.java_client.TouchAction touchAction = 
                                            new io.appium.java_client.TouchAction(driver);
                                        touchAction.tap(io.appium.java_client.touch.offset.PointOption.point(centerX, centerY)).perform();
                                        return;
                                    }
                                } catch (Exception e7) {
                                    throw new RuntimeException("Unable to click sign-in button using any method", e7);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Dismiss the keyboard to prevent click issues
     */
    public void dismissKeyboard() {
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
     * Complete login with credentials
     */
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        shortWait();
        // Dismiss keyboard before tapping sign in
        dismissKeyboard();
        shortWait();
        tapSignIn();
        waitForPageLoad();
    }

    /**
     * Tap Show/Hide Password icon
     */
    public void tapShowPassword() {
        click(showPasswordIcon);
    }

    /**
     * Tap Change Company Code link
     */
    public void tapChangeCompanyCode() {
        click(changeCompanyLink);
        waitForAnimation();
    }

    /**
     * Tap Forgot Password link
     */
    public void tapForgotPassword() {
        click(forgotPasswordLink);
        waitForAnimation();
    }

    /**
     * Clear email field
     */
    public void clearEmail() {
        waitForElementLoad();
        try {
            emailField.clear();
        } catch (Exception e) {
            emailFieldAlt.clear();
        }
    }

    /**
     * Clear password field
     */
    public void clearPassword() {
        waitForElementLoad();
        passwordField.clear();
    }

    /**
     * Clear all fields
     */
    public void clearAllFields() {
        clearEmail();
        clearPassword();
    }

    // ================================================================
    // VERIFICATION METHODS
    // ================================================================

    /**
     * Check if Login page is loaded
     */
    @Override
    public boolean isPageLoaded() {
        waitForElementLoad();
        return isDisplayed(passwordField);
    }

    /**
     * Get page name for logging
     */
    @Override
    public String getPageName() {
        return "Login Page";
    }

    /**
     * Check if email field is displayed
     */
    public boolean isEmailFieldDisplayed() {
        try {
            return isDisplayed(emailField) || isDisplayed(emailFieldAlt);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if password field is displayed
     */
    public boolean isPasswordFieldDisplayed() {
        return isDisplayed(passwordField);
    }

    /**
     * Check if Sign In button is displayed
     */
    public boolean isSignInButtonDisplayed() {
        try {
            return isDisplayed(signInButton) || isDisplayed(signInButtonAlt);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if Sign In button is ENABLED
     * 
     * CLIENT REQUIREMENT: Button should be DISABLED when fields are empty
     */
    public boolean isSignInButtonEnabled() {
        shortWait();
        try {
            return signInButton.isEnabled();
        } catch (Exception e) {
            try {
                return signInButtonAlt.isEnabled();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        waitForAnimation();
        return isDisplayed(errorMessage);
    }

    /**
     * Get error message text
     */
    public String getErrorMessageText() {
        if (isErrorMessageDisplayed()) {
            return getText(errorMessage);
        }
        return "";
    }

    /**
     * Get email field text
     */
    public String getEmailText() {
        try {
            return getAttribute(emailField, "value");
        } catch (Exception e) {
            return getAttribute(emailFieldAlt, "value");
        }
    }

    /**
     * Get password field text (will be masked)
     */
    public String getPasswordText() {
        return getAttribute(passwordField, "value");
    }

    /**
     * Check if email field is empty
     */
    public boolean isEmailFieldEmpty() {
        String value = getEmailText();
        return value == null || value.trim().isEmpty();
    }

    /**
     * Check if password field is empty
     */
    public boolean isPasswordFieldEmpty() {
        String value = getPasswordText();
        return value == null || value.trim().isEmpty();
    }

    /**
     * Check if Forgot Password link is displayed
     */
    public boolean isForgotPasswordDisplayed() {
        return isDisplayed(forgotPasswordLink);
    }

    /**
     * Check if Change Company link is displayed
     */
    public boolean isChangeCompanyLinkDisplayed() {
        return isDisplayed(changeCompanyLink);
    }

    // ================================================================
    // BUTTON DISABLED VALIDATION - CLIENT REQUIREMENT
    // ================================================================

    /**
     * VALIDATION: Sign In button disabled when BOTH fields are empty
     * 
     * CLIENT REQUIREMENT: This is EXPECTED behavior
     * Test should PASS if button is disabled when fields are empty
     * 
     * @return true if behavior is correct (empty fields = disabled button)
     */
    public boolean isButtonDisabledWhenBothFieldsEmpty() {
        clearAllFields();
        shortWait();
        boolean emailEmpty = isEmailFieldEmpty();
        boolean passwordEmpty = isPasswordFieldEmpty();
        boolean buttonDisabled = !isSignInButtonEnabled();
        
        // Expected: both fields empty AND button disabled
        return emailEmpty && passwordEmpty && buttonDisabled;
    }

    /**
     * VALIDATION: Sign In button disabled when email is empty
     * 
     * @return true if button is disabled when email is empty
     */
    public boolean isButtonDisabledWhenEmailEmpty() {
        clearEmail();
        enterPassword("somepassword");
        shortWait();
        boolean buttonDisabled = !isSignInButtonEnabled();
        clearPassword();
        return buttonDisabled;
    }

    /**
     * VALIDATION: Sign In button disabled when password is empty
     * 
     * @return true if button is disabled when password is empty
     */
    public boolean isButtonDisabledWhenPasswordEmpty() {
        enterEmail("test@email.com");
        clearPassword();
        shortWait();
        boolean buttonDisabled = !isSignInButtonEnabled();
        clearEmail();
        return buttonDisabled;
    }

    /**
     * VALIDATION: Sign In button ENABLED when BOTH fields have values
     * 
     * @return true if button is enabled when both fields have values
     */
    public boolean isButtonEnabledWhenBothFieldsFilled() {
        enterEmail("test@email.com");
        enterPassword("testpassword");
        shortWait();
        boolean buttonEnabled = isSignInButtonEnabled();
        clearAllFields();
        return buttonEnabled;
    }
}

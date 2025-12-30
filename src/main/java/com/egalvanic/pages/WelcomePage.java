package com.egalvanic.pages;

import com.egalvanic.base.BasePage;
import com.egalvanic.constants.AppConstants;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

/**
 * Welcome Page - Company Code Entry Screen
 * First screen shown when app launches
 * 
 * NOTE: Uses Thread.sleep before accessing companyCodeField
 * because the placeholder "(e.g. acme.egalvanic)" takes time to load
 */
public class WelcomePage extends BasePage {

    // ================================================================
    // PAGE ELEMENTS - Using iOSXCUITFindBy with various strategies
    // ================================================================

    // Company Code Text Field - The placeholder takes time to load!
    @iOSXCUITFindBy(iOSNsPredicate = "type == 'XCUIElementTypeTextField'")
    private WebElement companyCodeField;

    // Continue Button
    @iOSXCUITFindBy(accessibility = "Continue")
    private WebElement continueButton;

    // Alternative: Continue button by type and label
    @iOSXCUITFindBy(iOSNsPredicate = "type == 'XCUIElementTypeButton' AND label == 'Continue'")
    private WebElement continueButtonAlt;

    // Back Button (if navigating back from login)
    @iOSXCUITFindBy(accessibility = "Back")
    private WebElement backButton;

    // Info/Help Icon
    @iOSXCUITFindBy(accessibility = "info")
    private WebElement infoIcon;

    // Welcome Text/Title
    @iOSXCUITFindBy(iOSNsPredicate = "type == 'XCUIElementTypeStaticText' AND label CONTAINS 'Welcome'")
    private WebElement welcomeText;

    // Error Message (for invalid company code)
    @iOSXCUITFindBy(iOSNsPredicate = "type == 'XCUIElementTypeStaticText' AND label CONTAINS 'not found'")
    private WebElement errorMessage;

    // ================================================================
    // CONSTRUCTOR
    // ================================================================

    public WelcomePage() {
        super();
        // CRITICAL: Wait for page elements to fully load
        // The placeholder "(e.g. acme.egalvanic)" takes time to appear
        waitForPageLoad();
    }

    // ================================================================
    // PAGE ACTIONS
    // ================================================================

    /**
     * Enter company code into text field
     * Includes Thread.sleep because element takes time to load
     * 
     * @param companyCode The company code to enter
     */
    public void enterCompanyCode(String companyCode) {
        // Extra wait for the text field to be fully ready
        waitForElementLoad();
        
        try {
            waitForVisibility(companyCodeField);
            companyCodeField.clear();
            companyCodeField.sendKeys(companyCode);
        } catch (Exception e) {
            // Retry with additional wait if element not ready
            sleep(2000);
            companyCodeField.clear();
            companyCodeField.sendKeys(companyCode);
        }
    }

    /**
     * Tap Continue button
     */
    public void tapContinue() {
        shortWait();
        try {
            click(continueButton);
        } catch (Exception e) {
            // Fallback to alternative locator
            click(continueButtonAlt);
        }
    }

    /**
     * Tap Back button
     */
    public void tapBack() {
        click(backButton);
    }

    /**
     * Tap Info icon
     */
    public void tapInfoIcon() {
        click(infoIcon);
    }

    /**
     * Complete company code entry and proceed
     * 
     * @param companyCode The company code to enter
     */
    public void submitCompanyCode(String companyCode) {
        enterCompanyCode(companyCode);
        shortWait();
        tapContinue();
        waitForAnimation();
    }

    /**
     * Clear company code field
     */
    public void clearCompanyCode() {
        waitForElementLoad();
        
        // First clear the field
        companyCodeField.clear();
        
        // Then send backspace characters to ensure it's fully cleared
        // This is often needed for iOS text fields
        companyCodeField.sendKeys("\u0008\u0008\u0008\u0008\u0008\u0008\u0008\u0008\u0008\u0008"); // 10 backspaces
        
        // Send empty string to ensure field is cleared
        companyCodeField.sendKeys("");
        
        shortWait();
    }

    // ================================================================
    // VERIFICATION METHODS
    // ================================================================

    /**
     * Check if Welcome page is displayed
     */
    @Override
    public boolean isPageLoaded() {
        waitForElementLoad();
        return isDisplayed(companyCodeField);
    }

    /**
     * Get page name for logging
     */
    @Override
    public String getPageName() {
        return "Welcome Page (Company Code)";
    }

    /**
     * Check if company code field is displayed
     */
    public boolean isCompanyCodeFieldDisplayed() {
        waitForElementLoad();
        return isDisplayed(companyCodeField);
    }

    /**
     * Check if Continue button is displayed
     */
    public boolean isContinueButtonDisplayed() {
        try {
            return isDisplayed(continueButton) || isDisplayed(continueButtonAlt);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if Continue button is ENABLED
     * 
     * CLIENT REQUIREMENT: Button should be DISABLED when field is empty
     * This is EXPECTED behavior - test should PASS if button is disabled with empty field
     */
    public boolean isContinueButtonEnabled() {
        shortWait();
        try {
            // Try primary locator first
            return continueButton.isEnabled();
        } catch (Exception e) {
            try {
                return continueButtonAlt.isEnabled();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    /**
     * Get current text in company code field
     */
    public String getCompanyCodeText() {
        waitForElementLoad();
        return getAttribute(companyCodeField, "value");
    }

    /**
     * Get placeholder text from company code field
     */
    public String getPlaceholderText() {
        waitForElementLoad();
        return getAttribute(companyCodeField, "placeholderValue");
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
     * Check if field is empty
     */
    public boolean isCompanyCodeFieldEmpty() {
        try {
            // Wait a bit to ensure field is updated after clearing
            shortWait();
            
            String value = getCompanyCodeText();
            
            // Get the placeholder text
            String placeholder = getAttribute(companyCodeField, "placeholder");
            
            // Check if value is null or empty after trimming
            if (value == null || value.trim().isEmpty()) {
                return true;
            }
            
            // If the field value matches the placeholder, it means the field is effectively empty
            if (placeholder != null && value.equals(placeholder)) {
                return true;
            }
            
            // If the field has any other value besides the placeholder, it's not empty
            return false;
        } catch (Exception e) {
            // If there's an issue getting the value, assume it's not empty
            System.out.println("Error checking if company code field is empty: " + e.getMessage());
            return false;
        }
    }

    /**
     * VALIDATION: Button disabled when field is empty (Expected behavior)
     * 
     * @return true if behavior is correct (empty field = disabled button)
     */
    public boolean isButtonDisabledWhenFieldEmpty() {
        clearCompanyCode();
        shortWait();
        boolean fieldEmpty = isCompanyCodeFieldEmpty();
        boolean buttonDisabled = !isContinueButtonEnabled();
        
        // Expected: field empty AND button disabled
        return fieldEmpty && buttonDisabled;
    }
}
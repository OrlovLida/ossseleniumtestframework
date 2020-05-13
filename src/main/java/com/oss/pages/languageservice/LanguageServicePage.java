package com.oss.pages.languageservice;

import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class LanguageServicePage extends BasePage {

    public LanguageServicePage(WebDriver driver) {super(driver);}

    @FindBy(xpath = "//a[contains(@class, 'loginButton')]")
    private WebElement loginButton;
    @FindBy(xpath = "//span[@class='switcher-inner']")
    private WebElement alphaModeSwitcher;
    @FindBy(id = "frameworkCustomButtonsGroup")
    private WebElement menu;
    @FindBy(id = "tableExportGUI")
    private WebElement exportGui;

    private ExportGuiWizardPage exportGuiWizard;

    private LanguageServicePage expandMenu() {
        waitForVisibility(menu);
        menu.click();
        return this;
    }

    public ExportGuiWizardPage openExportGuiWizard() {
        expandMenu();
        waitForVisibility(exportGui);
        exportGui.click();
        return new ExportGuiWizardPage(driver);
    }

    public LanguageServicePage clickOnLoginButton() {
        waitForVisibility(loginButton);
        loginButton.click();
        return this;
    }

    public LanguageServicePage changeForAlphaMode() {
        clickOnLoginButton();
        waitForVisibility(alphaModeSwitcher);
        alphaModeSwitcher.click();
        clickOnLoginButton();
        return this;
    }
}

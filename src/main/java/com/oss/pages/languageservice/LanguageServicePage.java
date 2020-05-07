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

    private ExportGuiWizard exportGuiWizard;

    public LanguageServicePage expandMenu() {
        waitForVisibility(menu);
        menu.click();
        return this;
    }

    public ExportGuiWizard openExportGuiWizard() {
        waitForVisibility(menu);
        exportGui.click();
        return new ExportGuiWizard(driver);
    }

    public LanguageServicePage clickOnLoginButton() {
        waitForVisibility(loginButton);
        loginButton.click();
        return this;
    }

    public LanguageServicePage changeMode() {
        waitForVisibility(alphaModeSwitcher);
        alphaModeSwitcher.click();
        return this;
    }
}

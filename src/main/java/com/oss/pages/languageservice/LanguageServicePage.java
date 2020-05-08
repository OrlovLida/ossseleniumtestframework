package com.oss.pages.languageservice;

import com.oss.pages.BasePage;
import com.oss.pages.exportguiwizard.ExportGuiWizardPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;


public class LanguageServicePage extends BasePage {

    public LanguageServicePage(WebDriver driver) {super(driver);}

    @FindBy(xpath = "//a[contains(@class, 'loginButton')]")
    private WebElement loginButton;
    @FindBy(xpath = "//span[@class='switcher-inner']")
    private WebElement alphaModeSwitcher;
    @FindBy(id = "frameworkCustomButtonsGroup")
    private WebElement menu;
    @FindBy(id = "tableExportGUI")
    private List<WebElement> exportGui;

    private ExportGuiWizardPage exportGuiWizard;

    private LanguageServicePage expandMenu() {
        waitForVisibility(menu);
        menu.click();
        return this;
    }

    public ExportGuiWizardPage openExportGuiWizard() {
        expandMenu();
        if (!existsElement(exportGui)) {
        changeForAlphaMode();
        expandMenu();
        }
        exportGui.get(0).click();
        return new ExportGuiWizardPage(driver);
    }

    private LanguageServicePage clickOnLoginButton() {
        waitForVisibility(loginButton);
        loginButton.click();
        return this;
    }

    private boolean existsElement(List<WebElement> element) {
        return element.size() != 0;
    }

    public LanguageServicePage changeForAlphaMode() {
        clickOnLoginButton();
        waitForVisibility(alphaModeSwitcher);
        alphaModeSwitcher.click();
        clickOnLoginButton();
        return this;
    }


}

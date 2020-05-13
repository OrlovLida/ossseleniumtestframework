package com.oss.pages.languageservice;

import com.oss.pages.BasePage;
import com.oss.pages.exportguiwizard.ExportGuiWizardPage;
import org.openqa.selenium.By;
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
    @FindBy(id = "tableExportGUI") //exportButton
    private List<WebElement> exportGui;
    @FindBy(xpath = "//i[contains(@aria-label, 'Notification')]")
    private WebElement notificationButton;
    @FindBy(xpath = "//div[@data-attributename ='search']//input")
    private WebElement searchField;
    @FindBy(xpath = "//div[@class='notificationContainer']/div")
    private List<WebElement> notificationsList ;
    @FindBy(xpath = "(//div[@type='Translation'])[1]")
    private WebElement firstService;
    @FindBy(xpath = "//a[@class ='clear-action']")
    private WebElement clearAllNotifications;

    private ExportGuiWizardPage exportGuiWizard;
    private boolean existsElement(List<WebElement> element) {
        return element.size() != 0;
    }

    private LanguageServicePage expandMenu() {
        waitForVisibility(menu);
        menu.click();
        return this;
    }

    private LanguageServicePage clickOnLoginButton() {
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

    public ExportGuiWizardPage openExportGuiWizard() {
        expandMenu();
        if (!existsElement(exportGui)) {
            changeForAlphaMode();
            expandMenu();
        }
        exportGui.get(0).click();
        return new ExportGuiWizardPage(driver);
    }

    public LanguageServicePage openNotificationPanel() {
        waitforclickability(notificationButton);
        //  if(!notificationIsOpen()) {
        notificationButton.click();
        //    }
        return this;
    }

    public LanguageServicePage clearNotifications(){
        openNotificationPanel();
        clearAllNotifications.click();
        closeNotificationPanel();
        return this;
    }


    public LanguageServicePage closeNotificationPanel(){
        waitforclickability(notificationButton);
        // if(notificationIsOpen()) {
        notificationButton.click();
        // }
        return this;
    }

    public int howManyNotifications(){
        openNotificationPanel();
        int amountOfNotifications = notificationsList.size();
        closeNotificationPanel();
        return amountOfNotifications;
    }

    private boolean notificationIsOpen(){
        return notificationButton.findElement(By.xpath("./ancestor::*/div[contains(@class,'globalNotification')]")).getAttribute("class").contains("clicked");
    }

    public LanguageServicePage typeIdOfFirstServiceInSearch() {
        waitForVisibility(firstService);
        String idOfFirstElement = firstService.getAttribute("id");
        searchField.sendKeys(idOfFirstElement);
        return this;
    }

}

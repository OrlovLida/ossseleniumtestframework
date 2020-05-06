package com.oss.pages.platform;


import com.oss.framework.components.portals.PopupV2;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.oss.pages.BasePage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.physical.LocationWizardPage;


public class HomePage extends BasePage {

    @FindBy(className = "oss-header-logo")
    private WebElement logo;

    @FindBy(className = "globalNotification")
    private WebElement globalNotificationButton;

    @FindBy(className = "loginButton")
    private WebElement loginButton;

    @FindBy(className = "titleText")
    private WebElement pageTitle;

    @FindBy(css = "span.notificationType")
    private WebElement numberOfNotificationsLabel;

    @FindBy (xpath = "//button[@data-original-title='Save bookmark']/i")
    //@FindBy (xpath = "//i[contains(@class,'buttonIcon fa fa-floppy-o')]")
    private WebElement saveBookmarksButton;

    HomePage(WebDriver driver) {
        super(driver);
        waitForVisibility(logo);
    }

    public WebElement getLoginButton() {
        return loginButton;
    }

    public String getPageTitle() {
        waitForVisibility(pageTitle);
        return pageTitle.getText();
    }

    public String getNumberOfNotifications() {
        try {
            return numberOfNotificationsLabel.getText();

        } catch (NoSuchElementException e) {
            return "0";
        }
    }

    public FormAppPage goToFormPage(String url){
        driver.get(url);
        return new FormAppPage(driver);
    }

    public PopupV2 goToCreateBookmarkPopUp() {
        waitForVisibility(saveBookmarksButton);
        saveBookmarksButton.click();
        return new PopupV2(driver);
    }

    public InputsWizardPage goToInputsWizardPage(String url){
        driver.get(url);
        return new InputsWizardPage(driver);
    }

    public DeviceWizardPage goToDeviceWizardPage(String url){
        driver.get(url);
        return new DeviceWizardPage(driver);
    }

    public LocationWizardPage goToLocationWizardPage(String url){
        driver.get(url);
        return new LocationWizardPage(driver);
    }

    public InventoryViewPage goToInventoryViewPage(String url) {
        driver.get(url);
        return new InventoryViewPage(driver);
    }

    public HierarchyViewPage goToHierarchyViewPage(String url) {
        driver.get(url);
        return new HierarchyViewPage(driver);
    }
}

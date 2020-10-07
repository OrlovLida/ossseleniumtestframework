package com.oss.framework.widgets.propertypanel;

import java.util.List;

import com.oss.framework.components.common.AttributesChooser;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PropertiesFilter {

    public static final String PROPERTIES_FILTER_CLASS = "settingsWithAddComponent";
    private static final String FILTER_BTN_PATH = ".//i";
    private final String SWITCHER_XPATH = ".//div[@class='switcher']";
    private final String KEBAB_XPATH = ".//div[@id='frameworkCustomButtonsGroup']";
    private final String CHOOSE_CONFIGURATION_XPATH = "//a[@data-attributename='chooseConfiguration']";
    private final String DOWNLOAD_CONFIGURATION_XPATH = "//a[@data-attributename='propertyPanelDownload']";
    private final String SAVE_NEW_CONFIGURATION_XPATH = "//a[@data-attributename='propertyPanelSave']";

    protected final WebDriver driver;
    protected final WebElement webElement;
    protected final WebDriverWait wait;
    private PropertiesFilterPanel propertiesFilterPanel;

    public PropertiesFilter(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = driver.findElement(By.className(PROPERTIES_FILTER_CLASS));
        this.wait = wait;
    }

    public PropertiesFilter(WebDriver driver, WebDriverWait wait, PropertyPanel propertyPanel) {
        this.driver = driver;
        this.webElement = propertyPanel.webElement.findElement(By.xpath(".//../*[@class = '" + PROPERTIES_FILTER_CLASS + "']"));
        this.wait = wait;
    }

    public static PropertiesFilter createByPropertyPanel(WebDriver driver, WebDriverWait wait, PropertyPanel propertyPanel) {
        return new PropertiesFilter(driver, wait, propertyPanel);
    }

    public static PropertiesFilter create(WebDriver driver, WebDriverWait wait) {
        return new PropertiesFilter(driver, wait);
    }

    private WebElement getFilterIcon() {
        return this.webElement.findElement(By.xpath(FILTER_BTN_PATH));
    }

    private WebElement getSwitcher() {
        return this.webElement.findElement(By.xpath(SWITCHER_XPATH));
    }

    public AttributesChooser clickOnFilterIcon() {
        getFilterIcon().click();
        return AttributesChooser.create(driver, wait);
    }

    public void hideEmpty() {
        if (getSwitcher().findElement(By.xpath(".//input")).getAttribute("value").equals("false"))
            getSwitcher().click();
    }

    public void showEmpty() {
        if (getSwitcher().findElement(By.xpath(".//input")).getAttribute("value").equals("true"))
            getSwitcher().click();
    }

    public void openChooseConfigurationWizard() {
        this.webElement.findElement(By.xpath(KEBAB_XPATH)).click();
        DelayUtils.waitByXPath(wait, CHOOSE_CONFIGURATION_XPATH);
        this.webElement.findElement(By.xpath(CHOOSE_CONFIGURATION_XPATH)).click();
    }

    public void openDownloadConfigurationWizard() {
        this.webElement.findElement(By.xpath(KEBAB_XPATH)).click();
        DelayUtils.waitByXPath(wait, DOWNLOAD_CONFIGURATION_XPATH);
        this.webElement.findElement(By.xpath(DOWNLOAD_CONFIGURATION_XPATH)).click();
    }

    public void openSaveAsNewConfigurationWizard() {
        this.webElement.findElement(By.xpath(KEBAB_XPATH)).click();
        DelayUtils.waitByXPath(wait, SAVE_NEW_CONFIGURATION_XPATH);
        this.webElement.findElement(By.xpath(SAVE_NEW_CONFIGURATION_XPATH)).click();
    }

    public List<String> getPropertyChbxLabelsFromPopup() {
        return propertiesFilterPanel.getCheckboxLabels();
    }

    public String getNthPropertyChbxLabelFromPopup(int n) {
        return getPropertyChbxLabelsFromPopup().get(n - 1);
    }

    public void clickOnSave() {
        propertiesFilterPanel.clickSave();
        propertiesFilterPanel = null;
    }

    public void typeIntoSearch(String text) {
        propertiesFilterPanel.typeIntoSearch(text);
    }

    public boolean isCheckboxChecked(String chbxLabel) {
        return propertiesFilterPanel.isCheckboxChecked(chbxLabel);
    }

    public void clickCheckbox(String chbxLabel) {
        propertiesFilterPanel.clickCheckbox(chbxLabel);
    }
}

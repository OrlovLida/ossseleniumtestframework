package com.oss.framework.widgets.propertypanel;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.common.AttributesChooser;
import com.oss.framework.components.portals.ChooseConfigurationWizard;
import com.oss.framework.components.portals.SaveConfigurationWizard;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class PropertiesFilter {

    public static final String PROPERTIES_FILTER_CLASS = "settingsWithAddComponent";
    private static final String FILTER_BTN_PATH = ".//i";
    private static final String SWITCHER_XPATH = ".//div[@class='switcher']";
    private static final String KEBAB_XPATH = ".//div[@id='frameworkCustomButtonsGroup']";
    private static final String CHOOSE_CONFIGURATION_XPATH = "//a[@" + CSSUtils.TEST_ID + "='chooseConfiguration']";
    private static final String DOWNLOAD_CONFIGURATION_XPATH = "//a[@" + CSSUtils.TEST_ID + "='propertyPanelDownload']";
    private static final String SAVE_NEW_CONFIGURATION_XPATH = "//a[@" + CSSUtils.TEST_ID + "='propertyPanelSave']";

    protected final WebDriver driver;
    protected final WebElement webElement;
    protected final WebDriverWait wait;
    private PropertiesFilterPanel propertiesFilterPanel;

    private PropertiesFilter(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = driver.findElement(By.className(PROPERTIES_FILTER_CLASS));
    }

    public static PropertiesFilter create(WebDriver driver, WebDriverWait wait) {
        return new PropertiesFilter(driver, wait);
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

    public ChooseConfigurationWizard openChooseConfigurationWizard() {
        this.webElement.findElement(By.xpath(KEBAB_XPATH)).click();
        DelayUtils.waitByXPath(wait, CHOOSE_CONFIGURATION_XPATH);
        this.webElement.findElement(By.xpath(CHOOSE_CONFIGURATION_XPATH)).click();
        return ChooseConfigurationWizard.create(driver, wait);
    }

    public ChooseConfigurationWizard openDownloadConfigurationWizard() {
        this.webElement.findElement(By.xpath(KEBAB_XPATH)).click();
        DelayUtils.waitByXPath(wait, DOWNLOAD_CONFIGURATION_XPATH);
        this.webElement.findElement(By.xpath(DOWNLOAD_CONFIGURATION_XPATH)).click();
        return ChooseConfigurationWizard.create(driver, wait);
    }

    public SaveConfigurationWizard openSaveAsNewConfigurationWizard() {
        this.webElement.findElement(By.xpath(KEBAB_XPATH)).click();
        DelayUtils.waitByXPath(wait, SAVE_NEW_CONFIGURATION_XPATH);
        this.webElement.findElement(By.xpath(SAVE_NEW_CONFIGURATION_XPATH)).click();
        return SaveConfigurationWizard.create(driver, wait);
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

    private WebElement getFilterIcon() {
        return this.webElement.findElement(By.xpath(FILTER_BTN_PATH));
    }

    private WebElement getSwitcher() {
        return this.webElement.findElement(By.xpath(SWITCHER_XPATH));
    }
}

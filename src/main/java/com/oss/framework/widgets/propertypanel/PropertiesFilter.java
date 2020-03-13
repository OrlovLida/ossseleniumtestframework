package com.oss.framework.widgets.propertypanel;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PropertiesFilter {

    public static final String PROPERTIES_FILTER_CLASS = "settingsWithAddComponent";
    private static final String FILTER_BTN_PATH =".//i";

    protected final WebDriver driver;
    protected final WebElement webElement;
    private PropertiesFilterPanel propertiesFilterPanel;

    public PropertiesFilter(WebDriver driver) {
        this.driver = driver;
        this.webElement = driver.findElement(By.className(PROPERTIES_FILTER_CLASS));
    }

    public static PropertiesFilter create(WebDriver driver) {
        return new PropertiesFilter(driver);
    }

    public WebElement getFilterIcon() {
        return this.webElement.findElement(By.xpath(FILTER_BTN_PATH));
    }

    public void clickOnFilterIcon() {
        getFilterIcon().click();
        if(propertiesFilterPanel == null){
            propertiesFilterPanel = PropertiesFilterPanel.create(this.driver);
        }
    }

    public List<String> getPropertyChbxLabelsFromPopup() {
        return propertiesFilterPanel.getCheckboxLabels();
    }

    public String getNthPropertyChbxLabelFromPopup(int n) {
        return getPropertyChbxLabelsFromPopup().get(n-1);
    }

    public void clickOnSave(){
        propertiesFilterPanel.clickSave();
        propertiesFilterPanel = null;
    }

    public void typeIntoSearch(String text){
        propertiesFilterPanel.typeIntoSearch(text);
    }

    public boolean isCheckboxChecked(String chbxLabel){
        return propertiesFilterPanel.isCheckboxChecked(chbxLabel);
    }

    public void clickCheckbox(String chbxLabel){
        propertiesFilterPanel.clickCheckbox(chbxLabel);
    }
}

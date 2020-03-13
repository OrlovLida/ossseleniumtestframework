package com.oss.framework.widgets.propertypanel;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PropertiesFilterPanel {

    public static final String PROPERTIES_FILTER_PANEL_CLASS = "filters-settings";
    private static final String SAVE_BTN_PATH =".//a[@class='CommonButton btn btn-primary btn-md']";
    private final String searchInput = ".//input[@class='form-control SearchText']";
    private final String checkboxes = ".//input[@id]";
    private final String checkboxLabels = ".//label[@title]";

    protected final WebDriver driver;
    protected final WebElement webElement;


    public PropertiesFilterPanel(WebDriver driver) {
        this.driver = driver;
        this.webElement = driver.findElement(By.className(PROPERTIES_FILTER_PANEL_CLASS));
    }

    public static PropertiesFilterPanel create(WebDriver driver) {
        return new PropertiesFilterPanel(driver);
    }

    public void clickSave(){
        this.webElement.findElement(By.xpath(SAVE_BTN_PATH)).click();
    }

    public List<WebElement> getCheckboxes() {
        return this.webElement.findElements(By.xpath(checkboxes));
    }

    public List<String> getCheckboxLabels() {
        List<String> labels = new ArrayList<String>();
        for(WebElement element : this.webElement.findElements(By.xpath(checkboxLabels))){
            labels.add(element.getText());
        }
        return labels;
    }

    public WebElement getSearchInput(){
        return this.webElement.findElement(By.xpath(searchInput));
    }

    public void typeIntoSearch(String text){
        getSearchInput().sendKeys(text);
    }

    public void clickCheckbox(String checkboxLabel){
        int index = getCheckboxLabels().indexOf(checkboxLabel);
        getCheckboxes().get(index).click();
    }

    public boolean isCheckboxChecked(String checkboxLabel){
        int index = getCheckboxLabels().indexOf(checkboxLabel);
        Boolean result = false;
        try{
            String value = getCheckboxes().get(index).getAttribute("checked");
            if(value != null){
                result = true;
            }
        } catch (Exception e) {}
        return result;
    }
}

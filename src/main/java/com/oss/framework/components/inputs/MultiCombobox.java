package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.data.Data;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class MultiCombobox extends Input {
    
    private final static String TITLE_ITEM_CONTAINS_XPATH = ".//div[contains(@title,'%s')]";
    private final static String TITLE_ITEM_EQUAL_XPATH = ".//div[@title='%s']";
    private final static String ANGLE_UP_XPATH = ".//i[contains(@class,'angle-up')]";
    private final static String CLEAR_XPATH =
            ".//div[@" + CSSUtils.TEST_ID + "='%s-input']//i[contains(@class,'OSSIcon ossfont-close combo-box__close')]";
    
    static MultiCombobox create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new MultiCombobox(driver, wait, componentId);
    }
    
    static MultiCombobox createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new MultiCombobox(parent, driver, wait, componentId);
    }
    
    private MultiCombobox(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }
    
    private MultiCombobox(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }
    
    @Override
    public void setValue(Data value) {
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).click().build().perform();
        waitForSpinners();
        if (isSearchEnabled()) {
            searchItem(value.getStringValue());
        } else
            chooseItem(String.format(TITLE_ITEM_EQUAL_XPATH, value.getStringValue()));
    }
    
    @Override
    public void setValueContains(Data value) {
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).click().build().perform();
        waitForSpinners();
        if (isSearchEnabled()) {
            searchItem(value.getStringValue());
        } else
            chooseItem(String.format(TITLE_ITEM_CONTAINS_XPATH, value.getStringValue()));
    }
    
    private void acceptStringValue(WebElement input) {
        input.sendKeys(Keys.DOWN);
        input.sendKeys(Keys.ENTER);
        input.sendKeys(Keys.ESCAPE);
    }
    
    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(createDropdownSearchInputPath())).getAttribute("value"));
    }
    
    @Override
    public void clear() {
        webElement.findElement(By.xpath(String.format(CLEAR_XPATH, componentId))).click();
    }
    
    private String createDropdownSearchInputPath() {
        return "//input[@id='" + componentId + "-dropdown-search']";
    }
    
    @Override
    public String getLabel() {
        return webElement.findElement(By.xpath(".//span")).getText();
    }
    
    private boolean isSearchEnabled() {
        return !webElement.findElements(By.xpath(createDropdownSearchInputPath())).isEmpty();
    }
    
    private void searchItem(String value) {
        WebElement input = webElement.findElement(By.xpath(createDropdownSearchInputPath()));
        input.sendKeys(value);
        DelayUtils.sleep(); // TODO: wait for spinners
        acceptStringValue(input);
    }
    
    private void chooseItem(String xpath) {
        Actions actions = new Actions(driver);
        WebElement dropdown = webElement.findElement(By.xpath(createDropdownList()));
        WebElement item = dropdown.findElement(By.xpath(xpath));
        actions.moveToElement(item).click(item).build().perform();
        actions.sendKeys(Keys.ESCAPE).build().perform();
    }
    
    private String createDropdownList() {
        return "//div[@" + CSSUtils.TEST_ID + "='" + componentId + "-dropdown']";
    }
    
    private void waitForSpinners() {
        WebElement openInput = webElement.findElement(By.xpath(ANGLE_UP_XPATH));
        DelayUtils.waitForVisibility(webDriverWait, openInput);
        DelayUtils.sleep();
    }
    
}

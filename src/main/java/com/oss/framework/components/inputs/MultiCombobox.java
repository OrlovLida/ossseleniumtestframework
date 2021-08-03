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
        if (isSearchEnabled()) {
            WebElement input = webElement.findElement(By.xpath(createDropdownSearchInputPath()));
            input.sendKeys(value.getStringValue());
            DelayUtils.sleep(); // TODO: wait for spinners
            acceptStringValue(input);
        }
        WebElement dropdown = webElement.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + componentId + "-dropdown']"));
        WebElement item = dropdown.findElement(By.xpath(".//div[@title='" + value.getStringValue() + "']"));
        actions.moveToElement(item).click(item).build().perform();
    }
    
    @Override
    public void setValueContains(Data value) {
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).click().build().perform();
        if (isSearchEnabled()) {
            WebElement input = webElement.findElement(By.xpath(createDropdownSearchInputPath()));
            input.sendKeys(value.getStringValue());
            DelayUtils.sleep(); // TODO: wait for spinners
            acceptStringValue(input);
        }
        WebElement dropdown = webElement.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + componentId + "-dropdown']"));
        WebElement item = dropdown.findElement(By.xpath(".//div[contains(@title,'" + value.getStringValue() + "')]"));
        actions.moveToElement(item).click(item).build().perform();

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
        webElement.findElement(By.xpath(".//div[@" + CSSUtils.TEST_ID + "='" + componentId
                + "-input']//i[contains(@class,'OSSIcon ossfont-close combo-box__close')]")).click();
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
}

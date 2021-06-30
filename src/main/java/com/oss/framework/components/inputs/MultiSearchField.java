package com.oss.framework.components.inputs;

import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MultiSearchField extends Input {

    private static final String MULTI_SEARCH_PATH = "//input[contains(@class,'form-control search-input')]";

    static MultiSearchField create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new MultiSearchField(driver, wait, componentId);
    }

    static MultiSearchField createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new MultiSearchField(parent, driver, wait, componentId);
    }

    private MultiSearchField(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private MultiSearchField(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    @Override
    public void setValue(Data value) {
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).click().build().perform();
        WebElement input = webElement.findElement(By.xpath(MULTI_SEARCH_PATH));
        input.sendKeys(value.getStringValue());
        DelayUtils.sleep();
        input.sendKeys(Keys.DOWN);
        input.sendKeys(Keys.ENTER);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }

    @Override
    public void setValueContains(Data value) {
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).click().build().perform();
        WebElement input = webElement.findElement(By.xpath(MULTI_SEARCH_PATH));
        input.sendKeys(value.getStringValue());
        input.sendKeys(Keys.DOWN);
        input.sendKeys(Keys.ENTER);
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(MULTI_SEARCH_PATH)).getAttribute("value"));
    }

    @Override
    public void clear() {
        webElement.findElement(By.xpath(MULTI_SEARCH_PATH)).clear();
    }

    @Override
    public String getLabel() {
        return webElement.findElement(By.xpath(".//span")).getText();
    }

    @Override
    public void clickClearValue() {
        webElement.findElement(By.xpath(".//i[contains(@class,'OSSIcon ossfont-close')]")).click();
    }
}
package com.oss.framework.components;

import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.LocatingUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchField extends Input {
    private static final String searchFirstResultXpath = "(//div[@class='ExtendedSearchComponent']//div[contains(@class,'CustomSelectList-data')])[1]";

    static SearchField create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new SearchField(driver, wait, componentId);
    }

    static SearchField createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new SearchField(parent, driver, wait, componentId);
    }

    private SearchField(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private SearchField(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    @Override
    public void setValue(Data value) {
        webElement.click();
        DelayUtils.sleep();//wait for cursor
        webElement.findElement(By.xpath(".//input")).sendKeys(value.getStringValue());
        LocatingUtils.waitUsingXpath(searchFirstResultXpath, webDriverWait);
        webElement.findElement(By.xpath(searchFirstResultXpath)).click();
    }

    @Override
    public void setValueContains(Data value) {

    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(".//input")).getAttribute("value"));
    }

    @Override
    public void clear() {

    }

    @Override
    public String getLabel() {
        return webElement.getText();
    }
}

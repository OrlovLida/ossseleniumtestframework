package com.oss.framework.components.inputs;

import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;

public class MultiSearchField extends Input {

    private static final String MULTI_SEARCH_PATH = "//input[contains(@class,'form-control search-input')]";

    private MultiSearchField(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private MultiSearchField(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    static MultiSearchField create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new MultiSearchField(driver, wait, componentId);
    }

    static MultiSearchField createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new MultiSearchField(parent, driver, wait, componentId);
    }

    @Override
    public MouseCursor cursor() {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).build().perform();
        String cursor = webElement.findElement(By.className("md-input")).getCssValue("cursor");
        return getMouseCursor(cursor);
    }

    @Override
    public void setValueContains(Data value) {
        String selectListPath = "//div[@class='CustomSelectList-data' and (contains(@title, '" + value.getStringValue() + "'))]";

        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).click().build().perform();
        WebElement input = webElement.findElement(By.xpath(MULTI_SEARCH_PATH));
        input.sendKeys(value.getStringValue());
        DelayUtils.waitByXPath(webDriverWait, selectListPath);
        WebElement dropdownElement = webElement.findElement(By.xpath(selectListPath));
        actions.moveToElement(dropdownElement).click(dropdownElement).build().perform();
        input = webElement.findElement(By.xpath(MULTI_SEARCH_PATH));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }

    @Override
    public Data getValue() {
        return Data.createMultiData(webElement.findElements(By.xpath(".//span//span")).stream().map(value -> value.getAttribute("textContent")).collect(Collectors.toList()));

    }

    @Override
    public void setValue(Data value) {
        String selectListPath = "//div[@class='CustomSelectList-data' and @title= '" + value.getStringValue() + "']";

        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).click().build().perform();
        WebElement input = webElement.findElement(By.xpath(MULTI_SEARCH_PATH));
        input.sendKeys(value.getStringValue());
        DelayUtils.waitByXPath(webDriverWait, selectListPath);
        WebElement dropdownElement = webElement.findElement(By.xpath(selectListPath));
        actions.moveToElement(dropdownElement).click(dropdownElement).build().perform();
        input = webElement.findElement(By.xpath(MULTI_SEARCH_PATH));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }

    @Override
    public void clear() {
        webElement.findElement(By.xpath(".//i[contains(@class,'OSSIcon ossfont-close')]")).click();
    }

    @Override
    public String getLabel() {
        return webElement.findElement(By.xpath(".//span")).getText();
    }
}

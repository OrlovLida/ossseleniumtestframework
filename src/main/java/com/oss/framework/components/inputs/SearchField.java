package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.LocatingUtils;

public class SearchField extends Input {

    private String searchFirstResultXpath(String value) {
        return "(//div[@class='ExtendedSearchComponent']//div[contains(@class,'CustomSelectList-data')][./*[contains(text(), '" + value + "')]])[1]";
    }

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

    public void typeValue(String value) {
        webElement.click();
        webElement.findElement(By.xpath("./..//input")).sendKeys(value);
        Actions action = new Actions(driver);
        action.moveToElement(webElement)
                .click()
                .sendKeys(Keys.ENTER)
                .perform();
    }

    public void chooseFirstResult(Data value) {
        LocatingUtils.waitUsingXpath(searchFirstResultXpath(value.getStringValue()), webDriverWait);
        webElement.findElement(By.xpath(searchFirstResultXpath(value.getStringValue()))).click();
    }

    @Override
    public void setValue(Data value) {
        DelayUtils.waitForClickability(webDriverWait, webElement);
        webElement.click();
        DelayUtils.sleep();//wait for cursor
        clear();
        webElement.findElement(By.xpath(".//input")).sendKeys(value.getStringValue());
        chooseFirstResult(value);
    }

    @Override
    public void setValueContains(Data value) {
        webElement.click();
        webElement.findElement(By.xpath(".//input")).sendKeys(value.getStringValue());
        DropdownList dropdownList = DropdownList.create(driver, webDriverWait);
        dropdownList.selectOptionContains(value.getStringValue());
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(".//input")).getAttribute("value"));
    }

    @Override
    public void clear() {
        WebElement input = webElement.findElement(By.xpath(".//input"));
        input.clear();
    }

    @Override
    public String getLabel() {
        return webElement.getText();
    }
}

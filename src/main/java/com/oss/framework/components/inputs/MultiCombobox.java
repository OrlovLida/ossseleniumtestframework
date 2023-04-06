package com.oss.framework.components.inputs;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class MultiCombobox extends Input {

    private static final String TITLE_ITEM_CONTAINS_XPATH = ".//div[contains(@title,'%s')]";
    private static final String TITLE_ITEM_EQUAL_XPATH = ".//div[@title='%s']";
    private static final String CLEAR_XPATH =
            ".//div[@" + CSSUtils.TEST_ID + "='%s-input']//i[contains(@class,'OSSIcon ossfont-close button-close')]";
    private static final String LABEL_CSS = "span.oss-input__input-label";
    private static final String TAGS_CLASS = "tags-input__tag";
    private static final String TAGS_LABEL = "tags-input__label";

    private MultiCombobox(WebDriver driver, WebDriverWait wait, WebElement webElement, String componentId) {
        super(driver, wait, webElement, componentId);
    }

    static MultiCombobox create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new MultiCombobox(driver, wait, webElement, componentId);
    }

    static MultiCombobox createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new MultiCombobox(driver, wait, webElement, componentId);
    }

    @Override
    public MouseCursor cursor() {
        String cursor = webElement.findElement(By.className("oss-input__input")).getCssValue("cursor");
        return getMouseCursor(cursor);
    }

    @Override
    public void setValueContains(Data value) {
        WebElementUtils.clickWithRetry(driver, webElement, By.xpath(createDropdownList()));
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        if (value.isList()) {
            setMultiValue(value, true);
        } else {
            setSingleValue(value.getStringValue(), true);
        }
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ESCAPE).build().perform();
    }

    @Override
    public Data getValue() {
        List<WebElement> dataValues = webElement.findElements(By.className(TAGS_CLASS));
        List<String> values = dataValues.stream().map(value -> value.findElement(By.className(TAGS_LABEL)).getText())
                .collect(Collectors.toList());
        return Data.createMultiData(values);
    }

    @Override
    public void setValue(Data value) {
        WebElementUtils.clickWithRetry(driver, webElement, By.xpath(createDropdownList()));
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        if (value.isList()) {
            setMultiValue(value, false);
        } else {
            setSingleValue(value.getStringValue(), false);
        }
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ESCAPE).build().perform();
    }

    @Override
    public void clear() {
        WebElementUtils.clickWebElement(driver, webElement.findElement(By.xpath(String.format(CLEAR_XPATH, componentId))));
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ESCAPE).build().perform();
    }

    @Override
    public String getLabel() {
        return webElement.findElement(By.cssSelector(LABEL_CSS)).getAttribute("textContent");
    }

    private void acceptStringValue(WebElement input) {
        input.sendKeys(Keys.DOWN);
        input.sendKeys(Keys.ENTER);
    }

    private String createDropdownSearchInputPath() {
        return "//input[@id='" + componentId + "-dropdown-search']";
    }

    private boolean isSearchPresent() {
        return !webElement.findElements(By.xpath(createDropdownSearchInputPath())).isEmpty();
    }

    private void searchItem(String value, boolean isContains) {
        WebElement input = webElement.findElement(By.xpath(createDropdownSearchInputPath()));
        WebElementUtils.moveToElement(driver, input);
        input.clear();
        input.sendKeys(value);
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        if (isContains) {
            acceptStringValue(input);
        } else
            chooseItem(String.format(TITLE_ITEM_EQUAL_XPATH, value));
    }

    private void chooseItem(String xpath) {
        WebElement dropdown = driver.findElement(By.xpath(createDropdownList()));
        WebElement item = dropdown.findElement(By.xpath(xpath));
        item.click();
    }

    private String createDropdownList() {
        return "//div[@" + CSSUtils.TEST_ID + "='" + componentId + "-dropdown']";
    }

    private void setMultiValue(Data value, boolean isContains) {
        value.getStringValues().forEach(stringValue -> setSingleValue(stringValue, isContains));

    }

    private void setSingleValue(String value, boolean isContains) {
        if (isSearchPresent()) {
            searchItem(value, isContains);
        } else {
            if (isContains) {
                DropdownList.create(driver, webDriverWait).selectOptionByDataValueContains(value);
            } else
                DropdownList.create(driver, webDriverWait).selectOptionByDataValue(value);
        }

    }

}

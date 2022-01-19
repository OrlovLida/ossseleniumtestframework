package com.oss.framework.components.inputs;

import java.util.List;
import java.util.stream.Collectors;

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

    private static final String TITLE_ITEM_CONTAINS_XPATH = ".//div[contains(@title,'%s')]";
    private static final String TITLE_ITEM_EQUAL_XPATH = ".//div[@title='%s']";
    private static final String CLEAR_XPATH =
            ".//div[@" + CSSUtils.TEST_ID + "='%s-input']//i[contains(@class,'OSSIcon ossfont-close button-close')]";
    private static final String LABEL_XPATH = ".//span[@class='oss-input__input-label']";
    private static final String TAGS_XPATH = ".//div[@class='tags-input__tag']";
    private static final String TAGS_LABEL = "tags-input__label";

    private MultiCombobox(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private MultiCombobox(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    static MultiCombobox create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new MultiCombobox(driver, wait, componentId);
    }

    static MultiCombobox createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new MultiCombobox(parent, driver, wait, componentId);
    }

    @Override
    public MouseCursor cursor() {
        String cursor = webElement.findElement(By.className("oss-input__input")).getCssValue("cursor");
        return getMouseCursor(cursor);
    }

    @Override
    public void setValueContains(Data value) {
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).click().build().perform();
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        if (isSearchPresent()) {
            searchItem(value.getStringValue(), true);
        } else
            chooseItem(String.format(TITLE_ITEM_CONTAINS_XPATH, value.getStringValue()));
    }

    @Override
    public Data getValue() {
        List<WebElement> dataValues = webElement.findElements(By.xpath(TAGS_XPATH));
        List<String> values = dataValues.stream().map(value -> value.findElement(By.className(TAGS_LABEL)).getText())
                .collect(Collectors.toList());
        return Data.createMultiData(values);
    }

    @Override
    public void setValue(Data value) {
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).click().build().perform();
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        if (isSearchPresent()) {
            searchItem(value.getStringValue(), false);
        } else
            chooseItem(String.format(TITLE_ITEM_EQUAL_XPATH, value.getStringValue()));
    }

    @Override
    public void clear() {
        webElement.findElement(By.xpath(String.format(CLEAR_XPATH, componentId))).click();
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ESCAPE).build().perform();
    }

    @Override
    public String getLabel() {
        return webElement.findElement(By.xpath(LABEL_XPATH)).getAttribute("textContent");
    }

    private void acceptStringValue(WebElement input) {
        input.sendKeys(Keys.DOWN);
        input.sendKeys(Keys.ENTER);
        input.sendKeys(Keys.ESCAPE);
    }

    private String createDropdownSearchInputPath() {
        return "//input[@id='" + componentId + "-dropdown-search']";
    }

    private boolean isSearchPresent() {
        return !webElement.findElements(By.xpath(createDropdownSearchInputPath())).isEmpty();
    }

    private void searchItem(String value, boolean isContains) {
        WebElement input = webElement.findElement(By.xpath(createDropdownSearchInputPath()));
        input.sendKeys(value);
        DelayUtils.waitForSpinners(webDriverWait, webElement);
        if (isContains) {
            acceptStringValue(input);
        } else
            chooseItem(String.format(TITLE_ITEM_EQUAL_XPATH, value));
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

}

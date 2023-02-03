package com.oss.framework.components.inputs;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class SearchField extends Input {

    private static final String INPUT_XPATH = ".//input";
    private static final String MAGNIFIER_CSS = "i[aria-label='SEARCH']";
    private static final String BUTTON_CLOSE = ".ossfont-close";

    private SearchField(WebDriver driver, WebDriverWait wait, WebElement webElement, String componentId) {
        super(driver, wait, webElement, componentId);
    }

    static SearchField create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new SearchField(driver, wait, webElement, componentId);
    }

    static SearchField createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new SearchField(driver, wait, webElement, componentId);
    }

    @Override
    public void setValueContains(Data value) {
        typeValue(value);
        DropdownList.create(driver, webDriverWait).selectOptionContains(value.getStringValue());
    }

    public Set<String> getOptionsContains(Data value) {
        typeValue(value);
        return DropdownList.create(driver, webDriverWait).getOptions();
    }

    public Set<String> getOptions() {
        clickOnMagnifier();
        return DropdownList.create(driver, webDriverWait).getOptions();
    }

    private void clickOnMagnifier() {
        webElement.findElement(By.cssSelector(MAGNIFIER_CSS)).click();
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(INPUT_XPATH)).getAttribute("value"));
    }

    @Override
    public void setValue(Data value) {
        setValue(value, false);
    }

    @Override
    public void clear() {
        WebElementUtils.moveToElement(driver, webElement);
        if (isCloseIconPresent()) {
            webElement.findElement(By.cssSelector(BUTTON_CLOSE)).click();
        }
    }

    private boolean isCloseIconPresent() {
        return !webElement.findElements(By.cssSelector(BUTTON_CLOSE)).isEmpty();
    }

    public void setValueCaseSensitive(Data value) {
        setValue(value, true);
    }

    private void setValue(Data value, boolean isCaseSensitive) {
        typeValue(value);
        if (isCaseSensitive) {
            DropdownList.create(driver, webDriverWait).selectOptionCaseSensitive(value.getStringValue());
        } else {
            DropdownList.create(driver, webDriverWait).selectOption(value.getStringValue());
        }
    }

    private void typeValue(Data value) {
        DelayUtils.waitForClickability(webDriverWait, webElement);
        webElement.click();
        WebElement input = webElement.findElement(By.xpath(INPUT_XPATH));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(value.getStringValue());
        DelayUtils.waitForSpinners(webDriverWait, webElement);
    }
}

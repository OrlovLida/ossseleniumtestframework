package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class PhoneField extends Input {

    private static final String INPUT = ".//input";
    private static final String BUTTON_SELECTED_FLAG_CSS = "button.selected-flag";
    private static final String INPUT_LABEL_FILTER_XPATH = ".//input[@label='Filter']";
    private static final String BY_TEXT_PATTERN = "//div[starts-with(@class, 'country-list')]//*[text()='%s']";

    private PhoneField(WebDriver driver, WebDriverWait wait, WebElement webElement, String componentId) {
        super(driver, wait, webElement, componentId);
    }

    static PhoneField create(WebDriver driver, WebDriverWait wait, String phoneFieldId) {
        WebElement webElement = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(phoneFieldId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new PhoneField(driver, wait, webElement, phoneFieldId);
    }

    static PhoneField createFromParent(WebElement parent, WebDriver driver, WebDriverWait wait, String phoneFieldId) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(phoneFieldId)));
        WebElementUtils.moveToElement(driver, webElement);
        return new PhoneField(driver, wait, webElement, phoneFieldId);
    }

    @Override
    public void setValueContains(Data value) {
        setValue(value);
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(INPUT)).getAttribute("value"));
    }

    @Override
    public void setValue(Data value) {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        input.sendKeys(value.getStringValue());
    }

    @Override
    public void clear() {
        WebElement input = webElement.findElement(By.xpath(INPUT));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }

    public void setDialingCode(String value) {
        expandCountryList();
        typeValue(value);
        selectCountry(value);
    }

    private void expandCountryList() {
        webElement.findElement(By.cssSelector(BUTTON_SELECTED_FLAG_CSS)).click();
    }

    private void typeValue(String value) {
        WebElement input = webElement.findElement(By.xpath(INPUT_LABEL_FILTER_XPATH));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(value);
        DelayUtils.waitForSpinners(webDriverWait, webElement);
    }

    private void selectCountry(String countryLabel) {
        DelayUtils.waitByElement(webDriverWait, driver.findElement(By.xpath(String.format(BY_TEXT_PATTERN, countryLabel))));
        WebElement foundedElement =
                driver.findElement(By.xpath(String.format(BY_TEXT_PATTERN, countryLabel)));
        Actions actions = new Actions(driver);
        actions.click(foundedElement).build().perform();
    }

}

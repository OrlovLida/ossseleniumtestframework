package com.oss.framework.components.inputs;

import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HtmlEditor extends Input {

    // TODO: Use data-testId after resolving OSSSD-2039
    private static final String HTML_EDITOR_COMPONENT_XPATH = "//label[contains(@for, '%s')]/ancestor::div[contains(@class, 'html-editor-component')]";
    private static final String INPUT_XPATH = ".//div[contains(@role, 'textbox')]";

    public static HtmlEditor create(WebDriver webDriver, WebDriverWait webDriverWait, String widgetId) {
        String xPath = String.format(HTML_EDITOR_COMPONENT_XPATH, widgetId);
        DelayUtils.waitByXPath(webDriverWait, xPath);
        WebElement webElement = webDriver.findElement(By.xpath(xPath));
        return new HtmlEditor(webDriver, webDriverWait, webElement);
    }

    public HtmlEditor(WebDriver webDriver, WebDriverWait webDriverWait, WebElement component) {
        super(webDriver, webDriverWait, component);
    }

    @Override
    public void setValue(Data value) {
        WebElement input = webElement.findElement(By.xpath(INPUT_XPATH));
        clear();
        input.sendKeys(value.getStringValue());
    }

    @Override
    public void setValueContains(Data value) {
        setValue(value);
    }

    @Override
    public Data getValue() {
        return Data.createSingleData(webElement.findElement(By.xpath(INPUT_XPATH)).getText());
    }

    @Override
    public void clear() {
        WebElement input = webElement.findElement(By.xpath(INPUT_XPATH));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }
}
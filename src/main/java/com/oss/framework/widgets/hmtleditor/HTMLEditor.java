package com.oss.framework.widgets.hmtleditor;

import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HTMLEditor extends Widget {

    private static final String HTML_EDITOR_COMPONENT_XPATH = "//label[contains(@for, '%s')]/ancestor::div[contains(@class, 'html-editor-component')]";
    private static final String INPUT_XPATH = ".//div[contains(@role, 'textbox')]";

    public static HTMLEditor create(WebDriver webDriver, WebDriverWait webDriverWait, String widgetId) {
        String xPath = String.format(HTML_EDITOR_COMPONENT_XPATH, widgetId);
        DelayUtils.waitByXPath(webDriverWait, xPath);
        WebElement webElement = webDriver.findElement(By.xpath(xPath));
        return new HTMLEditor(webDriver, webDriverWait, widgetId, webElement);
    }

    public HTMLEditor(WebDriver driver, WebDriverWait webDriverWait, String widgetId, WebElement widget) {
        super(driver, webDriverWait, widgetId, widget);
    }

    public void setValue(Data value) {
        WebElement input = webElement.findElement(By.xpath(INPUT_XPATH));
        clear();
        input.sendKeys(value.getStringValue());
    }

    public void clear() {
        WebElement input = webElement.findElement(By.xpath(INPUT_XPATH));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
    }
}

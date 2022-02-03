package com.oss.framework.widgets.propertypanel;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

public class OldPropertyPanel extends Widget implements PropertyPanelInterface {

    private static final String PROPERTY_NAME_PATH = ".//li[@class='row']//div[@class='item-label']//span[text()='%s']";
    private static final String PROPERTY_VALUE_PATH = "./ancestor::li[@class='row']//div[@class='item-value']";
    private static final String SCROLL_INTO_VIEW_SCRIPT = "arguments[0].scrollIntoView(true);";
    private static final String EXPAND_PROPERTIES_BUTTON_XPATH =
            "//div[contains(@class, 'tabWindow')]//a[contains(@class, 'fullScreenButton')]";
    private static final String PROPERTY_NAME_PATTERN =
            "//div[contains(@class, 'OSSTableColumn Col_PropertyName')]/div[contains(@class,'Cell Row_%s')]";
    private static final String PROPERTY_VALUE_PATTERN =
            "//div[contains(@class, 'OSSTableColumn Col_PropertyValue')]/div[contains(@class,'Cell Row_%s')]";

    private OldPropertyPanel(WebDriver driver, WebDriverWait wait, String widgetId) {
        super(driver, wait, widgetId);
    }

    public static OldPropertyPanel createById(WebDriver driver, WebDriverWait wait, String widgetId) {
        Widget.waitForWidgetById(wait, widgetId);
        return new OldPropertyPanel(driver, wait, widgetId);
    }

    @Override
    public String getPropertyValue(String propertyName) {
        WebElement propertyNameWebElement = this.webElement.findElement(By.xpath(String.format(PROPERTY_NAME_PATH, propertyName)));
        ((JavascriptExecutor) driver).executeScript(SCROLL_INTO_VIEW_SCRIPT, propertyNameWebElement);
        WebElement propertyValueElement = propertyNameWebElement.findElement(By.xpath(PROPERTY_VALUE_PATH));
        return propertyValueElement.getText();
    }

    public Map<String, String> getPropertyNamesToValues() {
        int index = 0;
        clickExpandProperties();
        String propertyNamePath = String.format(PROPERTY_NAME_PATTERN, index);
        String propertyValuePath = String.format(PROPERTY_VALUE_PATTERN, index);
        Optional<WebElement> propertyName = driver.findElements(By.xpath(propertyNamePath)).stream().findFirst();
        Optional<WebElement> propertyValue = driver.findElements(By.xpath(propertyValuePath)).stream().findFirst();
        Map<String, String> namesToValues = new HashMap<>();
        while (propertyName.isPresent() && propertyValue.isPresent()) {
            index++;
            String propertyNameText = propertyName.get().getText();
            String propertyValueText = propertyValue.get().getText();
            namesToValues.put(propertyNameText, propertyValueText);
            propertyNamePath = String.format(PROPERTY_NAME_PATTERN, index);
            propertyValuePath = String.format(PROPERTY_VALUE_PATTERN, index);
            propertyName = driver.findElements(By.xpath(propertyNamePath)).stream().findFirst();
            propertyValue = driver.findElements(By.xpath(propertyValuePath)).stream().findFirst();
        }
        clickExpandProperties();
        return namesToValues;
    }

    private void clickExpandProperties() {
        WebElement expandButton = driver.findElement(By.xpath(EXPAND_PROPERTIES_BUTTON_XPATH));
        expandButton.click();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}

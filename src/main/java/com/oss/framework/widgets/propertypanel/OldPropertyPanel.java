package com.oss.framework.widgets.propertypanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;
import com.oss.framework.widgets.Widget;

public class OldPropertyPanel extends Widget implements PropertyPanelInterface {

    private static final String PROPERTY_NAME_PATH = ".//li[@class='row']//div[@class='item-label']//span[text()='%s']";
    private static final String PROPERTY_VALUE_PATH = "./ancestor::li[@class='row']//div[@class='item-value']";
    private static final String PROPERTY_ATTRIBUTES_NAME_CSS = ".item-label";
    private static final String PROPERTY_VALUES_CSS = ".item-value span";
    private static final String PROPERTY_CSS = ".item-label";
    private static final String ROW_CSS = ".row";
    private static final String LINK_CSS = "a[href]";
    private static final String TEXT_CONTENT = "textContent";
    private static final String TAB_CSS = ".defaultCssClass";
    private static final String TAB_IS_NOT_AVAILABLE_EXCEPTION = "Tab is not available";
    private static final String ACTIVE_TAB_XPATH = ".//li[@class='active propertyPanelTab']//p[text()='%s']";
    private static final String EMPTY_VALUE_CODING = "\u00a0";

    private OldPropertyPanel(WebDriver driver, WebDriverWait wait, String widgetId) {
        super(driver, wait, widgetId);
    }

    public static OldPropertyPanel createById(WebDriver driver, WebDriverWait wait, String widgetId) {
        Widget.waitForWidgetById(wait, widgetId);
        return new OldPropertyPanel(driver, wait, widgetId);
    }

    public List<String> getVisibleAttributes() {
        List<String> propertyLabel = new ArrayList<>();
        for (WebElement element : getProperties()) {
            propertyLabel.add(element.getAttribute(TEXT_CONTENT));
        }
        return propertyLabel;
    }

    private WebElement getPropertyElement(String propertyName) {
        DelayUtils.waitForNestedElements(webDriverWait, webElement, By.xpath(String.format(PROPERTY_NAME_PATH, propertyName)));
        WebElement propertyNameWebElement = this.webElement.findElement(By.xpath(String.format(PROPERTY_NAME_PATH, propertyName)));
        WebElementUtils.moveToElement(driver, propertyNameWebElement);
        return propertyNameWebElement.findElement(By.xpath(PROPERTY_VALUE_PATH));
    }

    public void clickLink(String propertyName) {
        getPropertyElement(propertyName).findElement(By.cssSelector(LINK_CSS)).click();
    }

    @Override
    public String getPropertyValue(String propertyName) {
        return getPropertyElement(propertyName).getText();
    }

    public Map<String, String> getPropertyNamesToValues() {
        DelayUtils.waitForNestedElements(webDriverWait, webElement, By.cssSelector(ROW_CSS));
        Map<String, String> properties = new HashMap<>();
        List<String> propertyNames = getPropertyNames();
        propertyNames.forEach(attribute -> properties.put(attribute, getPropertyValues().get(propertyNames.indexOf(attribute))));
        return properties;
    }

    private List<String> getPropertyNames() {
        return getCellValue(PROPERTY_ATTRIBUTES_NAME_CSS);
    }

    private List<String> getPropertyValues() {
        return getCellValue(PROPERTY_VALUES_CSS).stream().map(value -> value.replace(EMPTY_VALUE_CODING, "")).collect(Collectors.toList());
    }

    private List<String> getCellValue(String selector) {
        return webElement.findElements(By.cssSelector(selector)).stream()
                .map(item -> item.getAttribute(TEXT_CONTENT))
                .collect(Collectors.toList());
    }

    public int countRows() {
        return webElement.findElements(By.cssSelector(ROW_CSS)).size();
    }

    private List<WebElement> getProperties() {
        return this.webElement.findElements(By.cssSelector(PROPERTY_CSS));
    }

    public void selectTab(String tabLabel) {
        webElement.findElements(By.cssSelector(TAB_CSS)).stream()
                .filter(tab -> tab.getText().equals(tabLabel)).findFirst().ifPresentOrElse(WebElement::click, () -> {
                    throw new NoSuchElementException(TAB_IS_NOT_AVAILABLE_EXCEPTION);
                });
        DelayUtils.waitByElement(webDriverWait, webElement.findElement(By.xpath(String.format(ACTIVE_TAB_XPATH, tabLabel))));
    }
}
package com.oss.framework.widgets.propertypanel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Maps;
import com.oss.framework.components.attributechooser.AttributesChooser;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.utils.WebElementUtils;
import com.oss.framework.widgets.Widget;

public class PropertyPanel extends Widget implements PropertyPanelInterface {

    public static final String PROPERTY_PANEL_CLASS = "PropertyPanel";
    private static final String PROPERTIES_FILTER_PANEL_CLASS = "actionsGroup-settings";
    private static final String INPUT = ".//input";
    private static final String PROPERTY_CSS = ".propertyPanelRow";
    private static final String PROPERTY_NAME_CSS = ".propertyPanelRow-label";
    private static final String PROPERTY_VALUE_CSS = ".propertyPanelRow-value";
    private static final String PROPERTY_VALUE_PATH = "./ancestor::li[@class='row']//div[@class='item-value']";
    private static final String DRAGGABLE_ELEMENT_CSS = ".btn-drag";
    private static final String ID_ATTRIBUTE = "id";
    private static final String VALUE_ATTRIBUTE = "value";
    private static final String ACTION_SETTINGS_XPATH = ".//a[@" + CSSUtils.TEST_ID + "='chooseAttributes']";
    private static final String ACTIONS_DROPDOWN_CLASS = "actionsDropdown";
    private static final String SEARCH_XPATH = "//ancestor::div[@" + CSSUtils.TEST_ID + "='PropertyPanelWidget-search']";
    private static final String TEXT_CONTENT_ATTRIBUTE = "textContent";

    private PropertyPanel(WebDriver driver, WebDriverWait wait, String id, WebElement propertyPanel) {
        super(driver, wait, id, propertyPanel);
    }

    public static PropertyPanel createById(WebDriver driver, WebDriverWait wait, String testId) {
        Widget.waitForWidget(wait, PROPERTY_PANEL_CLASS);
        WebElement propertyPanel =
                driver.findElement(By.cssSelector("." + PROPERTY_PANEL_CLASS + "[" + CSSUtils.TEST_ID + "='" + testId + "']"));
        return new PropertyPanel(driver, wait, testId, propertyPanel);
    }

    public List<String> getPropertyLabels() {
        List<String> labels = new ArrayList<>();
        for (WebElement element : this.webElement.findElements(By.cssSelector(PROPERTY_NAME_CSS))) {
            WebElementUtils.moveToElement(driver, element);
            labels.add(element.getText());
        }
        return labels;
    }

    public List<String> getVisibleAttributes() {
        List<String> propertyId = new ArrayList<>();
        for (WebElement element : getProperties()) {
            propertyId.add(element.getAttribute(ID_ATTRIBUTE));
        }
        return propertyId;
    }

    public void changePropertyOrder(String id, int position) {
        DragAndDrop.dragAndDrop(getDraggableElement(id), getDropElement(position), 0, -5, driver);
    }

    public void hideEmpty() {
        if (getSwitcher().findElement(By.xpath(INPUT)).getAttribute(VALUE_ATTRIBUTE).equals("false"))
            getSwitcher().click();
    }

    public void showEmpty() {
        if (getSwitcher().findElement(By.xpath(INPUT)).getAttribute(VALUE_ATTRIBUTE).equals("true"))
            getSwitcher().click();
    }

    public void disableAttributeByLabel(String columnLabel, String... path) {
        AttributesChooser attributesChooser = getAttributesChooser();
        attributesChooser.disableAttributeByLabel(columnLabel, path);
        attributesChooser.clickApply();
    }

    public void enableAttributeByLabel(String columnLabel, String... path) {
        AttributesChooser attributesChooser = getAttributesChooser();
        attributesChooser.enableAttributeByLabel(columnLabel, path);
        attributesChooser.clickApply();
    }

    public List<String> getPropertiesToList() {
        Map<String, WebElement> properties = getPropertiesMap();
        return new ArrayList<>(properties.keySet());
    }

    public Map<String, String> getPropertiesValuesToList() {
        Map<String, WebElement> properties = getPropertiesMap();

        return properties.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> getPropertyText(e.getValue())));
    }

    @Override
    public String getPropertyValue(String propertyName) {
        Map<String, WebElement> properties = getPropertiesMap();
        if (!properties.get(propertyName).findElements(By.cssSelector(PROPERTY_VALUE_CSS)).isEmpty()) {
           return properties.get(propertyName).findElement(By.cssSelector(PROPERTY_VALUE_CSS)).getAttribute(TEXT_CONTENT_ATTRIBUTE);
        } else {
            return "";
        }
    }

    private String getPropertyText(WebElement webElement) {
        if (!webElement.findElements(By.xpath(PROPERTY_VALUE_PATH)).isEmpty()) {
            return webElement.findElement(By.xpath(PROPERTY_VALUE_PATH)).getText();
        } else {
            return "";
        }
    }

    public void fullTextSearch(String value) {
        Search search = new Search(driver, webDriverWait, webElement);
        search.fullTextSearch(value);
    }

    public void callAction(String groupId, String actionId) {
        ActionsContainer actionsContainer = ActionsContainer.createFromParent(webElement, this.driver, this.webDriverWait);
        actionsContainer.callActionById(groupId, actionId);
    }

    private DragAndDrop.DraggableElement getDraggableElement(String id) {
        WebElement source = getPropertyById(id).findElement(By.cssSelector(DRAGGABLE_ELEMENT_CSS));
        return new DragAndDrop.DraggableElement(source);
    }

    private DragAndDrop.DropElement getDropElement(int position) {
        WebElement target = this.webElement.findElements(By.cssSelector(PROPERTY_NAME_CSS)).get(position);
        return new DragAndDrop.DropElement(target);
    }

    private AttributesChooser getAttributesChooser() {
        webElement.findElement(By.className(PROPERTIES_FILTER_PANEL_CLASS)).click();
        openActionSettings();
        return AttributesChooser.create(driver, webDriverWait);
    }

    private WebElement getSwitcher() {
        WebElement propertyPanelWrapper = this.webElement.findElement(By.xpath(".//ancestor::div"));
        return propertyPanelWrapper.findElement(By.cssSelector("div.switcher"));
    }

    private void openActionSettings() {
        WebElement actionList = driver.findElement(By.className(ACTIONS_DROPDOWN_CLASS));
        actionList.findElement(By.xpath(ACTION_SETTINGS_XPATH)).click();
    }

    private Map<String, WebElement> getPropertiesMap() {
        Map<String, WebElement> properties = Maps.newHashMap();
        for (WebElement element : getProperties()) {
            properties.put(element.getAttribute(ID_ATTRIBUTE), element);
        }
        return properties;
    }

    private WebElement getPropertyById(String id) {
        return this.webElement.findElement(By.id(id));
    }

    private List<WebElement> getProperties() {
        return this.webElement.findElements(By.cssSelector(PROPERTY_CSS));
    }

    public static class Search {
        final WebDriver driver;
        final WebDriverWait webDriverWait;
        final WebElement webElement;

        private Search(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
            this.driver = driver;
            this.webDriverWait = webDriverWait;
            this.webElement = webElement;
        }

        private WebElement createSearch() {
            WebElement search = webElement.findElement(By.xpath(SEARCH_XPATH));
            return search.findElement(By.xpath(INPUT));
        }

        private void fullTextSearch(String value) {
            createSearch().sendKeys(value);
        }
    }
}

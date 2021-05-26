package com.oss.framework.widgets.propertypanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Maps;
import com.oss.framework.components.common.AttributesChooser;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.portals.ChooseConfigurationWizard;
import com.oss.framework.components.portals.SaveConfigurationWizard;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.framework.widgets.Widget;

public class PropertyPanel extends Widget implements PropertyPanelInterface {
    
    public static final String PROPERTIES_FILTER_CLASS = "settingsWithAddComponent";
    private static final String FILTER_BTN_PATH = ".//i";
    private static final String SWITCHER_XPATH = ".//div[@class='switcher']";
    private static final String KEBAB_XPATH = ".//div[@id='frameworkCustomButtonsGroup']";
    private static final String CHOOSE_CONFIGURATION_XPATH = "//a[@" + CSSUtils.TEST_ID + "='chooseConfiguration']";
    private static final String DOWNLOAD_CONFIGURATION_XPATH = "//a[@" + CSSUtils.TEST_ID + "='propertyPanelDownload']";
    private static final String SAVE_NEW_CONFIGURATION_XPATH = "//a[@" + CSSUtils.TEST_ID + "='propertyPanelSave']";
    
    public static final String PROPERTY_PANEL_CLASS = "PropertyPanel";
    public static final String PROPERTIES_FILTER_PANEL_CLASS = "settings-toggle";
    public static final String SWITCHER_CONTENT_CLASS = "switcher-content";
    
    private static final String PROPERTY_PATH = ".//div[contains(@class, 'propertyPanelRow row')]";
    private static final String PROPERTY_NAME_PATH = ".//div[@class='propertyPanelRow-label']";
    private static final String PROPERTY_VALUE_PATH =
            ".//div[@class='propertyPanelRow-value']";
    private static final String PROPERTY_VALUE__EMPTY_PATH = ".//div[@class='propertyPanelRow-value propertyPanelRow-value-empty']";
    private final Map<String, WebElement> properties = Maps.newHashMap();
    
    @Deprecated
    public static PropertyPanel create(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        Widget.waitForWidget(wait, PROPERTY_PANEL_CLASS);
        return new PropertyPanel(driver, wait);
    }
    
    @Deprecated
    public static PropertyPanel createById(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        Widget.waitForWidget(wait, PROPERTY_PANEL_CLASS);
        return new PropertyPanel(driver, wait, id);
    }
    
    public static PropertyPanel createById(WebDriver driver, WebDriverWait wait, String testId) {
        Widget.waitForWidget(wait, PROPERTY_PANEL_CLASS);
        return new PropertyPanel(driver, wait, testId);
    }
    
    private PropertyPanel(WebDriver driver, WebDriverWait wait) {
        super(driver, PROPERTY_PANEL_CLASS, wait);
    }
    
    private PropertyPanel(WebDriver driver, WebDriverWait wait, String id) {
        super(driver, wait, id);
    }
    
    private List<WebElement> getProperties() {
        return this.webElement.findElements(By.xpath(PROPERTY_PATH));
    }
    
    public List<String> getPropertyLabels() {
        List<String> labels = new ArrayList<String>();
        for (WebElement element: this.webElement.findElements(By.xpath(PROPERTY_NAME_PATH))) {
            labels.add(element.getText());
        }
        return labels;
    }
    @Deprecated
    public String getNthPropertyLabel(int n) {
        List<String> propertyLabels = getPropertyLabels();
        return propertyLabels.get(n - 1);
    }
    public List<String> getPropertyId(){
        List<String> propertyId = new ArrayList<String>();
        for (WebElement element: getProperties()){
             propertyId.add(element.getAttribute("id"));
        }
        return propertyId;
    }
    
    private Map<String, WebElement> getPropertiesMap() {
        for (WebElement element: getProperties()) {
            properties.put(element.getAttribute("id"), element);
        }
        return properties;
    }
    
    private WebElement getPropertyById(String id) {
        return this.webElement.findElement(By.id(id));
    }
    
    public void changePropertyOrder(String id, int position) {
        DragAndDrop.dragAndDrop(getDraggableElement(id), getDropElement(position), 0, -5, driver);
    }
    
    private DragAndDrop.DraggableElement getDraggableElement(String id) {
        WebElement source = getPropertyById(id).findElement(By.xpath(".//div[@class = 'btn-drag']"));
        return new DragAndDrop.DraggableElement(source);
    }
    
    private DragAndDrop.DropElement getDropElement(int position) {
        WebElement target = this.webElement.findElements(By.xpath(PROPERTY_NAME_PATH)).get(position);
        return new DragAndDrop.DropElement(target);
    }
    
    private AttributesChooser getAttributesChooser() {
        WebElement propertyPanelWrapper = this.webElement.findElement(By.xpath(".//ancestor::div"));
        propertyPanelWrapper.findElement(By.className(PROPERTIES_FILTER_PANEL_CLASS)).click();
        return AttributesChooser.create(driver, webDriverWait);
    }
    
    private WebElement getSwitcher() {
        return this.webElement.findElement(By.xpath(SWITCHER_XPATH));
    }
    
    public void hideEmpty() {
        if (getSwitcher().findElement(By.xpath(".//input")).getAttribute("value").equals("false"))
            getSwitcher().click();
    }
    
    public void showEmpty() {
        if (getSwitcher().findElement(By.xpath(".//input")).getAttribute("value").equals("true"))
            getSwitcher().click();
    }
    
    public void disableAttributeByLabel(String columnLabel, String... path) {
        getAttributesChooser()
                .disableAttributeByLabel(columnLabel, path)
                .clickApply();
    }
    
    public void enableAttributeByLabel(String columnLabel, String... path) {
        getAttributesChooser()
                .enableAttributeByLabel(columnLabel, path)
                .clickApply();
    }
    
    @Override
    public String getPropertyValue(String propertyName) {
        getPropertiesMap();
        if (!properties.get(propertyName).findElements(By.xpath(PROPERTY_VALUE_PATH)).isEmpty()) {
            return properties.get(propertyName).findElement(By.xpath(PROPERTY_VALUE_PATH)).getText();
        } else {
            return "";
        }
    }
    
    public void fullTextSearch(String value) {
        Search search = new Search(driver, webDriverWait, webElement);
        search.fullTextSearch(value);
    }
    
    // configuration
    
    public ChooseConfigurationWizard openChooseConfigurationWizard() {
        this.webElement.findElement(By.xpath(KEBAB_XPATH)).click();
        DelayUtils.waitByXPath(this.webDriverWait, CHOOSE_CONFIGURATION_XPATH);
        this.webElement.findElement(By.xpath(CHOOSE_CONFIGURATION_XPATH)).click();
        return ChooseConfigurationWizard.create(driver, this.webDriverWait);
    }
    
    public ChooseConfigurationWizard openDownloadConfigurationWizard() {
        this.webElement.findElement(By.xpath(KEBAB_XPATH)).click();
        DelayUtils.waitByXPath(this.webDriverWait, DOWNLOAD_CONFIGURATION_XPATH);
        this.webElement.findElement(By.xpath(DOWNLOAD_CONFIGURATION_XPATH)).click();
        return ChooseConfigurationWizard.create(driver, this.webDriverWait);
    }
    
    private WebElement getPropertyPanelParent() {
        WebElement propertyPanel = refreshWidgetByID();
        return propertyPanel.findElement(By.xpath("..//div"));
    }
    
    public SaveConfigurationWizard openSaveAsNewConfigurationWizard() {
        WebElement parentElement = getPropertyPanelParent();
        ActionsContainer actionsContainer = ActionsContainer.createFromParent(parentElement, this.driver, this.webDriverWait);
        actionsContainer.callAction(ActionsContainer.KEBAB_GROUP_ID, "propertyPanelSave");
        return SaveConfigurationWizard.create(driver, this.webDriverWait);
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
            WebElement search = webElement.findElement(By.xpath("./../.."));
            return search.findElement(By.xpath(".//input"));
        }
        
        private void fullTextSearch(String value) {
            createSearch().sendKeys(value);
        }
    }
}

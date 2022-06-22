package com.oss.framework.widgets.advancedsearch;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Multimap;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.widgets.Widget;

public class AdvancedSearchWidget extends Widget {
    
    private static final String ADD_BTN_PATH = ".//a[text()='Add']";
    private static final String ADVANCED_SEARCH_WIDGET_CLASS = "common-advancedsearchwidget";
    
    private AdvancedSearchWidget(WebDriver driver, WebDriverWait webDriverWait, String widgetId, WebElement webElement) {
        super(driver, webDriverWait, widgetId, webElement);
        
    }
    
    /**
     * @deprecated (to remove with next release 2.0.x because component now has an id)
     */
    @Deprecated
    public static AdvancedSearchWidget create(WebDriver driver, WebDriverWait wait) {
        waitForWidget(wait, ADVANCED_SEARCH_WIDGET_CLASS);
        WebElement webElement = driver.findElement(By.className(ADVANCED_SEARCH_WIDGET_CLASS));
        return new AdvancedSearchWidget(driver, wait, "advancedSearch", webElement);
    }
    
    public static AdvancedSearchWidget createById(WebDriver driver, WebDriverWait wait, String widgetId) {
        waitForWidgetById(wait, widgetId);
        WebElement webElement = driver.findElement(By.cssSelector(String.format(CSSUtils.WEB_ELEMENT_PATTERN, widgetId)));
        return new AdvancedSearchWidget(driver, wait, widgetId, webElement);
    }
    
    public Input getComponent(String componentId, ComponentType componentType) {
        return getAdvancedSearch().getComponent(componentId, componentType);
    }
    
    public void setFilter(String componentId, String value) {
        getAdvancedSearch().setFilter(componentId, value);
    }
    
    /**
     * @deprecated (to remove with next release 2.0.x, please use method getTableComponent())
     */
    @Deprecated
    public TableComponent getTableComponent(String widgetId) {
        return TableComponent.create(driver, webDriverWait, widgetId);
    }
    
    public TableComponent getTableComponent() {
        return TableComponent.create(driver, webDriverWait, id);
    }
    
    public void clickAdd() {
        this.webElement.findElement(By.xpath(ADD_BTN_PATH)).click();
    }
    
    public Multimap<String, String> getAppliedFilters() {
        return getAdvancedSearch().getAppliedFilters();
        
    }
    
    private AdvancedSearch getAdvancedSearch() {
        return AdvancedSearch.createByClass(driver, webDriverWait, ADVANCED_SEARCH_WIDGET_CLASS);
    }
}

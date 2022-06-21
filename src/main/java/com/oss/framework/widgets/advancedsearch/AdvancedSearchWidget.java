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
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

public class AdvancedSearchWidget extends Widget {
    
    private static final String ADD_BTN_PATH = ".//a[text()='Add']";
    private static final String ADVANCED_SEARCH_WIDGET_CLASS = "common-advancedsearchwidget";
    
    private AdvancedSearchWidget(WebDriver driver, WebDriverWait webDriverWait, String widgetId, WebElement webElement) {
        super(driver, webDriverWait, widgetId, webElement);
        
    }
    
    @Deprecated
    public static AdvancedSearchWidget create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, "//*[contains(@class,'" + ADVANCED_SEARCH_WIDGET_CLASS + "')]");
        WebElement webElement = driver.findElement(By.className(ADVANCED_SEARCH_WIDGET_CLASS));
        return new AdvancedSearchWidget(driver, wait, "advancedSearch", webElement);
    }
    
    public static AdvancedSearchWidget createById(WebDriver driver, WebDriverWait wait, String id) {
        DelayUtils.waitByXPath(wait, "//*[@id='" + id + "']");
        WebElement webElement = driver.findElement(By.xpath("//*[@id='" + id + "']"));
        return new AdvancedSearchWidget(driver, wait, id, webElement);
    }
    
    public Input getComponent(String componentId, ComponentType componentType) {
        return getAdvancedSearch().getComponent(componentId, componentType);
    }
    
    public void setFilter(String componentId, String value) {
        getAdvancedSearch().setFilter(componentId, value);
    }
    
    @Deprecated
    public TableComponent getTableComponent(String widgetId) {
        return TableComponent.create(this.driver, webDriverWait, widgetId);
    }
    
    public TableComponent getTableComponent() {
        return TableComponent.create(this.driver, webDriverWait, id);
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

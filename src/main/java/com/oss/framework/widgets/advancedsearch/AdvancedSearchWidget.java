package com.oss.framework.widgets.advancedsearch;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Multimap;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.WebElementUtils;
import com.oss.framework.widgets.Widget;

public class AdvancedSearchWidget extends Widget {

    private static final String ADD_BTN_ID = "addButton";
    private static final String CANCEL_BTN_ID = "cancelButton";

    private AdvancedSearchWidget(WebDriver driver, WebDriverWait webDriverWait, String widgetId, WebElement webElement) {
        super(driver, webDriverWait, widgetId, webElement);
    }

    public static AdvancedSearchWidget createById(WebDriver driver, WebDriverWait wait, String widgetId) {
        waitForWidgetById(wait, widgetId);
        WebElement webElement = driver.findElement(By.cssSelector(String.format(CSSUtils.WEB_ELEMENT_PATTERN, widgetId)));
        return new AdvancedSearchWidget(driver, wait, widgetId, webElement);
    }

    public Input getComponent(String componentId) {
        return getAdvancedSearch().getComponent(componentId);
    }

    public void setFilter(String componentId, String value) {
        getAdvancedSearch().setFilter(componentId, value);
    }

    public TableComponent getTableComponent() {
        return TableComponent.create(driver, webDriverWait, id);
    }

    public void clickAdd() {
        WebElementUtils.clickWebElement(driver, webElement.findElement(By.cssSelector(String.format(CSSUtils.WEB_ELEMENT_PATTERN, ADD_BTN_ID))));
    }

    public void clickCancel() {
        WebElementUtils.clickWebElement(driver, webElement.findElement(By.cssSelector(String.format(CSSUtils.WEB_ELEMENT_PATTERN, CANCEL_BTN_ID))));
    }

    public Multimap<String, String> getAppliedFilters() {
        return getAdvancedSearch().getAppliedFilters();

    }

    private AdvancedSearch getAdvancedSearch() {
        return AdvancedSearch.createByWidgetId(driver, webDriverWait, id);
    }
}

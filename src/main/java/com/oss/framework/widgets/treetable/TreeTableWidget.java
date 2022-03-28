/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.widgets.treetable;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.widgets.Widget;

/**
 * @author Gabriela Zaranek
 */
public class TreeTableWidget extends Widget {
    
    private static final String TREE_TABLE_WIDGET_CLASS = "common-treetablewidgetgraphql";
    
    private TreeTableWidget(WebDriver driver, WebDriverWait webDriverWait, String widgetId) {
        super(driver, webDriverWait, widgetId);
    }
    
    public static TreeTableWidget createById(WebDriver driver, WebDriverWait wait, String widgetId) {
        Widget.waitForWidget(wait, TREE_TABLE_WIDGET_CLASS);
        Widget.waitForWidgetById(wait, widgetId);
        return new TreeTableWidget(driver, wait, widgetId);
    }
    
    public void expandNode(String value, String columnId) {
        getTable().getRow(value, columnId).expandRow();
    }
    
    public void selectNode(String value, String columnId) {
        getTable().getRow(value, columnId).selectRow();
    }
    
    public void selectNode(int index) {
        getTable().selectRow(index);
    }
    
    public void collapseNode(String value, String columnId) {
        getTable().getRow(value,columnId).collapseRow();
        
    }

    public void collapseNode(int index){
        getTable().getRow(index).collapseRow();
    }
    
    public void callActionById(String groupId, String actionId) {
        ActionsContainer.createFromParent(webElement, driver, webDriverWait).callActionByLabel(groupId, actionId);
    }
    
    public void fullTextSearch(String text) {
        getAdvancedSearch().fullTextSearch(text);
    }
    
    private TableComponent getTable() {
        return TableComponent.create(driver, webDriverWait, id);
    }
    
    private AdvancedSearch getAdvancedSearch() {
        return AdvancedSearch.createByWidgetId(driver, webDriverWait, id);
    }
    
}

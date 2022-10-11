/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.widgets.treetable;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.table.OldTable;

/**
 * @author Comarch
 */
public class OldTreeTableWidget extends Widget {
    
    private OldTreeTableWidget(WebDriver driver, WebDriverWait wait, String id) {
        super(driver, wait, id);
    }
    
    public static OldTreeTableWidget create(WebDriver driver, WebDriverWait wait, String dataAttributeName) {
        Widget.waitForWidgetById(wait, dataAttributeName);
        return new OldTreeTableWidget(driver, wait, dataAttributeName);
    }
    
    public List<String> getAllVisibleNodes(String attributeNameLabel) {
        List<String> visibleNodes = new ArrayList<>();
        int tableObjectsCount = createTable().countRows(attributeNameLabel);
        for (int i = 0; i < tableObjectsCount; i++) {
            String cellValue = createTable().getCellTextValue(i, attributeNameLabel);
            visibleNodes.add(cellValue);
        }
        return visibleNodes;
    }
    
    public void expandNode(String value, String attributeNameLabel) {
        int rowNumber = createTable().getRowNumber(value, attributeNameLabel);
        new Node(driver, webDriverWait, rowNumber).expandNode();
    }
    
    public void selectNode(String value, String attributeNameLabel) {
        int rowNumber = createTable().getRowNumber(value, attributeNameLabel);
        createTable().selectRow(rowNumber);
    }

    public void unselectNode(String value, String attributeNameLabel) {
        createTable().unselectRow(attributeNameLabel, value);
    }

    public void collapseNode(String value, String attributeNameLabel) {
        int rowNumber = createTable().getRowNumber(value, attributeNameLabel);
        new Node(driver, webDriverWait, rowNumber).collapseNode();
    }

    public void collapseNode(int rowNumber) {
        new Node(driver, webDriverWait, rowNumber).collapseNode();
    }

    public void callActionById(String id) {
        createTable().callAction(id);
    }

    public void callActionByLabel(String groupLabel, String actionLabel) {
        createTable().callActionByLabel(groupLabel, actionLabel);
    }

    public String getGroupActionLabel(String groupId) {
        return createTable().getGroupActionLabel(groupId);
    }
    
    public String getActionLabel(String groupId, String actionId) {
        return createTable().getActionLabel(groupId, actionId);
    }
    
    public String getActionLabel(String actionId) {
        return createTable().getActionLabel(actionId);
    }

    public String getCellValue(int index, String attributeLabel) {
        return createTable().getCellValue(index, attributeLabel);
    }

    public int getRowNumber(String value, String attributeLabel) {
        return createTable().getRowNumber(value, attributeLabel);
    }

    public void setPageSize(int pageOption) {
        createTable().setPageSize(pageOption);
    }

    public void callActionById(String groupId, String actionId) {
        createTable().callAction(groupId, actionId);
    }

    public void fullTextSearch(String text) {
        getAdvancedSearch().fullTextSearch(text);
    }

    public void searchByAttribute(String attributeId, Input.ComponentType componentType, String value) {
        AdvancedSearch advancedSearch = getAdvancedSearch();
        advancedSearch.openSearchPanel();
        advancedSearch.setFilter(attributeId, componentType, value);
        advancedSearch.clickApply();
    }

    public void searchByAttribute(String attributeId, String value) {
        AdvancedSearch advancedSearch = getAdvancedSearch();
        advancedSearch.openSearchPanel();
        advancedSearch.setFilter(attributeId, value);
        advancedSearch.clickApply();
    }

    public void clickClearAll() {
        getAdvancedSearch().clickClearAll();
    }

    public boolean hasNoData() {
        return createTable().hasNoData();
    }

    public List<String> getColumnsHeader() {
        return createTable().getColumnsHeaders();
    }

    public void selectPredefinedFilter(String filterName) {
        createTable().selectPredefinedFilter(filterName);
    }

    public List<String> getSelectedPredefinedFilter(){
       return createTable().getSelectedPredefinedFilters();
    }
    
    private OldTable createTable() {
        return OldTable.createById(driver, webDriverWait, id);
    }
    
    private AdvancedSearch getAdvancedSearch() {
        return AdvancedSearch.createByWidgetId(driver, webDriverWait, id);
    }
    
    public static class Node {
        private static final String TREE_NODE_EXPAND_ICON_XPATH = ".//i[contains(@class,'tree-node-expand-icon')]";
        private static final String TREE_NODE_ADD_ICON_XPATH = ".//i[@aria-label='ADD']";
        private static final String TREE_NODE_MINUS_ICON_XPATH = ".//i[@aria-label='MINUS']";
        private final int row;
        private final WebDriver driver;
        private final WebDriverWait wait;
        
        Node(WebDriver driver, WebDriverWait wait, int row) {
            this.driver = driver;
            this.wait = wait;
            this.row = row;
        }
        
        private List<WebElement> getNodeExpandIcons() {
            DelayUtils.waitBy(wait, By.xpath(TREE_NODE_EXPAND_ICON_XPATH));
            return driver.findElements(By.xpath(TREE_NODE_EXPAND_ICON_XPATH));
        }
        
        private void expandNode() {
            WebElement node = getNodeExpandIcons().get(row);
            if (!isExpanded(node)) {
                wait.until(ExpectedConditions.elementToBeClickable(node.findElement(By.xpath(TREE_NODE_ADD_ICON_XPATH))));
                node.findElement(By.xpath(TREE_NODE_ADD_ICON_XPATH)).click();
                DelayUtils.waitForPageToLoad(driver, wait);
            }
        }
        
        private void collapseNode() {
            WebElement node = getNodeExpandIcons().get(row);
            if (isExpanded(node)) {
                wait.until(ExpectedConditions.elementToBeClickable(node.findElement(By.xpath(TREE_NODE_MINUS_ICON_XPATH))));
                node.findElement(By.xpath(TREE_NODE_MINUS_ICON_XPATH)).click();
                DelayUtils.waitForPageToLoad(driver, wait);
            }
        }
        
        private boolean isExpanded(WebElement node) {
            List<WebElement> notExpandedNode = node.findElements(By.xpath(TREE_NODE_ADD_ICON_XPATH));
            return notExpandedNode.isEmpty();
        }
    }
    
}

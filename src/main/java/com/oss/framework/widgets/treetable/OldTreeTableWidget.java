/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.widgets.treetable;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;
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

    public boolean isValuePresent(String attributeNameLabel, String value) {
        return createTable().isValuePresent(attributeNameLabel, value);
    }

    public void expandNode(String value, String attributeNameLabel) {
        int rowNumber = createTable().getRowNumber(value, attributeNameLabel);
        Node.createNode(driver, webDriverWait, rowNumber).expandNode();
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
        Node.createNode(driver, webDriverWait, rowNumber).collapseNode();
    }

    public void collapseNode(int rowNumber) {
        Node node = Node.createNode(driver, webDriverWait, rowNumber);
        node.collapseNode();
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

    public List<String> getSelectedPredefinedFilter() {
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
        private static final String FIRST_COLUMN = ".//div[starts-with(@class,'OSSTableColumn OSSTableTreeColumn') and contains(@class, 'leftSticky')]";
        private static final String CELL_ROW = ".//div[starts-with(@class,'Cell Row')]";
        private static final String CANNOT_FIND_ROW_EXCEPTION = "Cannot find row for given value.";
        private final WebElement nodeElement;
        private final WebDriver driver;
        private final WebDriverWait wait;

        private Node(WebDriver driver, WebDriverWait wait, WebElement nodeElement) {
            this.driver = driver;
            this.wait = wait;
            this.nodeElement = nodeElement;
        }

        static Node createNode(WebDriver driver, WebDriverWait wait, int row) {
            WebElement firstColumn = driver.findElement(By.xpath(FIRST_COLUMN));
            List<WebElement> nodes = firstColumn.findElements(By.xpath(CELL_ROW));
            if (nodes.size() < row) {
                throw new NoSuchElementException(CANNOT_FIND_ROW_EXCEPTION);
            }
            WebElement node = nodes.get(row);
            return new Node(driver, wait, node);
        }

        private void expandNode() {
            if (isExpandIconPresent() && !isExpanded()) {
                wait.until(ExpectedConditions.elementToBeClickable(nodeElement.findElement(By.xpath(TREE_NODE_ADD_ICON_XPATH))));
                nodeElement.findElement(By.xpath(TREE_NODE_ADD_ICON_XPATH)).click();
                DelayUtils.waitForPageToLoad(driver, wait);
            }
        }

        private void collapseNode() {
            if (isExpandIconPresent() && isExpanded()) {
                wait.until(ExpectedConditions.elementToBeClickable(nodeElement.findElement(By.xpath(TREE_NODE_MINUS_ICON_XPATH))));
                nodeElement.findElement(By.xpath(TREE_NODE_MINUS_ICON_XPATH)).click();
                DelayUtils.waitForPageToLoad(driver, wait);
            }
        }

        private boolean isExpandIconPresent() {
            return WebElementUtils.isElementPresent(nodeElement, By.xpath(TREE_NODE_EXPAND_ICON_XPATH));
        }

        private boolean isExpanded() {
            List<WebElement> notExpandedNode = nodeElement.findElements(By.xpath(TREE_NODE_ADD_ICON_XPATH));
            return notExpandedNode.isEmpty();
        }
    }

}

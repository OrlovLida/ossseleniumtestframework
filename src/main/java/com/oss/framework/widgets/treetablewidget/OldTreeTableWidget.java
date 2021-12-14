/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.widgets.treetablewidget;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.tablewidget.OldTable;

/**
 * @author Comarch
 */
public class OldTreeTableWidget extends Widget {
    public static OldTreeTableWidget create(WebDriver driver, WebDriverWait wait, String dataAttributeName) {
        return new OldTreeTableWidget(driver, wait, dataAttributeName);

    }
    private final String id;

    private OldTreeTableWidget(WebDriver driver, WebDriverWait wait, String id) {
      super(driver,wait,id);
       this.id =id;


    }

    private OldTable createTable() {
        return OldTable.createByComponentDataAttributeName(driver, webDriverWait,id );
    }

    public List<String> getAllVisibleNodes(String attributeNameLabel) {
        List<String> visibleNodes = new ArrayList<>();
        int tableObjectsCount = createTable().getNumberOfRowsInTable(attributeNameLabel);
        for (int i = 0; i < tableObjectsCount; i++) {
            String cellValue = createTable().getCellValue(i, attributeNameLabel);
            visibleNodes.add(cellValue);
        }
        return visibleNodes;
    }

    public void expandNode(String value, String attributeNameLabel) {
        int rowNumber = createTable().getRowNumber(value, attributeNameLabel);
        new Node(driver, webDriverWait, rowNumber).expandNode();
    }

    public void selectNode(String value, String attributeNameLabel){
        int rowNumber = createTable().getRowNumber(value, attributeNameLabel);
        createTable().selectRow(rowNumber);
    }
    public void collapseNode(String value, String attributeNameLabel) {
        int rowNumber = createTable().getRowNumber(value, attributeNameLabel);
        new Node(driver, webDriverWait, rowNumber).collapseNode();
    }
    public void callActionById(String id){
        createTable().callAction(id);
    }

    public static class Node {
        private static final String TREE_NODE_EXPAND_ICON_XPATH = ".//i[contains(@class,'tree-node-expand-icon')]";
        private static final String TREE_NODE_ADD_ICON_XPATH = ".//i[@aria-label='ADD']";
        private static final String TREE_NODE_MINUS_ICON_XPATH = ".//i[@aria-label='MINUS']";
        final private int row;
        final private WebDriver driver;
        final private WebDriverWait wait;

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
            if (!isExpanded(node)){
                wait.until(ExpectedConditions.elementToBeClickable(node.findElement(By.xpath(TREE_NODE_ADD_ICON_XPATH))));
                node.findElement(By.xpath(TREE_NODE_ADD_ICON_XPATH)).click();
                DelayUtils.waitForPageToLoad(driver,wait);

            }
        }

        private void collapseNode() {
            WebElement node = getNodeExpandIcons().get(row);
            if (isExpanded(node)) {
                wait.until(ExpectedConditions.elementToBeClickable(node.findElement(By.xpath(TREE_NODE_MINUS_ICON_XPATH))));
                node.findElement(By.xpath(TREE_NODE_MINUS_ICON_XPATH)).click();
                DelayUtils.waitForPageToLoad(driver,wait);
            }
        }

        private boolean isExpanded(WebElement node) {
            List<WebElement> notExpandedNode = node.findElements(By.xpath(TREE_NODE_ADD_ICON_XPATH));
            return notExpandedNode.isEmpty();
        }
    }

}

package com.oss.framework.widgets.tree;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;
import com.oss.framework.widgets.Widget;

public class TreeWidget extends Widget {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeWidget.class);
    private static final String TREE_VIEW_CLASS = "TreeView";
    private static final String SEARCH_XPATH = ".//div[contains(@class, 'ExtendedSearch')]";
    private static final String SEARCH_INPUT_XPATH = SEARCH_XPATH + "//input";
    private static final String TREE_ROW_PATTERN = "//p[contains(@class,'TreeViewLabel')][text()='%s']//ancestor::div[starts-with(@class, 'TreeRow')]";
    private static final String TREE_ROW_CONTAINS_PATTERN = "//p[contains(@class,'TreeViewLabel')][contains(text(), '%s')]//ancestor::div[starts-with(@class, 'TreeRow')]";
    private static final String NO_TREE_ROWS_EXCEPTION = "Can't find any TreeRow.";

    private TreeWidget(WebDriver driver, WebDriverWait webDriverWait, String dataAttributeName) {
        super(driver, webDriverWait, dataAttributeName);
    }

    public static TreeWidget createById(WebDriver driver, WebDriverWait webDriverWait, String id) {
        Widget.waitForWidget(webDriverWait, TREE_VIEW_CLASS);
        return new TreeWidget(driver, webDriverWait, id);
    }

    public void selectFirstTreeRow() {
        getVisibleTreeRow()
                .stream()
                .findFirst().orElseThrow(() -> new NoSuchElementException(NO_TREE_ROWS_EXCEPTION)).click();
    }

    public void selectTreeRow(String text) {
        getVisibleTreeRow()
                .stream()
                .filter(treeRow -> treeRow.getLabel().equals(text))
                .findFirst().orElseThrow(() -> new NoSuchElementException("Can't find TreeRow: " + text)).click();
    }

    public void selectTreeRowContains(String text) {
        getVisibleTreeRow()
                .stream()
                .filter(treeRow -> treeRow.getLabel().contains(text))
                .findFirst().orElseThrow(() -> new NoSuchElementException("Can't find TreeRow that contains: " + text)).click();
    }

    public void selectTreeRowByOrder(Integer order) {
        List<TreeRow> treeRowList = getVisibleTreeRow();
        if (treeRowList.size() > order) {
            treeRowList.get(order).click();
        } else
            throw new NoSuchElementException("Cannot select tree row number " + order + ". There are only " + treeRowList.size() + " tree rows.");
    }

    public void expandLastTreeRow() {
        List<TreeRow> treeRowList = getVisibleTreeRow();
        if (!treeRowList.isEmpty()) {
            treeRowList.get(treeRowList.size() - 1).expandTreeRow();
        } else
            throw new NoSuchElementException(NO_TREE_ROWS_EXCEPTION);
    }

    public void expandTreeRow(String treeRowName) {
        createTreeRow(String.format(TREE_ROW_PATTERN, treeRowName)).expandTreeRow();
    }

    public boolean isExpanderIconPresent(String treeRowName) {
        return createTreeRow(String.format(TREE_ROW_PATTERN, treeRowName)).isExpanderIconPresent();
    }

    public void expandTreeRowContains(String treeRowName) {
        createTreeRow(String.format(TREE_ROW_CONTAINS_PATTERN, treeRowName)).expandTreeRow();
    }

    public boolean isTreeRowExpanded(String treeRowName) {
        TreeRow treeRow = createTreeRow(String.format(TREE_ROW_PATTERN, treeRowName));
        return treeRow.isExpanded();
    }

    public String getGroupActionLabel(String id) {
        ActionsContainer actionsContainer = ActionsContainer.createFromParent(this.webElement, driver, webDriverWait);
        return actionsContainer.getGroupActionLabel(id);
    }

    public void callActionById(String id) {
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(this.webElement, driver, webDriverWait);
        actionsContainer.callActionById(id);
    }

    public void callActionById(String groupId, String id) {
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(this.webElement, driver, webDriverWait);
        actionsContainer.callActionById(groupId, id);
    }

    public void search(String inputText) {
        WebElement input = getSearchBox();
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(inputText);
        input.sendKeys(Keys.ENTER);
    }

    public boolean isRowPresent(String text) {
        return getVisibleTreeRow()
                .stream()
                .anyMatch(treeRow -> treeRow.getLabel().equals(text));
    }

    public boolean isRowPresentContains(String text) {
        return getVisibleTreeRow()
                .stream()
                .anyMatch(treeRow -> treeRow.getLabel().contains(text));
    }

    private List<TreeRow> getVisibleTreeRow() {
        List<TreeRow> treeRowList = this.webElement.findElements(By.className("TreeRow")).stream()
                .map(webElement -> new TreeRow(webElement, driver)).collect(Collectors.toList());
        LOGGER.debug("Visible three rows number {}.", treeRowList.size());
        return treeRowList;
    }

    private WebElement getSearchBox() {
        DelayUtils.waitForNestedElements(webDriverWait, webElement, SEARCH_XPATH);
        return webElement.findElement(By.xpath(SEARCH_INPUT_XPATH));
    }

    private TreeRow createTreeRow(String treeRowXpath) {
        DelayUtils.waitForNestedElements(webDriverWait, webElement, treeRowXpath);
        WebElement treeRowElement = this.webElement.findElement(By.xpath(treeRowXpath));
        return new TreeRow(treeRowElement, driver);
    }

    private static class TreeRow {
        private static final String TREE_ROW_LABEL = ".//p[contains(@class,'TreeViewLabel')]";
        private static final String EXPAND_TREE_ROW = ".//div[@class='tree-view-icon tree-view-close']";
        private static final String EXPAND_ICON = ".//div[contains(@class,'tree-view-icon')]";
        private static final String CLASS = "class";

        private final WebElement webElement;
        private final WebDriver driver;

        private TreeRow(WebElement webElement, WebDriver driver) {
            this.webElement = webElement;
            this.driver = driver;
        }

        private static void clickOnWebElement(WebDriver webDriver, WebElement webElement) {
            WebElementUtils.clickWebElement(webDriver, webElement);
        }

        private void expandTreeRow() {
            if (!isExpanded()) {
                WebElement expandIcon = this.webElement.findElement(By.xpath(EXPAND_TREE_ROW));
                clickOnWebElement(driver, expandIcon);
            }
        }

        private boolean isExpanded() {
            WebElement expandElement = this.webElement.findElement(By.xpath(EXPAND_ICON));
            return expandElement.getAttribute(CLASS).contains("open");
        }

        private boolean isExpanderIconPresent() {
            return WebElementUtils.isElementPresent(webElement, By.xpath(EXPAND_ICON));
        }

        private String getLabel() {
            return this.webElement.findElement(By.xpath(TREE_ROW_LABEL)).getText();
        }

        private void click() {
            clickOnWebElement(driver, webElement);
        }
    }
}

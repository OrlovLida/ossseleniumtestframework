package com.oss.framework.widgets.tree;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

public class TreeWidget extends Widget {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeWidget.class);
    private static final String TREE_VIEW_CLASS = "TreeView";
    private static final String TREE_ROW_PATTERN = "//p[contains(@class,'TreeViewLabel')][text()='%s']//ancestor::div[@class='TreeRow']";
    private static final String TREE_ROW_CONTAINS_PATTERN = "//p[contains(@class,'TreeViewLabel')][contains(text(), '%s')]//ancestor::div[@class='TreeRow']";
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

    public void expandTreeRowContains(String treeRowName) {
        createTreeRow(String.format(TREE_ROW_CONTAINS_PATTERN, treeRowName)).expandTreeRow();
    }

    public boolean isTreeRowExpanded(String treeRowName) {
        TreeRow treeRow = createTreeRow(String.format(TREE_ROW_PATTERN, treeRowName));
        return treeRow.isExpanded();
    }

    public void callActionById(String groupLabel, String id) {
        ActionsInterface actionsContainer = ActionsContainer.createFromParent(this.webElement, driver, webDriverWait);
        actionsContainer.callActionById(groupLabel, id);
    }

    public void search(String inputText) {
        WebElement input = getSearchBox();
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(inputText);
        input.sendKeys(Keys.ENTER);
    }

    private List<TreeRow> getVisibleTreeRow() {
        List<TreeRow> treeRowList = this.webElement.findElements(By.className("TreeRow")).stream()
                .map(webElement -> new TreeRow(webElement, webDriverWait, driver)).collect(Collectors.toList());
        LOGGER.debug("Visible three rows number {}.", treeRowList.size());
        return treeRowList;
    }

    private WebElement getSearchBox() {
        DelayUtils.waitForNestedElements(webDriverWait, webElement, "//div[@class='TreeViewBar']");
        return webElement.findElement(By.xpath("//div[@class='TreeViewBar']//input"));
    }

    private TreeRow createTreeRow(String treeRowXpath) {
        DelayUtils.waitForNestedElements(webDriverWait, webElement, treeRowXpath);
        WebElement treeRowElement = this.webElement.findElement(By.xpath(treeRowXpath));
        return new TreeRow(treeRowElement, webDriverWait, driver);
    }

    private static class TreeRow {
        private static final String TREE_ROW_LABEL = ".//p[contains(@class,'TreeViewLabel')]";
        private static final String EXPAND_TREE_ROW = ".//div[@class='tree-view-icon tree-view-close']";
        private static final String EXPAND_ICON = ".//div[contains(@class,'tree-view-icon')]";
        private static final String CLASS = "class";

        private final WebElement webElement;
        private final WebDriverWait webDriverWait;
        private final WebDriver driver;

        private TreeRow(WebElement webElement, WebDriverWait webDriverWait, WebDriver driver) {
            this.webElement = webElement;
            this.webDriverWait = webDriverWait;
            this.driver = driver;
        }

        private static void clickOnWebElement(WebDriver webDriver, WebDriverWait webDriverWait, WebElement webElement) {
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", webElement);
            Actions actions = new Actions(webDriver);
            actions.moveToElement(webElement).build().perform();
            webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
            actions.click(webElement).build().perform();
        }

        private void expandTreeRow() {
            WebElement expandIcos = this.webElement.findElement(By.xpath(EXPAND_TREE_ROW));
            clickOnWebElement(driver, webDriverWait, expandIcos);
        }

        private boolean isExpanded() {
            WebElement expandElement = this.webElement.findElement(By.xpath(EXPAND_ICON));
            return expandElement.getAttribute(CLASS).contains("open");
        }

        private String getLabel() {
            return this.webElement.findElement(By.xpath(TREE_ROW_LABEL)).getText();
        }

        private void click() {
            clickOnWebElement(driver, webDriverWait, webElement);
        }
    }
}

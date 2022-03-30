package com.oss.framework.iaa.widgets.dpe.treewidget;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;
import com.oss.framework.widgets.Widget;

public class KpiTreeWidget extends Widget {

    private static final Logger log = LoggerFactory.getLogger(KpiTreeWidget.class);

    private static final String KPI_TREE_WIDGET_XPATH = "//div[@" + CSSUtils.TEST_ID + "='%s']//div[@class='TreeTable']";
    private static final String SELECT_NODE = "Selecting node: ";
    private static final String WINDOW_XPATH = ".//ancestor::*[contains(@class, 'card-shadow')]";
    private static final String CARD_SHADOW_XPATH = "//*[contains(@class, 'card-shadow')]";
    private static final String TOOLBAR_INPUT_XPATH = ".//input[@" + CSSUtils.TEST_ID + "='search-toolbar-input']";
    private static final String SEARCH_TOOLBAR_ICON_XPATH = ".//*[@" + CSSUtils.TEST_ID + "='search-toolbar-button']";
    private static final String CLOSE_SEARCH_TOOLBAR_XPATH = ".//*[@" + CSSUtils.TEST_ID + "='search-toolbar-clean-button']";
    private static final String FIRST_SEARCH_RESULT_XPATH = "//*[starts-with(@class, 'resultsPopup')]/ol/li[1]";
    private static final String NODE_NAME_XPATH = "//div[@title ='%s']";
    private static final String NODE_OPTIONS_XPATH = NODE_NAME_XPATH + "//*[@" + CSSUtils.TEST_ID + "='dimension-options-button']";
    private static final String EXPAND_NODE_ICON_XPATH = NODE_NAME_XPATH +
            "//ancestor::div[@class='customComponent']//a[@href='#'and @class='fa expandIcon fa-caret-right']";
    private static final String EXPAND_NODE = "Expanding node: ";

    public KpiTreeWidget(WebDriver driver, WebDriverWait webDriverWait, String widgetId) {
        super(driver, webDriverWait, widgetId);
    }

    public static KpiTreeWidget create(WebDriver driver, WebDriverWait wait, String widgetId) {
        DelayUtils.waitByXPath(wait, String.format(KPI_TREE_WIDGET_XPATH, widgetId));
        return new KpiTreeWidget(driver, wait, widgetId);
    }

    public void selectNodes(List<String> nodesToExpand, List<String> nodesToSelect) {
        expandTree(nodesToExpand);
        selectExpandedObjects(nodesToSelect);
    }

    public void selectExpandedObjects(List<String> objectNames) {
        for (String objectName : objectNames) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            selectNode(objectName);
        }
    }

    public boolean isNodeSelected(String objectName) {
        return getNode(objectName).findElement(By.className("deselectNode")).isDisplayed();
    }

    public void searchInToolbarPanel(String value) {
        clickSearchIcon();
        WebElement input = getToolbar().findElement(By.xpath(TOOLBAR_INPUT_XPATH));
        input.sendKeys(value);
        clickSearchIcon();
        log.debug("Searching for: {}", value);
    }

    public void closeSearchToolbar() {
        WebElement closeButton = getToolbar().findElement(By.xpath(CLOSE_SEARCH_TOOLBAR_XPATH));
        WebElementUtils.clickWebElement(driver, closeButton);
        log.debug("Clicking close search button");
    }

    public void selectFirstSearchResult() {
        WebElement firstResult = getToolbar().findElement(By.xpath(FIRST_SEARCH_RESULT_XPATH));
        WebElementUtils.clickWebElement(driver, firstResult);
        log.debug("Clicking on first result in the list");
    }

    public void clickNodeOptions(String nodeName) {
        getToolbar().findElement(By.xpath(String.format(NODE_OPTIONS_XPATH, nodeName))).click();
        log.debug("Clicking dimension options on node: {}", nodeName);
    }

    private void expandTree(List<String> nodeNames) {
        for (String nodeName : nodeNames) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            expandNode(nodeName);
        }
    }

    private void expandNode(String objectName) {
        WebElement expandButton = findNodeElementByXPath(objectName);
        scrollToNode(expandButton);
        expandButton.click();
        log.debug(EXPAND_NODE + "{}", objectName);
    }

    private WebElement findNodeElementByXPath(String objectName) {
        return this.webElement.findElement(By
                .xpath(String.format(EXPAND_NODE_ICON_XPATH, objectName)));
    }

    private void selectNode(String objectName) {
        WebElement objectNode = getNode(objectName).findElement(By.className("selectNode"));

        scrollToNode(objectNode);
        objectNode.click();
        log.debug(SELECT_NODE + "{}", objectName);

    }

    private WebElement getNode(String objectName) {
        return webElement.findElement(By.xpath(String.format(NODE_NAME_XPATH, objectName)))
                .findElement(By.xpath("following-sibling::*"));
    }

    private void scrollToNode(WebElement node) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", node);
    }

    private WebElement getToolbar() {
        DelayUtils.waitByXPath(webDriverWait, CARD_SHADOW_XPATH);
        WebElement window = webElement.findElement(By.xpath(WINDOW_XPATH));
        return window.findElement(By.className("card-header"));
    }

    private void clickSearchIcon() {
        WebElement searchButton = getToolbar().findElement(By.xpath(SEARCH_TOOLBAR_ICON_XPATH));
        WebElementUtils.clickWebElement(driver, searchButton);
        log.debug("Clicking search button");
    }
}

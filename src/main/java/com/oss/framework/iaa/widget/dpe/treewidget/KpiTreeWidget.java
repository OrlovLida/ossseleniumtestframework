package com.oss.framework.iaa.widget.dpe.treewidget;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

import static com.oss.framework.utils.WidgetUtils.findElementByXpath;

public class KpiTreeWidget extends Widget {

    public static final String SELECT_NODE = "Selecting node: ";
    private static final Logger log = LoggerFactory.getLogger(KpiTreeWidget.class);
    private static final String WINDOW_XPATH = ".//ancestor::*[@class='card-shadow']";
    private static final String CARD_SHADOW_XPATH = "//*[@class='card-shadow']";
    private static final String TOOLBAR_INPUT_ID = "search-toolbar-input";
    private static final String DIMENSION_OPTIONS_BUTTON_ID = "dimension-options-button";
    private static final String EXPAND_NODE = "Expanding node: ";

    @Deprecated
    public KpiTreeWidget(WebDriver driver, WebElement webElement, WebDriverWait webDriverWait) {
        super(driver, webElement, webDriverWait);
    }

    @Deprecated
    public static KpiTreeWidget create(WebDriver driver, WebDriverWait wait, String componentId) {
        String xPath = "//div[@" + CSSUtils.TEST_ID + "='" + componentId + "']//div[@class='card-shadow']//div[@class='windowContent']//div[@class='custom-scrollbars']//div//div//div[@class='appContent pmsqm-dimension']";
        DelayUtils.waitByXPath(wait, xPath);
        WebElement webElement = driver.findElement(By.xpath(xPath));

        return new KpiTreeWidget(driver, webElement, wait);
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
        WebElement input = getToolbar().findElement(By.xpath(".//input[@" + CSSUtils.TEST_ID + "='" + TOOLBAR_INPUT_ID + "']"));
        input.sendKeys(value);
        clickSearchIcon();
        log.debug("Searching for: {}", value);
    }

    public void closeSearchToolbar() {
        WebElement closeButton = getToolbar().findElement(By.xpath(".//*[@" + CSSUtils.TEST_ID + "='search-toolbar-clean-button']"));
        Actions action = new Actions(driver);
        action.moveToElement(closeButton)
                .click(closeButton)
                .build()
                .perform();
        log.debug("Clicking close search button");
    }

    public void selectFirstSearchResult() {
        WebElement firstResult = getToolbar().findElement(By.xpath(".//*[starts-with(@class, 'resultsPopup')]/ol/li[1]"));
        Actions action = new Actions(driver);
        action.moveToElement(firstResult)
                .click(firstResult)
                .build()
                .perform();
        log.debug("Clicking on first result in the list");
    }

    public void clickNodeOptions(String nodeName) {
        String nodeXpath = "div[@title ='" + nodeName + "']//*";
        Button.createByXpath(DIMENSION_OPTIONS_BUTTON_ID, nodeXpath, CSSUtils.TEST_ID, driver).click();
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
        return findElementByXpath(this.webElement, "//*[contains(text(),'" + objectName + "')]/../../../../..//a[@href='#'and @class='fa expandIcon fa-caret-right']");
    }

    private void selectNode(String objectName) {
        WebElement objectNode = getNode(objectName).findElement(By.className("selectNode"));

        scrollToNode(objectNode);
        objectNode.click();
        log.debug(SELECT_NODE + "{}", objectName);

    }

    private WebElement getNode(String objectName) {
        return findElementByXpath(this.webElement, "//div[@title='" + objectName + "']")
                .findElement(By.xpath("following-sibling::*"));
    }

    private void scrollToNode(WebElement node) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", node);
    }

    private WebElement getToolbar() {
        DelayUtils.waitByXPath(webDriverWait, CARD_SHADOW_XPATH);
        WebElement window = webElement.findElement(By.xpath(WINDOW_XPATH));
        return window.findElement(By.className("windowHeader"));
    }

    private void clickSearchIcon() {
        WebElement searchButton = getToolbar().findElement(By.xpath(".//*[@" + CSSUtils.TEST_ID + "='search-toolbar-button']"));
        Actions action = new Actions(driver);

        action.moveToElement(webDriverWait.until(ExpectedConditions.elementToBeClickable(searchButton)))
                .click(searchButton)
                .build()
                .perform();
        log.debug("Clicking search button");
    }
}

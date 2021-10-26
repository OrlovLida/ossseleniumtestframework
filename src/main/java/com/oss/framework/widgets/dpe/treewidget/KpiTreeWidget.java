package com.oss.framework.widgets.dpe.treewidget;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.oss.framework.logging.LoggerMessages.EXPAND_NODE;
import static com.oss.framework.logging.LoggerMessages.SELECT_NODE;
import static com.oss.framework.utils.WidgetUtils.findElementByXpath;

public class KpiTreeWidget extends Widget {

    private static final Logger log = LoggerFactory.getLogger(KpiTreeWidget.class);

    private final String WINDOW_XPATH = ".//ancestor::*[@class='card-shadow']";
    private final String CARD_SHADOW_XPATH = "//*[@class='card-shadow']";
    private final String TOOLBAR_INPUT_ID = "search-toolbar-input";
    private final String DIMENSION_OPTIONS_BUTTON_ID = "dimension-options-button";

    public KpiTreeWidget(WebDriver driver, WebElement webElement, WebDriverWait webDriverWait) {
        super(driver, webElement, webDriverWait);
    }

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
        log.debug(EXPAND_NODE + objectName);
    }

    private WebElement findNodeElementByXPath(String objectName) {
        return findElementByXpath(this.webElement, "//*[contains(text(),'" + objectName + "')]/../../../../..//a[@href='#'and @class='fa expandIcon fa-caret-right']");
    }

    public void selectExpandedObjects(List<String> objectNames) {
        for (String objectName : objectNames) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            selectNode(objectName);
        }
    }

    private void selectNode(String objectName) {
        WebElement objectNode = getNode(objectName).findElement(By.className("selectNode"));

        scrollToNode(objectNode);
        objectNode.click();
        log.debug(SELECT_NODE + objectName);
    }

    private WebElement getNode(String objectName) {
        return findElementByXpath(this.webElement, "//div[@title='" + objectName + "']")
                .findElement(By.xpath("following-sibling::*"));
    }

    public boolean isNodeSelected(String objectName) {
        return getNode(objectName).findElement(By.className("deselectNode")).isDisplayed();
    }

    private void scrollToNode(WebElement node) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", node);
    }

    private WebElement getToolbar() {
        DelayUtils.waitByXPath(webDriverWait, CARD_SHADOW_XPATH);
        WebElement window = webElement.findElement(By.xpath(WINDOW_XPATH));
        return window.findElement(By.className("windowHeader"));
    }

    public void searchInToolbarPanel(String value) {
        clickSearchIcon();
        WebElement input = getToolbar().findElement(By.xpath(".//input[@" + CSSUtils.TEST_ID + "='" + TOOLBAR_INPUT_ID + "']"));
        input.sendKeys(value);
        clickSearchIcon();
        log.debug("Searching for: {}", value);
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
}

package com.oss.framework.widgets.dpe.treewidget;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.oss.framework.logging.LoggerMessages.EXPAND_NODE;
import static com.oss.framework.logging.LoggerMessages.SELECT_NODE;
import static com.oss.framework.utils.WidgetUtils.findElementByXpath;

public class KpiTreeWidget extends Widget {

    private static final Logger log = LoggerFactory.getLogger(KpiTreeWidget.class);

    public KpiTreeWidget(WebDriver driver, WebElement webElement, WebDriverWait webDriverWait) {
        super(driver, webElement, webDriverWait);
    }

    public static KpiTreeWidget create(WebDriver driver, WebDriverWait wait, String componentId){
        String xPath = "//div[@" + CSSUtils.TEST_ID + "='" + componentId + "']//div[@class='card-shadow']//div[@class='windowContent']//div[@class='custom-scrollbars']//div//div//div[@class='appContent pmsqm-dimension']";
        DelayUtils.waitByXPath(wait, xPath);
        WebElement webElement = driver.findElement(By.xpath(xPath));

        return new KpiTreeWidget(driver, webElement, wait);
    }

    public void selectNodes(List<String> nodesToExpand, List<String> nodesToSelect){
        expandTree(nodesToExpand);
        selectExpandedObjects(nodesToSelect);
    }

    private void expandTree(List<String> nodeNames){
        for(String nodeName: nodeNames){
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            expandNode(nodeName);
        }
    }

    private void expandNode(String objectName){
        WebElement expandButton = findNodeElementByXPath(objectName);
        scrollToNode(expandButton);
        expandButton.click();
        log.debug(EXPAND_NODE + objectName);
    }

    private WebElement findNodeElementByXPath(String objectName){
        return findElementByXpath(this.webElement, "//*[contains(text(),'" + objectName + "')]/../../../../..//a[@href='#'and @class='fa expandIcon fa-caret-right']");
    }

    public void selectExpandedObjects(List<String> objectNames){
        for(String objectName: objectNames){
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            selectNode(objectName);
        }
    }

    private void selectNode(String objectName) {
        WebElement objectNode = findElementByXpath(this.webElement, "//div[@title='" + objectName + "']")
                .findElement(By.xpath("following-sibling::*"))
                .findElement(By.className("selectNode"));

        scrollToNode(objectNode);
        objectNode.click();
        log.debug(SELECT_NODE + objectName);
    }

    private void scrollToNode(WebElement node) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", node);
    }
}

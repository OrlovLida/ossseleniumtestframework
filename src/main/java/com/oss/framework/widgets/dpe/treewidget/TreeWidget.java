package com.oss.framework.widgets.dpe.treewidget;

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

public class TreeWidget extends Widget {

    private static final Logger log = LoggerFactory.getLogger(TreeWidget.class);

    public TreeWidget(WebDriver driver, WebElement webElement, WebDriverWait webDriverWait) {
        super(driver, webElement, webDriverWait);
    }

    public static TreeWidget create(WebDriver driver, WebDriverWait wait, String componentId){
        String xPath = "//div[@data-attributename='" + componentId + "']//div[@class='card-shadow']//div[@class='windowContent']//div[@class='custom-scrollbars']//div//div//div[@class='appContent pmsqm-dimension']";
        DelayUtils.waitByXPath(wait, xPath);
        WebElement webElement = driver.findElement(By.xpath(xPath));

        return new TreeWidget(driver, webElement, wait);
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
        log.debug("Expanding node: {}", objectName);
    }

    private WebElement findNodeElementByXPath(String objectName){
        return findElementByXpath("//*[contains(text(),'" + objectName + "')]/../../../../..//a[@href='#'and @class='fa expandIcon fa-caret-right']");
    }

    private void selectExpandedObjects(List<String> objectNames){
        for(String objectName: objectNames){
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            selectNode(objectName);
        }
    }

    private void selectNode(String objectName) {
        WebElement objectNode = findElementByXpath("//div[@title='" + objectName + "']")
                .findElement(By.xpath("following-sibling::*"))
                .findElement(By.className("selectNode"));

        scrollToNode(objectNode);
        objectNode.click();
        log.debug("Selecting node: {}", objectName);
    }

    private WebElement findElementByXpath(String xpath) {
        return this.webElement.findElement(By.xpath(xpath));
    }

    private void scrollToNode(WebElement node) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", node);
    }
}

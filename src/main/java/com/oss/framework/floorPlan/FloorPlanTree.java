package com.oss.framework.floorPlan;

import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class FloorPlanTree {
    private static final String NODE_EXPANDER_PATTERN = "//span[contains(@class,  dynatree-node)]//span[text() = '%s']//..//preceding-sibling::span[@class='dynatree-expander']";
    private static final String NODE_PATTERN = "//span[contains(@class,  dynatree-node)]//span[text() = '%s']";
    private static final String LOCATION_NODE_PATTERN = "//span[text() = '%s']";
    private static final String OPEN_IN_THIS_VIEW_OPTION_PATTERN = "//li[@class = 'context-menu-item']/span[text() = 'Open in this view']";
    private static final String CELL_PATTERN = ".//li//span[text() = '%s']/parent::span/following-sibling::ul/li/span/div[contains(@class, 'treeTableCell')]";
    public static FloorPlanTree createById(WebDriver driver, WebDriverWait webDriverWait, String id) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        WebElement treeComponent = driver.findElement(By.id(id));
        return new FloorPlanTree(driver, webDriverWait, treeComponent);
    }

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final WebElement treeComponent;

    private FloorPlanTree(WebDriver driver, WebDriverWait webDriverWait, WebElement treeComponent) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.treeComponent = treeComponent;
    }

    public void expandNodeByName(String name) {
        String expander = String.format(NODE_EXPANDER_PATTERN, name);
        DelayUtils.waitForPresenceAndVisibility(webDriverWait, By.xpath(expander));
        this.treeComponent.findElement(By.xpath(expander)).click();
    }

    public void selectTreeRowByName(String name) {
        String node = String.format(NODE_PATTERN, name);
        DelayUtils.waitForPresenceAndVisibility(webDriverWait, By.xpath(node));
        this.treeComponent.findElement(By.xpath(node)).click();
    }

    public void openLocationInThisView(String locationName) {
        String locationNode = String.format(LOCATION_NODE_PATTERN, locationName);
        WebElement location = this.treeComponent.findElement(By.xpath(locationNode));
        Actions builder = new Actions(driver);
        Action seriesOfActions = builder
                .moveToElement(location)
                .click()
                .contextClick()
                .build();
        seriesOfActions.perform();
        WebElement element = driver.findElement(By.xpath(OPEN_IN_THIS_VIEW_OPTION_PATTERN));
        element.click();
        DelayUtils.waitForElementDisappear(webDriverWait, driver.findElement(By.className("oss-ico-sync")));
    }

    public String getTreeTableCellValue(String nodeName, int columnNr) {
        String cell = String.format(CELL_PATTERN, nodeName);
        List<WebElement> valueCells = driver.findElements(By.xpath(cell));
        return valueCells.get(columnNr).getText();
    }
}

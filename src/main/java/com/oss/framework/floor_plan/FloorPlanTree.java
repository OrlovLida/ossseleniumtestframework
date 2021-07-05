package com.oss.framework.floor_plan;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

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
        contextClick(location);
        WebElement openInThisViewButton = driver.findElement(By.xpath(OPEN_IN_THIS_VIEW_OPTION_PATTERN));
        contextClick(openInThisViewButton);

    }

    public String getTreeTableCellValue(String nodeName, int columnNr) {
        String cell = String.format(CELL_PATTERN, nodeName);
        List<WebElement> valueCells = driver.findElements(By.xpath(cell));
        return valueCells.get(columnNr).getText();
    }

    private void contextClick(WebElement webElement) {
        Actions builder = new Actions(driver);
        Action seriesOfActions = builder
                .moveToElement(webElement)
                .contextClick()
                .build();
        seriesOfActions.perform();
        waitForPageToLoad();
    }

    private void waitForPageToLoad() {
        DelayUtils.sleep(500);
        List<WebElement> spinners = driver.findElements(By.className("oss-ico-sync"));
        long startTime = System.currentTimeMillis();
        while ((spinners.size() > 0) && ((System.currentTimeMillis() - startTime) < 120000)) {
            webDriverWait.until(ExpectedConditions.invisibilityOfAllElements(spinners));
            DelayUtils.sleep(200);
            spinners = driver.findElements(By.className("oss-ico-sync"));
        }
        if ((System.currentTimeMillis() - startTime) > 120000) {
            System.out.println("Page did not load for a two minutes!");
        }
    }
}

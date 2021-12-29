package com.oss.framework.widgets.gisMap;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.utils.DelayUtils;

public class GisMap implements GisMapInterface {

    private static final String GIS_MAP_XPATH = "//div[@class='OssWindow']";
    private static final String GIS_MAP_SEARCH_XPATH = "//input[@class='form-control mapSearchInput']";
    private static final String CANVAS = "//canvas";

    private WebDriver driver;
    private WebDriverWait wait;
    private WebElement gisMapElement;

    private GisMap(WebDriver driver, WebDriverWait wait, WebElement gisMapElement) {
        this.driver = driver;
        this.wait = wait;
        this.gisMapElement = gisMapElement;
    }

    public static GisMapInterface create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, GIS_MAP_XPATH);
        WebElement gisMap = driver.findElement(By.xpath(GIS_MAP_XPATH));
        return new GisMap(driver, wait, gisMap);
    }

    @Override
    public void callActionByLabel(String actionLabel) {
        ActionsInterface actions = OldActionsContainer.createFromParent(driver, wait, gisMapElement);
        actions.callActionByLabel(actionLabel);
    }

    @Override
    public void searchFirstResult(String value) {
        DelayUtils.waitByXPath(wait, GIS_MAP_SEARCH_XPATH);
        WebElement search = gisMapElement.findElement(By.xpath(GIS_MAP_SEARCH_XPATH));
        search.clear();
        search.sendKeys(value);
        DelayUtils.waitForPageToLoad(driver, wait);
        search.sendKeys(Keys.ARROW_DOWN);
        search.sendKeys(Keys.ENTER);
    }

    @Override
    public void searchResult(String value) {
        DelayUtils.waitByXPath(wait, GIS_MAP_SEARCH_XPATH);
        WebElement search = gisMapElement.findElement(By.xpath(GIS_MAP_SEARCH_XPATH));
        search.clear();
        search.sendKeys(value);
        DelayUtils.waitForPageToLoad(driver, wait);
        search.findElement(By.xpath("//ul[@class='dropdown-menu searchResults']/li[@class='result']/span[text()='" + value + "']")).click();
    }

    @Override
    public void clickOnMapByCoordinates(int x, int y) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement canvas = driver.findElement(By.xpath(CANVAS));
        new Actions(driver).moveToElement(canvas, 0, 0).moveByOffset(canvas.getSize().width / x, canvas.getSize().height / y).click().build().perform();
    }

    @Override
    public void doubleClickOnMapByCoordinates(int x, int y) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement canvas = driver.findElement(By.xpath(CANVAS));
        new Actions(driver).moveToElement(canvas, 0, 0).moveByOffset(canvas.getSize().width / x, canvas.getSize().height / y).doubleClick().build().perform();
    }

    @Override
    public void clickOnMapByCoordinatesWithShift(int x, int y) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement canvas = driver.findElement(By.xpath(CANVAS));
        new Actions(driver).moveToElement(canvas, 0, 0).moveByOffset(canvas.getSize().width / x, canvas.getSize().height / y).keyDown(Keys.SHIFT).click().keyUp(Keys.SHIFT).build().perform();
    }

    @Override
    public void dragAndDropObject(int xSource, int ySource, int xDestination, int yDestination) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement canvas = driver.findElement(By.xpath(CANVAS));
        Actions action = new Actions(driver);
        action
                .moveToElement(canvas, 0, 0)
                .moveByOffset(canvas.getSize().width / xSource, canvas.getSize().height / ySource)
                .clickAndHold()
                .moveToElement(canvas, 0, 0)
                .moveByOffset(canvas.getSize().width / xDestination, canvas.getSize().height / yDestination)
                .release().perform();
    }
}

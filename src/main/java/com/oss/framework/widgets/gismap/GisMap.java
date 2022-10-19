package com.oss.framework.widgets.gismap;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class GisMap implements GisMapInterface {

    private static final String GIS_MAP_CLASS = "simple-card-container";
    private static final String GIS_MAP_SEARCH_XPATH = "//input[@class='form-control mapSearchInput']";
    private static final String CANVAS_XPATH = "//canvas";
    private static final String MAP_CHOOSER_WRAPPER_CLASS = "mapChooserWrapper";
    private static final String CHOOSE_MAP_PATTERN = ".//div[text()='%s']";

    private WebDriver driver;
    private WebDriverWait wait;
    private WebElement gisMapElement;

    private GisMap(WebDriver driver, WebDriverWait wait, WebElement gisMapElement) {
        this.driver = driver;
        this.wait = wait;
        this.gisMapElement = gisMapElement;
    }

    public static GisMapInterface create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitBy(wait, By.className(GIS_MAP_CLASS));
        WebElement gisMap = driver.findElement(By.className(GIS_MAP_CLASS));
        return new GisMap(driver, wait, gisMap);
    }

    @Override
    public boolean isCanvasPresent() {
        return WebElementUtils.isElementPresent(gisMapElement, By.xpath(CANVAS_XPATH));
    }

    @Override
    public void callActionByLabel(String actionLabel) {
        ActionsInterface actions = OldActionsContainer.createFromParent(driver, wait, gisMapElement);
        actions.callActionByLabel(actionLabel);
    }

    @Override
    public void setValueContains(String value) {
        DelayUtils.waitByXPath(wait, GIS_MAP_SEARCH_XPATH);
        WebElement search = gisMapElement.findElement(By.xpath(GIS_MAP_SEARCH_XPATH));
        search.clear();
        search.sendKeys(value);
        DelayUtils.waitForPageToLoad(driver, wait);
        search.sendKeys(Keys.ARROW_DOWN);
        search.sendKeys(Keys.ENTER);
    }

    @Override
    public void setValue(String value) {
        DelayUtils.waitByXPath(wait, GIS_MAP_SEARCH_XPATH);
        WebElement search = gisMapElement.findElement(By.xpath(GIS_MAP_SEARCH_XPATH));
        search.clear();
        search.sendKeys(value);
        DelayUtils.waitForPageToLoad(driver, wait);
        search.findElement(By.xpath("//ul[@class='dropdown-menu searchResults']/li[@class='result']/span[text()='" + value + "']")).click();
    }

    @Override
    public void clickMapByCoordinates(int x, int y) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement canvas = driver.findElement(By.xpath(CANVAS_XPATH));
        new Actions(driver).moveToElement(canvas, 0, 0).moveByOffset(canvas.getSize().width / x, canvas.getSize().height / y).click().build().perform();
    }

    @Override
    public void doubleClickMapByCoordinates(int x, int y) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement canvas = driver.findElement(By.xpath(CANVAS_XPATH));
        new Actions(driver).moveToElement(canvas, 0, 0).moveByOffset(canvas.getSize().width / x, canvas.getSize().height / y).doubleClick().build().perform();
    }

    @Override
    public void clickMapByCoordinatesWithShift(int x, int y) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement canvas = driver.findElement(By.xpath(CANVAS_XPATH));
        new Actions(driver).moveToElement(canvas, 0, 0).moveByOffset(canvas.getSize().width / x, canvas.getSize().height / y).keyDown(Keys.SHIFT).click().keyUp(Keys.SHIFT).build().perform();
    }

    @Override
    public void dragAndDropObject(int xSource, int ySource, int xDestination, int yDestination) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement canvas = driver.findElement(By.xpath(CANVAS_XPATH));
        Actions action = new Actions(driver);
        action
                .moveToElement(canvas, 0, 0)
                .moveByOffset(canvas.getSize().width / xSource, canvas.getSize().height / ySource)
                .clickAndHold()
                .moveToElement(canvas, 0, 0)
                .moveByOffset(canvas.getSize().width / xDestination, canvas.getSize().height / yDestination)
                .release().perform();
    }

    @Override
    public TreeComponent getLayersTree() {
        return TreeComponent.create(driver, wait, gisMapElement);
    }

    @Override
    public String getCanvasObject() {
        WebElement canvas = gisMapElement.findElement(By.xpath(CANVAS_XPATH));
        return ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].toDataURL('image/png').substring(21);", canvas).toString();
    }

    @Override
    public void setMap(String mapLabel) {
        openMapChooser().findElement(By.xpath(String.format(CHOOSE_MAP_PATTERN, mapLabel))).click();
    }

    private WebElement openMapChooser() {
        WebElement mapChooser = gisMapElement.findElement(By.className(MAP_CHOOSER_WRAPPER_CLASS));
        mapChooser.click();
        return mapChooser;
    }
}

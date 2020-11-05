package com.oss.framework.widgets.gisMap;

import com.oss.framework.components.contextactions.ActionsInterface;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GisMap implements GisMapInterface {

    private static final String gisMapXpath2 = "//div[@class='gisMap']";
    private static final String gisMapXpath3 = "//div[@class='OssWindow']";
    private static final String gisMapXpath = "//div[@data-attributename='template-gisview']";
    private static final String gisMapSearchXpath = "//input[@class='form-control mapSearchInput']";

    private WebDriver driver;
    private WebDriverWait wait;
    private WebElement gisMap;

    public static GisMapInterface create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, gisMapXpath3);
        WebElement gisMap = driver.findElement(By.xpath(gisMapXpath3));
        return new GisMap(driver, wait, gisMap);
    }

    private GisMap(WebDriver driver, WebDriverWait wait, WebElement gisMap) {
        this.driver = driver;
        this.wait = wait;
        this.gisMap = gisMap;
    }

    @Override
    public void callActionById(String actionId) {
        ActionsInterface actions = OldActionsContainer.createFromParent(driver, wait, gisMap);
        actions.callActionById(actionId);
    }

    //TODO temporaty due OSSWEB-9476
    @Override
    public void callActionByLabel(String group, String label) {
        DelayUtils.waitBy(wait, By.xpath("//div[text()='" + group + "']/.."));
        driver.findElement(By.xpath("//div[text()='" + group + "']/..")).click();
        DelayUtils.waitBy(wait, By.xpath("//a[@data-attributename='" + label + "']"));
        driver.findElement(By.xpath("//a[@data-attributename='" + label + "']")).click();
    }

    @Override
    public void searchFirstResult(String value) {
        DelayUtils.waitByXPath(wait, gisMapSearchXpath);
        WebElement search = gisMap.findElement(By.xpath(gisMapSearchXpath));
        search.sendKeys(value);
        DelayUtils.waitForPageToLoad(driver, wait);
        search.sendKeys(Keys.ARROW_DOWN);
        search.sendKeys(Keys.ENTER);
    }

    @Override
    public void clickOnMapByCoordinates(int x, int y) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement canvas = driver.findElement(By.xpath("//canvas"));
        new Actions(driver).moveToElement(canvas, 0, 0).moveByOffset(canvas.getSize().width / x, canvas.getSize().height / y).click().build().perform();
    }

    @Override
    public void doubleClickOnMapByCoordinates(int x, int y) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement canvas = driver.findElement(By.xpath("//canvas"));
        new Actions(driver).moveToElement(canvas, 0, 0).moveByOffset(canvas.getSize().width / x, canvas.getSize().height / y).doubleClick().build().perform();
    }

    @Override
    public void clickOnMapByCoordinatesWithShift(int x, int y) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement canvas = driver.findElement(By.xpath("//canvas"));
        new Actions(driver).moveToElement(canvas, 0, 0).moveByOffset(canvas.getSize().width / x, canvas.getSize().height / y).keyDown(Keys.SHIFT).click().keyUp(Keys.SHIFT).build().perform();
    }

    @Override
    public void dragAndDropObject(int xSource, int ySource, int xDestination, int yDestination) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement canvas = driver.findElement(By.xpath("//canvas"));
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

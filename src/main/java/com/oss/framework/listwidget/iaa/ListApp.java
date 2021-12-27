package com.oss.framework.listwidget.iaa;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

@Deprecated
public class ListApp {

    private static final Logger log = LoggerFactory.getLogger(ListApp.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement listApp;

    public static ListApp createFromParent(WebDriver driver, WebDriverWait wait, String windowId) {
        DelayUtils.waitBy(wait, By.xpath(".//div[contains(@" + CSSUtils.TEST_ID + ", '" + windowId + "')]//div[contains(@class, 'appList')]"));
        WebElement listApp = driver.findElement(By.xpath(".//div[contains(@" + CSSUtils.TEST_ID + ", '" + windowId + "')]//div[contains(@class, 'appList')]"));
        return new ListApp(driver, wait, listApp);
    }

    public static ListApp createById(WebDriver driver, WebDriverWait wait, String ListAppId) {
        DelayUtils.waitBy(wait, By.xpath("//div[contains(@" + CSSUtils.TEST_ID + ", '" + ListAppId + "')]"));
        WebElement listApp = driver.findElement(By.xpath("//div[contains(@" + CSSUtils.TEST_ID + ", '" + ListAppId + "')]"));
        return new ListApp(driver, wait, listApp);
    }

    private ListApp(WebDriver driver, WebDriverWait wait, WebElement listApp) {
        this.driver = driver;
        this.wait = wait;
        this.listApp = listApp;
    }

    private List<WebElement> getRows() {
        DelayUtils.waitForNestedElements(wait, listApp, ".//div[contains(@class, 'first last')]");
        return listApp.findElements(By.xpath(".//div[contains(@class, 'first last')]"));
    }

    public List<String> getValue() {
        List<String> values = getRows()
                .stream().map(row -> row.findElement(By.xpath(".//div[contains(@class, 'text-default')]")))
                .map(text -> text.getText()).collect(Collectors.toList());
        log.debug("Getting all values from app list");
        return values;
    }

    public void clickCreateNewNotification(){
        log.debug("Clicking create new notification button");
        listApp.findElement(By.xpath(".//*[contains(@href,'notification')]")).click();
    }
}

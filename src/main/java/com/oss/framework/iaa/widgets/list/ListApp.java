package com.oss.framework.iaa.widgets.list;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ListApp {

    private static final Logger log = LoggerFactory.getLogger(ListApp.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement listAppElement;

    private ListApp(WebDriver driver, WebDriverWait wait, WebElement listAppElement) {
        this.driver = driver;
        this.wait = wait;
        this.listAppElement = listAppElement;
    }

    public static ListApp createFromParent(WebDriver driver, WebDriverWait wait, String windowId) {
        DelayUtils.waitBy(wait, By.xpath(".//div[contains(@" + CSSUtils.TEST_ID + ", '" + windowId + "')]//div[contains(@class, 'appList')]"));
        WebElement listApp = driver.findElement(By.xpath(".//div[contains(@" + CSSUtils.TEST_ID + ", '" + windowId + "')]//div[contains(@class, 'appList')]"));
        return new ListApp(driver, wait, listApp);
    }

    public static ListApp createById(WebDriver driver, WebDriverWait wait, String listAppId) {
        DelayUtils.waitBy(wait, By.xpath("//div[contains(@" + CSSUtils.TEST_ID + ", '" + listAppId + "')]"));
        WebElement listApp = driver.findElement(By.xpath("//div[contains(@" + CSSUtils.TEST_ID + ", '" + listAppId + "')]"));
        return new ListApp(driver, wait, listApp);
    }

    public List<String> getValue() {
        List<String> values = getRows()
                .stream().map(row -> row.findElement(By.xpath(".//div[contains(@class, 'textFieldCont')]")))
                .map(WebElement::getText).collect(Collectors.toList());
        log.debug("Getting all values from app list");
        return values;
    }

    private List<WebElement> getRows() {
        DelayUtils.waitForNestedElements(wait, listAppElement, ".//div[contains(@class, 'first last')]");
        return listAppElement.findElements(By.xpath(".//div[contains(@class, 'first last')]"));
    }
}

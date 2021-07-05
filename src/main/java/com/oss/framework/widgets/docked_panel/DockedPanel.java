package com.oss.framework.widgets.docked_panel;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class DockedPanel implements DockedPanelInterface {

    private static final String DOCKED_PANEL = "//div[@class='menu__item-label' and text()='%s']";
    private WebDriver driver;
    private WebDriverWait wait;
    private WebElement webElement;

    public static DockedPanelInterface create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, "//div[contains(@class, 'dockedPanel')]");
        WebElement dockedPanel = driver.findElement(By.xpath("//div[contains(@class, 'dockedPanel')]"));
        return new DockedPanel(driver, wait, dockedPanel);
    }

    private DockedPanel(WebDriver driver, WebDriverWait wait, WebElement dockedPanel) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = dockedPanel;
    }

    public static DockedPanelInterface createDockedPanelByPosition(WebDriver driver, WebDriverWait wait, String position) {
        DelayUtils.waitByXPath(wait, String.format(DOCKED_PANEL, position));
        WebElement dockedPanel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(DOCKED_PANEL, position))));
        return new DockedPanel(driver, wait, dockedPanel);
    }

    @Override
    public void expandDockedPanel(String position) {
        if (!isElementPresent(driver, By.xpath("//div[contains(@class, 'dockedPanel')][contains(@class, '" + position + "')][contains(@class, 'expanded')]"))) {
            webElement.findElement(By.xpath(".//button[contains(@class,'splitterButton')]")).click();
        }
        wait.until(ExpectedConditions
                .attributeContains(webElement, "class", "expanded"));
    }

    @Override
    public void hideDockedPanel(String position) {
        if (isElementPresent(driver, By.xpath("//div[contains(@class, 'dockedPanel')][contains(@class, '" + position + "')][contains(@class, 'expanded')]"))) {
            webElement.findElement(By.xpath(".//button[contains(@class,'splitterButton')]")).click();
        }
    }

    private static boolean isElementPresent(WebDriver driver, By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}

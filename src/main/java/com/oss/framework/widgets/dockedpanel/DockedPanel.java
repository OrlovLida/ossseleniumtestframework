package com.oss.framework.widgets.dockedpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class DockedPanel implements DockedPanelInterface {

    private static final String DOCKED_PANEL = "//div[contains(@class, 'dockedPanel')]";
    private static final String DOCKED_PANEL_BY_POSITION = DOCKED_PANEL + "[(@" + CSSUtils.TEST_ID + "='dockedPanel-%s')]";
    private static final String SPLITTER_BUTTON = ".//button[contains(@class,'splitterButton')]";
    private static final String EXPANDED_XPATH = "[contains(@class, 'expanded')]";
    private WebDriver driver;
    private WebDriverWait wait;
    private WebElement webElement;

    private DockedPanel(WebDriver driver, WebDriverWait wait, WebElement dockedPanel) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = dockedPanel;
    }

    public static DockedPanelInterface create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, DOCKED_PANEL);
        WebElement dockedPanel = driver.findElement(By.xpath(DOCKED_PANEL));
        return new DockedPanel(driver, wait, dockedPanel);
    }

    public static DockedPanelInterface createDockedPanelByPosition(WebDriver driver, WebDriverWait wait, String position) {
        DelayUtils.waitByXPath(wait, String.format(DOCKED_PANEL_BY_POSITION, position));
        WebElement dockedPanel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(DOCKED_PANEL_BY_POSITION, position))));
        return new DockedPanel(driver, wait, dockedPanel);
    }

    private static boolean isElementPresent(WebDriver driver, By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public void expandDockedPanel(String position) {
        if (!isElementPresent(driver, By.xpath(String.format(DOCKED_PANEL_BY_POSITION, position) + EXPANDED_XPATH))) {
            webElement.findElement(By.xpath(SPLITTER_BUTTON)).click();
        }
        wait.until(ExpectedConditions
                .attributeContains(webElement, "class", "expanded"));
    }

    @Override
    public void hideDockedPanel(String position) {
        if (isElementPresent(driver, By.xpath(String.format(DOCKED_PANEL_BY_POSITION, position) + EXPANDED_XPATH))) {
            webElement.findElement(By.xpath(SPLITTER_BUTTON)).click();
        }
    }
}

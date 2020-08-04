package com.oss.framework.widgets.dockedPanel;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class DockedPanel implements DockedPanelInterface {
    private WebDriver driver;
    private WebDriverWait wait;
    private WebElement dockedPanel;

    public static DockedPanelInterface create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, "//div[contains(@class, 'dockedPanel')]");
        WebElement dockedPanel = driver.findElement(By.xpath("//div[contains(@class, 'dockedPanel')]"));
        return new DockedPanel(driver, wait, dockedPanel);
    }

    private DockedPanel(WebDriver driver, WebDriverWait wait, WebElement dockedPanel) {
        this.driver = driver;
        this.wait = wait;
        this.dockedPanel = dockedPanel;
    }

    public static DockedPanelInterface createDockedPanelByPosition(WebDriver driver, WebDriverWait wait, String position) {
        DelayUtils.waitByXPath(wait, "//div[contains(@class, 'dockedPanel')][contains(@class, '" + position + "')]");
        WebElement dockedPanel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'dockedPanel')][contains(@class, '" + position + "')]")));
        return new DockedPanel(driver, wait, dockedPanel);
    }

    @Override
    public void expandDockedPanel(String position) {
        if (!isElementPresent(driver, By.xpath("//div[contains(@class, 'dockedPanel')][contains(@class, '" + position + "')][contains(@class, 'expanded')]"))) {
            dockedPanel.findElement(By.xpath(".//button[contains(@class,'splitterButton')]")).click();
        }
        wait.until(ExpectedConditions
                .attributeContains(dockedPanel, "class", "expanded"));
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

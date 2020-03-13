package com.oss.framework.components.portals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PopupV2 {

    private static final String POPUP_CSS_SELECTOR = ".popupContainer";

    private final String title = ".//span[@class='popupTitle']";

    protected final WebDriver driver;
    protected final WebElement webElement;

    public PopupV2(WebDriver driver) {
        this.driver = driver;
        this.webElement = driver.findElement(By.className("popupContainer"));
    }

    public static PopupV2 create(WebDriver driver) {
        return new PopupV2(driver);
    }

    public static void waitForPopupBookmarks(WebDriverWait wait) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(POPUP_CSS_SELECTOR)));
    }
    public String getPopupTitle(){
        return this.webElement.findElement(By.xpath(title)).getText();
    }

}

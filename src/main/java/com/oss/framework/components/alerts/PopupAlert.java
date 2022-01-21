package com.oss.framework.components.alerts;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PopupAlert {
    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final Alert alert;

    private PopupAlert(WebDriver driver, WebDriverWait webDriverWait, Alert alert) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.alert = alert;
    }

    public static PopupAlert create(WebDriver driver, WebDriverWait webDriverWait) {
        Alert alert = webDriverWait.until(ExpectedConditions.alertIsPresent());
        return new PopupAlert(driver, webDriverWait, alert);
    }

    public void popupAccept() {
        alert.accept();
    }
}

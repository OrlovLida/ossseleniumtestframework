package com.oss.framework.alerts;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PopupAlert {
    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final Alert alert;

    public static PopupAlert create(WebDriver driver, WebDriverWait webDriverWait){
        Alert alert = webDriverWait.until(ExpectedConditions.alertIsPresent());
        return new PopupAlert(driver, webDriverWait, alert);
    }

    private PopupAlert(WebDriver driver, WebDriverWait webDriverWait, Alert alert){
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.alert = alert;
    }

    public void popupAccept(){
        alert.accept();
    }
}

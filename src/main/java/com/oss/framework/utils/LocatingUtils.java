package com.oss.framework.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LocatingUtils {
    public static void waitUsingXpath(String xpath, WebDriverWait wait) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    public static void waitUsingBy(By location, WebDriverWait wait) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(location));
    }

    public static void clickUsingXpath(String xpath, WebDriver driver) {
        driver.findElement(By.xpath(xpath)).click();
    }

    public static void clickUsingBy(By location, WebDriver driver) {
        driver.findElement(location).click();
    }
}

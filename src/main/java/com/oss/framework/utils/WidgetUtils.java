package com.oss.framework.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WidgetUtils {

    public static WebElement findOldWidget(WebDriver driver, WebDriverWait wait, String windowTitle, String className) {
        DelayUtils.waitForNestedElements(wait, "//div[contains(text(), '"+ windowTitle +"')]", "//div[contains(@class, '"+className+"')]");
        WebElement title = driver.findElement(By.xpath("//div[contains(text(), '"+ windowTitle +"')]"));
        return title.findElement(By.xpath("//div[contains(@class, '"+className+"')]"));
    }
}

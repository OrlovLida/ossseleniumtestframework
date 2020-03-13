package com.oss.framework.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DelayUtils {
    public static int HUMAN_REACTION_MS = 250;

    public static void sleep() {
        sleep(1000);
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            //Do nothing
        }
    }

    public static void waitBy(WebDriverWait wait, By by) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static void waitByXPath(WebDriverWait wait, String xPath) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
    }
}

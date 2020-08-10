package com.oss.framework.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class DragAndDrop {

    public static void dragAndDrop(String xpathToSource, String xpathToTarget, WebDriver driver) {
        WebElement source = driver.findElement(By.xpath(xpathToSource));
        WebElement target = driver.findElement(By.xpath(xpathToTarget));
        Actions action = new Actions(driver);
        action.moveToElement(source).clickAndHold(source).pause(100).moveToElement(target).pause(100).release().perform();
    }

    public static void dragAndDrop(String xpathToSource, String xpathToTarget, int pauseInMs, WebDriver driver) {
        WebElement source = driver.findElement(By.xpath(xpathToSource));
        WebElement target = driver.findElement(By.xpath(xpathToTarget));
        Actions action = new Actions(driver);
        action.moveToElement(source).clickAndHold(source).pause(pauseInMs).moveToElement(target).pause(pauseInMs).release().perform();
    }

    public static void dragAndDrop(WebElement source, WebElement target, WebDriver driver) {
        Actions action = new Actions(driver);
        action.moveToElement(source).clickAndHold(source).pause(100).moveToElement(target).pause(100).release().perform();
    }

    public static void dragAndDrop(WebElement source, WebElement target, int pauseInMs, WebDriver driver) {
        Actions action = new Actions(driver);
        action.moveToElement(source).clickAndHold(source).pause(pauseInMs).moveToElement(target).pause(pauseInMs).release().perform();
    }
}

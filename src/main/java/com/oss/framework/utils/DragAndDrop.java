package com.oss.framework.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class DragAndDrop {

    private DragAndDrop() {
        throw new IllegalStateException("Utility class");
    }

    public static void dragAndDrop(String xpathToSource, String xpathToTarget, WebDriver driver) {
        WebElement source = driver.findElement(By.xpath(xpathToSource));
        WebElement target = driver.findElement(By.xpath(xpathToTarget));
        Actions action = new Actions(driver);
        action.moveToElement(source).pause(100).clickAndHold(source).pause(100).moveByOffset(10, 10).moveToElement(target)
                .moveByOffset(1, 0).pause(100).release().perform();
    }

    public static void dragAndDrop(String xpathToSource, String xpathToTarget, int pauseInMs, WebDriver driver) {
        WebElement source = driver.findElement(By.xpath(xpathToSource));
        WebElement target = driver.findElement(By.xpath(xpathToTarget));
        Actions action = new Actions(driver);
        action.moveToElement(source).pause(pauseInMs).clickAndHold(source).pause(pauseInMs).moveByOffset(10, 10).moveToElement(target)
                .moveByOffset(1, 0).pause(pauseInMs).release().perform();
    }

    public static void dragAndDrop(DraggableElement source, DropElement target, WebDriver driver) {
        Actions action = new Actions(driver);
        action.moveToElement(source.getWebElement()).pause(100).clickAndHold(source.getWebElement()).pause(100).moveByOffset(10, 10)
                .moveToElement(target.getWebElement())
                .moveByOffset(1, 0).pause(100).release().perform();
    }

    public static void dragAndDrop(DraggableElement source, DropElement target, int xOffset, int yOffset, WebDriver driver) {
        Actions action = new Actions(driver);
        action.moveToElement(source.getWebElement()).pause(100).clickAndHold(source.getWebElement()).pause(100).moveByOffset(10, 10)
                .moveToElement(target.getWebElement())
                .moveByOffset(xOffset, yOffset).pause(100).release().perform();
    }

    public static void dragAndDrop(WebElement source, WebElement target, int pauseInMs, WebDriver driver) {
        Actions action = new Actions(driver);
        action.moveToElement(source).pause(pauseInMs).clickAndHold(source).pause(pauseInMs).moveByOffset(10, 10).moveToElement(target)
                .moveByOffset(1, 0).pause(pauseInMs).release().perform();
    }

    public static class DraggableElement {
        private final WebElement webElement;

        public DraggableElement(WebElement webElement) {
            this.webElement = webElement;
        }

        private WebElement getWebElement() {
            return webElement;
        }
    }

    public static class DropElement {
        private final WebElement webElement;

        public DropElement(WebElement webElement) {
            this.webElement = webElement;
        }

        private WebElement getWebElement() {
            return webElement;
        }
    }

}

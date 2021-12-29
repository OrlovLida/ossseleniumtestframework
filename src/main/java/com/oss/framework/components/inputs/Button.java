package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.oss.framework.utils.CSSUtils;

public class Button {

    private final WebElement webElement;
    private final WebDriver webDriver;

    private Button(WebDriver driver, String text) {
        this.webDriver = driver;
        this.webElement = driver.findElement(By.xpath("//button[text()='" + text + "']"));
    }

    private Button(WebDriver driver, String text, String selector) {
        this.webDriver = driver;
        this.webElement = driver.findElement(By.xpath("//" + selector + "[text()='" + text + "']"));
    }

    private Button(String componentId, WebDriver driver) {
        this.webDriver = driver;
        this.webElement = driver.findElement(By.xpath("//button[@" + CSSUtils.TEST_ID + "='" + componentId + "']"));
    }

    private Button(String selector, String componentId, WebDriver driver) {
        this.webDriver = driver;
        this.webElement = driver.findElement(By.xpath("//" + selector + "[@" + CSSUtils.TEST_ID + "='" + componentId + "']"));
    }

    private Button(String iconClass, WebDriver driver, String buttonClass) {
        this.webDriver = driver;
        this.webElement = driver.findElement(By.xpath("//button[contains (@class,'" + buttonClass + "')]/i[contains(@class,'" + iconClass + "')]"));
    }

    private Button(String componentId, String webElement, String selector, WebDriver driver) {
        this.webDriver = driver;
        this.webElement = driver.findElement(By.xpath("//" + webElement + "[@" + selector + "='" + componentId + "']"));
    }

    public static Button create(WebDriver driver, String text) {
        return new Button(driver, text);
    }

    public static Button create(WebDriver driver, String text, String selector) {
        return new Button(driver, text, selector);
    }

    public static Button createById(WebDriver driver, String componentId) {
        return new Button(componentId, driver);
    }

    public static Button createBySelectorAndId(WebDriver driver, String selector, String componentId) {
        return new Button(selector, componentId, driver);
    }

    public static Button createByIcon(WebDriver driver, String iconClass, String buttonClass) {
        return new Button(iconClass, driver, buttonClass);
    }

    public static Button createByXpath(String componentId, String webElement, String selector, WebDriver driver) {
        return new Button(componentId, webElement, selector, driver);
    }

    public void click() {
        Actions action = new Actions(webDriver);
        action.moveToElement(this.webElement).click(this.webElement).build().perform();
    }
}

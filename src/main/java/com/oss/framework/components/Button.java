package com.oss.framework.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Button {

    private final WebElement webElement;

    public static Button create(WebDriver driver, String componentId) {
        return new Button(driver, componentId);
    }

    public static Button create(WebDriver driver, String componentId, String selector) {
        return new Button(driver, componentId, selector);
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

    private Button(WebDriver driver, String componentId) {
        this.webElement = driver.findElement(By.xpath("//button[text()='" + componentId + "']"));
    }

    private Button(WebDriver driver, String componentId, String selector) {
        this.webElement = driver.findElement(By.xpath("//" + selector + "[text()='" + componentId + "']"));
    }

    private Button(String componentId, WebDriver driver) {
        this.webElement = driver.findElement(By.xpath("//button[@data-attributename='" + componentId + "']"));
    }

    private Button(String selector, String componentId, WebDriver driver){
        this.webElement = driver.findElement(By.xpath("//" + selector + "[@data-attributename='" + componentId + "']"));
    }

    private Button(String iconClass, WebDriver driver, String buttonClass){
        this.webElement = driver.findElement(By.xpath("//button[contains (@class,'" + buttonClass + "')]/i[contains(@class,'" + iconClass + "')]"));
    }

    public void click() {
        this.webElement.click();
    }
}

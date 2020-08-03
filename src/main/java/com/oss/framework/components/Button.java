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

    public static Button createByID(WebDriver driver, String componentId) {
        return new Button(componentId, driver);
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
    public void click() {
        this.webElement.click();
    }
}

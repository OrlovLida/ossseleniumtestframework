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

    private Button(WebDriver driver, String componentId) {
        this.webElement = driver.findElement(By.xpath("//button[text()='" + componentId + "']"));
    }

    private Button(WebDriver driver, String componentId, String selector) {
        this.webElement = driver.findElement(By.xpath("//" + selector + "[text()='" + componentId + "']"));
    }

    public void click() {
        this.webElement.click();
    }
}

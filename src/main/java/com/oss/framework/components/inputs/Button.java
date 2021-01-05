package com.oss.framework.components.inputs;

import com.oss.framework.utils.CSSUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Button {

    private final WebElement webElement;

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

    private Button(WebDriver driver, String text) {
        this.webElement = driver.findElement(By.xpath("//button[text()='" + text + "']"));
    }

    private Button(WebDriver driver, String text, String selector) {
        this.webElement = driver.findElement(By.xpath("//" + selector + "[text()='" + text + "']"));
    }

    private Button(String componentId, WebDriver driver) {
        this.webElement = driver.findElement(By.xpath("//button[@"+ CSSUtils.TEST_ID +"='" + componentId + "']"));
    }

    private Button(String selector, String componentId, WebDriver driver) {
        this.webElement = driver.findElement(By.xpath("//" + selector + "[@"+ CSSUtils.TEST_ID +"='" + componentId + "']"));
    }

    private Button(String iconClass, WebDriver driver, String buttonClass) {
        this.webElement = driver.findElement(By.xpath("//button[contains (@class,'" + buttonClass + "')]/i[contains(@class,'" + iconClass + "')]"));
    }

    public void click() {
        this.webElement.click();
    }
}

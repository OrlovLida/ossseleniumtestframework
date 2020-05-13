package com.oss.framework.widgets;

import com.google.common.collect.Maps;
import com.oss.framework.components.*;
import com.oss.framework.utils.DelayUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Map;

public class Wizard {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    private final Map<String, Input> components = Maps.newHashMap();

    public static Wizard createWizard(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, "//div[contains(@class,'OssWindow')]");
        return new Wizard(driver, wait);
    }

    private Wizard(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = driver.findElement(By.xpath("//div[contains(@class,'OssWindow')]"));
    }

    public Input getComponent(String componentId, Input.ComponentType componentType) {
        if (components.containsKey(componentId)) {
            return components.get(componentId);
        }

        Input input = ComponentFactory.create(componentId, componentType, this.driver, this.wait);
        components.put(componentId, input);
        return input;
    }

    public void clickNext() {
        Actions action = new Actions(driver);
        WebElement foundedElement =
                webElement.findElement(By.xpath(".//button[text()='Accept']"));
        action.moveToElement(foundedElement).click().perform();
    }

    public void clickPrevious() {

    }

    public void submit() {
        Actions action = new Actions(driver);
        WebElement foundedElement =
                webElement.findElement(By.xpath(".//button[text()='Submit']"));
        action.moveToElement(foundedElement).click().perform();
    }

    public void cancel() {
        Actions action = new Actions(driver);
        WebElement foundedElement =
                webElement.findElement(By.xpath(".//button[text()='Cancel']"));
        action.moveToElement(foundedElement).click().perform();
    }

    public void proceed() {
        Actions action = new Actions(driver);
        WebElement foundedElement =
                webElement.findElement(By.xpath(".//a[text()='Proceed']"));
        action.moveToElement(foundedElement).click().perform();
    }
}

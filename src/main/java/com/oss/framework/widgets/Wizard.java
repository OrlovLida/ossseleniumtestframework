package com.oss.framework.widgets;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Maps;
import com.oss.framework.components.ComponentFactory;
import com.oss.framework.components.Input;
import com.oss.framework.utils.DelayUtils;

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
                webElement.findElement(By.xpath("//button[text()='Next']"));
        wait.until(ExpectedConditions.elementToBeClickable(foundedElement));
        action.moveToElement(foundedElement).click().perform();
    }

    public void clickAccept() {
        DelayUtils.waitForNestedElements(wait, webElement, "//button[text()='Accept']");
        WebElement foundedElement =
                webElement.findElement(By.xpath("//button[text()='Accept']"));
        wait.until(ExpectedConditions.elementToBeClickable(foundedElement));
        foundedElement.click();
        //wait.until(ExpectedConditions.invisibilityOf(foundedElement));
    }

    public void waitToClose() {
        wait.until(ExpectedConditions.invisibilityOf(this.webElement));
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

    public void clickCreate() {
        Actions action = new Actions(driver);
        WebElement foundedElement =
                webElement.findElement(By.xpath(".//a[text()='Create']"));
        action.moveToElement(foundedElement).click().perform();
    }

    public void clickSave() {
        Actions action = new Actions(driver);
        WebElement foundedElement =
                webElement.findElement(By.xpath(".//a[text()='Save']"));
        action.moveToElement(foundedElement).click().perform();
    }

    public void clickDelete() {
        Actions action = new Actions(driver);
        WebElement foundedElement =
                webElement.findElement(By.xpath(".//button[text()='Delete']"));
        action.moveToElement(foundedElement).click().perform();
    }
}

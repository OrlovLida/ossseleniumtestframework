package com.oss.framework.widgets;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.ComponentFactory;
import com.oss.framework.components.Input;
import com.oss.framework.utils.DelayUtils;

public class Wizard {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    public static Wizard createWizard(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, "//div[contains(@class,'OssWindow')]");
        WebElement webElement = driver.findElement(By.xpath("//div[contains(@class,'OssWindow')]"));
        return new Wizard(driver, wait, webElement);
    }

    public static Wizard createByComponentId(WebDriver driver, WebDriverWait wait, String componentId) {
        DelayUtils.waitByXPath(wait, "//div[@data-attributename='" + componentId + "']");
        WebElement webElement = driver.findElement(By.xpath("//div[@data-attributename='" + componentId + "']"));
        return new Wizard(driver, wait, webElement);
    }

    private Wizard(WebDriver driver, WebDriverWait wait, WebElement webElement) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = webElement;
    }

    public Input getComponent(String componentId, Input.ComponentType componentType) {
        Input input = ComponentFactory.create(componentId, componentType, this.driver, this.wait);
        return input;
    }

    public void clickNext() {
        DelayUtils.waitForNestedElements(wait, webElement, "//button[text()='Next']");
        Actions action = new Actions(driver);
        action.moveToElement(wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath("//button[text()='Next']"))))).click().perform();
    }

    public void clickNextStep() {
        Actions action = new Actions(driver);
        WebElement foundedElement =
                webElement.findElement(By.xpath("//button[text()='Next Step']"));
        wait.until(ExpectedConditions.elementToBeClickable(foundedElement));
        action.moveToElement(foundedElement).click().perform();
    }

    public void clickAccept() {
        DelayUtils.waitForNestedElements(wait, webElement, "//button[text()='Accept']");
        wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath("//button[text()='Accept']")))).click();
    }

    public void clickAcceptOldWizard() {
        DelayUtils.waitByXPath(wait, "//div[contains(@class,'OssWindow')]");
        WebElement wizardElement = driver.findElement(By.xpath("//div[contains(@class,'OssWindow')]"));
        DelayUtils.waitForNestedElements(wait, wizardElement, "//button[text()='Accept']");
        wait.until(ExpectedConditions.elementToBeClickable(wizardElement.findElement(By.xpath("//button[text()='Accept']")))).click();
        wait.until(ExpectedConditions.invisibilityOf(wizardElement.findElement(By.xpath("//button[text()='Accept']"))));
    }

    public void waitToClose() {
        wait.until(ExpectedConditions.invisibilityOf(this.webElement));
    }

    public void clickPrevious() {

    }

    public void submit() {
        DelayUtils.waitForNestedElements(wait, webElement, "//button[text()='Submit']");
        Actions action = new Actions(driver);
        action.moveToElement(webElement.findElement(By.xpath(".//button[text()='Submit']"))).click().perform();
    }

    public void cancel() {
        DelayUtils.waitForNestedElements(wait, webElement, "//button[text()='Cancel']");
        Actions action = new Actions(driver);
        action.moveToElement(webElement.findElement(By.xpath(".//button[text()='Cancel']"))).click().perform();
    }

    public void proceed() {
        DelayUtils.waitForNestedElements(wait, webElement, "//a[text()='Proceed']");
        Actions action = new Actions(driver);
        action.moveToElement(webElement.findElement(By.xpath(".//a[text()='Proceed']"))).click().perform();
    }

    public void clickCreate() {
        DelayUtils.waitForNestedElements(wait, webElement, "//a[text()='Create']");
        Actions action = new Actions(driver);
        action.moveToElement(wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath(".//a[text()='Create']"))))).click().perform();
    }

    public void clickSave() {
        DelayUtils.waitForNestedElements(wait, webElement, "//a[text()='Save']");
        Actions action = new Actions(driver);
        action.moveToElement(wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath(".//a[text()='Save']"))))).click().perform();
    }

    public void clickUpdate() {
        DelayUtils.waitForNestedElements(wait, webElement, "//a[text()='Update']");
        Actions action = new Actions(driver);
        WebElement foundedElement =
                wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath(".//a[text()='Update']"))));
        action.moveToElement(foundedElement).click().perform();
        wait.until(ExpectedConditions.invisibilityOf(foundedElement));
    }

    public void clickOK() {
        DelayUtils.waitForNestedElements(wait, webElement, "//a[text()='OK']");
        Actions action = new Actions(driver);
        WebElement foundedElement =
                wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath("//a[text()='OK']"))));
        action.moveToElement(foundedElement).click().perform();
        wait.until(ExpectedConditions.invisibilityOf(foundedElement));
    }

    public void clickChange() {
        DelayUtils.waitForNestedElements(wait, webElement, "//a[text()='Change']");
        Actions action = new Actions(driver);
        WebElement foundedElement =
                wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath("//a[text()='Change']"))));
        action.moveToElement(foundedElement).click().perform();
        wait.until(ExpectedConditions.invisibilityOf(foundedElement));
    }

    public void clickDelete() {
        DelayUtils.waitForNestedElements(wait, webElement, "//a[text()='Delete']");
        Actions action = new Actions(driver);
        action.moveToElement(wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath("//a[text()='Delete']"))))).click().perform();
    }

    public void clickActionById(String actionId){
        DelayUtils.waitForNestedElements(wait,webElement,"//*[@data-attributename='"+actionId+"']");
        Actions action = new Actions(driver);
        action.moveToElement(wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath("//*[@data-attributename='"+actionId+"']"))))).click().perform();
    }
}

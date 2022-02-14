package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.oss.framework.utils.CSSUtils;

public class Button {

    private final WebElement webElement;
    private final WebDriver webDriver;

    private Button(WebDriver driver, WebElement webElement) {
        this.webDriver = driver;
        this.webElement = webElement;
    }

    public static Button createByLabel(WebDriver driver, String label) {
        WebElement button = driver.findElement(By.xpath(".//*[text()='" + label + "']"));
        return new Button(driver, button);
    }

    public static Button createByLabel(WebDriver driver, String componentId, String label) {
        WebElement component = driver.findElement(By.cssSelector("#" + componentId + ",[" + CSSUtils.TEST_ID + "='" + componentId + "']"));
        WebElement button = component.findElement(By.xpath(".//*[@title='" + label + "'] |  .//*[text()='" + label + "']"));
        return new Button(driver, button);

    }

    public static Button createById(WebDriver driver, String buttonId) {
        WebElement button = driver.findElement(
                By.cssSelector("[" + CSSUtils.TEST_ID + "='" + buttonId + "'],[" + CSSUtils.DATA_WIDGET_ID + "='" + buttonId + "']"));
        return new Button(driver, button);
    }

    public void click() {
        Actions action = new Actions(webDriver);
        action.moveToElement(this.webElement).click(this.webElement).build().perform();
    }
}

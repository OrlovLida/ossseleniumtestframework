package com.oss.framework.components.inputs;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.WebElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Button {

    private final WebElement webElement;
    private final WebDriver webDriver;

    private Button(WebDriver driver, WebElement webElement) {
        this.webDriver = driver;
        this.webElement = webElement;
    }

    public static Button createByLabel(WebDriver driver, String label) {
        WebElement button = driver.findElement(By.xpath(".//*[@title='" + label + "'] |  .//*[text()='" + label + "']"));
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

    public static Button createById(WebDriver driver, String componentClass, String buttonId) {
        WebElement button = driver.findElement(
                By.cssSelector("." + componentClass + " [" + CSSUtils.TEST_ID + "='" + buttonId + "']"));
        return new Button(driver, button);
    }

    public void click() {
        WebElementUtils.clickWebElement(webDriver, webElement);
    }

    public void clickWithRetry(String elementToWaitId) {
        WebElementUtils.clickWithRetry(webDriver, webElement, By.cssSelector(CSSUtils.getElementCssSelector(elementToWaitId)));
    }
}

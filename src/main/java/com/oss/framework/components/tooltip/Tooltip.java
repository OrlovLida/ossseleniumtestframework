package com.oss.framework.components.tooltip;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class Tooltip {
    private final WebDriver driver;
    private final WebElement webElement;
    private final WebDriverWait wait;

    private Tooltip(WebDriver driver, WebDriverWait wait, WebElement webElement) {
        this.driver = driver;
        this.webElement = webElement;
        this.wait = wait;
    }

    public static Tooltip create(WebDriver driver, WebDriverWait wait, String componentId) {
        WebElement component = driver.findElement(By.xpath("//div[@" + CSSUtils.TEST_ID + "='" + componentId + "']"));
        DelayUtils.waitBy(wait, By.xpath("//div[@class='form-hint']"));
        WebElement toolTip = component.findElement(By.className("form-hint"));
        return new Tooltip(driver, wait, toolTip);
    }

    public List<String> getMessages() {
        if (isMore()) {
            WebElement more = webElement.findElement(By.className("form-hint-tooltip-button"));
            more.click();
            List<WebElement> message = driver.findElements(By.xpath("//div[contains(@class,'MuiTooltip-tooltip')]"));
            return message.stream().map(WebElement::getText).collect(Collectors.toList());
        } else {
            List<WebElement> messages =
                    webElement.findElements(By.xpath(".//span[contains(@class,'form-hint-tooltip')]"));
            return messages.stream().map(WebElement::getText).collect(Collectors.toList());
        }
    }

    private boolean isMore() {
        return !webElement.findElements(By.className("form-hint-tooltip-button")).isEmpty();
    }

}

package com.oss.framework.components.icons.interactiveicons;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class InteractiveIconFactory {
    private InteractiveIconFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static InteractiveIcon create(WebDriver driver, WebElement parent) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(50));
        DelayUtils.waitForNestedElements(webDriverWait, parent, By.cssSelector(".OSSIcon"));
        String iconName = parent.findElement(By.cssSelector(".OSSIcon")).getAttribute("data-icon-name");

        if (iconName.contains("FAVOURITE")) {
            return Star.create(driver, parent);
        }

        throw new NoSuchElementException("Not supported icon:" + iconName);
    }
}

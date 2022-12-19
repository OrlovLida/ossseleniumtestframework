package com.oss.framework.components.icons.interactiveicons;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class InteractiveIconFactory {

    private static final String OSS_ICON_CSS = ".OSSIcon";
    private static final String DATA_ICON_NAME = "data-icon-name";
    private static final String FAVOURITE = "FAVOURITE";
    private static final String NOT_SUPPORTED_ICON_EXCEPTION = "Not supported icon:";

    private InteractiveIconFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static InteractiveIcon<?> create(WebDriver driver, WebElement parent) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(50));
        DelayUtils.waitForNestedElements(webDriverWait, parent, By.cssSelector(OSS_ICON_CSS));
        String iconName = parent.findElement(By.cssSelector(OSS_ICON_CSS)).getAttribute(DATA_ICON_NAME);

        if (iconName.contains(FAVOURITE)) {
            return Star.create(driver, webDriverWait, parent);
        }
        throw new NoSuchElementException(NOT_SUPPORTED_ICON_EXCEPTION + iconName);
    }
}

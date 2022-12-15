package com.oss.framework.components.icons.interactiveicons;

import java.time.Duration;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Star extends InteractiveIcon {

    private Star(WebDriver driver, WebDriverWait wait, WebElement parent, WebElement icon) {
        super(driver, wait, parent, icon);
    }

    public static Star create(WebDriver driver, WebElement parent) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(50));
        WebElement icon = parent.findElement(By.cssSelector("i.OSSIcon"));
        return new Star(driver, webDriverWait, parent, icon);
    }

    @Override
    public void setValue(IconValue value) {
        if (!(value instanceof IconStatus)) throw new NoSuchElementException ("Provided value doesn't match for Star Icon");
        if (value.equals(IconStatus.MARK)) {
            mark();
        }
        if (value.equals(IconStatus.UNMARK)) {
            unmark();
        }

    }

    @Override
    public IconValue getValue() {
        String iconName = getIcon().getAttribute("data-icon-name");
        if (iconName.contains("FAVOURITE_MARKED")) {
            return IconStatus.MARK;
        }
        if (iconName.contains("FAVOURITE_UNMARKED")) {
            return IconStatus.UNMARK;
        }
        if (iconName.contains("FAVOURITE_UNDETERMINED")) {
            return IconStatus.UNDETERMINED;
        }
        throw new NoSuchElementException("Cannot find status for this icon");

    }

    private void mark() {
        if (!isMarked()) {
            getIcon().click();
        }
    }

    private void unmark() {
        if (isMarked()) {
            getIcon().click();
        }
    }

    public enum IconStatus implements IconValue {
        MARK,
        UNMARK,
        UNDETERMINED
    }

    private boolean isMarked() {
        return getIcon().getAttribute("data-icon-name").equals("commonIcon-FAVOURITE_MARKED");
    }

    private WebElement getIcon() {
        return parent.findElement(By.cssSelector("i.OSSIcon"));

    }
}

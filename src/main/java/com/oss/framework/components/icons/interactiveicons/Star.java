package com.oss.framework.components.icons.interactiveicons;

import java.time.Duration;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Star extends InteractiveIcon<Star.StarStatus> {

    private Star(WebDriver driver, WebDriverWait wait, WebElement parent) {
        super(driver, wait, parent);
    }

    public static Star create(WebDriver driver, WebElement parent) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(50));
        return new Star(driver, webDriverWait, parent);
    }

    @Override
    public void setValue(StarStatus value) {
        if (value.equals(StarStatus.MARK)) {
            mark();
        }
        if (value.equals(StarStatus.UNMARK)) {
            unmark();
        }

    }

    @Override
    public StarStatus getValue() {
        String iconName = getIcon().getAttribute("data-icon-name");
        if (iconName.contains("FAVOURITE_MARKED")) {
            return StarStatus.MARK;
        }
        if (iconName.contains("FAVOURITE_UNMARKED")) {
            return StarStatus.UNMARK;
        }
        if (iconName.contains("FAVOURITE_UNDETERMINED")) {
            return StarStatus.UNDETERMINED;
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

    public enum StarStatus {
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

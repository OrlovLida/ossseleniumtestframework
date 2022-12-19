package com.oss.framework.components.icons.interactiveicons;

import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class Star extends InteractiveIcon<Star.StarStatus> {

    private static final String FAVOURITE_MARKED = "FAVOURITE_MARKED";
    private static final String FAVOURITE_UNMARKED = "FAVOURITE_UNMARKED";
    private static final String FAVOURITE_UNDETERMINED = "FAVOURITE_UNDETERMINED";
    private static final String CANNOT_FIND_STATUS_FOR_THIS_ICON_EXCEPTION = "Cannot find status for this icon";
    private static final String DATA_ICON_NAME = "data-icon-name";

    private Star(WebDriver driver, WebDriverWait wait, WebElement parent) {
        super(driver, wait, parent);
    }

    public static Star create(WebDriver driver, WebDriverWait wait, WebElement parent) {
        return new Star(driver, wait, parent);
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
        String iconName = getIcon().getAttribute(DATA_ICON_NAME);
        if (iconName.contains(FAVOURITE_MARKED)) {
            return StarStatus.MARK;
        }
        if (iconName.contains(FAVOURITE_UNMARKED)) {
            return StarStatus.UNMARK;
        }
        if (iconName.contains(FAVOURITE_UNDETERMINED)) {
            return StarStatus.UNDETERMINED;
        }
        throw new NoSuchElementException(CANNOT_FIND_STATUS_FOR_THIS_ICON_EXCEPTION);

    }

    private void mark() {
        if (!isMarked()) {
            getIcon().click();
            DelayUtils.waitForNestedElements(wait, parent, By.cssSelector("[aria-label='FAVOURITE']"));
        }
    }

    private void unmark() {
        if (isMarked()) {
            getIcon().click();
            DelayUtils.waitForNestedElements(wait, parent, By.cssSelector("[aria-label='STAR']"));
        }
    }

    public enum StarStatus {
        MARK,
        UNMARK,
        UNDETERMINED
    }

    private boolean isMarked() {
        return getIcon().getAttribute(DATA_ICON_NAME).equals("commonIcon-FAVOURITE_MARKED");
    }

    private WebElement getIcon() {
        return parent.findElement(By.cssSelector("i.OSSIcon"));

    }
}

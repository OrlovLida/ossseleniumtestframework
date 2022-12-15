package com.oss.framework.components.icons.interactiveicons;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class InteractiveIcon {
    protected final WebDriver driver;
    protected final WebElement parent;
    protected final WebDriverWait wait;
    protected final WebElement iconElement;

    InteractiveIcon(WebDriver driver, WebDriverWait wait, WebElement parent, WebElement iconElement) {
        this.driver = driver;
        this.wait = wait;
        this.parent = parent;
        this.iconElement = iconElement;
    }

    public abstract void setValue(IconValue value);

    public abstract IconValue getValue();

}

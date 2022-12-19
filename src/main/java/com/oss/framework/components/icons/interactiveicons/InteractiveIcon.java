package com.oss.framework.components.icons.interactiveicons;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class InteractiveIcon<T> {
    protected final WebDriver driver;
    protected final WebElement parent;
    protected final WebDriverWait wait;

    InteractiveIcon(WebDriver driver, WebDriverWait wait, WebElement parent) {
        this.driver = driver;
        this.wait = wait;
        this.parent = parent;
    }

    public abstract void setValue(T value);

    public abstract T getValue();
}

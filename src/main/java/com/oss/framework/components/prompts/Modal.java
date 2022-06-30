package com.oss.framework.components.prompts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class Modal {

    private static final String MODAL_CLASS = "header-modal";
    private static final String MODAL_TITLE_CLASS = "modal-header-title";
    private static final String CLOSE_BUTTON_CSS = "[title='Close']";
    private static final String MINIMIZE_BUTTON_CSS = "[title='Minimize']";

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final WebElement webElement;

    private Modal(WebDriver driver, WebDriverWait wait, WebElement webElement) {
        this.wait = wait;
        this.driver = driver;
        this.webElement = webElement;
    }

    public static Modal create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitBy(wait, By.className(MODAL_CLASS));
        WebElement webElement = driver.findElement(By.className(MODAL_CLASS));
        return new Modal(driver, wait, webElement);
    }

    public String getModalTitle() {
        return this.webElement.findElement(By.className(MODAL_TITLE_CLASS)).getText();
    }

    public void clickClose() {
        clickButton(CLOSE_BUTTON_CSS);
    }

    public void clickMinimize() {
        clickButton(MINIMIZE_BUTTON_CSS);
    }

    private void clickButton(String buttonCSS) {
        DelayUtils.waitForNestedElements(this.wait, this.webElement, By.cssSelector(buttonCSS));
        WebElement button = this.webElement.findElement(By.cssSelector((buttonCSS)));
        WebElementUtils.clickWebElement(driver, button);
        DelayUtils.waitForElementDisappear(wait, button);
    }
}


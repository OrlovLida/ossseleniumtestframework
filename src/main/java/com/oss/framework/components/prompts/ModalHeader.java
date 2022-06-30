package com.oss.framework.components.prompts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class ModalHeader {

    private static final String MODAL_HEADER_CLASS = "header-modal";
    private static final String MODAL_HEADER_TITLE_CLASS = "modal-header-title";
    private static final String BUTTON_BY_TITLE_PATTERN = "[title='%s']";

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final WebElement webElement;

    private ModalHeader(WebDriver driver, WebDriverWait wait, WebElement webElement) {
        this.wait = wait;
        this.driver = driver;
        this.webElement = webElement;
    }

    public static ModalHeader create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitBy(wait, By.className(MODAL_HEADER_CLASS));
        WebElement webElement = driver.findElement(By.className(MODAL_HEADER_CLASS));
        return new ModalHeader(driver, wait, webElement);
    }

    public String getModalHeaderTitle() {
        return this.webElement.findElement(By.className(MODAL_HEADER_TITLE_CLASS)).getText();
    }

    public void clickButtonByTitle(String buttonTitle) {
        DelayUtils.waitForNestedElements(this.wait, this.webElement, By.cssSelector(String.format(BUTTON_BY_TITLE_PATTERN, buttonTitle)));
        WebElement button = wait.until(ExpectedConditions
                .elementToBeClickable(this.webElement.findElement(By.cssSelector(String.format(BUTTON_BY_TITLE_PATTERN, buttonTitle)))));
        WebElementUtils.clickWebElement(driver, button);
        wait.until(ExpectedConditions.invisibilityOf(button));
    }
}


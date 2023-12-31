/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.components.prompts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

/**
 * @author Gabriela Kasza
 */
public class ConfirmationBox implements ConfirmationBoxInterface {

    public static final String PROCEED = "Proceed";
    public static final String DELETE = "Delete";
    public static final String YES = "Yes";
    private static final String BUTTON_BY_TEXT_PATTERN = ".//button[contains(text(),'%s')] | .//a[contains(text(),'%s')]";
    private static final String CONFIRMATION_BOX_CLASS = "ConfirmationBox";
    private static final String CONFIRMATION_BOX_APP_XPATH = ".//div[contains(@class, 'common-confirmationboxapp')]";
    private static final String RICH_TEXT_XPATH = ".//div[contains(@class, 'OSSRichText')]";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement prompt;

    private ConfirmationBox(WebDriver driver, WebDriverWait wait, WebElement prompt) {
        this.driver = driver;
        this.wait = wait;
        this.prompt = prompt;
    }

    public static ConfirmationBox create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitBy(wait, By.className(CONFIRMATION_BOX_CLASS));
        WebElement prompt = driver.findElement(By.className(CONFIRMATION_BOX_CLASS));
        DelayUtils.waitForElementToLoad(wait, prompt);
        return new ConfirmationBox(driver, wait, prompt);
    }

    @Override
    public void clickButtonByLabel(String label) {
        DelayUtils.waitForNestedElements(wait, this.prompt, String.format(BUTTON_BY_TEXT_PATTERN, label, label));
        WebElement button = wait.until(ExpectedConditions
                .elementToBeClickable(this.prompt.findElement(By.xpath(String.format(BUTTON_BY_TEXT_PATTERN, label, label)))));
        button.click();
    }

    @Override
    public void clickButtonById(String id) {
        DelayUtils.waitForElementToLoad(wait, prompt);
        WebElement button = wait.until(ExpectedConditions
                .elementToBeClickable(this.prompt.findElement(By.cssSelector(String.format(CSSUtils.WEB_ELEMENT_PATTERN, id)))));
        button.click();
    }

    @Override
    public String getMessage() {
        DelayUtils.waitByXPath(wait, CONFIRMATION_BOX_APP_XPATH);
        WebElement windowContent = driver.findElement(By.xpath(CONFIRMATION_BOX_APP_XPATH));
        WebElement message = windowContent.findElement(By.xpath(RICH_TEXT_XPATH));
        return message.getText();
    }
}

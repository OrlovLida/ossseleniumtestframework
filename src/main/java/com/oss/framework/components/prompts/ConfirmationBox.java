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
    private static final String BUTTON_CSS = "[" + CSSUtils.TEST_ID + "='%s']";
    private static final String BUTTON_BY_TEXT_PATTERN = ".//button[contains(text(),'%s')] | .//a[contains(text(),'%s')]";
    private static final String NEW_PROMPT_XPATH = "//div[contains(@class,'OssWindow newPrompt')]";
    private static final String WINDOW_CONTENT_XPATH = ".//div[contains(@class, 'windowContent')]";
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
        DelayUtils.waitByXPath(wait, NEW_PROMPT_XPATH);
        WebElement prompt = driver.findElement(By.xpath(NEW_PROMPT_XPATH));
        DelayUtils.waitForElementToLoad(wait, prompt);
        return new ConfirmationBox(driver, wait, prompt);
    }

    @Override
    public void clickButtonByLabel(String label) {
        DelayUtils.waitForNestedElements(wait, this.prompt, String.format(BUTTON_BY_TEXT_PATTERN, label, label));
        WebElement button = wait.until(ExpectedConditions
                .elementToBeClickable(this.prompt.findElement(By.xpath(String.format(BUTTON_BY_TEXT_PATTERN, label, label)))));
        button.click();
        wait.until(ExpectedConditions.invisibilityOf(button));
    }

    @Override
    public void clickButtonById(String id) {
        DelayUtils.waitForElementToLoad(wait, prompt);
        WebElement button = wait.until(ExpectedConditions
                .elementToBeClickable(this.prompt.findElement(By.cssSelector(String.format(BUTTON_CSS, id)))));
        button.click();
        wait.until(ExpectedConditions.invisibilityOf(button));
    }

    @Override
    public String getMessage() {
        DelayUtils.waitByXPath(wait, WINDOW_CONTENT_XPATH);
        WebElement windowContent = driver.findElement(By.xpath(WINDOW_CONTENT_XPATH));
        WebElement message = windowContent.findElement(By.xpath(RICH_TEXT_XPATH));
        return message.getText();
    }
}

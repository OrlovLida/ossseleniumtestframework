/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.prompts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

/**
 * @author Gabriela Kasza
 */
public class ConfirmationBox implements ConfirmationBoxInterface {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement prompt;

    private ConfirmationBox(WebDriver driver, WebDriverWait wait, WebElement prompt) {
        this.driver = driver;
        this.wait = wait;
        this.prompt = prompt;
    }

    public static ConfirmationBoxInterface create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, "//div[contains(@class,'OssWindow newPrompt')]");
        WebElement prompt = driver.findElement(By.xpath("//div[contains(@class,'OssWindow newPrompt')]"));
        return new ConfirmationBox(driver, wait, prompt);
    }

    @Override
    public void clickButtonByLabel(String label) {
        DelayUtils.waitForNestedElements(wait, this.prompt, ".//button[contains(text(),'" + label + "')]|.//a[contains(text(),'" + label + "')]");
        WebElement button = wait.until(ExpectedConditions
                .elementToBeClickable(this.prompt.findElement(By.xpath(".//button[contains(text(),'" + label + "')]|.//a[contains(text(),'" + label + "')]"))));
        button.click();
        wait.until(ExpectedConditions.invisibilityOf(button));
    }

    @Override
    public String getMessage() {
        DelayUtils.waitByXPath(wait, ".//div[contains(@class, 'windowContent')]");
        WebElement windowContent = driver.findElement(By.xpath(".//div[contains(@class, 'windowContent')]"));
        WebElement message = windowContent.findElement(By.xpath(".//div[contains(@class, 'OSSRichText')]"));
        return message.getText();
    }
}

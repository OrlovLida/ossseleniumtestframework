/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.components.contextactions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

/**
 * @author Gabriela Kasza
 */
public class ButtonContainer implements ActionsInterface {

    private static final String METHOD_NOT_IMPLEMENTED = "Method not implemented for Button Container";

    public static ButtonContainer create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, "//div[contains(@class,'commonButtons')] | //div[@class='windowIcons']");
        WebElement buttons = driver.findElement(By.xpath("//div[contains(@class,'commonButtons')] | //div[class='windowIcons']"));
        return new ButtonContainer(driver, wait, buttons);
    }

    public static ButtonContainer createFromParent(WebElement parentElement, WebDriver driver, WebDriverWait wait) {
        return new ButtonContainer(driver, wait, parentElement);
    }

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement buttons;

    private ButtonContainer(WebDriver driver, WebDriverWait wait, WebElement buttons) {
        this.driver = driver;
        this.wait = wait;
        this.buttons = buttons;
    }

    @Override
    public void callActionByLabel(String label) {
        DelayUtils.waitForNestedElements(wait, buttons, "//a[text()='" + label + "'] | //*[text()='" + label + "']/ancestor::button | //*[@aria-label='" + label + "']");
        WebElement button = buttons.findElement(By.xpath("//a[text()='" + label + "'] | //*[text()='" + label + "']/ancestor::button | //*[@aria-label='" + label + "']"));
        Actions action = new Actions(driver);
        action.moveToElement(button).click(button).perform();
    }

    @Override
    public void callActionById(String id) {
        clickAction(id);
    }

    @Override
    public void callActionByLabel(String groupLabel, String actionLabel) {
        throw new UnsupportedOperationException(METHOD_NOT_IMPLEMENTED);
    }

    @Override
    public void callActionById(String groupLabel, String actionId) {
        clickGroup(groupLabel);
        clickAction(actionId);
    }

    private void clickGroup(String groupLabel) {
        DelayUtils.waitForNestedElements(wait, buttons, ".//a[text()='" + groupLabel + "'] | .//*[text()='" + groupLabel + "']");
        WebElement button = buttons.findElement(By.xpath(".//a[text()='" + groupLabel + "'] | .//*[text()='" + groupLabel + "']"));
        button.click();
    }

    private void clickAction(String actionId) {
        DelayUtils.waitForNestedElements(wait, buttons, "//*[@" + CSSUtils.TEST_ID + "='" + actionId + "'] | //*[@id='" + actionId + "'] | //*[@data-widget-id='" + actionId + "']");
        Actions action = new Actions(driver);
        action.moveToElement(wait.until(ExpectedConditions.elementToBeClickable(
                buttons.findElement(By.xpath("//*[@" + CSSUtils.TEST_ID + "='" + actionId + "'] | //*[@id='" + actionId + "'] | //*[@data-widget-id='" + actionId + "']")))))
                .click()
                .perform();
    }
}

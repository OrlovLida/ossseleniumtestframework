/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.components.contextactions;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
    private static final String BUTTON_CONTAINER_XPATH = "//div[contains(@class,'commonButtons')] | //div[@class='ContextButtonsContainer']";
    private static final String GROUP_PATTERN = ".//a[text()='%s'] | .//*[text()='%s']";
    private static final String BUTTON_BY_LABEL_PATTERN = "//a[text()='%s'] | //*[text()='%s']/ancestor::button | //*[@aria-label='%s']";
    private static final String BUTTON_BY_ID_PATTERN = "//*[@" + CSSUtils.TEST_ID + "='%s'] | //*[@id='%s'] | //*[@data-widget-id='%s']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement buttons;

    private ButtonContainer(WebDriver driver, WebDriverWait wait, WebElement buttons) {
        this.driver = driver;
        this.wait = wait;
        this.buttons = buttons;
    }

    public static ButtonContainer create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, BUTTON_CONTAINER_XPATH);
        WebElement buttons = driver.findElement(By.xpath(BUTTON_CONTAINER_XPATH));
        return new ButtonContainer(driver, wait, buttons);
    }

    public static ButtonContainer createFromParent(WebElement parentElement, WebDriver driver, WebDriverWait wait) {
        return new ButtonContainer(driver, wait, parentElement);
    }

    private static void clickOnWebElement(WebDriver webDriver, WebDriverWait webDriverWait, WebElement webElement) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", webElement);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
        Actions actions = new Actions(webDriver);
        actions.moveToElement(webElement).click(webElement).build().perform();
    }

    @Override
    public void callActionByLabel(String label) {
        String buttonXpath = String.format(BUTTON_BY_LABEL_PATTERN, label, label, label);
        DelayUtils.waitForNestedElements(wait, buttons, buttonXpath);
        WebElement button = buttons.findElement(By.xpath(buttonXpath));
        clickOnWebElement(driver, wait, button);
    }

    @Override
    public void callActionByLabel(String groupLabel, String actionLabel) {
        throw new UnsupportedOperationException(METHOD_NOT_IMPLEMENTED);
    }

    @Override
    public void callActionById(String id) {
        String buttonXpath = String.format(BUTTON_BY_ID_PATTERN, id, id, id);
        DelayUtils.waitForNestedElements(wait, buttons, buttonXpath);
        clickOnWebElement(driver, wait, buttons.findElement(By.xpath(buttonXpath)));
    }

    @Override
    public void callActionById(String groupLabel, String actionId) {
        clickGroup(groupLabel);
        callActionById(actionId);
    }

    private void clickGroup(String groupLabel) {
        String groupXpath = String.format(GROUP_PATTERN, groupLabel, groupLabel);
        DelayUtils.waitForNestedElements(wait, buttons, groupXpath);
        WebElement button = buttons.findElement(By.xpath(groupXpath));
        clickOnWebElement(driver, wait, button);
    }
}

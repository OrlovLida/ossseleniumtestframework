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
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

/**
 * @author Gabriela Kasza
 */
public class ButtonContainer implements ActionsInterface{


    public static ButtonContainer create (WebDriver driver, WebDriverWait wait){
        DelayUtils.waitByXPath(wait, "//div[contains(@class,'commonButtons')]");
        WebElement buttons= driver.findElement(By.xpath("//div[contains(@class,'commonButtons')]"));
        return new ButtonContainer(driver,wait,buttons);
    }

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement buttons;

    private ButtonContainer (WebDriver driver, WebDriverWait wait, WebElement buttons) {
        this.driver = driver;
        this.wait = wait;
        this.buttons = buttons;
    }
    @Override
    public void callAction(String actionId) {
        throw new RuntimeException("Method not implemented for the old actions container Button Container");

    }

    @Override
    public void callActionByLabel(String label) {
        DelayUtils.waitForNestedElements(wait,this.buttons,".//a[contains(text(),'" + label + "')]");
        WebElement button = this.buttons.findElement(By.xpath(".//a[contains(text(),'" + label + "')]"));
        button.click();

    }

    @Override
    public void callAction(String groupId, String actionId) {
        throw new RuntimeException("Method not implemented for the old actions container Button Container");
    }

    @Override
    public void callActionByLabel(String groupLabel, String actionLabel) {
        throw new RuntimeException("Method not implemented for the old actions container Button Container");
    }
}

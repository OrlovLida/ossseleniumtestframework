/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

/**
 * @author Gabriela Kasza
 */
public class InlineForm {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    private InlineForm(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = driver.findElement(By.className("stickyFormContainer"));
    }

    public static InlineForm create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, "//div[contains(@class,'stickyFormContainer')]");
        return new InlineForm(driver, wait);

    }

    public void clickButtonByLabel(String label) {
        WebElement button = this.webElement.findElement(By.xpath(".//a[contains(text(),'" + label + "')]"));
        button.click();
    }

    public Input getComponent(String componentId, Input.ComponentType componentType) {
        return ComponentFactory.createFromParent(componentId, componentType, this.driver, this.wait, this.webElement);
    }

}

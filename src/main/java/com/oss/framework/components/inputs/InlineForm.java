/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.components.inputs;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Gabriela Kasza
 */
public class InlineForm {
    private static final String STICKY_FORM_XPATH = "//div[contains(@class,'stickyFormContainer')]";
    private static final String STICKY_FORM_CLASS = "stickyFormContainer";
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    private InlineForm(WebDriver driver, WebDriverWait wait, WebElement webElement) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = webElement;
    }

    public static InlineForm create(WebDriver driver, WebDriverWait wait) {
        WebElement webElement = driver.findElement(By.className(STICKY_FORM_CLASS));
        WebElementUtils.moveToElement(driver, webElement);
        DelayUtils.waitByXPath(wait, STICKY_FORM_XPATH);
        return new InlineForm(driver, wait, webElement);

    }

    public void clickButtonByLabel(String label) {
        DelayUtils.waitForPresence(wait, By.xpath("//a[contains(@class,'CommonButton btn') and text()='" + label + "']"));
        WebElement button = this.webElement.findElement(By.xpath(".//a[contains(text(),'" + label + "')]"));
        button.click();
    }

    public Input getComponent(String componentId, Input.ComponentType componentType) {
        return ComponentFactory.createFromParent(componentId, componentType, this.driver, this.wait, this.webElement);
    }

    public Input getComponent(String componentId) {
        return ComponentFactory.createFromParent(componentId, this.driver, this.wait, this.webElement);
    }

}

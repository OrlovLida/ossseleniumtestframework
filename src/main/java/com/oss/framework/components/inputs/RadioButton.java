/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.components.inputs;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;

public class RadioButton extends Input {
    static RadioButton create(WebDriver driver, WebDriverWait webDriverWait, String componentId) {
        return new RadioButton(driver, webDriverWait, componentId);
    }
    RadioButton(WebDriver driver, WebDriverWait webDriverWait, String componentId) {
        super(driver, webDriverWait, componentId);
    }

    @Override
    public void setValue(Data value) {
        String valueToSet = value.getStringValue();
        DelayUtils.waitForNestedElements(webDriverWait,webElement,"//label[text()='" + valueToSet + "']");
        WebElement radioButton = this.webElement.findElement(By.xpath("//label[text()='" + valueToSet + "']"));
        radioButton.click();
    }

    @Override
    public void setValueContains(Data value) {
        String valueToSet = value.getStringValue();
        DelayUtils.waitForNestedElements(webDriverWait,webElement,"//label[contains(text(),'"+ valueToSet +"')]");
        WebElement radioButton = this.webElement.findElement(By.xpath("//label[contains(text(),'" + valueToSet + "')]"));
        radioButton.click();

    }

    @Override
    public Data getValue() {
        List<String> names = new ArrayList<String>();
        DelayUtils.waitByElement(webDriverWait,this.webElement);
        List<WebElement> radioButtons = this.webElement.findElements(By.className("radio"));
        for (WebElement radioButton:radioButtons) {
            names.add(radioButton.getText());
        }
        return Data.createMultiData(names);
    }

    @Override
    public void clear() {
        throw new RuntimeException("Method not implemented for the Radio Button");
    }

}

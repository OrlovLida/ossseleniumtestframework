/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.components;


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
        setRadioButton(value);


    }

    @Override
    public void setValueContains(Data value) {

    }

    @Override
    public Data getValue() {
        return null;
    }

    @Override
    public void clear() {

    }
    private void setRadioButton(Data value){
        String valueToSet = value.getStringValue();
        DelayUtils.waitByXPath(webDriverWait,"//label[contains(text(),'"+ valueToSet +"')]");
        WebElement radioButton = driver.findElement(By.xpath("//label[contains(text(),'" + valueToSet + "')]"));
        radioButton.click();

    }
}

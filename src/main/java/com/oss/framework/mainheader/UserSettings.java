/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.mainheader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

/**
 * @author Gabriela Kasza
 */
public class UserSettings {
    private WebDriver driver;
    private WebDriverWait wait;

    public static UserSettings create (WebDriver driver, WebDriverWait wait){
        return new UserSettings(driver,wait);
    }
    private UserSettings (WebDriver driver, WebDriverWait wait){
        this.driver=driver;
        this.wait=wait;

    }
    public void chooseLanguage(String language){
        MainHeader toolbar = MainHeader.create(driver, wait);
        toolbar.callActionByLabel("loginButton");
        DelayUtils.waitByXPath(wait,"//div[@class='login-panel']");
        WebElement loginPanel = driver.findElement(By.xpath("//div[@class='login-panel']"));



    }

    private String checkLanguage(){
        DelayUtils.waitByXPath(wait,"//div[@class='login-panel']");
        WebElement loginPanel = driver.findElement(By.xpath("//div[@class='login-panel']"));

        return null;
    }

}

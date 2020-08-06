/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.mainheader;

import com.oss.framework.components.Button;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.ComponentFactory;
import com.oss.framework.components.Input;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;

/**
 * @author Gabriela Kasza
 */
public class UserSettings {
    private WebDriver driver;
    private WebDriverWait wait;
    private final static String LOGIN_BUTTON= "loginButton";
    private final static String XPATH_LOGIN_PANEL= "//div[@class='login-panel']";
    private final static String LANGUAGE_CHOOSER= "language-chooser";
    private final static String LOGIN_BUTTON_ID = "logout-button";

    public static UserSettings create (WebDriver driver, WebDriverWait wait){
        return new UserSettings(driver,wait);
    }

    private UserSettings (WebDriver driver, WebDriverWait wait){
        this.driver=driver;
        this.wait=wait;

    }
    public void chooseLanguage(String language){
        MainHeader toolbar = MainHeader.create(driver, wait);
        toolbar.callActionByLabel(LOGIN_BUTTON);
        DelayUtils.waitByXPath(wait,XPATH_LOGIN_PANEL);
        Input input = ComponentFactory.create(LANGUAGE_CHOOSER, Input.ComponentType.COMBOBOX, driver, wait);
        String currentLanguage = input.getStringValue();
        if (!currentLanguage.equals(language)){
            input.setSingleStringValue(language);
            ConfirmationBoxInterface prompt= ConfirmationBox.create(driver, wait);
            prompt.clickButtonByLabel("OK");
        } else {
            toolbar.callActionByLabel(LOGIN_BUTTON);
        }
    }

    public UserSettings open(){
        MainHeader.create(driver, wait).callActionByLabel(LOGIN_BUTTON);
        DelayUtils.waitByXPath(wait,"//button[contains (@data-attributename, "+LOGIN_BUTTON_ID+")]");
        return this;
    }

    public void logOut(){
        Button.createById(driver,LOGIN_BUTTON_ID).click();
    }



}

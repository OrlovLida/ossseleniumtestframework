/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.components.mainheader;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.prompts.Popup;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Gabriela Kasza
 */
public class LoginPanel {
    private static final String LANGUAGE_CHOOSER = "language-chooser";
    private static final String USER_GROUP_CHOOSER = "user_group_chooser";
    private static final String DATE_FORMAT_CHOOSER = "date-format-chooser";
    private static final String TIME_ZONE_CHOOSER = "time-zone-chooser";
    private static final String LOGIN_BUTTON_ID = "logout-button";
    private static final String ALPHA_MODE_SWITCHER = "alpha-mode-switcher";
    private static final String USING_SYSTEM_ZONE_SWITCHER = "using-system-zone-switcher";
    private static final String CHANGE_BUTTON = "Change";
    private static final String OK_BUTTON = "OK";
    private final WebDriver driver;
    private final WebDriverWait wait;

    private LoginPanel(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static LoginPanel create(WebDriver driver, WebDriverWait wait) {
        return new LoginPanel(driver, wait);
    }

    public void chooseLanguage(String language) {
        ToolbarWidget toolbar = ToolbarWidget.create(driver, wait);
        toolbar.openLoginPanel();
        Input input = ComponentFactory.create(LANGUAGE_CHOOSER, Input.ComponentType.COMBOBOX, driver, wait);
        String currentLanguage = input.getStringValue();
        if (!currentLanguage.equals(language)) {
            input.setSingleStringValue(language);
            ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
            prompt.clickButtonByLabel(OK_BUTTON);
            DelayUtils.waitForPageToLoad(driver, wait);
        } else {
            toolbar.closeLoginPanel();
        }
    }

    public void chooseGroupContext(String groupName) {
        ToolbarWidget toolbar = ToolbarWidget.create(driver, wait);
        toolbar.openLoginPanel();
        Input input = ComponentFactory.create(USER_GROUP_CHOOSER, Input.ComponentType.COMBOBOX, driver, wait);
        String currentGroup = input.getStringValue();
        if (!currentGroup.equals(groupName)) {
            input.setSingleStringValue(groupName);
            DelayUtils.waitForPageToLoad(driver, wait);
        } else {
            toolbar.closeLoginPanel();
        }
    }

    public void chooseDataFormat(String dataFormat) {
        ToolbarWidget toolbar = ToolbarWidget.create(driver, wait);
        toolbar.openLoginPanel();
        Input input = ComponentFactory.create(DATE_FORMAT_CHOOSER, Input.ComponentType.COMBOBOX, driver, wait);
        String currentDataFormat = input.getStringValue();
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern(dataFormat);
        String nowDataInSelectedFormat = dtf1.format(ZonedDateTime.now());
        if (!currentDataFormat.equals(nowDataInSelectedFormat)) {
            input.setSingleStringValue(nowDataInSelectedFormat);
            Popup prompt = Popup.create(driver, wait);
            prompt.clickButtonByLabel(CHANGE_BUTTON);
            DelayUtils.waitForPageToLoad(driver, wait);
        } else {
            toolbar.closeLoginPanel();
        }
    }

    public void disableAutoTimeZone() {
        ToolbarWidget toolbar = ToolbarWidget.create(driver, wait);
        toolbar.openLoginPanel();
        Input input = ComponentFactory.create(USING_SYSTEM_ZONE_SWITCHER, ComponentType.SWITCHER, driver, wait);
        input.setValue(Data.createSingleData("false"));
        Popup prompt = Popup.create(driver, wait);
        prompt.clickButtonByLabel(CHANGE_BUTTON);
        DelayUtils.waitForPageToLoad(driver, wait);

    }

    public void enableAutoTimeZone() {
        ToolbarWidget toolbar = ToolbarWidget.create(driver, wait);
        toolbar.openLoginPanel();
        Input input = ComponentFactory.create(USING_SYSTEM_ZONE_SWITCHER, ComponentType.SWITCHER, driver, wait);
        input.setValue(Data.createSingleData("true"));
        Popup prompt = Popup.create(driver, wait);
        prompt.clickButtonByLabel(CHANGE_BUTTON);
        DelayUtils.waitForPageToLoad(driver, wait);

    }

    public void chooseTimeZone(String timeZone) {
        ToolbarWidget toolbar = ToolbarWidget.create(driver, wait);
        toolbar.openLoginPanel();
        Input input = ComponentFactory.create(TIME_ZONE_CHOOSER, Input.ComponentType.COMBOBOX, driver, wait);
        String currentTimeZone = input.getStringValue();
        if (!currentTimeZone.equals(timeZone)) {
            input.setSingleStringValueContains(timeZone);
            Popup prompt = Popup.create(driver, wait);
            prompt.clickButtonByLabel(CHANGE_BUTTON);
            DelayUtils.waitForPageToLoad(driver, wait);
        } else {
            toolbar.closeLoginPanel();
        }
    }


    public LoginPanel open() {
        ToolbarWidget.create(driver, wait).openLoginPanel();
        DelayUtils.waitByXPath(wait, "//button[contains (@" + CSSUtils.TEST_ID + ", " + LOGIN_BUTTON_ID + ")]");
        return this;
    }

    public void switchToAlphaMode() {
        ToolbarWidget toolbar = ToolbarWidget.create(driver, wait);
        toolbar.openLoginPanel();
        Input input = ComponentFactory.create(ALPHA_MODE_SWITCHER, ComponentType.SWITCHER, driver, wait);
        input.setSingleStringValue("true");
        DelayUtils.waitForPageToLoad(driver, wait);

    }

    public void logOut() {
        Button.createById(driver, LOGIN_BUTTON_ID).click();
    }

}

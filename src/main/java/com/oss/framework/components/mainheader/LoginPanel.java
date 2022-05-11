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

import java.time.LocalDateTime;
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
        open();
        Input input = ComponentFactory.create(LANGUAGE_CHOOSER, Input.ComponentType.COMBOBOX, driver, wait);
        String currentLanguage = input.getStringValue();
        if (!currentLanguage.equals(language)) {
            input.setSingleStringValue(language);
            ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
            prompt.clickButtonByLabel(OK_BUTTON);
            DelayUtils.waitForPageToLoad(driver, wait);
        } else {
            close();
        }
    }

    public void chooseGroupContext(String groupName) {
        open();
        Input input = ComponentFactory.create(USER_GROUP_CHOOSER, Input.ComponentType.COMBOBOX, driver, wait);
        String currentGroup = input.getStringValue();
        if (!currentGroup.equals(groupName)) {
            input.setSingleStringValue(groupName);
            DelayUtils.waitForPageToLoad(driver, wait);
        } else {
            close();
        }
    }

    public void chooseDataFormat(String presentDateFormat, String targetDateFormat) {
        open();
        Input input = ComponentFactory.create(DATE_FORMAT_CHOOSER, Input.ComponentType.COMBOBOX, driver, wait);
        String currentDataFormat = input.getStringValue();
        DateTimeFormatter presentFormat = DateTimeFormatter.ofPattern(presentDateFormat);
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern(targetDateFormat);
        String nowDataInSelectedFormat = dtf1.format(LocalDateTime.parse(currentDataFormat, presentFormat));
        if (!currentDataFormat.equals(nowDataInSelectedFormat)) {
            input.setSingleStringValue(nowDataInSelectedFormat);
            popupAccept();
        } else {
            close();
        }
    }

    private void popupAccept() {
        Popup prompt = Popup.create(driver, wait);
        prompt.clickButtonByLabel(CHANGE_BUTTON);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void disableAutoTimeZone() {
        setAutoTimeZone("false");
    }

    public void enableAutoTimeZone() {
        setAutoTimeZone("true");
    }

    private void setAutoTimeZone(String value) {
        ToolbarWidget toolbar = ToolbarWidget.create(driver, wait);
        toolbar.openLoginPanel();
        Input input = ComponentFactory.create(USING_SYSTEM_ZONE_SWITCHER, ComponentType.SWITCHER, driver, wait);
        input.setValue(Data.createSingleData(value));
        popupAccept();
    }

    public void chooseTimeZone(String timeZone) {
        open();
        Input input = ComponentFactory.create(TIME_ZONE_CHOOSER, Input.ComponentType.COMBOBOX, driver, wait);
        String currentTimeZone = input.getStringValue();
        if (!currentTimeZone.equals(timeZone)) {
            input.setSingleStringValueContains(timeZone);
            popupAccept();
        } else {
            close();
        }
    }

    public LoginPanel open() {
        ToolbarWidget.create(driver, wait).openLoginPanel();
        DelayUtils.waitByXPath(wait, "//button[contains (@" + CSSUtils.TEST_ID + ", " + LOGIN_BUTTON_ID + ")]");
        return this;
    }

    private void close() {
        ToolbarWidget.create(driver, wait).closeLoginPanel();
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

/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.components.mainheader;

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

/**
 * @author Gabriela Kasza
 */
public class LoginPanel {
    private static final String LANGUAGE_CHOOSER = "language-chooser";
    private static final String LOGIN_BUTTON_ID = "logout-button";
    private static final String ALPHA_MODE_SWITCHER = "alpha-mode-switcher";
    private WebDriver driver;
    private WebDriverWait wait;

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
            prompt.clickButtonByLabel("OK");
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
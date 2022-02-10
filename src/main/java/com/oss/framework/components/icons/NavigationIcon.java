/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.components.icons;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

/**
 * @author Gabriela Zaranek
 */
public class NavigationIcon {

    private static final String ICONS_CHOOSER_CSS = ".icons-chooser";

    private final WebElement icons;

    public NavigationIcon(WebElement icons) {

        this.icons = icons;
    }

    public static NavigationIcon create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPresence(wait, By.cssSelector(ICONS_CHOOSER_CSS));
        WebElement icons = driver.findElement(By.cssSelector(ICONS_CHOOSER_CSS));
        return new NavigationIcon(icons);
    }

    public void selectIcon(String iconId) {
        icons.findElement(By.cssSelector("[" + CSSUtils.TEST_ID + "='" + iconId + "']")).click();
    }
}

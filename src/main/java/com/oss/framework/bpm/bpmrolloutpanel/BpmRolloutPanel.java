/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.bpm.bpmrolloutpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class BpmRolloutPanel {
    
    private static final String ARIA_EXPANDED_ATTRIBUTE = "aria-expanded";
    private static final String TRUE_VALUE = "true";
    private static final String TAG_CSS = "a";
    private static final String PANEL_TITLE_CSS = ".panel-title";
    private static final String TEXT_CONTENT = "textContent";
    private static final String FALSE_VALUE = "false";
    private static final String EXPANDED_PATTERN = "[" + ARIA_EXPANDED_ATTRIBUTE + "= '%s']";
    private final WebDriver driver;
    private final WebElement rolloutPanelElement;
    private final WebDriverWait wait;
    
    private BpmRolloutPanel(WebDriver driver, WebDriverWait wait, WebElement rolloutPanel) {
        this.driver = driver;
        this.wait = wait;
        this.rolloutPanelElement = rolloutPanel;
    }
    
    public static BpmRolloutPanel createById(WebDriver driver, WebDriverWait wait, String rolloutPanelId) {
        DelayUtils.waitBy(wait, By.cssSelector("#" + rolloutPanelId));
        WebElement rolloutPanel = driver.findElement(By.cssSelector("#" + rolloutPanelId));
        return new BpmRolloutPanel(driver, wait, rolloutPanel);
    }
    
    public String getValue() {
        return getElement(PANEL_TITLE_CSS).getAttribute(TEXT_CONTENT);
    }
    
    public void expandRolloutPanel() {
        if (!isExpanded()) {
            toggleRolloutPanel();
            DelayUtils.waitBy(wait, By.cssSelector(String.format(EXPANDED_PATTERN, TRUE_VALUE)));
        }
    }
    
    public void collapseRolloutPanel() {
        if (isExpanded()) {
            toggleRolloutPanel();
            DelayUtils.waitBy(wait, By.cssSelector(String.format(EXPANDED_PATTERN, FALSE_VALUE)));
        }
    }
    
    private boolean isExpanded() {
        return getElement(TAG_CSS).getAttribute(ARIA_EXPANDED_ATTRIBUTE).equals(TRUE_VALUE);
    }
    
    private WebElement getElement(String tagCss) {
        return rolloutPanelElement.findElement(By.cssSelector(tagCss));
    }
    
    private void toggleRolloutPanel() {
        WebElementUtils.clickWebElement(driver, getElement(TAG_CSS));
    }
}

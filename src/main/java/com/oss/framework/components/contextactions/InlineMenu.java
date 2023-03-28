package com.oss.framework.components.contextactions;

import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class InlineMenu implements InlineMenuInterface {

    private static final String GROUP_BUTTON_ID = "frameworkObjectButtonsGroup";
    private static final String NOT_DISABLED_INLINE_ACTIONS_GROUP_CSS = ":not(.actionsGroup-inline.disabled";

    private final WebDriver driver;
    private final WebElement webElement;
    private final WebDriverWait wait;

    private InlineMenu(WebElement webElement, WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = webElement;
    }

    public static InlineMenu create(WebElement webElement, WebDriver driver, WebDriverWait wait) {
        return new InlineMenu(webElement, driver, wait);
    }

    @Override
    public void callAction(String actionId) {
        expandKebabMenu();
        DropdownList.create(driver, wait).selectOptionById(actionId);
    }

    @Override
    public void callAction(String groupId, String actionId) {
        expandKebabMenu();
        DropdownList.create(driver, wait).selectOptions(Arrays.asList(groupId, actionId));
    }

    @Override
    public void callActionByLabel(String actionLabel) {
        expandKebabMenu();
        DropdownList.create(driver, wait).selectOption(actionLabel);
    }

    @Override
    public void callActionByLabel(String groupLabel, String actionLabel) {
        expandKebabMenu();
        DropdownList.create(driver, wait).selectOptionsByLabels(Arrays.asList(groupLabel, actionLabel));
    }

    private void expandKebabMenu() {
        WebElement inlineKebab = webElement.findElement(By.id(GROUP_BUTTON_ID));
        DelayUtils.waitForNestedElements(wait, webElement, By.cssSelector("#" + GROUP_BUTTON_ID + NOT_DISABLED_INLINE_ACTIONS_GROUP_CSS));
        WebElementUtils.clickWebElement(driver, inlineKebab);
    }

}

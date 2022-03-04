package com.oss.framework.components.contextactions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.WebElementUtils;

public class OldInlineMenu implements InlineMenuInterface {

    private static final String KEBAB_MENU_BUTTON_CLASS = "contextButtonMenu";
    private static final String METHOD_NOT_IMPLEMENTED = "Method not implemented for the old inline menu";

    private final WebDriver driver;
    private final WebElement oldInlineMenuElement;
    private final WebDriverWait wait;

    public static OldInlineMenu create(WebElement webElement, WebDriver driver, WebDriverWait wait) {
        return new OldInlineMenu(webElement, driver, wait);
    }

    private OldInlineMenu(WebElement webElement, WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.oldInlineMenuElement = webElement;
    }

    @Override
    public void callAction(String actionId) {
        throw new UnsupportedOperationException(METHOD_NOT_IMPLEMENTED);
    }

    @Override
    public void callAction(String groupId, String actionId) {
        throw new UnsupportedOperationException(METHOD_NOT_IMPLEMENTED);
    }

    @Override
    public void callActionByLabel(String actionLabel) {
        expandKebabMenu();
        DropdownList.create(driver, wait).selectOptionContains(actionLabel);
    }

    @Override
    public void callActionByLabel(String groupLabel, String actionLabel) {
        throw new UnsupportedOperationException(METHOD_NOT_IMPLEMENTED);
    }

    private void expandKebabMenu() {
        WebElement oldInlineKebab = oldInlineMenuElement.findElement(By.className(KEBAB_MENU_BUTTON_CLASS));
        WebElementUtils.clickWebElement(driver, oldInlineKebab);
    }
}

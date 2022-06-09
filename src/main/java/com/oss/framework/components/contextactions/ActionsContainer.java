package com.oss.framework.components.contextactions;

import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class ActionsContainer implements ActionsInterface {

    public static final String KEBAB_GROUP_ID = "frameworkCustomButtonsGroup";
    public static final String CREATE_GROUP_ID = "CREATE";
    public static final String EDIT_GROUP_ID = "EDIT";
    public static final String ASSIGN_GROUP_ID = "ASSIGN";
    public static final String OTHER_GROUP_ID = "OTHER";
    public static final String SHOW_ON_GROUP_ID = "NAVIGATION";
    private static final String MORE_GROUP_ID = "moreActions";
    private static final String CONTEXT_ACTIONS_CLASS = "actionsContainer--default";
    private static final String GROUP_ALL_PATTERN = ".//div[@id='%s'] | .//*[contains(text(),'%s')] | .//div[@id= '" + MORE_GROUP_ID + "']";
    private static final String GROUP_PATTERN = ".//div[@id='%s'] | .//*[contains(text(),'%s')]";
    private static final String UNSUPPORTED_EXCEPTION = "Method not implemented for Actions Container.";
    private static final String NO_ACTION_EXCEPTION = "No active Context Action.";

    private final WebElement webElement;
    private final WebDriver webDriver;
    private final WebDriverWait webDriverWait;

    private ActionsContainer(WebElement activeContextActions, WebDriver webDriver, WebDriverWait webDriverWait) {
        this.webDriver = webDriver;
        this.webElement = activeContextActions;
        this.webDriverWait = webDriverWait;
    }

    public static ActionsContainer createFromParent(WebElement parentElement, WebDriver webDriver, WebDriverWait webDriverWait) {
        DelayUtils.waitForNestedElements(webDriverWait, parentElement, By.className(CONTEXT_ACTIONS_CLASS));
        return new ActionsContainer(getActiveContextActions(parentElement), webDriver, webDriverWait);
    }

    private static WebElement getActiveContextActions(WebElement parentElement) {
        List<WebElement> allContextAction = parentElement.findElements(By.className(CONTEXT_ACTIONS_CLASS));
        return allContextAction.stream().filter(WebElement::isDisplayed).findFirst()
                .orElseThrow(() -> new NoSuchElementException(NO_ACTION_EXCEPTION));
    }

    @Override
    public void callActionByLabel(String label) {
        throw new UnsupportedOperationException(UNSUPPORTED_EXCEPTION);
    }

    @Override
    public void callActionByLabel(String groupLabel, String actionLabel) {
        clickOnGroup(groupLabel);
        DropdownList.create(webDriver, webDriverWait).selectOption(actionLabel);
    }

    @Override
    public void callActionById(String id) {
        if (isElementPresent(webElement, By.id(id))) {
            clickWebElement(webElement.findElement(By.id(id)));
        } else {
            clickWithRetry(webElement.findElement(By.id(MORE_GROUP_ID)), By.className(DropdownList.PORTAL_CLASS));
            DropdownList.create(webDriver, webDriverWait).selectOptionById(id);
        }
    }

    @Override
    public void callActionById(String groupId, String actionId) {
        clickOnGroup(groupId);
        DropdownList.create(webDriver, webDriverWait).selectOptionById(actionId);
    }

    private void clickOnGroup(String group) {
        DelayUtils.waitForNestedElements(webDriverWait, webElement, String.format(GROUP_ALL_PATTERN, group, group));
        if (isElementPresent(webElement, By.xpath(String.format(GROUP_PATTERN, group, group)))) {
            clickWithRetry(webElement.findElement(By.xpath(String.format(GROUP_PATTERN, group, group))), By.className(DropdownList.PORTAL_CLASS));
        } else {
            clickWithRetry(webElement.findElement(By.id(MORE_GROUP_ID)), By.className(DropdownList.PORTAL_CLASS));
            DropdownList.create(webDriver, webDriverWait).selectOptionById(group);
        }
    }

    private void clickWebElement(WebElement webElement) {
        WebElementUtils.clickWebElement(webDriver, webElement);
    }

    private void clickWithRetry(WebElement elementToClick, By elementToWait) {
        WebElementUtils.clickWithRetry(webDriver, elementToClick, elementToWait);
    }

    private boolean isElementPresent(WebElement webElement, By by) {
        return WebElementUtils.isElementPresent(webElement, by);
    }
}

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
    private static final String GROUP_ALL_PATTERN = ".//div[@id='%s'] | .//div[text()= '%s'] | .//div[@id= '" + MORE_GROUP_ID + "']";
    private static final String ACTION_ALL_PATTERN = ".//a[@id='%s'] | .//div[@id= '" + MORE_GROUP_ID + "']";
    private static final String GROUP_PATTERN = ".//div[@id='%s'] | .//div[text()='%s']";
    private static final String UNSUPPORTED_EXCEPTION = "Method not implemented for Actions Container.";
    private static final String NO_ACTION_EXCEPTION = "No active Context Action.";
    private static final String TEXT_CONTENT_ATTRIBUTE = "textContent";
    private static final String ACTIONS_DROPDOWN_CLASS = "actionsDropdown";

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
        clickWebElement(getAction(By.id(id), String.format(ACTION_ALL_PATTERN, id)));
    }
    
    @Override
    public void callActionById(String groupId, String actionId) {
        clickOnGroup(groupId);
        DropdownList.create(webDriver, webDriverWait).selectOptionById(actionId);
    }
    
    public String getGroupActionLabel(String groupId) {
        String xpath = String.format(GROUP_PATTERN, groupId, groupId);
        return getAction(By.xpath(xpath), String.format(GROUP_ALL_PATTERN, groupId, groupId)).getAttribute(TEXT_CONTENT_ATTRIBUTE);
    }
    
    public String getActionLabel(String groupId, String actionId) {
        clickOnGroup(groupId);
        return DropdownList.create(webDriver, webDriverWait).getOptionLabel(actionId);
    }
    
    public String getActionLabel(String actionId) {
        return getAction(By.id(actionId), String.format(ACTION_ALL_PATTERN, actionId)).getAttribute(TEXT_CONTENT_ATTRIBUTE);
    }
    
    private void clickOnGroup(String group) {
        clickWithRetry(getAction(By.xpath(String.format(GROUP_PATTERN, group, group)), String.format(GROUP_ALL_PATTERN, group, group)),
                By.className(ACTIONS_DROPDOWN_CLASS));
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
    
    private WebElement getAction(By by, String xpathWait) {
        DelayUtils.waitForNestedElements(webDriverWait, webElement, xpathWait);
        if (isElementPresent(webElement, by)) {
            return webElement.findElement(by);
        } else {
            clickWithRetry(webElement.findElement(By.id(MORE_GROUP_ID)), By.className(ACTIONS_DROPDOWN_CLASS));
            return webDriver.findElement(By.className(ACTIONS_DROPDOWN_CLASS)).findElement(by);
            
        }
    }
}

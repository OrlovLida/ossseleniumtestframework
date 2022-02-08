package com.oss.framework.components.contextactions;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.DelayUtils;

public class ActionsContainer implements ActionsInterface {
    public static final String KEBAB_GROUP_ID = "frameworkCustomButtonsGroup";
    public static final String CREATE_GROUP_ID = "CREATE";
    public static final String EDIT_GROUP_ID = "EDIT";
    public static final String ASSIGN_GROUP_ID = "ASSIGN";
    public static final String OTHER_GROUP_ID = "OTHER";
    public static final String SHOW_ON_GROUP_ID = "NAVIGATION";
    private static final String MORE_GROUP_ID = "moreActions";
    private static final String CONTEXT_ACTIONS_CLASS = "actionsContainer";

    private final WebElement webElement;
    private final WebDriver webDriver;
    private final WebDriverWait webDriverWait;

    private ActionsContainer(WebElement activeContextActions, WebDriver webDriver, WebDriverWait webDriverWait) {
        this.webDriver = webDriver;
        this.webElement = activeContextActions;
        this.webDriverWait = webDriverWait;
    }

    public static ActionsContainer createFromParent(WebElement parentElement, WebDriver webDriver, WebDriverWait webDriverWait) {
        DelayUtils.waitBy(webDriverWait, By.className(CONTEXT_ACTIONS_CLASS));
        List<WebElement> allContextAction = parentElement.findElements(By.className(CONTEXT_ACTIONS_CLASS));
        WebElement activeContextActions = allContextAction.stream().filter(WebElement::isDisplayed).findFirst()
                .orElseThrow(() -> new RuntimeException("No active Context Action"));
        return new ActionsContainer(activeContextActions, webDriver, webDriverWait);
    }

    private static boolean isElementPresent(WebElement webElement, By by) {
        return !webElement.findElements(by).isEmpty();
    }

    @Override
    public void callActionByLabel(String label) {
        throw new UnsupportedOperationException("Method not implemented for Actions Container");
    }

    @Override
    public void callActionByLabel(String groupId, String actionLabel) {
        clickOnGroup(groupId);
        DropdownList.create(webDriver, webDriverWait).selectOption(actionLabel);
    }

    @Override
    public void callActionById(String id) {
        if (isElementPresent(webElement, By.id(id))) {
            clickOnWebElement(webDriver, webDriverWait, this.webElement.findElement(By.id(id)));
        } else {
            clickOnWebElement(webDriver, webDriverWait, this.webElement.findElement(By.id(MORE_GROUP_ID)));
            DropdownList.create(webDriver, webDriverWait).selectOptionById(id);
        }
    }

    @Override
    public void callActionById(String groupId, String actionId) {
        clickOnGroup(groupId);
        DropdownList.create(webDriver, webDriverWait).selectOptionById(actionId);
    }

    private void clickOnGroup(String groupId) {
        DelayUtils.waitForNestedElements(this.webDriverWait, this.webElement,
                ".//div[@id='" + groupId + "'] | .//div[@id= '" + MORE_GROUP_ID + "']");
        if (isElementPresent(webElement, By.id(groupId))) {
            clickOnWebElement(webDriver, webDriverWait, this.webElement.findElement(By.id(groupId)));
        } else {
            clickOnWebElement(webDriver, webDriverWait, this.webElement.findElement(By.id(MORE_GROUP_ID)));
            DropdownList.create(webDriver, webDriverWait).selectOptionById(groupId);
        }
    }

    private void clickOnWebElement(WebDriver webDriver, WebDriverWait webDriverWait, WebElement webElement) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", webElement);
        Actions actions = new Actions(webDriver);
        actions.moveToElement(webElement).build().perform();
        webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
        actions.click(webElement).build().perform();
    }

}

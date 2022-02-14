package com.oss.framework.components.contextactions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class OldActionsContainer implements ActionsInterface {

    public static final String KEBAB_GROUP_ID = "frameworkCustomEllipsis";
    private static final String WINDOW_TOOLBAR_XPATH = "//div[contains(@class, 'windowToolbar')]";
    private static final String CONTEXT_WINDOW_TOOLBAR_XPATH = "." + WINDOW_TOOLBAR_XPATH;
    private static final String MORE_GROUP_DATA_GROUP_ID = "frameworkCustomMore";
    private static final String GROUP_BY_DATA_GROUP_ID_PATTERN = ".//li[@data-group-id='%s']//button | .//li[@data-group-id='%s']//i";
    private static final String GROUP_XPATH = String.format(GROUP_BY_DATA_GROUP_ID_PATTERN, MORE_GROUP_DATA_GROUP_ID, MORE_GROUP_DATA_GROUP_ID);
    private static final String ACTION_BY_LABEL_PATTERN = ".//a[contains(text(),'%s')] | .//i[contains(@aria-label,'%s')] | .//button[text()='%s']";
    private static final String KEBAB_BUTTON_XPATH = "//li[@data-group-id='frameworkCustomEllipsis']";
    private static final String ACTION_BY_ID_PATTERN = ".//a[@" + CSSUtils.TEST_ID + "='%s'] | .//*[@id='%s'] | .//*[@data-widget-id='%s']";
    private static final String DROPDOWN_PATTERN = "//a[@class='dropdown']//div[text()='%s']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement toolbar;

    private OldActionsContainer(WebDriver driver, WebDriverWait wait, WebElement toolbar) {
        this.driver = driver;
        this.wait = wait;
        this.toolbar = toolbar;
    }

    public static OldActionsContainer createFromParent(WebDriver driver, WebDriverWait wait, WebElement parent) {
        DelayUtils.waitForNestedElements(wait, parent, WINDOW_TOOLBAR_XPATH);
        return new OldActionsContainer(driver, wait, getToolbar(parent));
    }

    public static OldActionsContainer createById(WebDriver driver, WebDriverWait wait, String actionContainerId) {
        DelayUtils.waitBy(wait, By.cssSelector("[" + CSSUtils.TEST_ID + "='" + actionContainerId + "']"));
        WebElement toolbar = driver.findElement(By.cssSelector("[" + CSSUtils.TEST_ID + "='" + actionContainerId + "']"));
        return new OldActionsContainer(driver, wait, toolbar);
    }

    private static WebElement getToolbar(WebElement parent) {
        if (isElementPresent(parent, By.xpath(CONTEXT_WINDOW_TOOLBAR_XPATH))) {
            return parent.findElement(By.xpath(CONTEXT_WINDOW_TOOLBAR_XPATH));
        }
        return parent.findElement(By.xpath("./../.." + WINDOW_TOOLBAR_XPATH));
    }

    private static boolean isElementPresent(WebElement webElement, By by) {
        return WebElementUtils.isElementPresent(webElement, by);
    }

    @Override
    public void callActionByLabel(String label) {
        DelayUtils.waitForNestedElements(wait, toolbar, String.format(ACTION_BY_LABEL_PATTERN, label, label, label));
        clickWebElement(toolbar.findElement(By.xpath(String.format(ACTION_BY_LABEL_PATTERN, label, label, label))));
    }

    @Override
    public void callActionByLabel(String groupLabel, String actionLabel) {
        String groupXpath = String.format(ACTION_BY_LABEL_PATTERN, groupLabel, groupLabel, groupLabel);
        DelayUtils.waitForNestedElements(wait, toolbar, groupXpath);
        clickWithRetry(toolbar.findElement(By.xpath(groupXpath)), By.className(DropdownList.PORTAL_CLASS));
        DropdownList.create(driver, wait).selectOption(actionLabel);
    }

    @Override
    public void callActionById(String id) {
        DelayUtils.waitForVisibility(wait, toolbar);
        String actionXpath = String.format(ACTION_BY_ID_PATTERN, id, id, id);
        DelayUtils.waitForPageToLoad(driver, wait);
        if (!isElementPresent(toolbar, By.xpath(actionXpath))) {
            clickWithRetry(toolbar.findElement(By.xpath(GROUP_XPATH)), By.xpath(actionXpath));
            clickWebElement(driver.findElement(By.xpath(actionXpath)));
            return;
        }
        clickActionByXpath(actionXpath);
    }

    @Override
    public void callActionById(String groupId, String actionId) {
        if (KEBAB_GROUP_ID.equals(groupId)) {
            callActionFromKebab(actionId);
            return;
        }
        String groupXpath = String.format(GROUP_BY_DATA_GROUP_ID_PATTERN, groupId, groupId);
        String actionXpath = String.format(ACTION_BY_ID_PATTERN, actionId, actionId, actionId);
        DelayUtils.waitForNestedElements(wait, toolbar, groupXpath);
        clickWithRetry(toolbar.findElement(By.xpath(groupXpath)), By.xpath(actionXpath));
        clickWebElement(driver.findElement(By.xpath(actionXpath)));
    }

    public void callActionById(String groupId, String innerGroupLabel, String actionId) {
        DelayUtils.waitForVisibility(wait, toolbar);
        String groupXpath = String.format(GROUP_BY_DATA_GROUP_ID_PATTERN, groupId, groupId);
        String actionXpath = String.format(ACTION_BY_ID_PATTERN, actionId, actionId, actionId);
        if (isElementPresent(toolbar, By.xpath(groupXpath))) {
            clickWithRetry(toolbar.findElement(By.xpath(groupXpath)), By.xpath(actionXpath));
        } else {
            String dropdownXpath = String.format(DROPDOWN_PATTERN, innerGroupLabel);
            clickWithRetry(toolbar.findElement(By.xpath(GROUP_XPATH)), By.xpath(dropdownXpath));
            moveToInnerActionByXpath(dropdownXpath);
        }
        clickActionByXpath(actionXpath);
    }

    private void callActionFromKebab(String actionId) {
        clickWithRetry(toolbar.findElement(By.xpath(KEBAB_BUTTON_XPATH)), By.className(DropdownList.PORTAL_CLASS));
        DropdownList.create(driver, wait).selectOptionById(actionId);
    }

    private void moveToInnerActionByXpath(String innerActionXpath) {
        DelayUtils.waitForNestedElements(wait, toolbar, innerActionXpath);
        Actions action = new Actions(driver);
        WebElement foundedElement = wait.until(ExpectedConditions.elementToBeClickable(toolbar.findElement(By.xpath(innerActionXpath))));
        action.moveToElement(foundedElement).perform();
    }

    private void clickActionByXpath(String xpath) {
        DelayUtils.waitForNestedElements(wait, toolbar, xpath);
        clickWebElement(toolbar.findElement(By.xpath(xpath)));
    }

    private void clickWebElement(WebElement webElement) {
        WebElementUtils.clickWebElement(driver, webElement);
    }

    private void clickWithRetry(WebElement elementToClick, By elementToWait) {
        WebElementUtils.clickWithRetry(driver, elementToClick, elementToWait);
    }
}

package com.oss.framework.components.contextactions;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class OldActionsContainer implements ActionsInterface {

    private static String WINDOW_TOOLBAR_CLASS = "windowToolbar";
    private static String MORE_GROUP_DATA_GROUP_ID = "__more-group";
    private static String GROUP_BY_DATA_GROUP_ID_XPATH = ".//li[@data-group-id='%s']//button";
    private static String INNER_GROUP_BY_DATA_ATTRIBUTE_NAME_XPATH = "//a[contains(@"+ CSSUtils.TEST_ID +", '%s')]";
    private static String ACTION_BY_DATA_ATTRIBUTE_NAME_OR_ID_XPATH = "//a[@"+ CSSUtils.TEST_ID +"='%s'] | //*[@id='%s']";

    public static OldActionsContainer createFromParent(WebDriver driver, WebDriverWait wait, WebElement parent) {
        DelayUtils.waitForNestedElements(wait, parent, "//div[contains(@class, '" + WINDOW_TOOLBAR_CLASS + "')]");
        if (isElementPresent(parent, By.xpath(".//div[contains(@class, '" + WINDOW_TOOLBAR_CLASS + "')]"))) {
            WebElement toolbar = parent.findElement(By.xpath(".//div[contains(@class, '" + WINDOW_TOOLBAR_CLASS + "')]"));
            return new OldActionsContainer(driver, wait, toolbar);
        } else {
            WebElement toolbar = parent.findElement(By.xpath("./../..//div[contains(@class, '" + WINDOW_TOOLBAR_CLASS + "')]"));
            return new OldActionsContainer(driver, wait, toolbar);
        }
    }

    public static OldActionsContainer createFromXPath(WebDriver driver, WebDriverWait wait, String xpath) {
        DelayUtils.waitByXPath(wait, xpath);
        WebElement toolbar = driver.findElement(By.xpath(xpath));
        return new OldActionsContainer(driver, wait, toolbar);
    }

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement toolbar;

    private OldActionsContainer(WebDriver driver, WebDriverWait wait, WebElement toolbar) {
        this.driver = driver;
        this.wait = wait;
        this.toolbar = toolbar;
    }

    @Override
    public void callAction(String actionId) {
        throw new RuntimeException("Method not implemented for the old actions container");
    }

    @Override
    public void callActionByLabel(String label) {
        DelayUtils.waitForNestedElements(wait, this.toolbar, ".//a[contains(text(),'" + label + "')] | .//i[contains(@aria-label,'" + label + "')]");
        WebElement action = this.toolbar.findElement(By.xpath(".//a[contains(text(),'" + label + "')] | .//i[contains(@aria-label,'" + label + "')]"));
        wait.until(ExpectedConditions.elementToBeClickable(action));
        action.click();
    }

    @Override
    public void callAction(String groupId, String actionLabel) {
        DelayUtils.waitForNestedElements(wait, toolbar, ".//li[@data-group-id='" + groupId + "']//button");
        wait.until(ExpectedConditions.elementToBeClickable(toolbar.findElement(By.xpath(".//li[@data-group-id='" + groupId + "']//button")))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='widgetLabel'][text()='" + actionLabel + "']"))).click();
    }

    @Override
    public void callActionByLabel(String groupLabel, String actionLabel) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void callActionById(String id) {
        DelayUtils.waitForVisibility(wait, toolbar);
        String actionXpath = String.format(ACTION_BY_DATA_ATTRIBUTE_NAME_OR_ID_XPATH, id, id);
        DelayUtils.waitForPageToLoad(driver,wait);
        if (isElementPresent(toolbar, By.xpath(actionXpath))) {
            clickActionByXpath(actionXpath);
        } else {
            clickGroupByXpath(String.format(GROUP_BY_DATA_GROUP_ID_XPATH, MORE_GROUP_DATA_GROUP_ID));
            clickActionByXpath(actionXpath);
        }
    }

    @Override
    public void callActionById(String groupId, String actionDataAttributeName) {
        clickGroupByXpath(String.format(GROUP_BY_DATA_GROUP_ID_XPATH, groupId));
        clickActionByXpath(String.format(ACTION_BY_DATA_ATTRIBUTE_NAME_OR_ID_XPATH, actionDataAttributeName, actionDataAttributeName));
    }

    public void callActionById(String groupId, String innerGroupDataAttributeName, String actionDataAttributeName) {
        DelayUtils.waitForVisibility(wait, toolbar);
        String groupXpath = String.format(GROUP_BY_DATA_GROUP_ID_XPATH, groupId);
        String actionXpath = String.format(ACTION_BY_DATA_ATTRIBUTE_NAME_OR_ID_XPATH, actionDataAttributeName, actionDataAttributeName);
        if (isElementPresent(toolbar, By.xpath(groupXpath))) {
            clickGroupByXpath(groupXpath);
            clickActionByXpath(actionXpath);
        } else {
            clickGroupByXpath(String.format(GROUP_BY_DATA_GROUP_ID_XPATH, MORE_GROUP_DATA_GROUP_ID));
            moveToInnerActionByXpath(String.format(ACTION_BY_DATA_ATTRIBUTE_NAME_OR_ID_XPATH, innerGroupDataAttributeName, innerGroupDataAttributeName));
            clickActionByXpath(actionXpath);
        }
    }

    private void clickGroupByXpath(String groupXpath){
        DelayUtils.waitForNestedElements(wait, toolbar, groupXpath);
        wait.until(ExpectedConditions.elementToBeClickable(this.toolbar.findElement(By.xpath(groupXpath)))).click();
    }

    private void moveToInnerActionByXpath(String innerActionXpath){
        DelayUtils.waitForNestedElements(wait, toolbar, innerActionXpath);
        Actions action = new Actions(driver);
        WebElement foundedElement = wait.until(ExpectedConditions.elementToBeClickable(toolbar.findElement(By.xpath(innerActionXpath))));
        action.moveToElement(foundedElement).perform();
    }

    private void clickActionByXpath(String xpath){
        DelayUtils.waitForNestedElements(wait, toolbar, xpath);
        Actions action = new Actions(driver);
        action.moveToElement(wait.until(ExpectedConditions.elementToBeClickable(
                toolbar.findElement(By.xpath(xpath)))))
                .click()
                .perform();
    }

    private static boolean isElementPresent( WebElement webElement, By by) {
        try {
            webElement.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    }


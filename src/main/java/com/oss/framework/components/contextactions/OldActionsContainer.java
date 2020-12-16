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
        String actionXpath = String.format(ACTION_BY_DATA_ATTRIBUTE_NAME_OR_ID_XPATH, id, id);
        if (isElementPresent(toolbar, By.xpath(actionXpath))) {
            clickActionByXpath(actionXpath);
        } else {
            wait.until(ExpectedConditions.elementToBeClickable(this.toolbar.findElement(By.xpath(String.format(GROUP_BY_DATA_GROUP_ID_XPATH, MORE_GROUP_DATA_GROUP_ID))))).click();
            DelayUtils.waitForNestedElements(wait, toolbar, actionXpath);
            clickActionByXpath(actionXpath);
        }
    }

    private void clickActionByXpath(String xpath){
        Actions action = new Actions(driver);
        action.moveToElement(wait.until(ExpectedConditions.elementToBeClickable(
                toolbar.findElement(By.xpath(xpath)))))
                .click()
                .perform();
    }

    @Override
    public void callActionById(String groupId, String actionDataAttributeName) {
        DelayUtils.waitForNestedElements(wait, toolbar, String.format(GROUP_BY_DATA_GROUP_ID_XPATH, groupId));
        wait.until(ExpectedConditions.elementToBeClickable(toolbar.findElement(By.xpath(String.format(GROUP_BY_DATA_GROUP_ID_XPATH, groupId))))).click();
        DelayUtils.waitForNestedElements(wait, toolbar, String.format(ACTION_BY_DATA_ATTRIBUTE_NAME_OR_ID_XPATH, actionDataAttributeName, actionDataAttributeName));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(String.format(ACTION_BY_DATA_ATTRIBUTE_NAME_OR_ID_XPATH, actionDataAttributeName, actionDataAttributeName)))).click();
    }

    public void callActionById(String groupId, String innerGroupId, String actionDataAttributeName) {
        DelayUtils.waitForNestedElements(wait, toolbar, String.format(GROUP_BY_DATA_GROUP_ID_XPATH, groupId));
        wait.until(ExpectedConditions.elementToBeClickable(toolbar.findElement(By.xpath(String.format(GROUP_BY_DATA_GROUP_ID_XPATH, groupId))))).click();
        Actions action = new Actions(driver);
        WebElement foundedElement = wait.until(ExpectedConditions.elementToBeClickable(toolbar.findElement(By.xpath(String.format(INNER_GROUP_BY_DATA_ATTRIBUTE_NAME_XPATH, innerGroupId)))));
        action.moveToElement(foundedElement).perform();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(String.format(ACTION_BY_DATA_ATTRIBUTE_NAME_OR_ID_XPATH, actionDataAttributeName, actionDataAttributeName)))).click();
    }

    private static boolean isElementPresent(WebElement webElement, By by) {
        try {
            webElement.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}

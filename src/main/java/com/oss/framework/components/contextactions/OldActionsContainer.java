package com.oss.framework.components.contextactions;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class OldActionsContainer implements ActionsInterface {

    private static final String WINDOW_TOOLBAR_XPATH = "//div[contains(@class, 'windowToolbar')]";
    private static final String CONTEXT_WINDOW_TOOLBAR_XPATH = "." + WINDOW_TOOLBAR_XPATH;
    private static final String MAIN_WINDOW_TOOLBAR = "//div[@class='OssWindow']//div[@class='windowHeader']//div[@class='windowToolbar']";
    private static final String MORE_GROUP_DATA_GROUP_ID = "frameworkCustomMore";
    private static final String GROUP_BY_DATA_GROUP_ID_XPATH = ".//li[@data-group-id='%s']//button";
    private static final String ACTION_BY_LABEL_XPATH = ".//a[contains(text(),'%s')] | .//i[contains(@aria-label,'%s')]";
    private static final String METHOD_NOT_IMPLEMENTED = "Method not implemented for the old actions container";
    private static final String KEBAB_BUTTON_XPATH = "//li[@data-group-id='frameworkCustomEllipsis']";
    public static final String KEBAB_GROUP_ID = "frameworkCustomEllipsis";
    private static final String ACTION_BY_DATA_ATTRIBUTE_NAME_OR_ID_XPATH = "//a[@" + CSSUtils.TEST_ID + "='%s'] | //*[@id='%s']";
    private static final String DROPDOWN_XPATH = "//a[@class='dropdown']//div[text()='%s']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement toolbar;

    public static OldActionsContainer createFromParent(WebDriver driver, WebDriverWait wait, WebElement parent) {
        DelayUtils.waitForNestedElements(wait, parent, WINDOW_TOOLBAR_XPATH);
        if (isElementPresent(parent, By.xpath(CONTEXT_WINDOW_TOOLBAR_XPATH))) {
            WebElement toolbar = parent.findElement(By.xpath(CONTEXT_WINDOW_TOOLBAR_XPATH));
            return new OldActionsContainer(driver, wait, toolbar);
        } else {
            WebElement toolbar = parent.findElement(By.xpath("./../.." + WINDOW_TOOLBAR_XPATH));
            return new OldActionsContainer(driver, wait, toolbar);
        }
    }

    //TODO to be changed after OSSWEB-11953
    public static OldActionsContainer createForMainWindow(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement toolbar = driver.findElement(By.xpath(MAIN_WINDOW_TOOLBAR));
        return new OldActionsContainer(driver, wait, toolbar);
    }

    private OldActionsContainer(WebDriver driver, WebDriverWait wait, WebElement toolbar) {
        this.driver = driver;
        this.wait = wait;
        this.toolbar = toolbar;
    }

    @Override
    public void callActionByLabel(String label) {
        DelayUtils.waitForNestedElements(wait, this.toolbar,
                String.format(ACTION_BY_LABEL_XPATH, label, label));
        clickOnWebElement(driver, wait, this.toolbar.findElement(By.xpath(String.format(ACTION_BY_LABEL_XPATH, label, label))));
    }

    @Override
    public void callActionByLabel(String groupLabel, String actionLabel) {
        throw new UnsupportedOperationException(METHOD_NOT_IMPLEMENTED);
    }

    @Override
    public void callActionById(String id) {
        DelayUtils.waitForVisibility(wait, toolbar);
        String actionXpath = String.format(ACTION_BY_DATA_ATTRIBUTE_NAME_OR_ID_XPATH, id, id);
        DelayUtils.waitForPageToLoad(driver, wait);
        if (!isElementPresent(toolbar, By.xpath(actionXpath))) {
            clickActionByXpath(String.format(GROUP_BY_DATA_GROUP_ID_XPATH, MORE_GROUP_DATA_GROUP_ID));
        }
        clickActionByXpath(actionXpath);
    }

    @Override
    public void callActionById(String groupId, String actionId) {
        if (KEBAB_GROUP_ID.equals(groupId)) {
            callActionFromKebab(actionId);
            return;
        }
        String groupXpath = String.format(GROUP_BY_DATA_GROUP_ID_XPATH, groupId);
        String actionXpath = String.format(ACTION_BY_DATA_ATTRIBUTE_NAME_OR_ID_XPATH, actionId, actionId);
        DelayUtils.waitForNestedElements(wait, toolbar, groupXpath);
        clickOnWebElement(driver, wait, toolbar.findElement(By.xpath(groupXpath)));
        clickOnWebElement(driver, wait, driver.findElement(By.xpath(actionXpath)));
    }

    public void callActionById(String groupId, String innerGroupLabel, String actionDataAttributeName) {
        DelayUtils.waitForVisibility(wait, toolbar);
        String groupXpath = String.format(GROUP_BY_DATA_GROUP_ID_XPATH, groupId);
        String actionXpath = String.format(ACTION_BY_DATA_ATTRIBUTE_NAME_OR_ID_XPATH, actionDataAttributeName, actionDataAttributeName);
        if (isElementPresent(toolbar, By.xpath(groupXpath))) {
            clickActionByXpath(groupXpath);
        } else {
            clickActionByXpath(String.format(GROUP_BY_DATA_GROUP_ID_XPATH, MORE_GROUP_DATA_GROUP_ID));
            moveToInnerActionByXpath(String.format(DROPDOWN_XPATH, innerGroupLabel));
        }
        clickActionByXpath(actionXpath);
    }

    private void callActionFromKebab(String actionId) {
        clickOnWebElement(driver, wait, this.toolbar.findElement(By.xpath(KEBAB_BUTTON_XPATH)));
        DropdownList.create(driver, wait).selectOptionWithId(actionId);
    }

    private void moveToInnerActionByXpath(String innerActionXpath) {
        DelayUtils.waitForNestedElements(wait, toolbar, innerActionXpath);
        Actions action = new Actions(driver);
        WebElement foundedElement = wait.until(ExpectedConditions.elementToBeClickable(toolbar.findElement(By.xpath(innerActionXpath))));
        action.moveToElement(foundedElement).perform();
    }

    private void clickActionByXpath(String xpath) {
        DelayUtils.waitForNestedElements(wait, toolbar, xpath);
        clickOnWebElement(driver, wait, this.toolbar.findElement(By.xpath(xpath)));
    }

    private static boolean isElementPresent(WebElement webElement, By by) {
        try {
            webElement.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private static void clickOnWebElement(WebDriver webDriver, WebDriverWait webDriverWait, WebElement webElement) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", webElement);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(webElement));
        Actions actions = new Actions(webDriver);
        actions.moveToElement(webElement).click(webElement).build().perform();
    }
}

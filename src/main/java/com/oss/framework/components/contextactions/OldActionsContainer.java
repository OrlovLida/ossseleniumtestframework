package com.oss.framework.components.contextactions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class OldActionsContainer implements ActionsInterface {

    private static String WINDOW_TOOLBAR_CLASS = "windowToolbar";

    public static ActionsInterface createFromParent(WebDriver driver, WebDriverWait wait, WebElement parent) {
        DelayUtils.waitForNestedElements(wait, parent, "//div[contains(@class, '" + WINDOW_TOOLBAR_CLASS + "')]");
        WebElement toolbar = parent.findElement(By.className(WINDOW_TOOLBAR_CLASS));
        return new OldActionsContainer(driver, wait, toolbar);
    }

    public static ActionsInterface createFromWidget(WebDriver driver, WebDriverWait wait, WebElement widget) {
        DelayUtils.waitForNestedElements(wait, widget, "//div[contains(@class, '" + WINDOW_TOOLBAR_CLASS + "')]");
        WebElement toolbar = widget.findElement(By.xpath("./../..//div[contains(@class, '" + WINDOW_TOOLBAR_CLASS + "')]"));
        return new OldActionsContainer(driver, wait, toolbar);
    }

    private static ActionsInterface createFromXPath(WebDriver driver, WebDriverWait wait, String xpath) {
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
    public void callAction(String groupId, String actionId) {
        throw new RuntimeException("Method not implemented for the old actions container");
    }

    @Override
    public void callActionByLabel(String groupLabel, String actionLabel) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void callActionById(String id) {
        DelayUtils.waitForNestedElements(wait,toolbar,"//*[@data-attributename='"+id+"'] | //*[@id='"+id+"'] ");
        Actions action = new Actions(driver);
        action.moveToElement(wait.until(ExpectedConditions.elementToBeClickable(
                toolbar.findElement(By.xpath("//*[@data-attributename='"+id+"'] | //*[@id='"+id+"'] ")))))
                .click()
                .perform();
    }


    @Override
    public void callActionById(String groupLabel, String actionId) {
        throw new RuntimeException("Not implemented yet");
    }
}

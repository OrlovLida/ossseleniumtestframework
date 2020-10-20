package com.oss.framework.listwidget;

import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ListGroup {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final String LIST_GROUP_CLASS = "list-group";
    private static final String ITEM_LIST_CLASS = "list-group-item";
    private static final String SELECTED_ITEM_CLASS = ITEM_LIST_CLASS + " active";

    public static ListGroup create(WebDriver driver, WebDriverWait wait) {
        return new ListGroup(driver, wait);
    }

    private ListGroup(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    private WebElement getListGroup() {
        DelayUtils.waitByXPath(wait, "//*[@class = '" + LIST_GROUP_CLASS + "']");
        return driver.findElement(By.className(LIST_GROUP_CLASS));
    }

    public void selectItemByName(String itemName) {
        if (notSelected(itemName)) {
            Actions action = new Actions(driver);
            action.moveToElement(getListGroup().findElement(By.xpath("//button[@class='" + ITEM_LIST_CLASS + "' and text() = '" + itemName + "']"))).click();
        }
    }

    public boolean notVisible(String itemName) {
        return getListGroup().findElements(By.xpath("//button[@class='" + ITEM_LIST_CLASS + "' and text() = '" + itemName + "']")).isEmpty();
    }

    private boolean notSelected(String itemName) {
        return getListGroup().findElements(By.xpath("//button[@class='" + SELECTED_ITEM_CLASS + "' and text() = '" + itemName + "']")).isEmpty();
    }
}
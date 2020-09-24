package com.oss.framework.listwidget;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ListGroup {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webelement;

    private String LIST_GROUP_CLASS = "list-group";
    private String ITEM_LIST_CLASS = "list-group-item";


    public static ListGroup create(WebDriver driver, WebDriverWait wait) {
        return new ListGroup(driver, wait);
    }

    private ListGroup(WebDriver driver, WebDriverWait wait){
        this.driver = driver;
        this.wait = wait;
        this.webelement = driver.findElement(By.className(LIST_GROUP_CLASS));
    }

    public void selectItemByName(String name){
        Actions action = new Actions(driver);
        action.moveToElement(webelement.findElement(By.xpath("//button[@class='" + ITEM_LIST_CLASS + "' and text() = '" + name + "']"))).click();
    }
}
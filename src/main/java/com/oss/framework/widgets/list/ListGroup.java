package com.oss.framework.widgets.list;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class ListGroup {
    private static final String LIST_GROUP_CLASS = "list-group";
    private static final String ITEM_LIST_CLASS = "list-group-item";
    private final WebDriver driver;
    
    private ListGroup(WebDriver driver) {
        this.driver = driver;
    }
    
    public static ListGroup create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitBy(wait, By.className(LIST_GROUP_CLASS));
        return new ListGroup(driver);
    }
    
    public void selectItem(String itemName) {
        Item item = getItem(itemName);
        if (!item.isSelected()) {
            item.click();
        }
    }
    
    public void callAction(String itemName, String actionLabel) {
        getItem(itemName).callAction(actionLabel);
    }

    public List<String> getItemsName(){
        return driver.findElements(By.className(ITEM_LIST_CLASS)).stream().map(item-> new Item(driver, item)).map(Item::getValue).collect(Collectors.toList());
    }
    
    private Item getItem(String itemName) {
        return Item.create(driver, itemName);
    }
    
    private static class Item {
        
        private static final String ACTIVE = "active";
        private static final String CLASS = "class";
        private static final String CANNOT_ITEM_WITH_NAME_EXCEPTION = "Cannot item with name:";
        private WebDriver driver;
        private WebElement itemElement;
        
        private Item(WebDriver driver, WebElement item) {
            
            this.driver = driver;
            this.itemElement = item;
        }
        
        private static Item create(WebDriver driver, String itemName) {
            WebElement itemList = driver.findElements(By.className(ITEM_LIST_CLASS)).stream()
                    .filter(item -> item.getText().equals(itemName))
                    .findFirst().orElseThrow(() -> new NoSuchElementException(CANNOT_ITEM_WITH_NAME_EXCEPTION + itemName));

            return new Item(driver, itemList);
        }

        private static Item create(WebDriver driver, WebElement itemElement){
            return new Item(driver, itemElement);
        }
        
        private void callAction(String actionLabel) {
            Actions action = new Actions(driver);
            WebElement icon = itemElement.findElement(By.cssSelector(".icon-button[title='" + actionLabel + "']"));
            action.moveToElement(itemElement).pause(500).moveToElement(icon).click().build().perform();
        }

        private String getValue(){
            return itemElement.getText();
        }
        
        private boolean isSelected() {
            return itemElement.getAttribute(CLASS).contains(ACTIVE);
        }
        
        private void click() {
            itemElement.click();
        }
    }
}

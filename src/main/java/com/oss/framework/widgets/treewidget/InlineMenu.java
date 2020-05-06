package com.oss.framework.widgets.treewidget;

import com.oss.framework.utils.DelayUtils;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class InlineMenu {

    //TODO: reuse action class
    private final static String ACTIONS_LIST = "//div[@class='actionsList']/a";
    private final static String INLINE_MENU = "//div[@class='actionsDropdown']";

    private final WebDriver driver;
    private final WebElement webElement;

    public static InlineMenu create(WebDriver driver) { return new InlineMenu(driver); }
    private InlineMenu (WebDriver driver) {
        this.driver = driver;
        this.webElement = driver.findElement(By.className("actionsDropdown"));
    }

    public Boolean isActionListDisplayed() {
        DelayUtils.sleep(1000);
        return getActionsList().size() > 0;
    }

    private WebElement getInlineMenu() {return this.webElement.findElement(By.xpath(INLINE_MENU));}

    private List<WebElement> getActionsList() { return this.webElement.findElements(By.xpath(ACTIONS_LIST)); }

    private WebElement getInlineAction(String actionName) {return this.webElement.findElement(By.xpath(".//div[@class='actionsList']/a[contains(text(),'"+actionName+"')]"));}
}

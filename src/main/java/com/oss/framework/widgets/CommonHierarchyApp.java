package com.oss.framework.widgets;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.LocatingUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Ewa Frączek
 */

public class CommonHierarchyApp extends Widget {

    public static CommonHierarchyApp createByClass(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        return new CommonHierarchyApp(driver, widgetClass, webDriverWait);
    }

    private CommonHierarchyApp(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        super(driver, widgetClass, webDriverWait);
    }

    public void setFirstObjectInHierarchy(String value){
        setValue(1, value);
        selectObject(value);
    }

    public void setNextObjectInHierarchy(String objectName){
        selectObject(objectName);
    }

    private void setValue(int hierarchyLevel, String value) {
        webElement.click();
        DelayUtils.sleep();//wait for cursor
        webElement.findElement(By.xpath("(.//input)["+hierarchyLevel+"]"))
                .sendKeys(value);
        this.webElement.findElement(By.xpath("(.//button)")).click();
        this.webElement.findElement(By.xpath("(.//button)")).click();
        DelayUtils.sleep();
    }

    private void selectObject(String value){
        String searchResultXpath = "(//div[@class='CommonHierarchyApp']//span[text()='"+value+"'])";
        LocatingUtils.waitUsingXpath(searchResultXpath, webDriverWait);
        webElement.findElement(By.xpath(searchResultXpath)).click();
    }
}

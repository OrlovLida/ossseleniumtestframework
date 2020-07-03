/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.mainheader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

/**
 * @author Gabriela Kasza
 */
public class MainHeader {
    public static MainHeader create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, "//div[@class='oss-header-cont']");
        WebElement toolbar = driver.findElement(By.xpath("//div[@class='oss-header-cont']"));
        return new MainHeader(driver, wait, toolbar);
    }
    
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement toolbar;
    
    private MainHeader(WebDriver driver, WebDriverWait wait, WebElement toolbar) {
        this.driver = driver;
        this.wait = wait;
        this.toolbar = toolbar;
    }
    
    public void callActionByLabel(String label) {
        {
            DelayUtils.waitForNestedElements(wait, this.toolbar, ".//div[@class='oss-header-toolbar']");
            WebElement headerToolbar = this.toolbar.findElement(By.xpath(".//div[@class='oss-header-toolbar']"));
            DelayUtils.waitForNestedElements(wait, headerToolbar, "//a[contains(@class,'" + label + "')] | //div[contains(@class,'"+ label+"')]");
            WebElement action = headerToolbar.findElement(By.xpath("//a[contains(@class,'" + label + "')] | //div[contains(@class,'"+ label+"')]"));
            action.click();
            
        }
    }
    
}

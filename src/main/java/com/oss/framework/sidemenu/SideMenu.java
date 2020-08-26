/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.sidemenu;

import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Kamil Szota
 */
public class SideMenu {
    private static final String TAB_NAME_PATH_PATTERN = "//span[text()='%s']";
    private static final String PRODUCT_NAME_PATH_PATTERN = "//div[@class='subMenu isExpanded']//span[text()='%s']";
    private static final String TECHNOLOGY_NAME_PATH_PATTERN = "//span[text()='%s']";
    private final WebDriver driver;
    private final WebDriverWait wait;

    public SideMenu(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void goToTabByLeftSideMenu(String tab) {
        String tabXpath = String.format(TAB_NAME_PATH_PATTERN, tab);
        DelayUtils.waitByXPath(wait, tabXpath);
        driver.findElement(By.xpath(tabXpath)).click();
    }

    public void goToProductByLeftSideMenu(String tab, String product) {
        goToTabByLeftSideMenu(tab);
        String productXpath = String.format(PRODUCT_NAME_PATH_PATTERN, product);
        DelayUtils.waitByXPath(wait, productXpath);
        driver.findElement(By.xpath(productXpath)).click();
    }

    public void goToTechnologyByLeftSideMenu(String tab, String product, String technology) {
        goToProductByLeftSideMenu(tab, product);
        String technologyXpath = String.format(TECHNOLOGY_NAME_PATH_PATTERN, technology);
        DelayUtils.waitByXPath(wait, technologyXpath);
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.xpath(technologyXpath)))
                .sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN)
                .pause(500).build().perform();
        actions.moveToElement(driver.findElement(By.xpath(technologyXpath)))
                .click().build().perform();
    }
}

/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.widgets.sidemenu;

import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Kamil Szota
 */
public class SideMenu {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public SideMenu(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void goToTabByLeftSideMenu(String tab) {

        String xPath = "//span[text()='%s']";
        String tabXpath = String.format(xPath, tab);
        WebElement tabElement = driver.findElement(By.xpath(tabXpath));
        DelayUtils.waitByElement(wait, tabElement);
        tabElement.click();

    }

    public void goToProductByLeftSideMenu(String tab, String product) {

        goToTabByLeftSideMenu(tab);

        String xPath2 = "//div[@class='subMenu isExpanded']//span[text()='%s']";
        String productXpath = String.format(xPath2, product);
        WebElement productElement = driver.findElement(By.xpath(productXpath));
        DelayUtils.waitByElement(wait, productElement);
        productElement.click();

    }

    public void goToTechnologyByLeftSideMenu(String tab, String product, String technology) {

        goToProductByLeftSideMenu(tab, product);
        DelayUtils.sleep(500);

        String xPath3 = "//span[text()='%s']";
        String technologyXpath = String.format(xPath3, technology);
        WebElement technologyElement = driver.findElement(By.xpath(technologyXpath));
        DelayUtils.waitByElement(wait, technologyElement);
        technologyElement.click();

    }
}

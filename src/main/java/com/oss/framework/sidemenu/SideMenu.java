/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.sidemenu;

import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
        WebElement tabElement = driver.findElement(By.xpath(tabXpath));
        DelayUtils.waitByElement(wait, tabElement);
        tabElement.click();
    }

    public void goToProductByLeftSideMenu(String tab, String product) {
        goToTabByLeftSideMenu(tab);

        String productXpath = String.format(PRODUCT_NAME_PATH_PATTERN, product);
        WebElement productElement = driver.findElement(By.xpath(productXpath));
        DelayUtils.waitByElement(wait, productElement);
        productElement.click();
    }

    public void goToTechnologyByLeftSideMenu(String tab, String product, String technology) {
        goToProductByLeftSideMenu(tab, product);
        DelayUtils.sleep(500);

        String technologyXpath = String.format(TECHNOLOGY_NAME_PATH_PATTERN, technology);
        WebElement technologyElement = driver.findElement(By.xpath(technologyXpath));
        DelayUtils.waitByElement(wait, technologyElement);
        technologyElement.click();
    }
}

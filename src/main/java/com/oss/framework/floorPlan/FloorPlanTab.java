package com.oss.framework.floorPlan;

import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FloorPlanTab {
    private static final String TABS_LIST_XPATH = ".//div[contains(@class, tabs-list)]";
    private static final String TAB_PATTERN = ".//span[contains(text(),'%1$s')] | .//div[@class='tab-label'][contains(text(),'%1$s')]";

    protected final WebDriver driver;
    protected final WebDriverWait webDriverWait;
    protected final String id;

    private FloorPlanTab(WebDriver driver, WebDriverWait wait, String id) {
        this.driver = driver;
        this.webDriverWait = wait;
        this.id = id;
    }

    public static FloorPlanTab createById(WebDriver driver, WebDriverWait wait, String id) {
        return new FloorPlanTab(driver, wait, id);
    }

    public void selectTabByLabel(String tabLabel) {
        DelayUtils.waitByXPath(webDriverWait, TABS_LIST_XPATH);
        String xPathForTab = String.format(TAB_PATTERN, tabLabel);
        WebElement tabToSelect = driver.findElement(By.xpath(xPathForTab));
        webDriverWait.until(ExpectedConditions.elementToBeClickable(tabToSelect));
        tabToSelect.click();
    }
}

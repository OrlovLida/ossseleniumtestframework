package com.oss.framework.mainheader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class Share {
    private static final String LINK_XPATH = ".//input[@id='copyField']";
    private WebDriver driver;
    private WebDriverWait wait;

    private Share(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static Share create(WebDriver driver, WebDriverWait wait) {
        return new Share(driver, wait);
    }

    public String copyLink() {
        ToolbarWidget toolbar = ToolbarWidget.create(driver, wait);
        toolbar.openSharePanel();
        DelayUtils.waitByXPath(wait, LINK_XPATH);
        return driver.findElement(By.xpath(LINK_XPATH)).getAttribute("value");
    }
}

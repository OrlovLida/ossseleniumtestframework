package com.oss.framework.mainheader;

import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Share {
    private WebDriver driver;
    private WebDriverWait wait;

    private static final String LINK_XPATH = ".//input[@id='copyField']";

    public static Share create(WebDriver driver, WebDriverWait wait) {
        return new Share(driver, wait);
    }

    private Share(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public String copyLink() {
        ToolbarWidget toolbar = ToolbarWidget.create(driver, wait);
        toolbar.openSharePanel();
        DelayUtils.waitByXPath(wait, LINK_XPATH);
        return driver.findElement(By.xpath(LINK_XPATH)).getAttribute("value");
    }
}

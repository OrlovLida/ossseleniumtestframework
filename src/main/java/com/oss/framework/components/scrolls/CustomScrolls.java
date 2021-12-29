package com.oss.framework.components.scrolls;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class CustomScrolls {
    private static final String SCROLLS_XPATH = ".//div[contains(@class, 'custom-scrollbars')]";

    private final WebElement scrolls;
    private final WebDriverWait wait;
    private final WebDriver driver;

    private CustomScrolls(WebDriver driver, WebDriverWait wait, WebElement scrolls) {
        this.driver = driver;
        this.wait = wait;
        this.scrolls = scrolls;
    }

    public static CustomScrolls create(WebDriver driver, WebDriverWait wait, WebElement parent) {
        DelayUtils.waitForNestedElements(wait, parent, SCROLLS_XPATH);
        WebElement scrolls = parent.findElement(By.xpath(SCROLLS_XPATH));
        return new CustomScrolls(driver, wait, scrolls);
    }

    public int getHorizontalBarWidth() {
        return getHorizontalBar().getSize().width;
    }

    public void scrollHorizontally(int offset) {
        Actions action = new Actions(this.driver);
        action.moveToElement(getHorizontalBar()).clickAndHold();
        action.moveByOffset(offset, 0);
        action.release();
        action.perform();
    }

    public int getTranslateXValue() {
        String barStyle = getHorizontalBar().getAttribute("style");
        String translateX = barStyle.split("translateX\\(")[1];
        return Integer.parseInt(translateX.split("px")[0]);
    }

    public void scrollVertically(int offset) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int getHorizontalScrollWidth() {
        return getHorizontalScroll().getSize().width;
    }

    private WebElement getHorizontalScroll() {
        List<WebElement> divs = scrolls.findElements(By.xpath("./div"));
        return divs.get(1);
    }

    private WebElement getHorizontalBar() {
        WebElement horizontal = getHorizontalScroll();
        return horizontal.findElement(By.xpath("./div"));
    }
}

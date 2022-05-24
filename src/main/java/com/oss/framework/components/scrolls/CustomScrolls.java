package com.oss.framework.components.scrolls;

import java.math.BigDecimal;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class CustomScrolls {
    private static final String SCROLLS_XPATH = ".//div[contains(@class, 'custom-scrollbars')]";
    private static final String DIV_TAG_XPATH = "./div";
    
    private final WebElement scrolls;
    private final WebDriver driver;
    
    private CustomScrolls(WebDriver driver, WebElement scrolls) {
        this.driver = driver;
        this.scrolls = scrolls;
    }
    
    public static CustomScrolls create(WebDriver driver, WebDriverWait wait, WebElement parent) {
        DelayUtils.waitForNestedElements(wait, parent, SCROLLS_XPATH);
        WebElement scrolls = parent.findElement(By.xpath(SCROLLS_XPATH));
        return new CustomScrolls(driver, scrolls);
    }
    
    public int getHorizontalBarWidth() {
        return getHorizontalBar().getSize().width;
    }
    
    public int getVerticalBarHeight() {
        return getVerticalBar().getSize().height;
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
        if (barStyle.contains("translateX")) {
            String translateX = barStyle.split("translateX\\(")[1];
            return BigDecimal.valueOf(Double.parseDouble(translateX.split("px")[0])).toBigInteger().intValue();
        }
        return 0;
    }
    
    public int getTranslateYValue() {
        String barStyle = getVerticalBar().getAttribute("style");
        if (barStyle.contains("translateY")) {
            String translateX = barStyle.split("translateY\\(")[1];
            return BigDecimal.valueOf(Double.parseDouble(translateX.split("px")[0])).toBigInteger().intValue();
        }
        return 0;
    }
    
    public void scrollVertically(int offset) {
        Actions action = new Actions(this.driver);
        action.moveToElement(getVerticalBar()).clickAndHold();
        action.moveByOffset(0, offset);
        action.release();
        action.perform();
    }
    
    public int getHorizontalScrollWidth() {
        return getHorizontalScroll().getSize().width;
    }
    
    private WebElement getHorizontalScroll() {
        List<WebElement> divs = scrolls.findElements(By.xpath(DIV_TAG_XPATH));
        return divs.get(1);
    }
    
    private WebElement getVerticalScroll() {
        List<WebElement> divs = scrolls.findElements(By.xpath(DIV_TAG_XPATH));
        return divs.get(2);
    }
    
    private WebElement getHorizontalBar() {
        WebElement horizontal = getHorizontalScroll();
        return horizontal.findElement(By.xpath(DIV_TAG_XPATH));
    }
    
    private WebElement getVerticalBar() {
        WebElement verticalScroll = getVerticalScroll();
        return verticalScroll.findElement(By.xpath(DIV_TAG_XPATH));
    }
}

package com.oss.framework.components.portals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ExpandedTextTooltip {

    private static final String EXPANDED_TEXT = ".//div[contains(@class,'OSSRichText')]";

    protected final WebDriver driver;
    protected final WebElement webElement;

    public ExpandedTextTooltip(WebDriver driver) {
        this.driver = driver;
        this.webElement = driver.findElement(By.className("longTextBoxComponent"));
    }

    public static ExpandedTextTooltip create(WebDriver driver) {
        return new ExpandedTextTooltip(driver);
    }

    public WebElement getExpandedTextElement() {
        return this.webElement.findElement(By.xpath(EXPANDED_TEXT));
    }

    public String getExpandedText() {
        return getExpandedTextElement().getText();
    }
}

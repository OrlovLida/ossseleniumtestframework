package com.oss.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;

public class BasePage {
    protected final WebDriver driver;
    public final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 50);
        PageFactory.initElements(driver, this);
    }

    public void waitForVisibility(WebElement webelement) {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(webelement));
    }

    protected void waitForVisibility(List<WebElement> webElements) {
        wait.until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    protected void waitforclickability(WebElement webelement) {
        wait.until(ExpectedConditions.elementToBeClickable(webelement));
    }

    public void waitForComponent(String xpath) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    public void waitForBy(By by) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected String randomInteger(int length) {
        Random rand = new Random();
        String random = "";

        for (int i = 0; i < length; i++) {
            random += rand.nextInt(9);
        }
        return random;
    }
}

package com.oss.framework.components.systemMessage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

public class SystemMessage implements SystemMessageInterface {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement systemMessage;

    private SystemMessage(WebDriver driver, WebDriverWait wait, WebElement systemMessage) {
        this.driver = driver;
        this.wait = wait;
        this.systemMessage = systemMessage;
    }

    public static SystemMessageInterface create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, "//div[contains(@class,'systemMessagesContainer')]");
        WebElement prompt = driver.findElement(By.xpath("//div[contains(@class,'systemMessagesContainer')]"));
        return new SystemMessage(driver, wait, prompt);

    }

    @Override
    public String getMessage() {
        DelayUtils.waitByXPath(wait, ".//div[contains(@class, 'textContainer')]");
        WebElement windowContent = driver.findElement(By.xpath(".//div[contains(@class, 'textContainer')]"));
        WebElement message = windowContent.findElement(By.xpath("//div[contains(@class, 'OSSRichText')]"));
        return message.getText();
    }
}
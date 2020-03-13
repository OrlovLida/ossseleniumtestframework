package com.oss.framework.components.portals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class Tooltip {
    private final WebDriver driver;

    public Tooltip(WebDriver driver) {
        this.driver = driver;
    }

    public List<String> getMessages() {
        List<WebElement> messages =
                this.driver.findElements(By.xpath("//div[contains(@class,'tooltip-content')]//p"));
        return messages.stream().map(WebElement::getText).collect(Collectors.toList());
    }
}

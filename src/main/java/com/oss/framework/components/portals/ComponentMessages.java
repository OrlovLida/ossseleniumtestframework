package com.oss.framework.components.portals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class ComponentMessages {
    private final WebElement webElement;

    public ComponentMessages(WebElement webElement) {
        this.webElement = webElement;
    }

    public List<String> getMessages() {
        List<WebElement> messages =
                webElement.findElement(By.xpath(".//div[@class='error-message-list']")).findElements(By.xpath(".//li"));
        return messages.stream().map(WebElement::getText).collect(Collectors.toList());
    }
}

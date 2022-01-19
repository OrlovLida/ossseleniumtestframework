package com.oss.framework.components.portals;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class InputMessage {

    private static final String ERROR_MESSAGE_LIST_XPATH = ".//div[@class='error-message-list']";

    private final WebElement webElement;

    public InputMessage(WebElement webElement) {
        this.webElement = webElement;
    }

    public List<String> getMessages() {
        List<WebElement> messages =
                webElement.findElement(By.xpath(ERROR_MESSAGE_LIST_XPATH)).findElements(By.xpath(".//li"));
        return messages.stream().map(message -> message.getAttribute("textContent")).collect(Collectors.toList());
    }
}

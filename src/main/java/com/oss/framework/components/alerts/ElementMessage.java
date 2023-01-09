package com.oss.framework.components.alerts;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.oss.framework.utils.CSSUtils;

public class ElementMessage {
    private static final String TEXT_CONTENT = "textContent";
    private static final String LI_TAG = ".//li";
    private static final String DATA_PARENT_TEST_ID_PATTERN = "[" + CSSUtils.DATA_PARENT_TEST_ID + "='%s']";
    private final WebElement webElement;

    private ElementMessage(WebElement webElement) {
        this.webElement = webElement;
    }

    public static ElementMessage create(WebDriver driver, String elementId) {
        WebElement webElement = driver.findElement(By.cssSelector(String.format(DATA_PARENT_TEST_ID_PATTERN, elementId)));
        return new ElementMessage(webElement);
    }

    public List<String> getMessagesText() {
        return getMessages().stream()
                .map(Message::getText)
                .collect(Collectors.toList());
    }

    private List<Message> getMessages() {
        return webElement.findElements(By.xpath(LI_TAG)).stream()
                .map(message -> Message.create(message.getAttribute(TEXT_CONTENT), CSSUtils.getAllClasses(webElement)))
                .collect(Collectors.toList());
    }
}

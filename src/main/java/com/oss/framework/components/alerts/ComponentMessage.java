package com.oss.framework.components.alerts;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.oss.framework.utils.CSSUtils;

public class ComponentMessage {

    private static final String WIZARD_MESSAGE_ELEMENT_CSS = ".wizard-message-element";
    private static final String TEXT_CONTENT = "textContent";
    private static final String SHOW_MORE_ID = "-show-more-id";
    private static final String COMPONENT_XPATH = ".//ancestor::div[@class='component']";
    private static final String BUTTON_MORE = ".//button[contains(@" + CSSUtils.TEST_ID + ",'show-more-id')]";
    private final WebElement webElement;

    private ComponentMessage(WebElement webElement) {
        this.webElement = webElement;
    }

    public static ComponentMessage create(WebDriver driver, String componentId) {
        WebElement parent = driver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId))).findElement(By.xpath(COMPONENT_XPATH));
        return new ComponentMessage(parent);
    }

    public List<String> getMessagesText() {
        return getMessages().stream().map(Message::getText).collect(Collectors.toList());
    }

    public List<Message> getMessages() {
        clickAllMore();
        return webElement.findElements(By.cssSelector(WIZARD_MESSAGE_ELEMENT_CSS)).stream()
                .map(message -> Message.create(message.getAttribute(TEXT_CONTENT), CSSUtils.getAllClasses(message)))
                .collect(Collectors.toList());
    }

    public void clickMore(MessageType messageType) {
        String type = messageType.toString();
        if (isMorePresent(type)) {
            webElement.findElement(By.cssSelector(CSSUtils.getElementCssSelector(type + SHOW_MORE_ID))).click();
        }
    }

    private void clickAllMore() {
        if (isMorePresent()) {
            getButtonsMore().forEach(WebElement::click);
        }
    }

    private List<WebElement> getButtonsMore() {
        return webElement.findElements(By.xpath(BUTTON_MORE));
    }

    private boolean isMorePresent() {
        return !getButtonsMore().isEmpty();
    }

    private boolean isMorePresent(String messageType) {
        return !webElement.findElements(By.cssSelector(CSSUtils.getElementCssSelector(messageType + SHOW_MORE_ID))).isEmpty();
    }

}

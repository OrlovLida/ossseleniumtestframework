package com.oss.framework.components.alerts;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
@Deprecated
/**
 * @Depracated - replace by ComponentMessage, will be removed in 4.0.x
 */
public class InputMessage {
    
    private static final String TEXT_CONTENT = "textContent";
    private static final String LI_TAG = ".//li";
    private static final String DATA_PARENT_TEST_ID_PATTERN = "[data-parent-testid='%s']";
    private final WebElement webElement;
    
    private InputMessage(WebElement webElement) {
        this.webElement = webElement;
    }
    
    public static InputMessage create(WebDriver driver, String inputId) {
        WebElement webElement = driver.findElement(By.cssSelector(String.format(DATA_PARENT_TEST_ID_PATTERN, inputId)));
        return new InputMessage(webElement);
    }
    
    public List<String> getMessages() {
        List<WebElement> messages =
                webElement.findElements(By.xpath(LI_TAG));
        return messages.stream().map(message -> message.getAttribute(TEXT_CONTENT)).collect(Collectors.toList());
    }
}

package com.oss.framework.components.layout;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class ErrorCard {

    private static final String MAIN_CSS = "[role='main']";
    private static final String ERROR_PAGE_ID = "ErrorPageV2";

    private final WebElement cardElement;

    private ErrorCard(WebElement cardElement) {
        this.cardElement = cardElement;
    }

    public static ErrorCard create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitBy(wait, By.cssSelector(MAIN_CSS));
        WebElement main = driver.findElement(By.cssSelector(MAIN_CSS));
        return new ErrorCard(main);
    }

    public ErrorInformation getErrorInformation() {
        return new ErrorInformation(cardElement);
    }

    public boolean isErrorPagePresent() {
        return WebElementUtils.isElementPresent(cardElement, By.cssSelector(String.format(CSSUtils.WEB_ELEMENT_PATTERN, ERROR_PAGE_ID)));
    }

    public static class ErrorInformation {

        private static final String TEXT_CONTENT_ATTRIBUTE = "textContent";
        private static final String ERROR_TEXT_CLASS = "errorText";
        private static final String ERROR_DESCRIPTION_CLASS = "errorDescription";
        private static final String ERROR_MESSAGE_CLASS = "errorMessage";

        private final WebElement errorPage;

        private ErrorInformation(WebElement cardElement) {
            this.errorPage = cardElement.findElement(By.cssSelector(String.format(CSSUtils.WEB_ELEMENT_PATTERN, ERROR_PAGE_ID)));
        }

        public String getErrorText() {
            return errorPage.findElement(By.className(ERROR_TEXT_CLASS)).getAttribute(TEXT_CONTENT_ATTRIBUTE);
        }

        public String getErrorDescription() {
            return errorPage.findElement(By.className(ERROR_DESCRIPTION_CLASS)).getAttribute(TEXT_CONTENT_ATTRIBUTE);
        }

        public String getErrorMessage() {
            return errorPage.findElement(By.className(ERROR_MESSAGE_CLASS)).getAttribute(TEXT_CONTENT_ATTRIBUTE);
        }
    }
}

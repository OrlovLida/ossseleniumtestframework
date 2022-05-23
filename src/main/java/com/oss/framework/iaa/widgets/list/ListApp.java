package com.oss.framework.iaa.widgets.list;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class ListApp {

    private static final Logger log = LoggerFactory.getLogger(ListApp.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement listAppElement;

    private static final String APP_LIST_PATTERN = "[" + CSSUtils.TEST_ID + "='%s'] .appList";
    private static final String ACTIVE_TAB_CONTENT_CSS = ".active .view-v2-widget";
    private static final String TEXT_FIELD_CSS = ".textFieldCont";

    private ListApp(WebDriver driver, WebDriverWait wait, WebElement listAppElement) {
        this.driver = driver;
        this.wait = wait;
        this.listAppElement = listAppElement;
    }

    public static ListApp createFromParent(WebDriver driver, WebDriverWait wait, String windowId) {
        DelayUtils.waitBy(wait, By.cssSelector(String.format(APP_LIST_PATTERN, windowId)));
        WebElement listApp = driver.findElement(By.cssSelector(String.format(APP_LIST_PATTERN, windowId)));
        return new ListApp(driver, wait, listApp);
    }

    public List<String> getValues() {
        log.debug("Getting all values from app list");
        return getActiveTabContent()
                .stream().map(textField -> textField.findElement(By.cssSelector(TEXT_FIELD_CSS)))
                .map(WebElement::getText).collect(Collectors.toList());
    }

    public String getValueFromField(String textFieldId) {
        return listAppElement.findElement(By.cssSelector(String.format(CSSUtils.WEB_ELEMENT_PATTERN, textFieldId))).getText();
    }

    private List<WebElement> getActiveTabContent() {
        DelayUtils.waitForNestedElements(wait, listAppElement, By.cssSelector(ACTIVE_TAB_CONTENT_CSS));
        return listAppElement.findElements(By.cssSelector(ACTIVE_TAB_CONTENT_CSS));
    }
}

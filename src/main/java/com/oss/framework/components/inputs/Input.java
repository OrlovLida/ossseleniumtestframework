package com.oss.framework.components.inputs;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.alerts.ElementMessage;
import com.oss.framework.components.data.Data;
import com.oss.framework.components.tooltip.Tooltip;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public abstract class Input {

    private static final String DEFAULT = "default";
    private static final String TEXT = "text";
    private static final String POINTER = "pointer";
    private static final String NOT_ALLOWED = "not-allowed";
    private static final String CANNOT_FIND_MOUSE_COURSE_EXCEPTION = "Cannot find mouse course for your input";
    private static final String LABEL = ".//label";
    private static final String DATA_PARENT_TEST_ID_PATTERN = "[" + CSSUtils.DATA_PARENT_TEST_ID + "='%s']";
    static final String NOT_SUPPORTED_EXCEPTION = "Not supported for this type of input";
    protected final WebDriver driver;
    protected final WebDriverWait webDriverWait;
    protected final WebElement webElement;
    protected final String componentId;

    Input(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.webElement = webElement;
        this.componentId = null;
    }

    Input(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement, String componentId) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.webElement = webElement;
        this.componentId = componentId;
    }

    public final void hover() {
        Actions action = new Actions(this.driver);
        action.moveToElement(webElement).build().perform();
    }

    public final void click() {
        WebElementUtils.clickWebElement(driver, webElement);
        DelayUtils.sleep();
    }

    public final void doubleClick() {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).doubleClick().perform();
        DelayUtils.sleep();
    }

    public final void clearByAction() {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).doubleClick().sendKeys(Keys.DELETE).perform();
        DelayUtils.sleep();
    }

    public MouseCursor cursor() {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).build().perform();
        String cursor = webElement.findElement(By.xpath(".//input")).getCssValue("cursor");
        return getMouseCursor(cursor);

    }

    public final List<String> getHint() {
        Tooltip tooltip = Tooltip.create(driver, webDriverWait, componentId);
        return tooltip.getMessages();
    }

    public final List<String> getMessages() {
        ElementMessage messages = ElementMessage.create(driver, componentId);
        return messages.getMessagesText();
    }

    public boolean isMessagePresent() {
        return !driver.findElements(By.cssSelector(String.format(DATA_PARENT_TEST_ID_PATTERN, componentId))).isEmpty();
    }

    public abstract void setValueContains(Data value);

    public abstract Data getValue();

    public abstract void setValue(Data value);

    public abstract void clear();

    public String getLabel() {
        WebElement label = webElement.findElement(By.xpath(LABEL));
        return label.getText();
    }

    public final void setMultiStringValue(List<String> values) {
        this.setValue(Data.createMultiData(values));
    }

    public final void setMultiStringValueContains(List<String> values) {
        this.setValueContains(Data.createMultiData(values));
    }

    public final void setSingleStringValue(String value) {
        this.setValue(Data.createSingleData(value));
    }

    public final void setSingleStringValueContains(String value) {
        this.setValueContains(Data.createSingleData(value));
    }

    public final String getStringValue() {
        return getValue().getStringValue();
    }

    public final List<String> getStringValues() {
        return getValue().getStringValues();
    }

    MouseCursor getMouseCursor(String cursor) {
        switch (cursor) {
            case DEFAULT: {
                return MouseCursor.DEFAULT;
            }
            case TEXT: {
                return MouseCursor.TEXT;
            }
            case POINTER: {
                return MouseCursor.POINTER;
            }
            case NOT_ALLOWED: {
                return MouseCursor.NOT_ALLOWED;
            }
            default:
        }
        throw new IllegalArgumentException(CANNOT_FIND_MOUSE_COURSE_EXCEPTION);
    }

    public enum ComponentType {
        BPM_COMBOBOX,
        CHECKBOX,
        COMBOBOX,
        CUSTOM_SELECT,
        COMMENT_TEXT_FIELD,
        OLD_TEXT_FIELD_APP,
        COORDINATES,
        DATE,
        DATE_TIME,
        DATE_TIME_RANGE,
        FILE_CHOOSER,
        /**
         * @Deprecated use HTML_EDITOR_COMPONENT, HTML_EDITOR will be removed in 4.0.x release
         */
        @Deprecated
        HTML_EDITOR,
        HTML_EDITOR_COMPONENT,
        MULTI_COMBOBOX,
        MULTI_SEARCH_FIELD,
        MULTI_SEARCHBOX,
        NUMBER_FIELD,
        OBJECT_SEARCH_FIELD,
        OBJECT_SEARCH_FIELD_REST,
        PASSWORD_FIELD,
        PHONE_FIELD,
        RADIO_BUTTONS,
        SCRIPT_COMPONENT,
        SEARCH_BOX,
        SEARCHBOX,
        SEARCH_FIELD,
        SLIDER,
        SWITCHER,
        TAGS,
        TEXT_AREA,
        TEXT_FIELD,
        TIME
    }

    public enum MouseCursor {
        NOT_ALLOWED, DEFAULT, TEXT, POINTER
    }

}

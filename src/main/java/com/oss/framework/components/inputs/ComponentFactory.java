package com.oss.framework.components.inputs;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class ComponentFactory {

    private ComponentFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static Input create(String componentId, ComponentType componentType, WebDriver webDriver, WebDriverWait wait) {
        DelayUtils.waitBy(wait, By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        switch (componentType) {
            case BPM_COMBOBOX: {
                return BpmCombobox.create(webDriver, wait, componentId);
            }
            case CHECKBOX: {
                return Checkbox.create(webDriver, wait, componentId);
            }
            case COMBOBOX: {
                return Combobox.create(webDriver, wait, componentId);
            }
            case COMMENT_TEXT_FIELD: {
                return CommentTextField.create(webDriver, wait, componentId);
            }
            case COORDINATES: {
                return Coordinates.create(webDriver, wait, componentId);
            }
            case DATE: {
                return Date.create(webDriver, wait, componentId);
            }
            case DATE_TIME: {
                return DateTime.create(webDriver, wait, componentId);
            }
            case DATE_TIME_RANGE: {
                return DateTimeRange.create(webDriver, wait, componentId);
            }
            case FILE_CHOOSER: {
                return FileChooser.create(webDriver, wait, componentId);
            }
            case MULTI_COMBOBOX: {
                return MultiCombobox.create(webDriver, wait, componentId);
            }
            case MULTI_SEARCH_FIELD: {
                return MultiSearchField.create(webDriver, wait, componentId);
            }
            case MULTI_SEARCHBOX:
            case TAGS: {
                return Tags.create(webDriver, wait, componentId);
            }
            case NUMBER_FIELD: {
                return NumberField.create(webDriver, wait, componentId);
            }
            case OBJECT_SEARCH_FIELD: {
                return ObjectSearchField.create(webDriver, wait, componentId);
            }
            case PASSWORD_FIELD: {
                return PasswordField.create(webDriver, wait, componentId);
            }
            case PHONE_FIELD: {
                return PhoneField.create(webDriver, wait, componentId);
            }
            case RADIO_BUTTONS:
            case RADIO_BUTTON: {
                return RadioButton.create(webDriver, wait, componentId);
            }
            case SCRIPT_COMPONENT: {
                return ScriptComponent.create(webDriver, wait, componentId);
            }
            case SEARCH_FIELD: {
                return SearchField.create(webDriver, wait, componentId);
            }
            case SEARCH_BOX: {
                return SearchBox.create(webDriver, wait, componentId);
            }
            case SEARCHBOX: {
                return SearchBoxV2.create(webDriver, wait, componentId);
            }
            case SLIDER:
            case TEXT_FIELD: {
                return TextField.create(webDriver, wait, componentId);
            }
            case SWITCHER: {
                return Switcher.create(webDriver, wait, componentId);
            }
            case TEXT_AREA: {
                return TextArea.create(webDriver, wait, componentId);
            }
            case TIME: {
                return Time.create(webDriver, wait, componentId);
            }
            default:
                throw new NoSuchElementException("Not supported component type: " + componentType);
        }
    }

    public static Input createFromParent(String componentId, ComponentType componentType, WebDriver webDriver, WebDriverWait wait,
                                         WebElement parent) {
        DelayUtils.waitBy(wait, By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        switch (componentType) {
            case BPM_COMBOBOX: {
                return BpmCombobox.create(webDriver, wait, componentId);
            }
            case CHECKBOX: {
                return Checkbox.createFromParent(parent, webDriver, wait, componentId);
            }
            case COMBOBOX: {
                return Combobox.createFromParent(parent, webDriver, wait, componentId);
            }
            case COMMENT_TEXT_FIELD: {
                return CommentTextField.create(webDriver, wait, componentId);
            }
            case COORDINATES: {
                return Coordinates.create(parent, webDriver, wait, componentId);
            }
            case DATE: {
                return Date.createFromParent(parent, webDriver, wait, componentId);
            }
            case DATE_TIME: {
                return DateTime.createFromParent(parent, webDriver, wait, componentId);
            }
            case DATE_TIME_RANGE: {
                return DateTimeRange.createFromParent(parent, webDriver, wait, componentId);
            }
            case FILE_CHOOSER: {
                return FileChooser.create(webDriver, wait, componentId);
            }
            case MULTI_COMBOBOX: {
                return MultiCombobox.createFromParent(parent, webDriver, wait, componentId);
            }
            case MULTI_SEARCH_FIELD: {
                return MultiSearchField.createFromParent(parent, webDriver, wait, componentId);
            }
            case MULTI_SEARCHBOX:
            case TAGS: {
                return Tags.createFromParent(parent, webDriver, wait, componentId);
            }
            case NUMBER_FIELD: {
                return NumberField.createFromParent(parent, webDriver, wait, componentId);
            }
            case OBJECT_SEARCH_FIELD: {
                return ObjectSearchField.createFromParent(parent, webDriver, wait, componentId);
            }
            case PASSWORD_FIELD: {
                return PasswordField.createFromParent(parent, webDriver, wait, componentId);
            }
            case PHONE_FIELD: {
                return PhoneField.createFromParent(parent, webDriver, wait, componentId);
            }
            case RADIO_BUTTONS:
            case RADIO_BUTTON: {
                return RadioButton.create(webDriver, wait, componentId);
            }
            case SCRIPT_COMPONENT: {
                return ScriptComponent.create(parent, webDriver, wait, componentId);
            }
            case SEARCH_FIELD: {
                return SearchField.createFromParent(parent, webDriver, wait, componentId);
            }
            case SEARCH_BOX: {
                return SearchBox.createFromParent(parent, webDriver, wait, componentId);
            }
            case SEARCHBOX: {
                return SearchBoxV2.createFromParent(parent, webDriver, wait, componentId);
            }
            case SLIDER:
            case TEXT_FIELD: {
                return TextField.create(parent, webDriver, wait, componentId);
            }
            case SWITCHER: {
                return Switcher.createFromParent(parent, webDriver, wait, componentId);
            }
            case TEXT_AREA: {
                return TextArea.createFromParent(parent, webDriver, wait, componentId);
            }
            case TIME: {
                return Time.create(parent, webDriver, wait, componentId);
            }
            default:
                throw new NoSuchElementException("Not supported component type: " + componentType);
        }
    }

    public static Input create(String componentId, WebDriver webDriver, WebDriverWait wait) {
        DelayUtils.waitBy(wait, By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        ComponentType componentType = getComponentType(componentId, webDriver);
        return create(componentId, componentType, webDriver, wait);
    }

    public static Input createFromParent(String componentId, WebDriver webDriver, WebDriverWait wait, WebElement parent) {
        DelayUtils.waitBy(wait, By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        ComponentType componentType = getComponentTypeFromParent(componentId, parent);
        return createFromParent(componentId, componentType, webDriver, wait, parent);
    }

    private static ComponentType getComponentType(String componentId, WebDriver webDriver) {
        WebElement webElement = webDriver.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        return ComponentType.valueOf(webElement.getAttribute("data-input-type"));
    }

    private static ComponentType getComponentTypeFromParent(String componentId, WebElement parent) {
        WebElement webElement = parent.findElement(By.cssSelector(CSSUtils.getElementCssSelector(componentId)));
        return ComponentType.valueOf(webElement.getAttribute("data-input-type"));
    }
}

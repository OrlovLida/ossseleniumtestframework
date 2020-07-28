package com.oss.framework.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.Input.ComponentType;
import com.oss.framework.utils.DelayUtils;

public class ComponentFactory {

    public static Input create(String componentId, ComponentType componentType, WebDriver webDriver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, Input.createComponentPath(componentId));

        switch(componentType) {
            case CHECKBOX : {
                return Checkbox.create(webDriver, wait, componentId);
            }
            case COMBOBOX : {
                return Combobox.create(webDriver, wait, componentId);
            }
            case COORDINATES : {
                return Coordinates.create(webDriver, wait, componentId);
            }
            case DATE : {
                return Date.create(webDriver, wait, componentId);
            }
            case DATE_TIME : {
                return DateTime.create(webDriver, wait, componentId);
            }
            case DATE_TIME_RANGE : {
                return DateTimeRange.create(webDriver, wait, componentId);
            }
            case MULTI_COMBOBOX : {
                return MultiCombobox.create(webDriver, wait, componentId);
            }
            case MULTI_SEARCH_FIELD : {
                return MultiSearchField.create(webDriver, wait, componentId);
            }
            case NUMBER_FIELD : {
                return NumberField.create(webDriver, wait, componentId);
            }
            case PASSWORD_FIELD : {
                return PasswordField.create(webDriver, wait, componentId);
            }
            case PHONE_FIELD : {
                return PhoneField.create(webDriver, wait, componentId);
            }
            case SEARCH_FIELD : {
                return SearchField.create(webDriver, wait, componentId);
            }
            case SWITCHER : {
                return Switcher.create(webDriver, wait, componentId);
            }
            case TEXT_AREA : {
                return TextArea.create(webDriver, wait, componentId);
            }
            case TEXT_FIELD : {
                return TextField.create(webDriver, wait, componentId);
            }
            case TIME : {
                return Time.create(webDriver, wait, componentId);
            }
            case FILE_CHOOSER: {
                return FileChooser.create(webDriver,wait,componentId);
            }
            case RADIO_BUTTON:{
                return RadioButton.create(webDriver,wait,componentId);
            }
        }

        throw new RuntimeException("Not supported component type: " + componentType);
    }

    public static Input createFromParent(String componentId, ComponentType componentType, WebDriver webDriver, WebDriverWait wait, WebElement parent) {
        DelayUtils.waitByXPath(wait, Input.createComponentPath(componentId));

        switch(componentType) {
//            case CHECKBOX : {
//                return Checkbox.create(webDriver, wait, componentId);
//            }
            case COMBOBOX : {
                return Combobox.create(webDriver, wait, componentId);
            }
//            case COORDINATES : {
//                return Coordinates.create(webDriver, wait, componentId);
//            }
//            case DATE : {
//                return Date.create(webDriver, wait, componentId);
//            }
//            case DATE_TIME : {
//                return DateTime.create(webDriver, wait, componentId);
//            }
//            case DATE_TIME_RANGE : {
//                return DateTimeRange.create(webDriver, wait, componentId);
//            }
//            case MULTI_COMBOBOX : {
//                return MultiCombobox.create(webDriver, wait, componentId);
//            }
//            case MULTI_SEARCH_FIELD : {
//                return MultiSearchField.create(webDriver, wait, componentId);
//            }
//            case NUMBER_FIELD : {
//                return NumberField.create(webDriver, wait, componentId);
//            }
//            case PASSWORD_FIELD : {
//                return PasswordField.create(webDriver, wait, componentId);
//            }
//            case PHONE_FIELD : {
//                return PhoneField.create(webDriver, wait, componentId);
//            }
//            case SEARCH_FIELD : {
//                return SearchField.create(webDriver, wait, componentId);
//            }
//            case SWITCHER : {
//                return Switcher.create(webDriver, wait, componentId);
//            }
//            case TEXT_AREA : {
//                return TextArea.create(webDriver, wait, componentId);
//            }
//            case TEXT_FIELD : {
//                return TextField.create(webDriver, wait, componentId);
//            }
//            case TIME : {
//                return Time.create(webDriver, wait, componentId);
//            }
        }

        throw new RuntimeException("Not supported component type: " + componentType);
    }
}

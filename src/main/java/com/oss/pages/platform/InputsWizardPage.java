package com.oss.pages.platform;

import com.google.common.collect.ImmutableSet;
import com.oss.framework.components.*;
import com.oss.framework.components.Input.ComponentType;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

public class InputsWizardPage extends BasePage {
    public static final String MANDATORY_CONTROLLER_ID = "mandatoryController";
    public static final String DANGER_MESSAGE_CONTROLLER_ID = "messageDangerController";
    public static final String WARNING_MESSAGE_CONTROLLER_ID = "messageWarningController";
    public static final String HIDDEN_CONTROLLER_ID = "hiddenController";
    public static final String READ_ONLY_CONTROLLER_ID = "readOnlyController";

    public static final String ACCEPT_BTN_ID = "Accept";

    public static final String TEXT_FIELD_ID = "TEXT_FIELD";
    public static final String TEXT_AREA_ID = "TEXT_AREA";
    public static final String PASSWORD_FIELD_ID = "PASSWORD_FIELD";
    public static final String NUMBER_FIELD_ID = "NUMBER_FIELD";
    public static final String DATE_TIME_RANGE_ID = "DATE_TIME_RANGE";
    public static final String DATE_TIME_ID = "DATE_TIME";
    public static final String DATE_ID = "DATE";
    public static final String TIME_ID = "TIME";
    public static final String CHECKBOX_ID = "CHECKBOX";
    public static final String SWITCHER_ID = "SWITCHER";
    public static final String SEARCH_FIELD_ID = "SEARCH_FIELD";
    public static final String MULTI_SEARCH_FIELD_ID = "MULTI_SEARCH_FIELD";
    public static final String COMBOBOX_ID = "COMBOBOX";
    public static final String MULTI_COMBOBOX_ID = "MULTI_COMBOBOX";
    public static final String FILE_CHOOSER_ID = "FILE_CHOOSER";
    public static final String COORDINATES_ID = "COORDINATES";
    public static final String PHONE_FIELD_ID = "PHONE_FIELD";

    public static final String DISABLED_FIELD_XPATH = "//div[contains(@class,'disabled')]";

    private static final ImmutableSet<String> inputLabels = ImmutableSet.of("DATE_TIME", "COMBOBOX");
    private static final ImmutableSet<String> controllerIds = ImmutableSet.of("mandatoryController",
            "messageDangerController",
            "messageWarningController", "hiddenController", "disabledController", "readOnlyController");

    private Wizard wizard = Wizard.createWizard(driver, wait);

    InputsWizardPage(WebDriver driver) {
        super(driver);
    }

    //Lazy
    private Wizard getWizard() {
        if(this.wizard == null) {
            this.wizard = Wizard.createWizard(this.driver, this.wait);
        }
        return this.wizard;
    }

    public void setComponentValue(String componentId, String value, Input.ComponentType componentType) {
        Input input = getWizard().getComponent(componentId, componentType);
        input.setSingleStringValue(value);
    }

    public String getComponentValue(String componentId, Input.ComponentType componentType) {
        Input input = getWizard().getComponent(componentId, componentType);
        return input.getStringValue();
    }

    public Input getComponent(String componentId, Input.ComponentType componentType) {
        return getWizard().getComponent(componentId, componentType);
    }

    public void setControllerValue(String controllerId, String componentId) {
        setComponentValue(controllerId, componentId, ComponentType.COMBOBOX);
        DelayUtils.sleep();
    }

    public void clearController(String controllerId) {
        getWizard().getComponent(controllerId, ComponentType.COMBOBOX).clear();
        DelayUtils.sleep();
    }

    public void clearAllControllers() {
        controllerIds.forEach(this::clearController);
    }

    public void submit() {
        this.wizard.submit();
        DelayUtils.sleep();
    }

    public void cancel() {
        this.wizard.cancel();
        DelayUtils.sleep();
    }

}

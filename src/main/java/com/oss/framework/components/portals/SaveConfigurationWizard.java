package com.oss.framework.components.portals;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaveConfigurationWizard {

    public static final String SAVE_CONFIG_ID = "saveNewConfig";

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final String DEFAULT_VIEW_COMBOBOX_ID = "default_view_combo";
    private static final String NAME_TEXTFIELD_ID = "name";
    private static final String DESCRIPTION_TEXTFIELD_ID = "description";
    private static final String TYPE_COMBOBOX_ID = "type";
    private static final String WIZARD_ID = "configuration_popup";
    private static final String SAVE_AS_NEW_ID = "configuration_popup_button_save_as_new";
    private static final String SAVE_ID = "configuration_popup_button_save";
    private static final String CANCEL_BUTTON_ID = "configuration_popup_button_cancel";
    private static final String GROUPS_ID = "groups-input";
    private static final String GROUPS_DROPDOWN_ID = "groups-dropdown-search";

    private SaveConfigurationWizard(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static SaveConfigurationWizard create(WebDriver driver, WebDriverWait wait) {
        return new SaveConfigurationWizard(driver, wait);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    public void saveAsNew(String name, Field... fields) {
        setName(name);
        for (Field field : fields) {
            setValue(field);
        }
        clickOnSaveAsNew();
    }

    public void save(Field... fields) {
        for (Field field : fields) {
            setValue(field);
        }
        clickOnSave();
    }

    private SaveConfigurationWizard setName(String name) {
        getWizard().getComponent(NAME_TEXTFIELD_ID, Input.ComponentType.TEXT_FIELD).setSingleStringValue(name);
        return this;
    }

    private SaveConfigurationWizard setDescription(String description) {
        getWizard().getComponent(DESCRIPTION_TEXTFIELD_ID, Input.ComponentType.TEXT_FIELD).setSingleStringValue(description);
        return this;
    }

    private SaveConfigurationWizard setAsDefault(String value) {
        getWizard().getComponent(DEFAULT_VIEW_COMBOBOX_ID, Input.ComponentType.COMBOBOX).setSingleStringValue(value);
        return this;
    }

    private SaveConfigurationWizard setAsDefaultForMe() {
        getWizard().getComponent(DEFAULT_VIEW_COMBOBOX_ID, Input.ComponentType.COMBOBOX).setSingleStringValue("Me");
        return this;
    }

    private SaveConfigurationWizard setAsDefaultForGroup(List<String> groupNames) {
        setAsDefault("Groups");
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().getComponent(GROUPS_ID, Input.ComponentType.COMBOBOX).click();
        for (String groupName : groupNames) {
            getWizard().getComponent(GROUPS_DROPDOWN_ID, Input.ComponentType.COMBOBOX).setSingleStringValue(groupName);
        }
        getWizard().getComponent(NAME_TEXTFIELD_ID, Input.ComponentType.COMBOBOX).click();
        return this;
    }

    private SaveConfigurationWizard setAsDefaultForAll() {
        getWizard().getComponent(DEFAULT_VIEW_COMBOBOX_ID, Input.ComponentType.COMBOBOX).setSingleStringValue("All");
        return this;
    }

    private SaveConfigurationWizard setType(String type) {
        getWizard().getComponent(TYPE_COMBOBOX_ID, Input.ComponentType.COMBOBOX).setSingleStringValue(type);
        return this;
    }

    private void clickOnSaveAsNew() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickActionById(SAVE_AS_NEW_ID);
    }

    private void clickOnSave() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickActionById(SAVE_ID);
    }

    private void clickOnCancel() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickActionById(CANCEL_BUTTON_ID);
    }

    private void setValue(Field field) {
        switch (field.getProperty()) {
            case NAME:
                setName(field.getFirstValue());
                break;
            case TYPE:
                setType(field.getFirstValue());
                break;
            case DEFAULT_VIEW_FOR:
                setAsDefault(field.getFirstValue());
                break;
            case GROUPS:
                setAsDefaultForGroup(field.getValues());
                break;
            case DESCRIPTION:
                setDescription(field.getFirstValue());
                break;
        }
    }

    public enum Property {NAME, DESCRIPTION, TYPE, DEFAULT_VIEW_FOR, GROUPS}

    public Field createField (Property property, String... value) {
        ArrayList<String> values = new ArrayList<>(Arrays.asList(value));
        return new Field(property, values);
    }

    public static class Field {
        Property property;
        ArrayList<String> values;

        private Field(Property property, ArrayList<String> values) {
            this.property = property;
            this.values = values;
        }

        Property getProperty() {
            return this.property;
        }

        List<String> getValues() {
            return this.values;
        }

        String getFirstValue() {
            return this.values.get(0);
        }
    }
}
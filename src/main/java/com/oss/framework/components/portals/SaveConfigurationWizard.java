package com.oss.framework.components.portals;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SaveConfigurationWizard {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final String DEFAULTT_VIEW_COMBOBOX_ID = "default_view_combo";
    private final String NAME_TEXTFIELD_ID = "name";
    private final String TYPE_COMBOBOX_ID = "type";
    private final String WIZARD_ID = "configuration_popup";
    private final String SAVE_AS_NEW_ID = "configuration_popup_button_save_as_new";
    private final String SAVE_ID = "configuration_popup_button_save";
    private final String CANCEL_BUTTON_ID = "configuration_popup_button_cancel";
    private final String GROUPS_ID = "groups-input";
    private final String GROUPS_DROPDOWN_ID = "groups-dropdown-search";

    private SaveConfigurationWizard(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static SaveConfigurationWizard create(WebDriver driver, WebDriverWait wait) {
        return new SaveConfigurationWizard(driver, wait);
    }

    private Wizard getWizard(){
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    public SaveConfigurationWizard typeName(String name){
        getWizard().getComponent(NAME_TEXTFIELD_ID, Input.ComponentType.TEXT_FIELD).setSingleStringValue(name);
        return this;
    }

    public SaveConfigurationWizard setAsDefaultForMe(){
        getWizard().getComponent(DEFAULTT_VIEW_COMBOBOX_ID, Input.ComponentType.COMBOBOX).setSingleStringValue("Me");
        return this;
    }

    public SaveConfigurationWizard setAsDefaultForGroup(String groupName){
        getWizard().getComponent(DEFAULTT_VIEW_COMBOBOX_ID, Input.ComponentType.COMBOBOX).setSingleStringValue("Groups");
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().getComponent(GROUPS_ID, Input.ComponentType.COMBOBOX).click();
        getWizard().getComponent(GROUPS_DROPDOWN_ID, Input.ComponentType.COMBOBOX).setSingleStringValue(groupName);
        getWizard().getComponent(NAME_TEXTFIELD_ID, Input.ComponentType.COMBOBOX).click();
        return this;
    }

    public SaveConfigurationWizard setAsDefaultForAll(){
        getWizard().getComponent(DEFAULTT_VIEW_COMBOBOX_ID, Input.ComponentType.COMBOBOX).setSingleStringValue("All");
        return this;
    }

    public SaveConfigurationWizard setForType(String type){
        getWizard().getComponent(TYPE_COMBOBOX_ID, Input.ComponentType.COMBOBOX).setSingleStringValue(type);
        return this;
    }

    public void saveAsNew(){
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickActionById(SAVE_AS_NEW_ID);
    }

    public void save(){
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickActionById(SAVE_ID);
    }

    public void cancel(){
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickActionById(CANCEL_BUTTON_ID);
    }
}

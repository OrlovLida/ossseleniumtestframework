package com.oss.pages.physical;

import com.oss.framework.components.*;
import com.oss.framework.utils.LocatingUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
//import org.testng.Assert;

public class LocationWizardPage extends BasePage {
    public static final String COMBOBOX_ID_TYPE = "physicalinventory_physical_location_form_type";
    public static final String SEARCH_FIELD_ID_PARENT_LOCATION = "physicalinventory_physical_location_form_parent_location";
    public static final String TEXT_FIELD_ID_NAME = "physicalinventory_physical_location_form_name";
    public static final String TEXT_FIELD_ID_ABBREVIATION = "physicalinventory_physical_location_form_abbreviation";
    public static final String TEXT_FIELD_ID_LATITUDE = "physicalinventory_physical_location_form_latitude";
    public static final String TEXT_FIELD_ID_LONGITUDE = "physicalinventory_physical_location_form_longitude";
    public static final String TEXT_FIELD_ID_DESCRIPTION = "physicalinventory_physical_location_form_description";
    public static final String TEXT_FIELD_ID_REMARKS = "physicalinventory_physical_location_remarks";
    public static final String COMBOBOX_ID_IMPORTANCE = "physicalinventory_physical_location_form_importance_category";
    public static final String COMBOBOX_ID_USE = "BuildingUseCategory-MasterBuildingUseCategory-Name";

    private Wizard wizard;

    public LocationWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard getWizard() {
        if(this.wizard == null) {
            this.wizard = Wizard.createWizard(this.driver, this.wait);
        }
        return wizard;
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

    public void proceed() {
        getWizard().proceed();
    }

    public void checkIfSuccess() {
        LocatingUtils.waitUsingXpath("//div[contains(@class,'success')]", wait);
//        Asserts.assertTrue(driver.findElement(By.xpath("//div[contains(@class,'success')]")).isEnabled());
    }

}


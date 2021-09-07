package com.oss.framework.components.portals;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.listwidget.EditableList;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ApplicationPopup extends PopupV2 {

    private static final String APPLICATION_INPUT_ID = "application-input";
    private static final String NAME_TEXT_FIELD_ID = "name";
    private static final String DESCRIPTION_TEXT_AREA_ID = "description";
    private static final String SAVE_BUTTON_FULL_XPATH = "//div[@class='popupBackground']//a[@class='CommonButton btn btn-primary btn-md']";
    private static final String QUERY_PARAMS_LABEL_XPATH = "//div[@class='categoryLabel']";
    private static final String EDITABLE_LIST_KEY_COMPONENT_ID = "key-TEXT_FIELD";
    private static final String EDITABLE_LIST_VALUE_COMPONENT_ID = "value-TEXT_FIELD";
    private static final String QUERY_PARAM_KEY_COLUMN_ID = "1_key";
    private static final String QUERY_PARAM_VALUE_COLUMN_ID = "1_value";

    public ApplicationPopup(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void setApplication(String application){
        Input applicationInput = ComponentFactory.create(APPLICATION_INPUT_ID, Input.ComponentType.COMBOBOX, driver, wait);
        applicationInput.setSingleStringValue(application);
    }

    public void setApplicationName(String applicationName){
        setComponentValue(NAME_TEXT_FIELD_ID, applicationName, Input.ComponentType.TEXT_FIELD);
    }

    public void setDescription(String applicationName){
        setComponentValue(DESCRIPTION_TEXT_AREA_ID, applicationName, Input.ComponentType.TEXT_AREA);
    }

    public void clickSaveButton(){
        driver.findElement(By.xpath(SAVE_BUTTON_FULL_XPATH)).click();
    }

    public void openQueryParamsTable(){ driver.findElement(By.xpath(QUERY_PARAMS_LABEL_XPATH)).click(); }

    public void addTestQueryParams() {
        EditableList editableList = EditableList.create(driver, wait);

        EditableList.Row firstRow = editableList.addRow();
        firstRow.setEditableAttributeValue("testParameter", QUERY_PARAM_KEY_COLUMN_ID, EDITABLE_LIST_KEY_COMPONENT_ID, Input.ComponentType.TEXT_FIELD);
        firstRow.setEditableAttributeValue("testValue", QUERY_PARAM_VALUE_COLUMN_ID, EDITABLE_LIST_VALUE_COMPONENT_ID, Input.ComponentType.TEXT_FIELD);
    }

    public EditableList getQueryParamsList(){
        return EditableList.create(driver, wait);
    }
}

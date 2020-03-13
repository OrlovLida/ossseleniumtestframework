package com.oss;

import com.oss.framework.components.Combobox;
import com.oss.framework.components.Input;
import com.oss.framework.components.SearchField;
import com.oss.framework.components.TextField;
import com.oss.pages.physical.LocationWizardPage;
import org.testng.annotations.Test;

public class PhysicalWizardTest extends BaseTestCase {

    private static final String PHYSICAL_CREATE_LOCATION_URL = String.format("%s"
            + "/#/view/physical-inventory/wizard/location/create?perspective=LIVE", BASIC_URL);

    @Test(description = "Location Wizard Test")
    public void locationWizardTest() {
        Combobox combobox;
        SearchField searchField;
        TextField textField;

        LocationWizardPage locationWizardPage = homePage.goToLocationWizardPage(PHYSICAL_CREATE_LOCATION_URL);

        combobox = (Combobox) locationWizardPage.getComponent
                (LocationWizardPage.COMBOBOX_ID_TYPE,
                        Input.ComponentType.COMBOBOX);
        combobox.setSingleStringValue("Building");

        searchField = (SearchField) locationWizardPage.getComponent
                (LocationWizardPage.SEARCH_FIELD_ID_PARENT_LOCATION,
                        Input.ComponentType.SEARCH_FIELD);
        searchField.setSingleStringValue("BuildingChild");

        textField = (TextField) locationWizardPage.getComponent
                (LocationWizardPage.TEXT_FIELD_ID_NAME,
                        Input.ComponentType.TEXT_FIELD);
        textField.setSingleStringValue("Test Tworzenia Lokacji");

        textField = (TextField) locationWizardPage.getComponent
                (LocationWizardPage.TEXT_FIELD_ID_ABBREVIATION,
                        Input.ComponentType.TEXT_FIELD);
        textField.setSingleStringValue("Skrót Test");

        textField = (TextField) locationWizardPage.getComponent
                (LocationWizardPage.TEXT_FIELD_ID_LATITUDE,
                        Input.ComponentType.TEXT_FIELD);
        textField.setSingleStringValue("45");

        textField = (TextField) locationWizardPage.getComponent
                (LocationWizardPage.TEXT_FIELD_ID_LONGITUDE,
                        Input.ComponentType.TEXT_FIELD);
        textField.setSingleStringValue("45");

        textField = (TextField) locationWizardPage.getComponent
                (LocationWizardPage.TEXT_FIELD_ID_DESCRIPTION,
                        Input.ComponentType.TEXT_FIELD);
        textField.setSingleStringValue("Opis");

        textField = (TextField) locationWizardPage.getComponent
                (LocationWizardPage.TEXT_FIELD_ID_REMARKS,
                        Input.ComponentType.TEXT_FIELD);
        textField.setSingleStringValue("Rimarks");

        combobox = (Combobox) locationWizardPage.getComponent
                (LocationWizardPage.COMBOBOX_ID_IMPORTANCE,
                        Input.ComponentType.COMBOBOX);
        combobox.setSingleStringValue("test");

        combobox = (Combobox) locationWizardPage.getComponent
                (LocationWizardPage.COMBOBOX_ID_USE,
                        Input.ComponentType.COMBOBOX);
        combobox.setSingleStringValue("Technical");

        locationWizardPage.proceed();

        locationWizardPage.checkIfSuccess();
    }

    @Test(description = "Device Wizard Test")
    public void DeviceWizardTest() {

    }
}

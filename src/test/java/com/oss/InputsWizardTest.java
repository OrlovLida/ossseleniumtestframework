package com.oss;

import com.oss.framework.components.*;
import com.oss.framework.components.Input.ComponentType;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.InputsWizardPage;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class InputsWizardTest extends BaseTestCase {
    private static final String INPUTS_WIZARD_URL = String.format("%s%s/wizards/inputs-wizard?perspective=LIVE" +
            "&withRemoved=false", BASIC_URL, MOCK_PATH);
    private static final String LIST_COLLAPSE_XPATH = "//i[contains(@class,'fa-angle-up')]";
    private static final String PHYSICAL_CREATE_LOCATION_URL = String.format("%s/#/view/physical-inventory/wizard" +
            "/location/create?perspective=LIVE", BASIC_URL);

    private InputsWizardPage inputsWizardPage;

    @BeforeClass
    public void openInputsWizard() {
        inputsWizardPage = homePage.goToInputsWizardPage(INPUTS_WIZARD_URL);
    }

    @Test
    public void testCheckbox() {
        Checkbox checkbox = (Checkbox) inputsWizardPage
                .getComponent(InputsWizardPage.CHECKBOX_ID, ComponentType.CHECKBOX);
        testComponent(InputsWizardPage.CHECKBOX_ID, checkbox);
    }

    @Test
    public void testCombobox() {
        driver.get(PHYSICAL_CREATE_LOCATION_URL);
        final String COMBOBOX_ID = "physicalinventory_physical_location_form_type";

        Combobox combobox = (Combobox) inputsWizardPage
                .getComponent(COMBOBOX_ID, ComponentType.COMBOBOX);
        //Combobox specific test cases

        combobox.setSingleStringValue("Building");
        Assertions.assertThat(combobox.getStringValue()).isEqualTo("Building");
        combobox.clear();
        Assertions.assertThat(combobox.getStringValue()).isEqualTo("");
        combobox.setSingleStringValue("Manhole");
        Assertions.assertThat(combobox.getStringValue()).isEqualTo("Manhole");

        inputsWizardPage = homePage.goToInputsWizardPage(INPUTS_WIZARD_URL);
        combobox = (Combobox) inputsWizardPage
                .getComponent(InputsWizardPage.COMBOBOX_ID, ComponentType.COMBOBOX);

        //Component tests
        testComponent(InputsWizardPage.COMBOBOX_ID, combobox);
    }

    @Test
    public void testCoordinates() {
        Coordinates coordinates = (Coordinates) inputsWizardPage
                .getComponent(InputsWizardPage.COORDINATES_ID, ComponentType.COORDINATES);
        List<String> coordinatesList = new ArrayList<>();
        coordinatesList.add("S");
        coordinatesList.add("120");
        coordinatesList.add("45");
        coordinatesList.add("30");

        //Coordinates specific test cases
        coordinates.setMultiStringValue(coordinatesList);
        Assertions.assertThat(coordinates.getStringValues()).isEqualTo(coordinatesList);

        coordinates.clear();
        coordinatesList.clear();
        coordinatesList.add("N");
        coordinatesList.add("0");
        coordinatesList.add("0");
        coordinatesList.add("0");
        Assertions.assertThat(coordinates.getStringValues()).isEqualTo(coordinatesList);

        //Component tests
        testComponent(InputsWizardPage.COORDINATES_ID, coordinates);
    }

    @Test
    //mask 1111-11-11
    public void testDate() {
        Date date = (Date) inputsWizardPage
                .getComponent(InputsWizardPage.DATE_ID, ComponentType.DATE);

        date.setValue(Data.createSingleData("2018-12-02"));
        Assertions.assertThat(date.getStringValue()).isEqualTo("2018-12-02");

        date.clear();
        Assertions.assertThat(date.getStringValue()).isEqualTo("");

        DelayUtils.sleep(1000);
        date.chooseDate("2020-12-02");
        DelayUtils.sleep(500);
        Assertions.assertThat(date.getStringValue()).isEqualTo("2020-12-02");

        DelayUtils.sleep(1000);
        date.chooseDate("2018-01-28");
        DelayUtils.sleep(500);
        Assertions.assertThat(date.getStringValue()).isEqualTo("2018-01-28");

        DelayUtils.sleep(1000);
        date.chooseDate("2019-06-21");
        DelayUtils.sleep(500);
        Assertions.assertThat(date.getStringValue()).isEqualTo("2019-06-21");

        DelayUtils.sleep(1000);
        date.chooseDate("2019-11-04");
        DelayUtils.sleep(500);
        Assertions.assertThat(date.getStringValue()).isEqualTo("2019-11-04");

        DelayUtils.sleep(1000);
        date.chooseDate("2022-02-06");
        DelayUtils.sleep(500);
        Assertions.assertThat(date.getStringValue()).isEqualTo("2022-02-06");

        //Component tests
        testComponent(InputsWizardPage.DATE_ID, date);

    }

    @Test(enabled = false)
    public void testDateTime() {
        DateTime dateTime = (DateTime) inputsWizardPage
                .getComponent(InputsWizardPage.DATE_TIME_ID, ComponentType.DATE_TIME);

        //Date Time specific test cases
        dateTime.setSingleStringValue("19951111121212");
        Assertions.assertThat(dateTime.getStringValue()).isEqualTo("1995-11-11 12:12:12");
        dateTime.clear();

        dateTime.setSingleStringValue("www");
        Assertions.assertThat(dateTime.getStringValue()).isEqualTo("");
        dateTime.clear();

        dateTime.setSingleStringValue("123");
        dateTime.hover();
        List<String> validationMessage = dateTime.getMessages();
        Assertions.assertThat(validationMessage).contains("Wrong date.");
        dateTime.clear();

        //
        dateTime.chooseTimeMore(6, 12, 35);
        Assertions.assertThat(dateTime.getStringValue()).contains("06:12:35");
        dateTime.clear();

        //Component tests
        testComponent(InputsWizardPage.DATE_TIME_ID, dateTime);
    }

    @Test(enabled = false)
    public void testDateRange() {

    }

    @Test(enabled = false)
    public void testFileChooser() {

        //TODO: refactor fileChooser - fileChooser nie dziala, nie wrzuca wybranego pliku
        FileChooser fileChooser = new FileChooser(driver);
    }


    @Test(enabled = true)
    public void testMultiCombobox() {
        MultiCombobox multiCombobox = (MultiCombobox) inputsWizardPage
                .getComponent(InputsWizardPage.MULTI_COMBOBOX_ID, ComponentType.MULTI_COMBOBOX);

        //Multi Combobox specific tests
        multiCombobox.setSingleStringValue("Test MultiCombobox");
        Assertions.assertThat(multiCombobox.getStringValue()).isEqualTo("Test MultiCombobox");
        multiCombobox.clear();
        Assertions.assertThat(multiCombobox.getStringValue()).isEqualTo("");

        //Component tests
        testComponent(InputsWizardPage.MULTI_COMBOBOX_ID, multiCombobox);
    }

    @Test(enabled = true)
    public void testMultiSearchField() {
        MultiSearchField multiSearchField = (MultiSearchField) inputsWizardPage
                .getComponent(InputsWizardPage.MULTI_SEARCH_FIELD_ID, ComponentType.MULTI_SEARCH_FIELD);

        //Multi Searchfield specific tests
        multiSearchField.setSingleStringValue("Test MultiSearchField");
        Assertions.assertThat(multiSearchField.getStringValue()).isEqualTo("Test MultiSearchField");
        multiSearchField.clear();
        Assertions.assertThat(multiSearchField.getStringValue()).isEqualTo("");

        //Component tests
        testComponent(InputsWizardPage.MULTI_SEARCH_FIELD_ID, multiSearchField);
    }

    @Test
    public void testNumberField() {
        NumberField numberField = (NumberField) inputsWizardPage
                .getComponent(InputsWizardPage.NUMBER_FIELD_ID, ComponentType.NUMBER_FIELD);

        //Number field specific tests
        numberField.setSingleStringValue("20191111");
        Assertions.assertThat(numberField.getStringValue()).isEqualTo("20 191 111");
        numberField.clear();
        Assertions.assertThat(numberField.getStringValue()).isEqualTo("");
        numberField.setSingleStringValue("alphabet");
        Assertions.assertThat(numberField.getStringValue()).isEqualTo("");

        //Component tests
        testComponent(InputsWizardPage.NUMBER_FIELD_ID, numberField);
    }

    @Test
    public void testPasswordField() {
        PasswordField passwordField = (PasswordField) inputsWizardPage
                .getComponent(InputsWizardPage.PASSWORD_FIELD_ID, ComponentType.PASSWORD_FIELD);
        //Password Field specific test cases

        passwordField.setSingleStringValue("Maliny^&");
        Assertions.assertThat(passwordField.getStringValue()).isEqualTo("Maliny^&");
        passwordField.clear();
        Assertions.assertThat(passwordField.getStringValue()).isEqualTo("");

        //Component tests
        testComponent(InputsWizardPage.PASSWORD_FIELD_ID, passwordField);
    }

    @Test
    public void testPhoneField() {
        PhoneField phoneField = (PhoneField) inputsWizardPage
                .getComponent(InputsWizardPage.PHONE_FIELD_ID, ComponentType.PHONE_FIELD);
        ;

        //Phone Field specific test cases

        phoneField.setSingleStringValue("+49 2056");
        Assertions.assertThat(phoneField.getStringValue()).isEqualTo("+49 2056");
        phoneField.clear();
        Assertions.assertThat(phoneField.getStringValue()).isEqualTo("");

        //Component tests
        testComponent(InputsWizardPage.PHONE_FIELD_ID, phoneField);
    }

    @Test
    public void testSearchField() {
        SearchField searchField = (SearchField) inputsWizardPage
                .getComponent(InputsWizardPage.SEARCH_FIELD_ID, ComponentType.SEARCH_FIELD);

        //Search Field specific test case
        DelayUtils.sleep();
        DelayUtils.sleep();
    }

    /*
    @Test(enabled = false)
    public void testSwitcher() throws InterruptedException {
        Switcher switcher = inputsWizardPage.getSwitcher();
        //Switcher specific test cases
        switcher.set();
        //Component tests
        testComponent(InputsWizardPage.SWITCHER_ID, switcher);
    }*/

    @Test
    public void testTextArea() {
        TextArea textArea = (TextArea) inputsWizardPage
                .getComponent(InputsWizardPage.TEXT_AREA_ID, ComponentType.TEXT_AREA);
        //Text Area specific test cases
        textArea.setSingleStringValue("testOSS123!@#");
        Assertions.assertThat(textArea.getStringValue()).isEqualTo("testOSS123!@#");
        textArea.clear();
        Assertions.assertThat(textArea.getStringValue()).isEqualTo("");
        //Component tests
        testComponent(InputsWizardPage.TEXT_AREA_ID, textArea);
    }

    @Test
    public void testTextField() {
        TextField textField = (TextField) inputsWizardPage
                .getComponent(InputsWizardPage.TEXT_FIELD_ID, ComponentType.TEXT_FIELD);
        //Text Field specific test cases

        textField.setSingleStringValue("duap");
        Assertions.assertThat(textField.getStringValue()).isEqualTo("duap");
        textField.clear();
        Assertions.assertThat(textField.getStringValue()).isEqualTo("");

        //Component tests
        testComponent(InputsWizardPage.TEXT_FIELD_ID, textField);
    }

    @Test
    public void testTime() {
        Time time = (Time) inputsWizardPage
                .getComponent(InputsWizardPage.TIME_ID, ComponentType.TIME);

        time.setSingleStringValue("05:23");
        Assertions.assertThat(time.getStringValue()).isEqualTo("05:23");

        time.clear();
        Assertions.assertThat(time.getStringValue()).isEqualTo("");

        time.chooseTime("6:25");
        time.chooseTime("21:03");
        time.chooseTime("03:44:16");
        time.chooseTime("00:00:16");

        testComponent(InputsWizardPage.TIME_ID, time);
    }

    private void testComponent(String componentId, Input input) {
        //clean up
        //inputsWizardPage.clearAllControllers();

        //Label Test
        String label = input.getLabel();
        Assertions.assertThat(componentId).isEqualTo(label);

        //Messages danger test
        inputsWizardPage.setControllerValue(InputsWizardPage.DANGER_MESSAGE_CONTROLLER_ID, componentId);
        driver.findElement(By.xpath(LIST_COLLAPSE_XPATH));
        input.hover();
        List<String> messages = input.getMessages();
        Assertions.assertThat(messages).contains("DANGER");
        inputsWizardPage.clearController(InputsWizardPage.DANGER_MESSAGE_CONTROLLER_ID);

        //Messages warning test


        //Read only test //
        inputsWizardPage.setControllerValue(InputsWizardPage.READ_ONLY_CONTROLLER_ID, componentId);
        driver.findElement(By.xpath(LIST_COLLAPSE_XPATH));

        Assertions.assertThat(input.cursor()).isEqualTo("not-allowed");

        inputsWizardPage.clearController(InputsWizardPage.READ_ONLY_CONTROLLER_ID);

        //Tooltip message test
        Assertions.assertThat(componentId).isEqualTo(input.getHint().get(0));

        //Mandatory label test
        inputsWizardPage.setControllerValue(InputsWizardPage.MANDATORY_CONTROLLER_ID, componentId);
        String mandatoryLabel = input.getLabel();
        driver.findElement(By.xpath(LIST_COLLAPSE_XPATH));
        Assertions.assertThat(mandatoryLabel).contains("*");

        //Mandatory validation test //
        inputsWizardPage.submit();
        input.hover();
        List<String> mandatoryMessages = input.getMessages();
        Assertions.assertThat(mandatoryMessages).contains("This field is mandatory.");
        inputsWizardPage.clearController(InputsWizardPage.MANDATORY_CONTROLLER_ID);

        //Hidden test, must be always last
        inputsWizardPage.setControllerValue(InputsWizardPage.HIDDEN_CONTROLLER_ID, componentId);
        Assertions.assertThatThrownBy(input::hover).isInstanceOf(StaleElementReferenceException.class);
        inputsWizardPage.clearController(InputsWizardPage.HIDDEN_CONTROLLER_ID);
    }
}

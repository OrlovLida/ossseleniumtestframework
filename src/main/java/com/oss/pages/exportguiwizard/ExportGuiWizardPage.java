package com.oss.pages.exportguiwizard;

import com.oss.framework.components.*;
import com.oss.framework.data.Data;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ExportGuiWizardPage extends BasePage {

    private Wizard wizard;
    private Checkbox checkbox;

public ExportGuiWizardPage(WebDriver driver) {super(driver);}

    @FindBy(xpath = "//label[contains (@for, 'exportgui-components-withheadercheckbox')]")
    private WebElement exportWithHeadersCheckbox;
    @FindBy(xpath = "//label[contains (@for, 'exportgui-components-generatepdfcheckbox')]")
    private WebElement generatePDFCheckbox;
    @FindBy(xpath = "//label[contains (@for, 'exportgui-components-compressfilecheckbox')]")
    private WebElement compressFileCheckbox;
    @FindBy(xpath = "//label[contains (@for, 'exportgui-components-scheduleexportcheckbox')]")
    private WebElement scheduleExportCheckbox;
    @FindBy(xpath = "//label[contains (@for, 'exportgui-components-sendbyemailcheckbox')]")
    private WebElement sendByEmailCheckbox;
    @FindBy(xpath = "//label[contains (@for, 'exportgui-components-remoteuploadcheckbox')]")
    private WebElement remoteUploadCheckbox;

    private final String CHECKBOX_EXPORT_WITH_HEADERS_ID = "exportgui-components-withheadercheckbox";
    private final String CHECKBOX_GENERATE_PDF_ID = "exportgui-components-withheadercheckbox";
    private final String CHECKBOX_COMPRESS_FILE_ID = "exportgui-components-compressfilecheckbox";
    private final String CHECKBOX_SCHEDULE_EXPORT_TASK_ID = "exportgui-components-scheduleexportcheckbox";
    private final String CHECKBOX_SEND_BY_EMAIL_ID = "exportgui-components-sendbyemailcheckbox";
    private final String CHECKBOX_REMOTE_UPLOAD_ID = "exportgui-components-remoteuploadcheckbox";

    Combobox combobox;
    SearchField searchField;
    TextField textField;

    public Wizard getWizard() {
        if (wizard == null) {
            wizard = Wizard.createWizard(driver, wait);
        }
        return wizard;
    }

    public void setValueOnCombobox (String COMBOBOX_ID, String Value){
        combobox = (Combobox) getComponent(COMBOBOX_ID,Input.ComponentType.COMBOBOX);
        combobox.setSingleStringValue(Value);
    }

    public void setValueOnTextField (String TEXT_FIELD_ID, Data Value){
        textField = (TextField) getComponent(TEXT_FIELD_ID,Input.ComponentType.TEXT_FIELD);
        textField.setValue(Value);
    }

    public Input getComponent(String componentId, Input.ComponentType componentType) {
        return getWizard().getComponent(componentId, componentType);
    }

    private WebElement getCheckbox(String path){
        return driver.findElement(checkbox.getCheckbox(path));
    }

    public ExportGuiWizardPage checkTheCheckbox(WebElement element){
        if(!isChecked(element))
            element.click();
        return this;
    }

    private ExportGuiWizardPage uncheckTheCheckbox(WebElement element){
        if(isChecked(element))
            element.click();
        return this;
    }

    boolean isChecked(WebElement element){
        String checked = element.findElement(By.xpath("./../input")).getAttribute("value");
        return (checked.equals("true"));
    }

    public void clickOnProceed() {
        getWizard().proceed();
    }

    public void clickOnNext() {
        getWizard().clickNext();
    }

    public void checkTheCheckbox() throws InterruptedException {
        waitForVisibility(generatePDFCheckbox);
        System.out.println(isChecked(generatePDFCheckbox));
        System.out.println(isChecked(exportWithHeadersCheckbox));
        System.out.println(isChecked(compressFileCheckbox));
        checkTheCheckbox(sendByEmailCheckbox);
        Thread.sleep(1000);
        checkTheCheckbox(generatePDFCheckbox);
        Thread.sleep(1000);
        checkTheCheckbox(exportWithHeadersCheckbox);
        Thread.sleep(1000);
        checkTheCheckbox(compressFileCheckbox);
        Thread.sleep(1000);
        checkTheCheckbox(generatePDFCheckbox);
        System.out.println(isChecked(generatePDFCheckbox));
        System.out.println(isChecked(exportWithHeadersCheckbox));
        System.out.println(isChecked(compressFileCheckbox));
        Thread.sleep(1000);
        clickOnNext();
        Thread.sleep(1000);
        //uncheckTheCheckbox(CHECKBOX_SEND_BY_EMAIL_ID);
    }
}

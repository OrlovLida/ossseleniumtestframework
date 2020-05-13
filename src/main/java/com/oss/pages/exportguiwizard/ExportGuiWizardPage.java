package com.oss.pages.exportguiwizard;

import com.oss.framework.components.*;
import com.oss.framework.data.Data;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.languageservice.LanguageServicePage;
import org.openqa.selenium.*;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

public class ExportGuiWizardPage extends BasePage {

    private Wizard wizard;
   // private Checkbox checkbox;

public ExportGuiWizardPage(WebDriver driver) {super(driver); getWizard(); }

    private final String CHECKBOX_EXPORT_WITH_HEADERS_ID = "exportgui-components-withheadercheckbox";
    private final String CHECKBOX_GENERATE_PDF_ID = "exportgui-components-withheadercheckbox";
    private final String CHECKBOX_COMPRESS_FILE_ID = "exportgui-components-compressfilecheckbox";
    private final String CHECKBOX_SCHEDULE_EXPORT_TASK_ID = "exportgui-components-scheduleexportcheckbox";
    private final String CHECKBOX_SEND_BY_EMAIL_ID = "exportgui-components-sendbyemailcheckbox";
    private final String CHECKBOX_REMOTE_UPLOAD_ID = "exportgui-components-remoteuploadcheckbox";
    private final String COMBOBOX_FILE_TYPE_ID = "exportgui-components-filetypechoose-input";
    private final String TEXT_FIELD_FILE_NAME_ID = "exportgui-components-filenametxt";
    private final String COMBOBOX_DATE_MASK_ID = "exportgui-components-dateMaskchoose-input";
    private final String COMBOBOX_CSV_DELIMITER_ID = "exportgui-components-csvdelimitertxt-input";
    private final String COMBOBOX_QUOTE_CHARACTER_ID = "exportgui-components-csvquotechartxt-input";

    WebElement combobox;
    TextField textField;

    public Wizard getWizard() {
        if (wizard == null) {
            wizard = Wizard.createWizard(driver, wait);
        }
        return wizard;
    }

    private void setValueOnCombobox (String COMBOBOX_ID, String value){
        combobox = driver.findElement(By.xpath("//input[contains (@id,'" + COMBOBOX_ID + "')]"));
        Actions actions = new Actions(driver);
        combobox.click();
        combobox.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        combobox.sendKeys(Keys.DELETE);
        combobox.sendKeys(value);
        actions.sendKeys(Keys.ARROW_DOWN).perform();
        actions.sendKeys(Keys.ARROW_DOWN).perform();
        actions.sendKeys(Keys.ENTER).perform();
    }

    private void setValueOnTextField (String TEXT_FIELD_ID, Data value){
        textField = (TextField) getComponent(TEXT_FIELD_ID,Input.ComponentType.TEXT_FIELD);
        textField.setValue(value);
    }

    private void checkTheCheckbox(String CHECKBOX_ID){
        WebElement element = driver.findElement(By.xpath("//label[contains (@for, '"+CHECKBOX_ID+"')]"));
        if(!isChecked(element))
            element.click();
    }

    private void uncheckTheCheckbox(String CHECKBOX_ID){
        WebElement element = driver.findElement(By.xpath("//label[contains (@for, '"+CHECKBOX_ID+"')]"));
        if(isChecked(element))
            element.click();
    }

    public Input getComponent(String componentId, Input.ComponentType componentType) {
        return getWizard().getComponent(componentId, componentType);
    }

    boolean isChecked(WebElement element){
        String checked = element.findElement(By.xpath("./../input")).getAttribute("value");
        return (checked.equals("true"));
    }

    private void clickOnAccept() {getWizard().clickAccept();}

    private void clickOnNext() {getWizard().clickNext();}

    public ExportGuiWizardPage chooseCSV(){setValueOnCombobox(COMBOBOX_FILE_TYPE_ID, "CSV"); return this;}

    public ExportGuiWizardPage chooseXLSX(){setValueOnCombobox(COMBOBOX_FILE_TYPE_ID, "XLSX"); return this;}

    public ExportGuiWizardPage chooseXLS(){setValueOnCombobox(COMBOBOX_FILE_TYPE_ID, "XLS"); return this;}

    public ExportGuiWizardPage chooseXML(){setValueOnCombobox(COMBOBOX_FILE_TYPE_ID, "XML"); return this;}

    public ExportGuiWizardPage chooseExportToPDF(){checkTheCheckbox(CHECKBOX_GENERATE_PDF_ID); return this;}

    public ExportGuiWizardPage chooseCompressedFile(){checkTheCheckbox(CHECKBOX_COMPRESS_FILE_ID); return this;}

    public ExportGuiWizardPage chooseExportToFileWithHeaders(){checkTheCheckbox(CHECKBOX_EXPORT_WITH_HEADERS_ID); return this;}

    public ExportGuiWizardPage chooseSendByEmail(){checkTheCheckbox(CHECKBOX_SEND_BY_EMAIL_ID); return this;}

    public ExportGuiWizardPage chooseScheduleExport(){checkTheCheckbox(CHECKBOX_SCHEDULE_EXPORT_TASK_ID); return this;}

    public ExportGuiWizardPage chooseRemoteUpload(){checkTheCheckbox(CHECKBOX_REMOTE_UPLOAD_ID); return this;}

    public ExportGuiWizardPage typeFileName(String FILE_NAME){setValueOnTextField(TEXT_FIELD_FILE_NAME_ID, Data.createSingleData(FILE_NAME)); return this;}

    public LanguageServicePage closeTheWizard(){clickOnAccept(); return new LanguageServicePage(driver);}

    public FillServerDataPage goToTheFillServerData(){clickOnNext(); return new FillServerDataPage(driver);}

    public ScheduleTaskPage goToTheScheduleTask(){clickOnNext(); return new ScheduleTaskPage(driver);}

    public SendFileByEmailPage goToSendFileByEmailPage(){waitForVisibility(driver.findElement(By.xpath("//div[contains (@class, 'simple-progress-bar') and contains(text(), 'E-mail')]")));clickOnNext(); return new SendFileByEmailPage(driver);}

    public ExportGuiWizardPage uncheckTheExportToFileWithHeaders(){uncheckTheCheckbox(CHECKBOX_EXPORT_WITH_HEADERS_ID); return this;}

    public ExportGuiWizardPage changeQuoteCharacter(String value){setValueOnCombobox(COMBOBOX_QUOTE_CHARACTER_ID, value); return this;}

    public ExportGuiWizardPage changeCSVDelimiter(String value){setValueOnCombobox(COMBOBOX_CSV_DELIMITER_ID, value); return this;}

    public ExportGuiWizardPage changeDateMask(String value){setValueOnCombobox(COMBOBOX_DATE_MASK_ID, value); return this;}

}

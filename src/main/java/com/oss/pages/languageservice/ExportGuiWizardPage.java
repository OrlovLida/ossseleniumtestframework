package com.oss.pages.languageservice;

import com.oss.framework.components.*;
import com.oss.pages.BasePage;
import com.oss.pages.physical.LocationWizardPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ExportGuiWizardPage extends BasePage {

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

    private final String EXPORT_WITH_HEADERS = "exportgui-components-withheadercheckbox";
    private final String GENERATE_PDF = "exportgui-components-withheadercheckbox";
    private final String COMPRESS_FILE = "exportgui-components-compressfilecheckbox";

    Combobox combobox;
    SearchField searchField;
    TextField textField;

    private WebElement getCheckbox(String path){
        return driver.findElement(checkbox.getCheckbox(path));
    }

    private ExportGuiWizardPage checkTheCheckbox(WebElement element){
        if(isChecked(element))
            element.click();
        return this;
    }
    private ExportGuiWizardPage uncheckTheCheckbox(WebElement element){
        if(!isChecked(element))
            element.click();
        return this;
    }

    boolean isChecked(WebElement element){
        String checked = element.findElement(By.xpath("./../input")).getAttribute("value");
        return (checked.equals("true"));
    }

    public void checkTheCheckbox()  {
        System.out.println(isChecked(generatePDFCheckbox));
        System.out.println(isChecked(exportWithHeadersCheckbox));
        System.out.println(isChecked(compressFileCheckbox));
        checkTheCheckbox(generatePDFCheckbox);
        checkTheCheckbox(exportWithHeadersCheckbox);
        checkTheCheckbox(compressFileCheckbox);
        checkTheCheckbox(generatePDFCheckbox);
        System.out.println(isChecked(generatePDFCheckbox));
        System.out.println(isChecked(exportWithHeadersCheckbox));
        System.out.println(isChecked(compressFileCheckbox));
    }
}

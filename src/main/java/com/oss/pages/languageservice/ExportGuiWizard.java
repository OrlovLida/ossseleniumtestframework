package com.oss.pages.languageservice;

import com.oss.framework.components.Checkbox;
import com.oss.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ExportGuiWizard extends BasePage {

    private Checkbox checkbox;

public ExportGuiWizard (WebDriver driver) {super(driver);}

    @FindBy(xpath = "//label[contains (@for, 'exportgui-components-withheadercheckbox')]")
    public static WebElement exportWithHeadersCheckbox;
    @FindBy(xpath = "//label[contains (@for, 'exportgui-components-generatepdfcheckbox')]")
    public static WebElement generatePDFCheckbox;
    @FindBy(xpath = "//label[contains (@for, 'exportgui-components-compressfilecheckbox')]")
    public static WebElement compressFileCheckbox;
    @FindBy(xpath = "//label[contains (@for, 'exportgui-components-scheduleexportcheckbox')]")
    public static WebElement scheduleExportCheckbox;
    @FindBy(xpath = "//label[contains (@for, 'exportgui-components-sendbyemailcheckbox')]")
    public static WebElement sendByEmailCheckbox;
    @FindBy(xpath = "//label[contains (@for, 'exportgui-components-remoteuploadcheckbox')]")
    public static WebElement remoteUploadCheckbox;

    private final String EXPORT_WITH_HEADERS = "exportgui-components-withheadercheckbox";
    private final String GENERATE_PDF = "exportgui-components-withheadercheckbox";
    private final String COMPRESS_FILE = "exportgui-components-compressfilecheckbox";

    private WebElement getCheckbox(String path){
        return driver.findElement(checkbox.getCheckbox(path));
    }

    boolean isChecked(String path){
        return getCheckbox(path).isSelected();
    }

    boolean isChecked(WebElement element){
        return element.isSelected();
    }

    public void checkTheCheckbox() throws InterruptedException {
        Thread.sleep(10000);
        System.out.println(isChecked(generatePDFCheckbox));
        System.out.println(isChecked(exportWithHeadersCheckbox));
        System.out.println(isChecked(compressFileCheckbox));
    }
}

package com.oss;
import com.oss.pages.exportguiwizard.ExportGuiWizardPage;

import com.oss.pages.languageservice.LanguageServicePage;
import org.testng.Assert;
import org.testng.annotations.*;

public class ExportGuiWizardTest extends BaseTestCase {

    private static final String LANGUAGE_SERVICE_PAGE_URL = String.format("%s/#/views/languagesservice/views/translations" +
            "?perspective=LIVE", BASIC_URL);
    private LanguageServicePage languageServicePage;
 //   private ExportGuiWizardPage exportGuiWizardPage;

    @BeforeClass
    public void prepareTests() {
        languageServicePage = homePage.goToLanguageServicePage(LANGUAGE_SERVICE_PAGE_URL);
        languageServicePage
                .changeForAlphaMode()
                .typeIdOfFirstServiceInSearch();
    }

    @BeforeMethod
    public void openExportGuiWizard() {
        languageServicePage
                .clearNotifications()
                .openExportGuiWizard();
            }


    @Test
    public void exportCSVFile(){
        new ExportGuiWizardPage(driver)
                .chooseCSV()
                .uncheckTheExportToFileWithHeaders();
    }

    @Test
    public void exportXLSFile(){
        new ExportGuiWizardPage(driver)
                .chooseXLS();
    }

    @Test
    public void exportXLSXFile(){
        new ExportGuiWizardPage(driver)
                .chooseXLSX();
    }

    @Test
    public void exportXMLFile(){
        new ExportGuiWizardPage(driver)
                .chooseXML();
    }

    @Test
    public void exportToPDFFile(){
        new ExportGuiWizardPage(driver)
                .chooseExportToPDF();
    }

    @Test
    public void exportToCompressedFile(){
        new ExportGuiWizardPage(driver)
                .chooseCompressedFile();
    }

    @Test
    public void exportToFileWithHeaders(){
        new ExportGuiWizardPage(driver)
                .chooseExportToFileWithHeaders();
    }

    @Test
    public void exportToFileWithChangedDataMask(){
        new ExportGuiWizardPage(driver)
                .chooseCSV()
                .changeDateMask("Basic ISO Date");
    }

    @Test
    public void exportToFileWithChangedFormat(){
        new ExportGuiWizardPage(driver)
                .chooseCSV()
                .changeQuoteCharacter("Single Quote")
                .changeCSVDelimiter("Comma");
    }
   /*
   @Test
    public void singleExportUsingScheduleExportTask(){

   }

   @Test
    public void dailyExportUsingScheduleExportTask(){

    }

    @Test
    public void weeklyExportUsingScheduleExportTask(){

    }

    @Test
    public void monthlyExportUsingScheduleExportTask(){

    }

    @Test
    public void yearlyExportUsingScheduleExportTask(){

    }

    @Test
    public void exportWithSendingEmail(){
        new ExportGuiWizardPage(driver)
                .chooseSendByEmail()
                .goToSendFileByEmailPage();
    }

    @Test
    public void exportWithSendingEmailWithAttachment(){

    }*/



    @AfterMethod
    public void closeTheWizard(){
        new ExportGuiWizardPage(driver)
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);

    }

}

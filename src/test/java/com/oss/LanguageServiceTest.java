package com.oss;

import com.oss.pages.languageservice.LanguageServicePage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class LanguageServiceTest extends BaseTestCase{


    private static final String LANGUAGE_SERVICE_PAGE_URL = String.format("%s/#/views/languagesservice/views/translations" +
            "?perspective=LIVE", BASIC_URL);
    private LanguageServicePage languageServicePage;

    @BeforeClass
    public void goToLanguageService(){languageServicePage = homePage.goToLanguageServicePage(LANGUAGE_SERVICE_PAGE_URL); }

    @Test
    public void openExportGuiWizard() throws InterruptedException {
        languageServicePage
                .changeForAlphaMode()
                .openExportGuiWizard();
    Thread.sleep(30000);
    }

    @Test
    public void ExportCSVFile() throws InterruptedException {
        languageServicePage
                .changeForAlphaMode()
                .openExportGuiWizard()
                .clickOnNext();
        Thread.sleep(30000);
    }

}

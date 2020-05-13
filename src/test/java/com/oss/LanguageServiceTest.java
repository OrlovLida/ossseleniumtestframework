package com.oss;

import com.oss.pages.languageservice.ExportGuiWizardPage;
import com.oss.pages.languageservice.LanguageServicePage;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;


public class LanguageServiceTest extends BaseTestCase{


    private static final String LANGUAGE_SERVICE_PAGE_URL = String.format("%s/#/views/languagesservice/views/translations" +
            "?perspective=LIVE&oss_console_mode=alpha", BASIC_URL);
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
                .checkTheCheckbox();
    }

}

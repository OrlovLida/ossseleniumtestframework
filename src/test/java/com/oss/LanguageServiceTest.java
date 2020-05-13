package com.oss;

import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.languageservice.LanguageServicePage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;




public class LanguageServiceTest extends BaseTestCase{


    private static final String LANGUAGE_SERVICE_PAGE_URL = String.format("%s/#/views/languagesservice/views/translations" +
            "?perspective=LIVE&oss_console_mode=alpha", BASIC_URL);
    private LanguageServicePage languageServicePage;

    @BeforeClass
    public void goToLanguageService(){languageServicePage = homePage.goToLanguageServicePage(LANGUAGE_SERVICE_PAGE_URL); }

    @Test
    public void openExportGuiWizard() throws InterruptedException {
        languageServicePage
                .clickOnLoginButton()
                .changeMode()
                .clickOnLoginButton()
                .expandMenu()
                .openExportGuiWizard()
                .checkTheCheckbox();
    Thread.sleep(30000);
    }
}

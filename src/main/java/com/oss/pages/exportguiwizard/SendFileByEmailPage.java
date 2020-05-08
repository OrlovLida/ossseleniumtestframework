package com.oss.pages.exportguiwizard;

import org.openqa.selenium.WebDriver;

public class SendFileByEmailPage extends ExportGuiWizardPage{

    public SendFileByEmailPage(WebDriver driver){super(driver);}

    private String RECEIPIENTS_ID = "exportgui-components-emailreceipientsmultisearchtag";
    private String ATTACH_EXPORTED_FILE_ID = "exportgui-components-emailattachexportedcheckbox";

}

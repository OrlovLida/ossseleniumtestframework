package com.oss.pages.exportguiwizard;

import org.openqa.selenium.WebDriver;

public class ScheduleTaskPage extends ExportGuiWizardPage{

    public ScheduleTaskPage(WebDriver driver){super(driver);}

    private String TYPE_OF_SCHEDULE_ID = "schedulerInput";
    private String DATA_SINGLE_ID = "dateSingle ";
    private String TIME_SINGLE_ID = "timeSingle";
    private String OCCURRENCE_INPUT_DAILY_SINGLE_ID = "occurrenceInputDaily";
    private String TIME_INPUT_ID = "timeInput";



}

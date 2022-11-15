/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.components.mainheader;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.wizard.Wizard;

/**
 * @author Gabriela Kasza
 */
public class PerspectiveChooser {
    private static final String LIVE = "Live";
    private static final String NETWORK = "Network";
    private static final String PLAN = "Plan";
    private static final String WITH_REMOVE = "With removed";
    private static final String WITHOUT_REMOVED = "Without removed";
    private static final String CURRENT_TASK = "Display my current Task";
    private static final String PLAN_CONTEXT_WIZARD_ID = "plaPlanChooserView_prompt-card";
    private static final String EXISTING_PROJECTS_INPUT_ID = "searchBoxId";
    private static final String SAVE_PLAN_CONTEXT_WIZARD_BUTTON_ID = "plaPlanChooserView_planChooserFormButtons-1";
    private static final String PLAN_CONTEXT_RADIOBUTTON_ID = "radioGroupId";
    private static final String DATE = "Date";
    private static final String DATE_INPUT_ID = "dateFieldId";
    private WebDriver driver;
    private WebDriverWait wait;

    private PerspectiveChooser(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static PerspectiveChooser create(WebDriver driver, WebDriverWait wait) {
        return new PerspectiveChooser(driver, wait);
    }

    public void setLivePerspective() {
        setPerspective(LIVE);
        wait.until(url -> driver.getCurrentUrl().contains(LIVE.toUpperCase()));
    }

    public void setNetworkPerspective() {
        setPerspective(NETWORK);
        wait.until(url -> driver.getCurrentUrl().contains(NETWORK.toUpperCase()));
    }

    public void setPlanPerspective(String processCodeOrName) {
        setPerspective(PLAN);
        Wizard planChooser = Wizard.createByComponentId(driver, wait, PLAN_CONTEXT_WIZARD_ID);
        planChooser.getComponent(EXISTING_PROJECTS_INPUT_ID).setSingleStringValueContains(processCodeOrName);
        planChooser.clickButtonById(SAVE_PLAN_CONTEXT_WIZARD_BUTTON_ID);
        wait.until(url -> driver.getCurrentUrl().contains(PLAN.toUpperCase()));
    }

    public void setPlanDatePerspective(String date) {
        setPerspective(PLAN);
        Wizard dataChooser = Wizard.createByComponentId(driver, wait, PLAN_CONTEXT_WIZARD_ID);
        dataChooser.setComponentValue(PLAN_CONTEXT_RADIOBUTTON_ID, DATE);
        dataChooser.setComponentValue(DATE_INPUT_ID, date);
        dataChooser.clickButtonById(SAVE_PLAN_CONTEXT_WIZARD_BUTTON_ID);
        wait.until(url -> driver.getCurrentUrl().contains(date));
    }

    public void setWithRemove() {
        setPerspective(WITH_REMOVE);
        wait.until(url -> driver.getCurrentUrl().contains("withRemoved=true"));
    }

    public void setWithoutRemoved() {
        setPerspective(WITHOUT_REMOVED);
        wait.until(url -> driver.getCurrentUrl().contains("withRemoved=false"));
    }

    public void setCurrentTask() {
        setPerspective(CURRENT_TASK);
        wait.until(url -> driver.getCurrentUrl().contains("current-task"));
    }

    private void setPerspective(String perspective) {
        ToolbarWidget toolbar = ToolbarWidget.create(driver, wait);
        toolbar.openQueryContextContainer();
        DropdownList dropdownList = DropdownList.create(driver, wait);
        dropdownList.selectOption(perspective);
    }

    public String getCurrentPerspective() {
        ToolbarWidget toolbar = ToolbarWidget.create(driver, wait);
        return toolbar.getQueryContext();
    }
}

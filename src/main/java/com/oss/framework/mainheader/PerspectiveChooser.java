/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.mainheader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;

import static com.oss.framework.widgets.Wizard.createWizard;

/**
 * @author Gabriela Kasza
 */
public class PerspectiveChooser {
    private static final String LIVE = "Live";
    private static final String NETWORK = "Network";
    private static final String PLAN = "Plan";
    private static final String WITH_REMOVE = "With removed";
    private static final String WITHOUT_REMOVED = "Without removed";
    private static final String CURRENT_TASK = "Current task";
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
        wait.until(url -> driver.getCurrentUrl().contains("LIVE"));
    }

    public void setNetworkPerspective() {
        setPerspective(NETWORK);
        wait.until(url -> driver.getCurrentUrl().contains("NETWORK"));

    }

    public void setPlanPerspective(String processCodeOrName) {
        setPerspective(PLAN);
        Wizard planChooser = Wizard.createWizard(driver, wait);
        Input input = planChooser.getComponent("searchBoxId", Input.ComponentType.SEARCH_FIELD);
        input.setSingleStringValue(processCodeOrName);
        planChooser.clickActionById("plaPlanChooserView_planChooserFormButtons-1");

        wait.until(url -> driver.getCurrentUrl().contains("PLAN"));
    }

    public void setPlanDatePerspective(String date) {
        setPerspective(PLAN);
        Wizard dataChooser = createWizard(driver, wait);
        Input radioButtons = dataChooser.getComponent("radioGroupId", Input.ComponentType.RADIO_BUTTON);
        radioButtons.setSingleStringValue("Date");
        Input selectDate = dataChooser.getComponent("dateFieldId", Input.ComponentType.DATE);
        selectDate.setSingleStringValue(date);
        dataChooser.clickActionById("plaPlanChooserView_planChooserFormButtons-1");
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
        DelayUtils.waitByXPath(wait, "//div[contains(@class,'query-context__dropdown')]");
        WebElement element = driver.findElement(By.className("query-context__dropdown"));
        DelayUtils.waitForNestedElements(wait, element, ".//a[text()='" + perspective + "']");
        WebElement context = element.findElement(By.xpath(".//a[text()='" + perspective + "']"));
        context.click();
    }
}

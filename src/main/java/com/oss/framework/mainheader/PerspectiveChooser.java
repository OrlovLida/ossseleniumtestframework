/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.mainheader;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.Input;
import com.oss.framework.components.RadioButton;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;

import static com.oss.framework.widgets.Wizard.*;

/**
 * @author Gabriela Kasza
 */
public class PerspectiveChooser {
    private WebDriver driver;
    private WebDriverWait wait;
    private static String LIVE ="Live";
    private static String NETWORK="Network";
    private static String PLAN="Plan";
    private static String WITH_REMOVE="With removed";
    private static String WITHOUT_REMOVED = "Without removed";


    public static PerspectiveChooser create (WebDriver driver, WebDriverWait wait){return new PerspectiveChooser(driver, wait);}
    private PerspectiveChooser (WebDriver driver, WebDriverWait wait){
        this.driver=driver;
        this.wait= wait;

    }
    public void setLivePerspective(){
     setPerspective(LIVE);
        wait.until(url ->driver.getCurrentUrl().contains("LIVE"));
    }
    public void setNetworkPerspective(){
        setPerspective(NETWORK);
        wait.until(url ->driver.getCurrentUrl().contains("NETWORK"));

    }
    public void setPlanPerspective(String processCodeOrName) {
        setPerspective(PLAN);
        Wizard planChooser = Wizard.createWizard(driver, wait);
        Input input = planChooser.getComponent("searchBoxId", Input.ComponentType.SEARCH_FIELD);
        input.setSingleStringValue(processCodeOrName);
        if (this.driver.getPageSource().contains("Change")){
            planChooser.clickChange();
        }
        else planChooser.clickSave();
        //planChooser.waitToClose();
        wait.until(url -> driver.getCurrentUrl().contains("PLAN"));
    }
    public void setPlanDatePerspective (String date){
        setPerspective(PLAN);
        Wizard dataChooser = createWizard(driver, wait);
        Input radioButtons = dataChooser.getComponent("radioGroupId", Input.ComponentType.RADIO_BUTTON);
        radioButtons.setSingleStringValue("Date");
        Input selectDate = dataChooser.getComponent("dateFieldId", Input.ComponentType.DATE);
        selectDate.setSingleStringValue(date);
        if (this.driver.getPageSource().contains("Change")){
        dataChooser.clickChange();
        }
        else dataChooser.clickSave();
        //dataChooser.waitToClose();
        wait.until(url -> driver.getCurrentUrl().contains(date));
    }
    public void setWithRemove(){
        setPerspective(WITH_REMOVE);
        wait.until(url ->driver.getCurrentUrl().contains("withRemoved=true"));
    }
    public void setWithoutRemoved(){
        setPerspective(WITHOUT_REMOVED);
        wait.until(url ->driver.getCurrentUrl().contains("withRemoved=false"));
    }
     private void setPerspective(String perspective){
         MainHeader toolbar = MainHeader.create(driver, wait);
         toolbar.callActionByLabel("queryContextContainer");
         DelayUtils.waitByXPath(wait,"//div[@class= 'CustomSelectList-rowsContainer']");
         WebElement element = driver.findElement(By.xpath(".//div[@class= 'CustomSelectList-rowsContainer']"));
         DelayUtils.waitForNestedElements(wait,element,".//div[text()='"+perspective+"']");
         WebElement context = element.findElement(By.xpath(".//div[text()='"+perspective+"']"));
         context.click();
}
}

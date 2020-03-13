package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

public class DeviceWizardPage extends BasePage {

    private Wizard wizard;

    public DeviceWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard getWizard() {
        if(this.wizard == null) {
            this.wizard = Wizard.createWizard(this.driver, this.wait);
        }
        return wizard;
    }

    public void setComponentValue(String componentId, String value, Input.ComponentType componentType) {
        Input input = getWizard().getComponent(componentId, componentType);
        input.setSingleStringValue(value);
    }

    public String getComponentValue(String componentId, Input.ComponentType componentType) {
        Input input = getWizard().getComponent(componentId, componentType);
        return input.getStringValue();
    }

    public void submit() {
        getWizard().submit();
    }

    public void cancel() {
        getWizard().cancel();
    }


}

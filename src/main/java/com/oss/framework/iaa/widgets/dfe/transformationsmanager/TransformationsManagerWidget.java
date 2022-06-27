package com.oss.framework.iaa.widgets.dfe.transformationsmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

public class TransformationsManagerWidget extends Widget {

    public static final String TRANSFORMATION_PATH = "//section[@class='TransformationComponentContainer']";
    private static final String ADD_BTN_PATH = "//button[@class='btn btn-primary btn-add-transformation']";
    private static final String SELECT_TRANSFORMATION_INPUT_ID = "availableTransformationsComboBox";
    private static final String TRANSFORMATIONS_MANAGER_PATH = "//div[@class='TransformationsManagerContainer']";
    private static final String WIDGET_ID = "transformations-manager";

    private TransformationsManagerWidget(WebDriver driver, WebDriverWait webDriverWait, String widgetId) {
        super(driver, webDriverWait, widgetId);
    }

    public static TransformationsManagerWidget create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, TRANSFORMATIONS_MANAGER_PATH);
        return new TransformationsManagerWidget(driver, wait, WIDGET_ID);
    }

    public void selectTransformation(String transformationName) {
        getComponent(SELECT_TRANSFORMATION_INPUT_ID).setValue(Data.createSingleData(transformationName));

    }

    public void clickAdd() {
        this.webElement.findElement(By.xpath(ADD_BTN_PATH)).click();
        DelayUtils.waitByXPath(webDriverWait, TRANSFORMATION_PATH);
    }

    private Input getComponent(String componentId) {
        return ComponentFactory.create(componentId, driver, webDriverWait);
    }

}

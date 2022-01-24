package com.oss.framework.iaa.widget.dfe.transformationsmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

public class TransformationsManagerWidget extends Widget {

    public static final String TRANSFORMATION_PATH = "//section[@class='TransformationComponentContainer']";
    private static final String ADD_BTN_PATH = "//button[@class='btn btn-primary btn-add-transformation']";
    private static final String SELECT_TRANSFORMATION_INPUT_ID = "availableTransformationsComboBox-input";
    private static final String TRANSFORMATIONS_MANAGER_PATH = "//div[@class='TransformationsManagerContainer']";

    @Deprecated
    private TransformationsManagerWidget(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
        super(driver, webElement, webDriverWait);
    }

    @Deprecated
    public static TransformationsManagerWidget create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, TRANSFORMATIONS_MANAGER_PATH);
        WebElement webElement = driver.findElement(By.xpath(TRANSFORMATIONS_MANAGER_PATH));

        return new TransformationsManagerWidget(driver, wait, webElement);
    }

    public void selectTransformation(String transformationName) {
        Combobox selectTransformation = (Combobox) getComponent(SELECT_TRANSFORMATION_INPUT_ID, Input.ComponentType.COMBOBOX);
        selectTransformation.setValue(Data.createSingleData(transformationName));

    }

    public void clickAdd() {
        this.webElement.findElement(By.xpath(ADD_BTN_PATH)).click();
        DelayUtils.waitByXPath(webDriverWait, TRANSFORMATION_PATH);
    }

    private Input getComponent(String componentId, Input.ComponentType componentType) {
        return ComponentFactory.create(componentId, componentType, driver, webDriverWait);
    }

}

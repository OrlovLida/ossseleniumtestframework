package com.oss.framework.widgets.serviceDeskAdvancedSearch;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

public class ServiceDeskAdvancedSearch extends Widget {

    private ServiceDeskAdvancedSearch(WebDriver driver, WebDriverWait webDriverWait, String windowId, WebElement webElement) {
        super(driver, webDriverWait, windowId, webElement);
    }

    public static ServiceDeskAdvancedSearch create(WebDriver driver, WebDriverWait webDriverWait, String windowId) {
        String xPath = "//div[@" + CSSUtils.TEST_ID + "='" + windowId + "']/.//*[@class='appContent service-desk-advancedsearchwidget']";

        DelayUtils.waitByXPath(webDriverWait, xPath);
        WebElement webElement = driver.findElement(By.xpath(xPath));

        return new ServiceDeskAdvancedSearch(driver, webDriverWait, windowId, webElement);
    }

    public Input getComponent(String componentId, Input.ComponentType componentType) {
        return ComponentFactory.create(componentId, componentType, driver, webDriverWait);
    }
}
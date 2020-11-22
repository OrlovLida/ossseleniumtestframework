package com.oss.framework.widgets.advancedsearch;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.search.SearchPanel;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.tablewidget.TableWidget;

public class AdvancedSearchWidget {

    private static final String ADD_BTN_PATH = ".//a[text()='Add']";

    public static AdvancedSearchWidget create(WebDriver driver, WebDriverWait webDriverWait) {
        return new AdvancedSearchWidget(driver, webDriverWait,null);
    }

    public static AdvancedSearchWidget createById(WebDriver driver, WebDriverWait wait, String id) {
        DelayUtils.waitByXPath(wait, "//*[@id='" + id + "']");
        WebElement webElement = driver.findElement(By.xpath("//*[@id='" + id + "']"));
        return new AdvancedSearchWidget (driver, wait, webElement);
    }

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    private AdvancedSearchWidget(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement) {
        this.driver = driver;
        this.wait = webDriverWait;
        this.webElement = webElement;
    }

    private SearchPanel getSearchPanel() {
        return SearchPanel.create(driver, wait);
    }

    public Input getComponent(String componentId, ComponentType componentType) {
        return getSearchPanel().getComponent(componentId, componentType);
    }

    public TableWidget getTableWidget() {
        Widget.waitForWidget(wait, "right-side");
        return TableWidget.create(driver, "right-side", wait);
    }

    public void clickAdd() {
        this.webElement.findElement(By.xpath(ADD_BTN_PATH)).click();
    }
}

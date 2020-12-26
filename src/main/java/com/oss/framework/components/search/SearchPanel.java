package com.oss.framework.components.search;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;

public class SearchPanel {
    private static final String ADVANCED_SEARCH_PANEL_CLASS = "advanced-search_panel";

    private static final String APPLY_BTN_PATH = ".//a[text()='Apply']";
    private static final String CANCEL_BTN_PATH = ".//a[text()='Cancel']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    public static SearchPanel create(WebDriver driver, WebDriverWait wait) {
        return new SearchPanel(driver, wait);
    }

    private SearchPanel(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.webElement = this.driver.findElement(By.xpath("//*[@class='" + ADVANCED_SEARCH_PANEL_CLASS + "'] | //*[@class='filters-box']"));
    }

    public void applyFilter() {
        wait.until(ExpectedConditions.elementToBeClickable(webElement.findElement(By.xpath(APPLY_BTN_PATH)))).click();
    }

    public void cancel() {
        this.webElement.findElement(By.xpath(CANCEL_BTN_PATH)).click();
    }

    public Input getComponent(String componentId, ComponentType componentType) {
        return ComponentFactory.create(componentId, componentType, this.driver, this.wait);
    }
}

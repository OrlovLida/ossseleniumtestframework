package com.oss.framework.widgets.dpe.toolbarpanel;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.oss.framework.logging.LoggerMessages.clickButton;
import static com.oss.framework.utils.WidgetUtils.findElementByXpath;
import static com.oss.framework.widgets.dpe.toolbarpanel.KpiToolbarPanel.KPI_TOOLBAR_PATH;

public class FiltersPanel {

    private static final Logger log = LoggerFactory.getLogger(FiltersPanel.class);

    private final static String FILTERS_BUTTON_PATH = "//i[@aria-label='SETTINGS']";
    private final static String FILTERS_CLEAR_BUTTON_TEXT = "Clear All";
    private final static String FILTERS_CONFIRM_BUTTON_TEXT = "Confirm";
    private final static String FILTER_WRAPPER = "onOffWrapper";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    static FiltersPanel create(WebDriver driver, WebDriverWait webDriverWait){
        WebElement webElement = driver.findElement(By.xpath(KPI_TOOLBAR_PATH));

        return new FiltersPanel(driver, webDriverWait, webElement);
    }

    private FiltersPanel(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement){
        this.driver = driver;
        this.wait = webDriverWait;
        this.webElement = webElement;
    }

    public void openFilters() {
        DelayUtils.waitForPresence(wait, By.className(FILTER_WRAPPER));
        WebElement filters = findElementByXpath(this.webElement, FILTERS_BUTTON_PATH);
        DelayUtils.waitForPresenceAndVisibility(wait, By.xpath(FILTERS_BUTTON_PATH));
        DelayUtils.waitForClickability(wait, filters);
        DelayUtils.sleep();
        filters.click();
        log.debug(clickButton("Filters"));
    }

    public void clearFilters() {
        Button.create(driver, FILTERS_CLEAR_BUTTON_TEXT).click();
        log.debug(clickButton("Clear All"));
    }

    public void turnOnFilters(List<String> filtersToEnable) {
        List<WebElement> filterElements = this.webElement.findElements(By.className(FILTER_WRAPPER))
                .stream()
                .filter(filter -> filtersToEnable.contains(filter.getText()))
                .collect(Collectors.toList());

        if(filterElements.size() == 0){
            log.warn("Can't find any filters");
            throw new NoSuchElementException("Can't find any filters");
        } else if(filterElements.size() != filtersToEnable.size()){
            log.warn("Can't find some filters to enable from list: " + filtersToEnable);
            throw new NoSuchElementException("Can't find some filters to enable from list: " + filtersToEnable);
        }
        filterElements
                .forEach(WebElement::click);
    }

    public void clickConfirm(){
        Button.create(driver, FILTERS_CONFIRM_BUTTON_TEXT).click();
        log.debug(clickButton("Confirm"));
    }
}

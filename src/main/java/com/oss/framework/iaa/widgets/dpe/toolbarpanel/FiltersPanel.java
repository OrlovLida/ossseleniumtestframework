package com.oss.framework.iaa.widgets.dpe.toolbarpanel;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;



public class FiltersPanel {

    private static final Logger log = LoggerFactory.getLogger(FiltersPanel.class);

    private static final String FILTERS_CLEAR_BUTTON_TEXT = "Clear All";
    private static final String FILTERS_CONFIRM_BUTTON_TEXT = "Confirm";
    private static final String FILTER_WRAPPER = "onOffWrapper";
    private static final String FILTER_PANEL_XPATH = ".//div[@data-testid='filter-menu']";
    private static final String CLICK_BTN = "Clicking button: ";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement filterPanel;

    private FiltersPanel(WebDriver driver, WebDriverWait webDriverWait, WebElement filterPanel) {
        this.driver = driver;
        this.wait = webDriverWait;
        this.filterPanel = filterPanel;
    }

    static FiltersPanel create(WebDriver driver, WebDriverWait webDriverWait) {
        WebElement filterPanel = driver.findElement(By.xpath(FILTER_PANEL_XPATH));

        return new FiltersPanel(driver, webDriverWait, filterPanel);
    }

    public void clearFilters() {
        Button.create(driver, FILTERS_CLEAR_BUTTON_TEXT).click();
        log.debug(CLICK_BTN + FILTERS_CLEAR_BUTTON_TEXT);
    }

    public void turnOnFilters(List<String> filtersToEnable) {
        List<WebElement> filterElements = this.filterPanel.findElements(By.className(FILTER_WRAPPER))
                .stream()
                .filter(filter -> filtersToEnable.contains(filter.getText()))
                .collect(Collectors.toList());

        if (filterElements.isEmpty()) {
            log.warn("Can't find any filters");
            throw new NoSuchElementException("Can't find any filters");
        } else if (filterElements.size() != filtersToEnable.size()) {
            log.warn("Can't find some filters to enable from list: {}", filtersToEnable);
            throw new NoSuchElementException("Can't find some filters to enable from list: " + filtersToEnable);
        }
        filterElements
                .forEach(webElement -> {
                    webElement.click();
                    log.debug("Enabling filter: {}", webElement.getText());
                });
    }

    public void clickConfirm() {
        Button.create(driver, FILTERS_CONFIRM_BUTTON_TEXT).click();
        log.debug(CLICK_BTN + FILTERS_CONFIRM_BUTTON_TEXT);
    }
}
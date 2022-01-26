package com.oss.framework.iaa.widgets.dpe.toolbarpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;

public class TopNPanel {

    private static final Logger log = LoggerFactory.getLogger(TopNPanel.class);

    private static final String PERFORM_BUTTON_ID = "drill-dow-confirm-button";
    private static final String TOP_N_PANEL_XPATH = "//div[@data-testid='drill-down-menu']";
    private static final String DIMENSION_COMBOBOX_XPATH = ".//div[@data-testid='drill-down-dimension-select']";
    private static final String LEVEL_COMBOBOX_XPATH = ".//div[@data-testid='level-drill-down-select']";
    private static final String CLICK_BTN = "Clicking button: ";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement topNPanelElement;

    private TopNPanel(WebDriver driver, WebDriverWait webDriverWait, WebElement topNPanelElement) {
        this.driver = driver;
        this.wait = webDriverWait;
        this.topNPanelElement = topNPanelElement;
    }

    public static TopNPanel create(WebDriver driver, WebDriverWait wait) {
        WebElement topNPanel = driver.findElement(By.xpath(TOP_N_PANEL_XPATH));
        return new TopNPanel(driver, wait, topNPanel);
    }

    public void setDimension(String dimensionId) {
        WebElement dimensionCombobox = topNPanelElement.findElement(By.xpath(DIMENSION_COMBOBOX_XPATH));
        dimensionCombobox.click();
        selectOptionById(dimensionId);
        log.debug("Setting dimension: {}", dimensionId);
    }

    public void setLevel(String levelId) {
        WebElement levelCombobox = topNPanelElement.findElement(By.xpath(LEVEL_COMBOBOX_XPATH));
        levelCombobox.click();
        selectOptionById(levelId);
        log.debug("Setting drill down levet to: {}", levelId);
    }

    public void clickPerform() {
        Button.createById(driver, PERFORM_BUTTON_ID).click();
        log.debug(CLICK_BTN + "Perform");
    }

    private void selectOptionById(String optionId) {
        WebElement optionToSelect = driver.findElement(By.xpath("//div[@data-testid='" + optionId + "']"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", optionToSelect);
        js.executeScript("arguments[0].click();", optionToSelect);
    }
}

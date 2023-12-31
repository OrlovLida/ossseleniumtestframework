package com.oss.framework.widgets.commonhierarchy;

import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

/**
 * @author Ewa Frączek
 */

public class CommonHierarchyApp extends Widget {

    private static final String HORIZONTAL_SECTION_PATTERN = "//div[@class='CommonHierarchyAppList horizontal'][%d]";
    private static final String SEARCH_FIELD_PATH = "//input[contains(@class, 'form-control SearchText')]";
    private static final String SINGLE_CHOOSABLE_ELEMENT_PATH = "//ul[(@class = 'levelElementsList')]//li[@class='levelElement']";
    private static final String ACTION_BUTTON_CSS = "button.squareButton";
    private static final String COMPONENT_CLASS_NAME = "CommonHierarchyApp";
    private static final String ELEMENT_TO_CLICK_PATTERN = "//span[text()='%s']";

    private CommonHierarchyApp(WebDriver driver, WebDriverWait webDriverWait, String widgetId) {
        super(driver, webDriverWait, widgetId);
    }

    public static CommonHierarchyApp create(WebDriver driver, WebDriverWait wait, String widgetId) {
        waitForWidget(wait, COMPONENT_CLASS_NAME);
        waitForWidgetById(wait, widgetId);
        return new CommonHierarchyApp(driver, wait, widgetId);
    }

    /**
     * Goes through CommonHierarchyApp using pathLabels.
     * Should be used when there is no selection of elements at deepest level of hierarchy
     *
     * @param pathLabels path to go through. Example: locationName, deviceName
     */
    public void navigateToPath(String... pathLabels) {
        for (int depthLevel = 0; depthLevel < pathLabels.length; ++depthLevel) {
            String horizontalSectionPath = String.format(HORIZONTAL_SECTION_PATTERN, depthLevel + 1);
            searchIfAvailable(depthLevel, pathLabels[depthLevel]);
            String elementPath = String.format(ELEMENT_TO_CLICK_PATTERN, pathLabels[depthLevel]);
            WebElement elementToChoose = webElement.findElement(By.xpath(horizontalSectionPath + elementPath));
            elementToChoose.click();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
    }

    /**
     * Goes through CommonHierarchyApp using pathLabels.
     * Should be used when there is selection of elements at deepest level of hierarchy
     *
     * @param valueLabels values on which should action be executed at deepest level. Example: names of interfaces
     * @param actionName  action that should be executed on valueLabels. Example: Remove/Select
     * @param pathLabels  path to go through. Example: locationName, deviceName
     */
    public void callAction(List<String> valueLabels, String actionName, String... pathLabels) {
        navigateToPath(pathLabels);
        String deepestHorizontalSectionPath = String.format(HORIZONTAL_SECTION_PATTERN, pathLabels.length + 1);
        for (String valueLabel : valueLabels) {
            searchIfAvailable(pathLabels.length, valueLabel);
            List<WebElement> rowCandidates = webElement.findElements(By.xpath(deepestHorizontalSectionPath +
                    SINGLE_CHOOSABLE_ELEMENT_PATH));
            callAction(valueLabel, rowCandidates, actionName);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
    }

    /**
     * Goes through CommonHierarchyApp using pathLabels.
     * Should be used when there is selection of elements at deepest level of hierarchy
     * Calls available action of valueLabels. If there is remove available - calls it. If select - calls it.
     *
     * @param valueLabels values that should be selected at deepest level. Example: names of interfaces
     * @param pathLabels  path to go through. Example: locationName, deviceName
     */
    public void callAvailableAction(List<String> valueLabels, String... pathLabels) {
        callAction(valueLabels, "", pathLabels);
    }

    /**
     * Goes through CommonHierarchyApp using pathLabels.
     * Should be used when there is selection of elements at deepest level of hierarchy
     * Acts the same as method callAction with parameter actionName set as 'Select'
     *
     * @param valueLabels values that should be selected at deepest level. Example: names of interfaces
     * @param pathLabels  path to go through. Example: locationName, deviceName
     */
    public void selectValue(List<String> valueLabels, String... pathLabels) {
        callAction(valueLabels, "Select", pathLabels);
    }

    private void callAction(String valueLabel, List<WebElement> rowCandidates, String action) {
        Optional<WebElement> actionButton = rowCandidates.stream()
                .filter(row -> row.getText().contains(valueLabel))
                .map(row -> row.findElements(By.cssSelector("[" + CSSUtils.TEST_ID + "='" + action + "']")).stream()
                        .findFirst()
                        .orElse(row.findElement(By.cssSelector(ACTION_BUTTON_CSS))))
                .findFirst();
        actionButton.ifPresent(WebElement::click);
    }

    private void searchIfAvailable(int depthLevel, String phraseToSearchFor) {
        String horizontalSectionPath = String.format(HORIZONTAL_SECTION_PATTERN, depthLevel + 1);
        if (isSearchFieldPresent(depthLevel)) {
            WebElement searchField = webElement.findElement(By.xpath(horizontalSectionPath + SEARCH_FIELD_PATH));
            searchField.sendKeys(Keys.CONTROL + "a");
            searchField.sendKeys(Keys.DELETE);
            searchField.sendKeys(phraseToSearchFor);
            searchField.sendKeys(Keys.ENTER);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
    }

    private boolean isSearchFieldPresent(int depthLevel) {
        String horizontalSectionPath = String.format(HORIZONTAL_SECTION_PATTERN, depthLevel + 1);
        return !webElement.findElements(By.xpath(horizontalSectionPath + SEARCH_FIELD_PATH)).isEmpty();
    }


}
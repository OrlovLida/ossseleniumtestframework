package com.oss.framework.widgets;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.LocatingUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * @author Ewa Frączek
 */

public class CommonHierarchyApp extends Widget {

    private static final String HORIZONTAL_SECTION_PATTERN = "//div[@class='CommonHierarchyAppList horizontal'][%d]";
    private static final String SEARCH_FIELD_PATH = "//input[contains(@class, 'form-control SearchText')]";

    public static CommonHierarchyApp createByClass(WebDriver driver, WebDriverWait webDriverWait) {
        return new CommonHierarchyApp(driver, "CommonHierarchyApp", webDriverWait);
    }

    private CommonHierarchyApp(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        super(driver, widgetClass, webDriverWait);
    }

    public void setFirstObjectInHierarchy(String value){
        setValue(1, value);
        selectObject(value);
    }

    public void setNextObjectInHierarchy(String objectName){
        selectObject(objectName);
    }

    private void setValue(int hierarchyLevel, String value) {
        webElement.click();
        DelayUtils.sleep();//wait for cursor
        webElement.findElement(By.xpath("(.//input)["+hierarchyLevel+"]"))
                .sendKeys(value);
        this.webElement.findElement(By.xpath("(.//button)")).click();
        this.webElement.findElement(By.xpath("(.//button)")).click();
        DelayUtils.sleep();
    }

    private void selectObject(String value){
        String searchResultXpath = "(//div[@class='CommonHierarchyApp']//span[text()='"+value+"'])";
        LocatingUtils.waitUsingXpath(searchResultXpath, webDriverWait);
        webElement.findElement(By.xpath(searchResultXpath)).click();
    }

    /**
     * Goes through CommonHierarchyApp using pathLabels.
     * Should be used when there is no selection of elements at deepest level of hierarchy
     * @param pathLabels path to go through. Example: locationName, deviceName
     */
    public void navigateToPath(String... pathLabels) {
        for(int depthLevel = 0; depthLevel < pathLabels.length; ++depthLevel){
            String horizontalSectionPath = String.format(HORIZONTAL_SECTION_PATTERN, depthLevel + 1);
            searchIfAvailable(depthLevel, pathLabels[depthLevel]);
            WebElement elementToChoose = webElement.findElement(By.xpath(horizontalSectionPath + "//span[text()='" + pathLabels[depthLevel] + "']"));
            elementToChoose.click();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
    }

    /**
     * Goes through CommonHierarchyApp using pathLabels.
     * Should be used when there is selection of elements at deepest level of hierarchy
     * @param valueLabels values on which should action be executed at deepest level. Example: names of interfaces
     * @param actionName action that should be executed on valueLabels. Example: Remove/Select
     * @param pathLabels path to go through. Example: locationName, deviceName
     */
    public void callAction(List<String> valueLabels, String actionName, String... pathLabels){
        navigateToPath(pathLabels);
        String deepestHorizontalSectionPath = String.format(HORIZONTAL_SECTION_PATTERN, pathLabels.length + 1);
        for(String valueLabel: valueLabels) {
            searchIfAvailable(pathLabels.length, valueLabel);
            List<WebElement> rowCandidates = webElement.findElements(By.xpath(deepestHorizontalSectionPath +
                    "//ul[(@class = 'levelElementsList')]//li[@class='levelElement']"));
            makeActionOnCorrectElement(valueLabel, rowCandidates, actionName);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
    }

    /**
     * Goes through CommonHierarchyApp using pathLabels.
     * Should be used when there is selection of elements at deepest level of hierarchy
     * Calls available action of valueLabels. If there is remove available - calls it. If select - calls it.
     * @param valueLabels values that should be selected at deepest level. Example: names of interfaces
     * @param pathLabels path to go through. Example: locationName, deviceName
     */
    public void callAvailableAction(List<String> valueLabels, String... pathLabels){
        callAction(valueLabels, "", pathLabels);
    }

    /**
     * Goes through CommonHierarchyApp using pathLabels.
     * Should be used when there is selection of elements at deepest level of hierarchy
     * Acts the same as method callAction with parameter actionName set as 'Select'
     * @param valueLabels values that should be selected at deepest level. Example: names of interfaces
     * @param pathLabels path to go through. Example: locationName, deviceName
     */
    public void selectValue(List<String> valueLabels, String... pathLabels){
        callAction(valueLabels, "Select", pathLabels);
    }


    private void makeActionOnCorrectElement(String valueLabel, List<WebElement> rowCandidates, String action) {
        for (WebElement correctRowCandidate : rowCandidates) {
            String elementText = correctRowCandidate.getText();
            if (elementText.contains(valueLabel) && elementText.contains(action)) {
                WebElement optionButton = correctRowCandidate.findElement(By.xpath(".//button[contains(@class, 'squareButton')]"));
                optionButton.click();
                DelayUtils.waitForPageToLoad(driver, webDriverWait);
            }
        }
    }

    private void searchIfAvailable(int depthLevel, String phraseToSearchFor) {
        String horizontalSectionPath = String.format(HORIZONTAL_SECTION_PATTERN, depthLevel + 1);
        if(isSearchFieldPresent(depthLevel)){
            WebElement searchField = webElement.findElement(By.xpath(horizontalSectionPath + SEARCH_FIELD_PATH));
            searchField.clear();
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

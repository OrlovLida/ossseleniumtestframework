package com.oss.framework.components.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;

public class ListAttributesChooser {

    private static final Logger log = LoggerFactory.getLogger(ListAttributesChooser.class);

    private static final String X_PATH_ID = "//div[@id='attributes-management']";
    private static final String APPLY_BUTTON_XPATH = ".//a[contains(@class,'btn-primary')]";
    private static final String CANCEL_BUTTON_XPATH = ".//div[@class='management-basic-buttons']/a[contains(@class,'btn-flat')]";
    private static final String DEFAULT_BUTTON_XPATH = ".//div[@class='management-default-button']/a[contains(@class,'btn-flat')]";
    private static final String INACTIVE_LIST_LAYOUT_BUTTON_XPATH = ".//button[@title='List']";
    private static final String INACTIVE_TWO_COLUMNS_LAYOUT_BUTTON_XPATH = ".//button[@title='Two columns']";
    private static final String ACTIVE_TWO_COLUMNS_LAYOUT_BUTTON_XPATH = ".//button[@class='is-columns-layout']";
    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final WebElement listAttributesChooserElement;

    private ListAttributesChooser(WebDriver driver, WebDriverWait webDriverWait, WebElement listAttributesChooserElement) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.listAttributesChooserElement = listAttributesChooserElement;
    }

    public static ListAttributesChooser create(WebDriver driver, WebDriverWait webDriverWait) {
        DelayUtils.waitByXPath(webDriverWait, X_PATH_ID);
        WebElement listAttributesChooser = driver.findElement(By.xpath(X_PATH_ID));
        return new ListAttributesChooser(driver, webDriverWait, listAttributesChooser);
    }

    public void enableAttributeById(String attributeId) {
        if (!isAttributeSelected(attributeId)) {
            getAttribute(attributeId).click();
        }
    }

    public void disableAttributeById(String attributeId) {
        if (isAttributeSelected(attributeId)) {
            getAttribute(attributeId).click();
        }
    }

    public boolean isAttributeSelected(String attributeId) {
        return getAttribute(attributeId).isSelected();
    }

    public void dragColumnToTarget(String sourceId, String targetId) {
        DragAndDrop.dragAndDrop(getDraggableElement(sourceId), getDropElement(targetId), driver);
    }

    public void clickApply() {
        this.listAttributesChooserElement.findElement(By.xpath(APPLY_BUTTON_XPATH)).click();
    }

    public void clickCancel() {
        this.listAttributesChooserElement.findElement(By.xpath(CANCEL_BUTTON_XPATH)).click();
    }

    public void clickDefaultSettings() {
        this.listAttributesChooserElement.findElement(By.xpath(DEFAULT_BUTTON_XPATH)).click();
    }

    public void clickSave() {
        Button.createByIcon(driver, "OSSIcon fa fa-save", "save").click();
    }

    public void selectTwoColumnsLayout() {
        if (!isTwoColumnsLayoutActive()) {
            this.listAttributesChooserElement.findElement(By.xpath(INACTIVE_TWO_COLUMNS_LAYOUT_BUTTON_XPATH)).click();
            log.debug("Switching to Two Columns Layout");
        } else {
            log.debug("Selected layout is: Two Columns");
        }
    }

    public void selectListLayout() {
        if (isTwoColumnsLayoutActive()) {
            this.listAttributesChooserElement.findElement(By.xpath(INACTIVE_LIST_LAYOUT_BUTTON_XPATH)).click();
            log.debug("Switching to List Layout");
        } else {
            log.debug("Selected layout is: List");
        }
    }

    private DragAndDrop.DraggableElement getDraggableElement(String columnId) {
        return new DragAndDrop.DraggableElement(dragOrDropElement(columnId));
    }

    private DragAndDrop.DropElement getDropElement(String columnId) {
        return new DragAndDrop.DropElement(dragOrDropElement(columnId));
    }

    private WebElement getAttribute(String attributeId) {
        return this.listAttributesChooserElement.findElement(By.xpath(".//input[@id='checkbox-" + attributeId + "']"));
    }

    private WebElement dragOrDropElement(String columnId) {
        return this.listAttributesChooserElement.findElement(By.xpath(".//div[@data-rbd-drag-handle-draggable-id='" + columnId + "']"));
    }

    private boolean isTwoColumnsLayoutActive() {
        return !this.listAttributesChooserElement.findElements(By.xpath(ACTIVE_TWO_COLUMNS_LAYOUT_BUTTON_XPATH)).isEmpty();
    }
}

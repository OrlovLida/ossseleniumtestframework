package com.oss.framework.components.common;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListAttributesChooser {

    private static final Logger log = LoggerFactory.getLogger(ListAttributesChooser.class);

    private static final String X_PATH_ID = "//div[@id='attributes-management']";
    private static final String APPLY_BUTTON_XPATH = ".//a[contains(@class,'btn-primary')]";
    private static final String CANCEL_BUTTON_XPATH = ".//div[@class='management-basic-buttons']/a[contains(@class,'btn-flat')]";
    private static final String DEFAULT_BUTTON_XPATH = ".//div[@class='management-default-button']/a[contains(@class,'btn-flat')]";

    public static ListAttributesChooser create(WebDriver driver, WebDriverWait webDriverWait) {
        DelayUtils.waitByXPath(webDriverWait, X_PATH_ID);
        WebElement listAttributesChooser = driver.findElement(By.xpath(X_PATH_ID));
        return new ListAttributesChooser(driver, webDriverWait, listAttributesChooser);
    }

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;
    private final WebElement listAttributesChooser;

    private ListAttributesChooser(WebDriver driver, WebDriverWait webDriverWait, WebElement listAttributesChooser) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
        this.listAttributesChooser = listAttributesChooser;
    }

    public ListAttributesChooser enableAttributeById(String attributeId) {
        if (!isAttributeSelected(attributeId)) {
            attribute(attributeId).click();
        }
        return this;
    }

    public ListAttributesChooser disableAttributeById(String attributeId) {
        if (isAttributeSelected(attributeId)) {
            attribute(attributeId).click();
        }
        return this;
    }

    public boolean isAttributeSelected(String attributeId) {
        return attribute(attributeId).isSelected();
    }

    public void dragColumnToTarget(String sourceId, String targetId) {
        DragAndDrop.dragAndDrop(getDraggableElement(sourceId), getDropElement(targetId), driver);
    }

    private DragAndDrop.DraggableElement getDraggableElement(String columnId) {
        return new DragAndDrop.DraggableElement(dragOrDropElement(columnId));
    }

    private DragAndDrop.DropElement getDropElement(String columnId) {
        return new DragAndDrop.DropElement(dragOrDropElement(columnId));
    }

    public void clickApply() {
        this.listAttributesChooser.findElement(By.xpath(APPLY_BUTTON_XPATH)).click();
    }

    public void clickCancel() {
        this.listAttributesChooser.findElement(By.xpath(CANCEL_BUTTON_XPATH)).click();
    }

    public void clickDefaultSettings() {
        this.listAttributesChooser.findElement(By.xpath(DEFAULT_BUTTON_XPATH)).click();
    }

    public void clickSaveSelectedColumnsButton() {
        Button.createByIcon(driver, "OSSIcon fa fa-save", "save").click();
    }

    public void selectToTwoColumnsLayout() {
        if (this.listAttributesChooser.findElements(By.xpath(".//button[@class='is-columns-layout']")).isEmpty()) {
            this.listAttributesChooser.findElement(By.xpath(".//button[@title='Two columns']")).click();
            log.debug("Switching to Two Columns Layout");
        } else {
            log.debug("Selected layout is: Two Columns");
        }
    }

    public void selectListColumnsLayout() {
        if (this.listAttributesChooser.findElements(By.xpath(".//button[@class='is-list-layout']")).isEmpty()) {
            this.listAttributesChooser.findElement(By.xpath(".//button[@title='List']")).click();
            log.debug("Switching to List Layout");
        } else {
            log.debug("Selected layout is: list");
        }
    }

    private WebElement attribute(String attributeId) {
        return this.listAttributesChooser.findElement(By.xpath(".//input[@id='checkbox-" + attributeId + "']"));
    }

    private WebElement dragOrDropElement(String columnId) {
        return this.listAttributesChooser.findElement(By.xpath(".//div[@data-rbd-drag-handle-draggable-id='" + columnId + "']"));
    }
}

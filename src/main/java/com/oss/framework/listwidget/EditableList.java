/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.listwidget;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.InlineForm;
import com.oss.framework.components.Input;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;

/**
 * @author Gabriela Kasza
 */
public class EditableList extends Widget {
    private static final String LIST_WIDGET_CLASS = "ExtendedList";
    private static final String XPATH_ADD_ROW = "//button[contains(@class, 'add-row-button')]";
    private static final String XPATH_ROWS_OF_LIST = "//li[contains(@class,'editableListElement')]";
    private static final String TEXT_CONTAINER = "textContainer";

    public static EditableList create(WebDriver driver, WebDriverWait webDriverWait){
        DelayUtils.waitBy(webDriverWait, By.className(LIST_WIDGET_CLASS));
        return new EditableList(driver,LIST_WIDGET_CLASS,webDriverWait);
    }

    private EditableList(WebDriver driver, String widgetClass, WebDriverWait webDriverWait) {
        super(driver, widgetClass, webDriverWait);
    }
    public void addRow(){
        DelayUtils.waitByXPath(webDriverWait,XPATH_ADD_ROW);
        WebElement row = driver.findElement(By.xpath(XPATH_ADD_ROW));
        row.click();
    }
    public void setValue(String value, String columnId, int row, String componentId, Input.ComponentType componentType){
        WebElement webElement = selectRow(row - 1);
        WebElement element = selectCell(columnId,webElement);
        setValue(value,element,componentId,componentType);

    }
    public void callActionByLabel(String actionLabel, int row) {
        selectRow(row - 1).click();
        ActionsContainer action = ActionsContainer.createFromParent(webElement, driver, webDriverWait);
        action.callActionByLabel("frameworkObjectButtonsGroup",actionLabel);

    }
    public  void callActionByLabel(String actionLabel, String columnId, String value){
        selectRowByAttributeValue(columnId,value).click();
        ActionsContainer action = ActionsContainer.createFromParent(webElement, driver, webDriverWait);
        action.callActionByLabel("frameworkObjectButtonsGroup",actionLabel);

    }

    private WebElement selectRow(int row) {
        List<WebElement> allRows = driver.findElements(By.xpath(XPATH_ROWS_OF_LIST));
         return allRows.get(row);

    }
   private void setValue(String value, WebElement element,String componentId, Input.ComponentType componentType){
       Actions action = new Actions(driver);
       action.moveToElement(element.findElement(By.className(TEXT_CONTAINER))).click().build().perform();

       InlineForm inlineForm = InlineForm.create(driver, webDriverWait);
       Input component = inlineForm.getComponent(componentId, componentType);
       component.setSingleStringValue(value);
       inlineForm.clickButtonByLabel("Save");
   }
   private WebElement selectCell(String columnId, WebElement row){
       DelayUtils.waitByXPath(webDriverWait, "//div[contains(@class,'"+columnId+"')]");
       return row.findElement(By.xpath(".//div[contains(@class,'"+columnId+"')]"));

   }

    private WebElement selectRowByAttributeValue(String columnId, String value) {
        List<WebElement> allRows = driver.findElements(By.xpath(XPATH_ROWS_OF_LIST));
        for (WebElement row : allRows){
            WebElement element = selectCell(columnId, row);
            String getValue = element.findElement(By.className(TEXT_CONTAINER)).getText();
            if (getValue.equals(value)){
                return row;
            }
        }
        throw new RuntimeException("Cannot find a row with the provided value");
    }




}

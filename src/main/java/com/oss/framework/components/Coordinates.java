package com.oss.framework.components;

import com.oss.framework.data.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class Coordinates extends Input {

    private final WebElement labelN = webElement.findElement(By.xpath(".//label[contains(@for,'-N')]"));
    private final WebElement inputN = webElement.findElement(By.xpath(".//input[contains(@id,'-N')]"));
    private final WebElement labelS = webElement.findElement(By.xpath(".//label[contains(@for,'-S')]"));
    private final WebElement inputS = webElement.findElement(By.xpath(".//input[contains(@id,'-S')]"));
    private final WebElement inputDegrees = webElement.findElement(By.xpath(".//input[@name='degrees']"));
    private final WebElement inputMinutes = webElement.findElement(By.xpath(".//input[@name='minutes']"));
    private final WebElement inputSeconds = webElement.findElement(By.xpath(".//input[@name='seconds']"));

    static Coordinates create(WebDriver driver, WebDriverWait wait, String componentId) {
        return new Coordinates(driver, wait, componentId);
    }

    static Coordinates create(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        return new Coordinates(parent, driver, wait, componentId);
    }

    private Coordinates(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    private Coordinates(WebElement parent, WebDriver driver, WebDriverWait wait, String componentId) {
        super(parent, driver, wait, componentId);
    }

    @Override
    public void setValue(Data value) {
        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).click().build().perform();

        if (value.getStringValues().get(0).equals("N")) {
            labelN.click();
        } else if (value.getStringValues().get(0).equals("S")) {
            labelS.click();
        }

        inputDegrees.clear();
        inputDegrees.sendKeys(value.getStringValues().get(1));
        inputMinutes.clear();
        inputMinutes.sendKeys(value.getStringValues().get(2));
        inputSeconds.clear();
        inputSeconds.sendKeys(value.getStringValues().get(3));
    }

    @Override
    public void setValueContains(Data value) {

    }

    @Override
    public Data getValue() {
        List<String> getList = new ArrayList<>();

        if (inputN.isSelected()) {
            getList.add(labelN.getText());
        } else if (inputS.isSelected()) {
            getList.add(labelS.getText());
        }

        getList.add(inputDegrees.getAttribute("value"));
        getList.add(inputMinutes.getAttribute("value"));
        getList.add(inputSeconds.getAttribute("value"));
        return Data.createMultiData(getList);
    }

    @Override
    public void clear() {
        List<String> clearList = new ArrayList<>();
        clearList.add("N");
        for (int i = 0; i < 3; i++) {
            clearList.add("0");
        }
        setValue(Data.createMultiData(clearList));
    }

    @Override
    public String getLabel() {
        return webElement.findElement(By.xpath(".//label")).getText();
    }

}

package com.oss.framework.components.inputs;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.data.Data;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class FileChooser extends Input {
    public static final String DELETE_CLASS = "delete";
    public static final String UPLOAD_STATUS_XPATH = ".//span[@class='uploadStatus']";
    public static final String FILE_NAME_CLASS = "fileName";
    private static final String INPUT_XPATH = "//input[@type='file']";
    private static final String UPLOAD_SUCCESS_XPATH = "//span[@class='uploadStatus'][text()='Upload success']";
    private static final String POPUP_XPATH = "//ul[@class='UploadedFiles']";

    private FileChooser(WebDriver driver, WebDriverWait wait, String componentId) {
        super(driver, wait, componentId);
    }

    public static FileChooser create(WebDriver driver, WebDriverWait wait, String componentId) {
        DelayUtils.waitByXPath(wait, "//div[@" + CSSUtils.TEST_ID + "='" + componentId + "']");
        return new FileChooser(driver, wait, componentId);
    }

    @Override
    public void setValueContains(Data value) {
        throw new UnsupportedOperationException("Method not implemented for the File Chooser");
    }

    @Override
    public Data getValue() {
        List<String> names = new ArrayList<>();
        DelayUtils.waitByXPath(webDriverWait, POPUP_XPATH);

        List<WebElement> fileNames = webElement.findElements(By.className(FILE_NAME_CLASS));
        for (WebElement fileName : fileNames) {
            names.add(fileName.getText());
        }
        return Data.createMultiData(names);
    }

    @Override
    public void setValue(Data value) {
        DelayUtils.waitByElement(webDriverWait, this.webElement);
        WebElement elem = webElement.findElement(By.xpath(INPUT_XPATH));
        String js = "arguments[0].style.height='50px'; arguments[0].style.visibility='visible'; arguments[0].style.display='block';";
        ((JavascriptExecutor) driver).executeScript(js, elem);
        ((JavascriptExecutor) driver).executeScript("HTMLInputElement.prototype.click = function(){}");
        elem.sendKeys(value.getStringValue());
        ((JavascriptExecutor) driver).executeScript("delete HTMLInputElement.prototype.click");
        DelayUtils.waitByXPath(webDriverWait, UPLOAD_SUCCESS_XPATH);
    }

    @Override
    public void clear() {
        DelayUtils.waitByXPath(webDriverWait, POPUP_XPATH);
        WebElement attachments = webElement.findElement(By.xpath(POPUP_XPATH));
        while (!webElement.findElements(By.xpath(POPUP_XPATH)).isEmpty()) {
            attachments.findElement(By.className(DELETE_CLASS)).click();
        }
    }

    public String getStatus() {
        DelayUtils.waitByXPath(webDriverWait, POPUP_XPATH);
        WebElement importStatus = driver.findElement(By.xpath(UPLOAD_STATUS_XPATH));
        return importStatus.getText();
    }
}

package com.oss.framework.components;

import java.util.ArrayList;
import java.util.List;

import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FileChooser extends Input{


    public static FileChooser create(WebDriver driver, WebDriverWait wait, String componentId){
        DelayUtils.waitByXPath(wait,"//div[@data-attributename='"+componentId+"']");
        return new FileChooser(driver,wait,componentId);
    }

    private FileChooser(WebDriver driver,WebDriverWait wait, String componentId) {
        super(driver,wait,componentId);
    }

    @Override
    public void setValue(Data value) {
        DelayUtils.waitByXPath(webDriverWait,"//div[@data-attributename='file']");
        WebElement elem = webElement.findElement(By.xpath("//input[@type='file']"));
        String js = "arguments[0].style.height='50px'; arguments[0].style.visibility='visible'; arguments[0].style.display='block';";
        ((JavascriptExecutor) driver).executeScript(js, elem);
        ((JavascriptExecutor) driver).executeScript("HTMLInputElement.prototype.click = function(){}");
        elem.sendKeys(value.getStringValue());
        ((JavascriptExecutor) driver).executeScript("delete HTMLInputElement.prototype.click");


    }

    @Override
    public void setValueContains(Data value) {
        throw new RuntimeException("Method not implemented for the File Chooser");
    }

    @Override
    public Data getValue() {
        List<String> names = new ArrayList<String>();
        DelayUtils.waitByXPath(webDriverWait,"//ul[@class='UploadedFiles']");

        List<WebElement> fileNames = webElement.findElements(By.className("fileName"));
        for (WebElement fileName:fileNames) {
            names.add(fileName.getText());
        }
        return Data.createMultiData(names) ;
    }

    @Override
    public void clear() {
        DelayUtils.waitByXPath(webDriverWait,"//ul[@class='UploadedFiles']");
        WebElement attachments = webElement.findElement(By.xpath("//ul[@class='UploadedFiles']"));
        while (webElement.findElements(By.xpath("//ul[@class='UploadedFiles']")).size() != 0){
            attachments.findElement(By.className("delete")).click();
        }
    }
}

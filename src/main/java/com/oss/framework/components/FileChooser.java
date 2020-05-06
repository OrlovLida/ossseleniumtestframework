package com.oss.framework.components;

import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FileChooser {
    private WebDriver driver;
    public FileChooser(WebDriver driver) {
        this.driver = driver;
    }

    public void addAttachment(String directory) {
        DelayUtils.sleep(5000);
        By t1 = By.xpath("//input[@type='file']");
        DelayUtils.sleep(5000);
        WebElement elem = driver.findElement(t1);
         String js = "arguments[0].style.height='50px'; arguments[0].style.visibility='visible'; arguments[0].style.display='block';";
         ((JavascriptExecutor) driver).executeScript(js, elem);
         ((JavascriptExecutor) driver).executeScript("HTMLInputElement.prototype.click = function(){}");
        DelayUtils.sleep(7000);
        elem.sendKeys(directory);
         ((JavascriptExecutor) driver).executeScript("delete HTMLInputElement.prototype.click");
        DelayUtils.sleep(5000);
    }
}

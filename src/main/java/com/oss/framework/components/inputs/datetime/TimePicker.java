package com.oss.framework.components.inputs.datetime;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class TimePicker {
    private final WebDriver driver;

    private final By hoursContainer = By.xpath("(//div[@class='timePicker-valuePickerContainer'])[1]");
    private final By minutesContainer = By.xpath("(//div[@class='timePicker-valuePickerContainer'])[2]");
    private final By secondsContainer = By.xpath("(//div[@class='timePicker-valuePickerContainer'])[3]");
    private final By upButton = By.xpath(".//div[contains(@class,'navButton--more')]");
    private final By downButton = By.xpath(".//div[contains(@class,'navButton--less')]");
    private final By value = By.xpath(".//div[@class='timePicker-value']");

    private TimePicker(WebDriver driver) {
        this.driver = driver;
    }

    public static TimePicker create(WebDriver driver, WebDriverWait webDriverWait) {
        String timePickerXpath = "//div[@" + CSSUtils.TEST_ID + "='dateTimePicker']";
        DelayUtils.waitByXPath(webDriverWait, timePickerXpath);
        return new TimePicker(driver);
    }

    public void chooseTime(String time) {

        List<String> timeList = Arrays.asList(time.split(":"));

        int hours = Integer.parseInt(timeList.get(0));
        int minutes = Integer.parseInt(timeList.get(1));

        pickInContainer(hoursContainer(), hours);
        pickInContainer(minutesContainer(), minutes);

        if (timeList.size() > 2) {
            int seconds = Integer.parseInt(timeList.get(2));
            pickInContainer(secondsContainer(), seconds);
        }

    }

    public void pickInContainer(WebElement container, int value) {
        WebElement upButtonElement = container.findElement(this.upButton);
        WebElement downButtonElement = container.findElement(this.downButton);

        int currentValue = getContainerValue(container);

        if (value > currentValue) {
            for (int i = 0; i < value - currentValue; i++) {
                upButtonElement.click();
            }
        } else if (currentValue > value) {
            for (int i = 0; i < currentValue - value; i++) {
                downButtonElement.click();
            }
        }
    }

    private WebElement hoursContainer() {
        return driver.findElement(this.hoursContainer);
    }

    private WebElement minutesContainer() {
        return driver.findElement(this.minutesContainer);
    }

    private WebElement secondsContainer() {
        return driver.findElements(this.secondsContainer).stream().findFirst()
                .orElseThrow(() -> new RuntimeException("It is not possible provide seconds"));
    }

    private int getContainerValue(WebElement container) {
        return Integer.parseInt(container.findElement(value).getText());
    }

}

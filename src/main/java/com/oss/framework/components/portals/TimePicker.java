package com.oss.framework.components.portals;

import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

public class TimePicker {
    private final WebDriver driver;
    protected WebElement webElement;

    private final By hoursContainer = By.xpath("(//div[@class='timePicker-valuePickerContainer'])[1]");
    private final By minutesContainer = By.xpath("(//div[@class='timePicker-valuePickerContainer'])[2]");
    private final By secondsContainer = By.xpath("(//div[@class='timePicker-valuePickerContainer'])[3]");
    private final By timeIcon = By.xpath(".//i[contains(@class,'fa-clock-o')]");
    private final By upButton = By.xpath(".//div[contains(@class,'navButton--more')]");
    private final By downButton = By.xpath(".//div[contains(@class,'navButton--less')]");
    private final By value = By.xpath(".//div[@class='timePicker-value']");

    public static TimePicker create(WebDriver driver, WebElement webElement) {
        DelayUtils.sleep();
        return new TimePicker(driver, webElement);
    }

    public TimePicker(WebDriver driver, WebElement webElement) {
        this.webElement = webElement;
        this.driver = driver;
    }

    private WebElement hoursContainer() {
        WebElement hoursContainer = driver.findElement(this.hoursContainer);
        return hoursContainer;
    }

    private WebElement minutesContainer() {
        WebElement minutesContainer = driver.findElement(this.minutesContainer);
        return minutesContainer;
    }

    private WebElement secondsContainer() {
        WebElement secondsContainer = driver.findElement(this.secondsContainer);
        return secondsContainer;
    }


    public void chooseTime(String time) {
        clickClock();

        List<String> timeList = Arrays.asList(time.split(":"));

        int hours = Integer.parseInt(timeList.get(0));
        int minutes = Integer.parseInt(timeList.get(1));

        pickInContainer(hoursContainer(), hours);
        pickInContainer(minutesContainer(), minutes);

        if (timeList.size() > 2) {
            int seconds = Integer.parseInt(timeList.get(0));
            pickInContainer(secondsContainer(), seconds);
        }

        clickClock();
    }

    private int getContainerValue(WebElement container) {
        return Integer.parseInt(container.findElement(value).getText());
    }

    public void pickInContainer(WebElement container, int value) {
        WebElement upButton = container.findElement(this.upButton);
        WebElement downButton = container.findElement(this.downButton);

        int currentValue = getContainerValue(container);

        if (value > currentValue) {
            for (int i = 0; i < value - currentValue; i++) {
                upButton.click();
            }
        } else if (currentValue > value) {
            for (int i = 0; i < currentValue - value; i++) {
                downButton.click();
            }
        }
    }

    public void clickClock() {
        DelayUtils.sleep(200);
        webElement.findElement(this.timeIcon).click();
        DelayUtils.sleep(200);
    }
}

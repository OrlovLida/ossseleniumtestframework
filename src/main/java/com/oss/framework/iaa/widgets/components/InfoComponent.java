package com.oss.framework.iaa.widgets.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class InfoComponent {

    private static final String INFO_COMPONENT_CLASS_CSS = ".info-component";
    private static final String INFO_COMPONENT_MESSAGE_CLASS = "info-component-message";
    private static final String ICON_SUCCESS_XPATH = "//i[contains(@class, 'text-success')]";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement infoComponentElement;

    private InfoComponent(WebDriver driver, WebDriverWait wait, WebElement infoComponent) {
        this.driver = driver;
        this.wait = wait;
        this.infoComponentElement = infoComponent;
    }

    public static InfoComponent create(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitBy(wait, By.cssSelector(INFO_COMPONENT_CLASS_CSS));
        WebElement infoComponent = driver.findElement(By.cssSelector(INFO_COMPONENT_CLASS_CSS));
        return new InfoComponent(driver, wait, infoComponent);
    }

    public String getInfoComponentMessage() {
        return infoComponentElement.findElement(By.className(INFO_COMPONENT_MESSAGE_CLASS)).getText();
    }

    public boolean isIconSuccessPresent() {
        return WebElementUtils.isElementPresent(infoComponentElement, By.xpath(ICON_SUCCESS_XPATH));
    }
}

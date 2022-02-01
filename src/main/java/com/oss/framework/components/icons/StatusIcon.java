package com.oss.framework.components.icons;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;

public class StatusIcon {

    private static final String SERVICE_ICONS_AREA_XPATH = "//*[@" + CSSUtils.TEST_ID + "='ci-services-icons']";
    private static final String LIST_ICON_CIRCLES_XPATH = ".//div[starts-with(@class, 'icon-wrapper')]";
    private static final String LIST_ICON_LABELS_XPATH = ".//div[starts-with(@class, 'icon-panel__label')]";
    private static final String ICON_SUCCESS = "btn-success";
    private static final String ICON_PANEL_XPATH = ".//div[@class='icon-panel']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement iconListWidgetElement;

    private StatusIcon(WebDriver driver, WebDriverWait wait, WebElement iconListWidget) {
        this.driver = driver;
        this.wait = wait;
        this.iconListWidgetElement = iconListWidget;
    }

    public static StatusIcon createStatusIcon(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitByXPath(wait, SERVICE_ICONS_AREA_XPATH);
        WebElement iconListWidget = driver.findElement(By.xpath(SERVICE_ICONS_AREA_XPATH));
        return new StatusIcon(driver, wait, iconListWidget);
    }

    public List<IconItem> getIcons() {
        return iconListWidgetElement.findElements(By.xpath(ICON_PANEL_XPATH)).stream()
                .map(iconItem -> IconItem.create(driver, wait, iconItem))
                .collect(Collectors.toList());
    }

    public static class IconItem {

        private final WebDriver driver;
        private final WebDriverWait wait;
        private final WebElement iconElement;

        private IconItem(WebDriver driver, WebDriverWait wait, WebElement iconItem) {
            this.driver = driver;
            this.wait = wait;
            this.iconElement = iconItem;
        }

        public static IconItem create(WebDriver driver, WebDriverWait wait, WebElement iconItem) {
            return new IconItem(driver, wait, iconItem);
        }

        public boolean isIconGreen() {
            return iconElement.findElement(By.xpath(LIST_ICON_CIRCLES_XPATH)).getAttribute("class").contains(ICON_SUCCESS);
        }

        public String getIconLabel() {
            return iconElement.findElement(By.xpath(LIST_ICON_LABELS_XPATH)).getText();
        }
    }
}
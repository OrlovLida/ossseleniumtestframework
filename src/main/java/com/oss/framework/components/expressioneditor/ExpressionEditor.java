package com.oss.framework.components.expressioneditor;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.WebElementUtils;

public class ExpressionEditor {

    private static final String EXPRESSION_EDITOR_CLASS_XPATH = "//div[starts-with(@class, 'expression-editor')]";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement expressionEditorElement;

    private ExpressionEditor(WebDriver driver, WebDriverWait wait, WebElement expressionEditorElement) {
        this.driver = driver;
        this.wait = wait;
        this.expressionEditorElement = expressionEditorElement;
    }

    public static ExpressionEditor createById(WebDriver driver, WebDriverWait wait, String componentId) {
        DelayUtils.waitBy(wait, By.xpath(EXPRESSION_EDITOR_CLASS_XPATH));
        WebElement expressionEditor = driver.findElement(By.cssSelector(String.format(CSSUtils.WEB_ELEMENT_PATTERN, componentId)));
        return new ExpressionEditor(driver, wait, expressionEditor);
    }

    public void setValue(String value) {
        WebElementUtils.moveToElement(driver, expressionEditorElement);
        clear();
        expressionEditorElement.sendKeys(value);
    }

    public String getValue() {
        WebElementUtils.moveToElement(driver, expressionEditorElement);
        return expressionEditorElement.getText();
    }

    private void clear() {
        WebElementUtils.moveToElement(driver, expressionEditorElement);
        expressionEditorElement.sendKeys(Keys.CONTROL + "a");
        expressionEditorElement.sendKeys(Keys.DELETE);
    }
}

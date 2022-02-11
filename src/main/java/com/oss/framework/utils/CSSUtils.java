package com.oss.framework.utils;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CSSUtils {

    public static final String ATTRIBUTES_SEPARATOR = ";";
    public static final String VALUE_SEPARATOR = ": ";
    public static final String STYLE_ATTRIBUTE = "style";
    public static final String TOP_ATTRIBUTE = "top";
    public static final String HEIGHT_ATTRIBUTE = "height";
    public static final String WIDTH_ATTRIBUTE = "width";
    public static final String LEFT_ATTRIBUTE = "left";
    public static final String TEST_ID = "data-testid";
    public static final String DATA_WIDGET_ID = "data-widget-id";

    private static Splitter attributeSplitter = Splitter.on(ATTRIBUTES_SEPARATOR);
    private static Splitter valueSplitter = Splitter.on(VALUE_SEPARATOR);

    private CSSUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<String, String> getStyleAttribute(WebElement webElement) {
        Map<String, String> attributes = Maps.newHashMap();
        String style = webElement.getAttribute(STYLE_ATTRIBUTE);
        attributeSplitter.split(style.trim()).forEach(attribute -> {
            if (!attribute.equals("")) {
                List<String> split = Lists.newArrayList(valueSplitter.split(attribute));
                attributes.put(split.get(0), split.get(1));
            }
        });
        return attributes;
    }

    public static List<String> getAllClasses(WebElement webElement) {
        String aClass = webElement.getAttribute("class");
        Iterable<String> classes = Splitter.on(" ").split(aClass);
        return Lists.newArrayList(classes);
    }

    public static int getTopValue(WebElement webElement) {
        return getIntegerValue(TOP_ATTRIBUTE, webElement);
    }

    public static int getHeightValue(WebElement webElement) {
        return getIntegerValue(HEIGHT_ATTRIBUTE, webElement);
    }

    public static int getWidthValue(WebElement webElement) {
        return getIntegerValue(WIDTH_ATTRIBUTE, webElement);
    }

    public static double getDecimalWidthValue(WebElement webElement) {
        return getDecimalValue(webElement);
    }

    public static int getLeftValue(WebElement webElement) {
        return getIntegerValue(LEFT_ATTRIBUTE, webElement);
    }

    public static int getIntegerValue(String attributeName, WebElement webElement) {
        String value = webElement.getCssValue(attributeName);
        value = value.replaceAll("[a-z]|\\.(.*)", "");
        return Integer.parseInt(value);
    }

    public static String getAttributeValue(String attributeName, WebElement webElement) {
        return webElement.getAttribute(attributeName);
    }

    private static double getDecimalValue(WebElement webElement) {
        String value = webElement.getCssValue(CSSUtils.WIDTH_ATTRIBUTE);
        value = value.replaceAll("[^\\d.]", "");
        return Double.parseDouble(value);
    }
}

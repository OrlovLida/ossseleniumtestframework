package com.oss.framework.utils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebElement;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CSSUtils {

    public static String ATTRIBUTES_SEPARATOR = ";";
    public static String VALUE_SEPARATOR = ": ";

    public static String STYLE_ATTRIBUTE = "style";
    public static String TOP_ATTRIBUTE = "top";
    public static String HEIGHT_ATTRIBUTE = "height";
    public static String WIDTH_ATTRIBUTE = "width";

    private static Splitter attributeSplitter = Splitter.on(ATTRIBUTES_SEPARATOR);
    private static Splitter valueSplitter = Splitter.on(VALUE_SEPARATOR);

    public static Map<String, String> getStyleAttribute(WebElement webElement) {
        Map<String, String> attributes = Maps.newHashMap();
        String style = webElement.getAttribute(STYLE_ATTRIBUTE);
        attributeSplitter.split(style.trim()).forEach(attribute -> {
            if(!attribute.equals("")) {
                List<String> split = Lists.newArrayList(valueSplitter.split(attribute));
                attributes.put(split.get(0), split.get(1));
            }
        });
        return attributes;
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

    private static int getIntegerValue(String attributeName, WebElement webElement) {
        String value = webElement.getCssValue(attributeName);
        value = value.replaceAll("[^\\d.]", "");
        return Integer.valueOf(value);
    }
}

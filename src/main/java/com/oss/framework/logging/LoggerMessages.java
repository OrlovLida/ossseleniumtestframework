package com.oss.framework.logging;

public class LoggerMessages {

    public static String clickButton(String label){
        return String.format("Clicking '%s' button", label);
    }

    public static String clickElement(String element){
        return String.format("Clicking '%s'", element);
    }

    public static String expandNode(String node){
        return String.format("Expanding node: %s", node);
    }

    public static String moveMouseOver(String element){
        return String.format("Moving mouse over %s", element);
    }

    public static String elementPresentAndVisible(String element){
        return String.format("%s is present and visible", element);
    }

    public static String setElementWithValue(String element, String value){
        return String.format("Setting %s: %s", element, value);
    }

}

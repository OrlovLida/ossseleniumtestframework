/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.widgets.propertypanel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;

/**
 * @author Kamil Szota
 */
public interface PropertyPanelInterface {

//    List<WebElement> getProperties();

//    List<String> getPropertyLabels();
//
//    String getNthPropertyLabel(int n);
//
//    Map<String, WebElement> getPropertiesMap();

    String getPropertyValue(String propertyName);
}

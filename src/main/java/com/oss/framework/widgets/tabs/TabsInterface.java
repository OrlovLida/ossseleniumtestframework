/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.widgets.tabs;

import com.oss.framework.iaa.widgets.list.MessageListWidget;

/**
 * @author Gabriela Kasza
 */
public interface TabsInterface {

    void selectTabByLabel(String tabLabel);

    void selectTabById(String id);

    void callActionByLabel(String actionLabel);

    void callActionByLabel(String groupLabel, String label);

    void callActionById(String groupLabel, String id);

    void callActionById(String id);

    boolean isButtonPresent(String text);

    MessageListWidget getMessageListWidget(String cardContainerId);

}

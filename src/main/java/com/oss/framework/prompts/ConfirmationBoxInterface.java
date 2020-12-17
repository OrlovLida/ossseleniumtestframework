/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.framework.prompts;

/**
 * @author Gabriela Kasza
 */
public interface ConfirmationBoxInterface {
    void clickButtonByLabel(String label);
    void clickButtonByDataAttributeName(String dataAttributeName);
    String getMessage();
}

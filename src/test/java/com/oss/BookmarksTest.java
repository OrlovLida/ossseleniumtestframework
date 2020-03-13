package com.oss;

import com.oss.framework.components.Button;
import com.oss.framework.components.Input;
import com.oss.framework.components.Input.ComponentType;
import com.oss.framework.components.ComponentFactory;
import com.oss.framework.components.portals.PopupV2;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.platform.InventoryViewPage;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

public class BookmarksTest extends BaseTestCase {

    private static final String TABLE_WIDGET_URL = String.format("%s/#/views/management/views/inventory-view/Location?perspective=LIVE", BASIC_URL);
    private static final int DEFAULT_COLUMN_WIDTH = 200;
    private InventoryViewPage inventoryViewPage;
    private TableWidget tableWidget;

    @Test
    public void createBookmark() {
        tableWidget = homePage
                .goToInventoryViewPage(TABLE_WIDGET_URL)
                .getTableWidget();
        //tableWidget.clickOnGearIcon();
        //tableWidget.clickColumnsMgmtChbx("Abbreviation");
        //tableWidget.dragAndDropChbx("ID",-120,0);
        //tableWidget.confirmColumnsMgmt();
        //homePage.goToCreateBookmarkPopUp();
        PopupV2 popupV2 = homePage.goToCreateBookmarkPopUp();

        //TODO fix new WebDriver, create Page for bookmarks, try to use wizard class
        Input nameField = ComponentFactory
                .create("viewName", ComponentType.TEXT_FIELD, this.driver, new WebDriverWait(this.driver, 800));
        nameField.setValue(Data.createSingleData("Selenium Bookmark"));
        Assertions.assertThat(nameField.getStringValue()).isEqualTo("Selenium Bookmark");
        Input categoryField = ComponentFactory
                .create("viewCategory", ComponentType.TEXT_FIELD, this.driver, new WebDriverWait(this.driver, 800));
        categoryField.setValue(Data.createSingleData("OSSMFC"));

        DelayUtils.sleep(2000);
        System.out.println(categoryField.getStringValue());

        Assertions.assertThat(categoryField.getStringValue()).isEqualTo("OSSMFC");
        Button saveBookmarkButton = Button.create(driver, "Save", "a");
        saveBookmarkButton.click();
    }
}

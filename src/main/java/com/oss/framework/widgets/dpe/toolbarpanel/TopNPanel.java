package com.oss.framework.widgets.dpe.toolbarpanel;

import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.oss.framework.logging.LoggerMessages.CLICK_BTN;
import static com.oss.framework.logging.LoggerMessages.CLICK_ELEMENT;
import static com.oss.framework.logging.LoggerMessages.SET_ELEMENT;
import static com.oss.framework.utils.WidgetUtils.findElementByXpath;

public class TopNPanel {

    private static final Logger log = LoggerFactory.getLogger(TopNPanel.class);

    private final static String TOP_N_BUTTON_PATH = "//i[@aria-label='DRILL_DOWN']";
    private final static String PERFORM_BUTTON_PATH = "//button[@class='btn performButton']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement webElement;

    public enum TopNPanelInput {
        DIMENSION_INPUT(1),
        NTH_LEVEL(5);

        public final int id;
        private TopNPanelInput(int id){
            this.id = id;
        }
    }

    public static TopNPanel create(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement){
        return new TopNPanel(driver, webDriverWait, webElement);
    }

    private TopNPanel(WebDriver driver, WebDriverWait webDriverWait, WebElement webElement){
        this.driver = driver;
        this.wait = webDriverWait;
        this.webElement = webElement;
    }

    public void openTopNPanel(){
        DelayUtils.waitForClickability(wait, findElementByXpath(this.webElement, TOP_N_BUTTON_PATH));
        findElementByXpath(this.webElement, TOP_N_BUTTON_PATH).click();
        log.debug(CLICK_BTN + "TopN");
    }

    public void setTopNDimension(String dimension){
        getTopNDimensionInput().click();
        log.debug(CLICK_ELEMENT + "TopN dimension combo box");
        chooseOptionFromDropDownInRow(dimension);
        log.debug(SET_ELEMENT + "Top N dimension = " + dimension);
    }

    private WebElement getTopNDimensionInput(){
        //TODO Workaround with unusable component ID, please fix when OSSASR-9595 will be finished
        return findElementByXpath(this.webElement, getTopNPanelInputXPath(TopNPanelInput.DIMENSION_INPUT));
    }

    public void setNthLevel(String nthLevel){
        getNthLevelInput().click();
        log.debug(CLICK_BTN + "nth level combo box");
        chooseOptionFromDropDownInRow(nthLevel);
        log.debug(SET_ELEMENT + "nth level " + nthLevel);
    }

    private WebElement getNthLevelInput(){
        //TODO Workaround with unusable component ID, please fix when OSSASR-9595 will be finished
        return findElementByXpath(this.webElement, getTopNPanelInputXPath(TopNPanelInput.NTH_LEVEL));
    }

    private String getTopNPanelInputXPath(TopNPanelInput topNInput){
        return "(//div[@class='drillDownMenu']/.//div[@class='comboBox'])[" + topNInput.id + "]";
    }

    private void chooseOptionFromDropDownInRow(String choice) {
        List<WebElement> listOfOptions = this.webElement.findElements(By.xpath("//div[@class='CustomSelectList-rowsContainer']/..//*[@class='CustomSelectList-row']"));
        for (WebElement option : listOfOptions) {
            String innerHTML = option.getAttribute("innerHTML");
            if (innerHTML.contains(choice)) {
                option.click();
                log.debug(CLICK_ELEMENT + choice);
                break;
            }
        }
    }

    public void clickPerform(){
        WebElement performButton = findElementByXpath(this.webElement, PERFORM_BUTTON_PATH);
        DelayUtils.waitForClickability(wait, performButton);
        performButton.click();
        log.debug(CLICK_BTN + "Perform");
    }
}

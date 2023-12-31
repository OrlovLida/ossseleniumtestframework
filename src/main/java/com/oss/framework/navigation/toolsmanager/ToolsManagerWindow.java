package com.oss.framework.navigation.toolsmanager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;

import static com.oss.framework.utils.DragAndDrop.dragAndDrop;

public class ToolsManagerWindow {

    private static final String ADD_CATEGORY_BUTTON_CLASS = "tools-manager__bar__add-category";
    private static final String SEARCH_TEST_ID = "search";
    private static final String VIEWS_MANAGER_CSS = ".tools-manager .categories";
    private static final String VIEWS_MANAGER_CLASS = "tools-manager";
    private static final String CANNOT_LOCATE_TOOLS_MANAGER_WINDOW_EXCEPTION = "Cannot locate Tools Manager Window";
    private static final String CANNOT_FIND_APPLICATION_WITH_NAME_EXCEPTION = "Cannot find Application with name: ";
    private static final String CANNOT_FIND_SUBCATEGORY_WITH_NAME_EXCEPTION = "Cannot find Subcategory with name: ";
    private static final String CATEGORIES_CLASS = "categories";
    private static final String CHECKBOX_CSS = ".oss-checkbox input";
    private static final String CATEGORY_BOX_CSS = ".category-box";
    private static final String BY_TEXT_PATTERN = "//*[text()='%s']";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebElement toolsManager;

    private ToolsManagerWindow(WebDriver driver, WebDriverWait wait, WebElement toolsManager) {
        this.driver = driver;
        this.wait = wait;
        this.toolsManager = toolsManager;
    }

    public static ToolsManagerWindow create(WebDriver driver, WebDriverWait wait) {
        try {
            DelayUtils.waitBy(wait, By.cssSelector(VIEWS_MANAGER_CSS));
            WebElement toolsManager = driver.findElement(By.className(VIEWS_MANAGER_CLASS));
            return new ToolsManagerWindow(driver, wait, toolsManager);
        } catch (Exception exception) {
            throw new NoSuchElementException(CANNOT_LOCATE_TOOLS_MANAGER_WINDOW_EXCEPTION, exception);
        }
    }

    public Optional<String> getApplicationURL(String applicationName) {
        Application application = Application.createApplicationByName(driver, wait, toolsManager, applicationName);
        return application.getApplicationsURL();
    }

    public List<String> getApplicationNames(String categoryName) {
        return getCategoryByName(categoryName).getApplications().stream().map(Application::getApplicationName).collect(Collectors.toList());
    }

    public List<Application> getApplicationWithoutoSubcategory(String categoryName) {
        return getCategoryByName(categoryName).getApplicationsWithoutSubcategory();
    }

    public List<String> getApplicationNames() {
        List<Application> applications = toolsManager.findElements(By.cssSelector(CATEGORY_BOX_CSS)).stream()
                .map(application -> Application.createApplication(driver, wait, application))
                .collect(Collectors.toList());
        return applications.stream().map(Application::getApplicationName).collect(Collectors.toList());
    }

    public List<String> getCategoriesName() {
        return getCategories().stream().map(Category::getName).collect(Collectors.toList());
    }

    public String getCategoryDescription(String categoryName) {
        return getCategoryByName(categoryName).getDescription();
    }

    public void search(String name) {
        Input input = ComponentFactory.create(SEARCH_TEST_ID, Input.ComponentType.SEARCH_FIELD, driver, wait);
        input.setSingleStringValue(name);
    }

    public int getPlaceOfSubcategory(String categoryName, String subcategoryName) {
        List<String> subcategories = getSubcategoriesNames(categoryName);
        return subcategories.indexOf(subcategoryName);
    }

    public List<String> getSubcategoriesNames(String categoryName) {
        List<Subcategory> subcategories = getSubcategories(categoryName);
        return subcategories.stream().map(Subcategory::getSubcategoryName).collect(Collectors.toList());
    }

    public void clickCreateCategory() {
        toolsManager.findElement(By.className(ADD_CATEGORY_BUTTON_CLASS)).click();
    }

    public Category getCategoryByName(String categoryName) {
        return Category.createCategoryByName(driver, wait, this.toolsManager, categoryName);
    }

    public void callActionSubcategory(String subcategoryName, String categoryName, String actionId) {
        getSubcategoryByName(subcategoryName, categoryName).callAction(actionId);
    }

    public void callActionApplication(String applicationName, String categoryName, String actionId) {
        getApplication(applicationName, categoryName).callAction(actionId);
    }

    public void callAction(String categoryName, String actionId) {
        Category.createCategoryByName(driver, wait, toolsManager, categoryName).callAction(actionId);
    }

    public Subcategory getSubcategoryByName(String subcategoryName, String categoryName) {
        Category category = Category.createCategoryByName(driver, wait, toolsManager, categoryName);
        if (!category.isCategoryExpanded()) {
            category.expandCategory();
        }
        return category.getSubcategories().stream()
                .filter(subcategory -> subcategory.getSubcategoryName()
                        .equals(subcategoryName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(CANNOT_FIND_SUBCATEGORY_WITH_NAME_EXCEPTION + subcategoryName));
    }

    public void openApplication(String categoryName, String applicationName) {
        getApplication(applicationName, categoryName).openApplication();
    }

    public void openApplication(String categoryName, String subcategoryName, String applicationName) {
        Subcategory subcategory = getSubcategoryByName(subcategoryName, categoryName);
        subcategory.clickShowAll();
        subcategory.getApplications().stream().filter(application -> application.getApplicationName()
                        .equals(applicationName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(CANNOT_FIND_APPLICATION_WITH_NAME_EXCEPTION + applicationName))
                .openApplication();
    }

    public void changeCategoryOrder(String categoryName, int position) {
        List<Category> categories = getCategories();
        DragAndDrop.DraggableElement sourceCategory = getCategoryByName(categoryName).getDragElement();
        Category target = categories.get(position);
        dragAndDrop(sourceCategory, target.getDropElement(), driver);
        sourceCategory.waitUntilElementRecalculate(wait);
    }

    public void changeSubcategoryOrder(String categoryName, String subcategoryName, int position) {
        DragAndDrop.DraggableElement source = getSubcategoryByName(subcategoryName, categoryName).getDragElement();
        Subcategory target = getSubcategories(categoryName).get(position);
        dragAndDrop(source, target.getDropElement(), driver);
        source.waitUntilElementRecalculate(wait);
    }

    public void changeApplicationOrder(String categoryName, String applicationName, int position) {
        Application sourceApplication = getApplication(applicationName, categoryName);
        Application target = getCategoryByName(categoryName).getApplications().get(position);
        dragAndDrop(sourceApplication.getDragElement(), target.getDropElement(), driver);
    }

    public void setShowOnlyFavourites() {
        toolsManager.findElement(By.cssSelector(CHECKBOX_CSS)).click();
    }

    public void waitForNavigationElement(String name) {
        DelayUtils.waitForPresence(wait, By.xpath(String.format(BY_TEXT_PATTERN, name)));
    }

    public Application getApplication(String applicationName, String categoryName) {
        Category category = Category.createCategoryByName(driver, wait, toolsManager, categoryName);
        if (!category.isCategoryExpanded()) {
            category.expandCategory();
        }
        return category.getApplications().stream().filter(application -> application.getApplicationName()
                .equals(applicationName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(CANNOT_FIND_APPLICATION_WITH_NAME_EXCEPTION + applicationName));
    }

    private List<Subcategory> getSubcategories(String categoryName) {
        Category category = Category.createCategoryByName(driver, wait, toolsManager, categoryName);
        if (!category.isCategoryExpanded()) {
            category.expandCategory();
            DelayUtils.waitForElementToLoad(wait, toolsManager);
        }
        return category.getSubcategories();

    }

    private List<Category> getCategories() {
        DelayUtils.waitBy(wait, By.className(CATEGORIES_CLASS));
        return toolsManager.findElements(By.className(CATEGORIES_CLASS)).stream()
                .map(category -> Category.createCategory(driver, wait, category))
                .collect(Collectors.toList());
    }
}

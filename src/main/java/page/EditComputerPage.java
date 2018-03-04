package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.Random;

public class EditComputerPage extends Page {

    public EditComputerPage(WebDriver driver) {
        super(driver);
    }

    private By computerNameInput = By.id("name");
    private By introducedDateInput = By.id("introduced");
    private By discontinuedDateInput = By.id("discontinued");
    private By companyDropDown = By.id("company");
    private By saveButton = By.xpath("//input[@value='Save this computer']");
    private By cancelButton = By.xpath("//a[text()='Cancel']");
    private By deleteButton = By.xpath("//input[@value='Delete this computer']");


    //---get data---
    public String getComputerName() {
        return getElement(computerNameInput).getAttribute("value");
    }

    public String getIntroducedDate() {
        return getElement(introducedDateInput).getAttribute("value");
    }

    public String getDiscontinuedDate() {
        return getElement(discontinuedDateInput).getAttribute("value");
    }

    public String getCompany() {
        Select dropdown = new Select(getElement(companyDropDown));
         return  dropdown.getFirstSelectedOption().getText();
    }


    //---edit inputs---
    public void editComputerNameInput(String computerName) {
        WebElement element = getElement(computerNameInput);
        element.clear();
        element.sendKeys(computerName);
        element.click();
    }

    public void editIntroducedDateInput(String date) {
        WebElement element = getElement(introducedDateInput);
        element.clear();
        element.sendKeys(date);
        element.click();
    }

    public void editDiscontinuedDateInput(String date) {
        WebElement element = getElement(discontinuedDateInput);
        element.clear();
        element.sendKeys(date);
        element.click();
    }

    public void chooseCompanyFromDropdown(String name) {
        Select dropdown = new Select(getElement(companyDropDown));
        dropdown.selectByVisibleText(name);
    }

    public void chooseRandomCompanyFromDropdown() {
        int n = new Random().nextInt(43) + 1;
        Select dropdown = new Select(getElement(companyDropDown));
        dropdown.selectByValue(String.valueOf(n));
    }


    //---buttons---
    public void pressSaveButton() {
        getElement(saveButton).click();
    }

    public void pressCancelButton() {
        getElement(cancelButton).click();
    }

    public void pressDeleteButton() {
        getElement(deleteButton).click();
    }


    //---warnings---
    public boolean isComputerNameHighlighted() {
        return isFieldHighlighted(computerNameInput);
    }

    public boolean isIntroducedDateHighlighted() {
        return isFieldHighlighted(introducedDateInput);
    }

    public boolean isDiscontinuedHighlighted() {
        return isFieldHighlighted(discontinuedDateInput);
    }

}

package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.Random;

public class NewComputerPage extends Page {

    public NewComputerPage(WebDriver driver) {
        super(driver);
    }

    private By computerNameInput = By.id("name");
    private By introducedDateInput = By.id("introduced");
    private By discontinuedInput = By.id("discontinued");
    private By companyDropDown = By.id("company");
    private By createButton = By.xpath("//input[@value='Create this computer']");
    private By cancelButton = By.xpath("//a[text()='Cancel']");


    //---fill inputs---
    public void fillComputerNameInput(String computerName) {
        WebElement element = getElement(computerNameInput);
        element.clear();
        element.sendKeys(computerName);

    }

    public void fillIntroducedDateInput(String date) {
        WebElement element = getElement(introducedDateInput);
        element.clear();
        element.sendKeys(date);
    }

    public void fillDiscontinuedDateInput(String date) {
        getElement(discontinuedInput).sendKeys(date);
    }

    public void chooseRandomCompanyFromDropdown() {
        int n = new Random().nextInt(43) + 1;
        Select dropdown = new Select(getElement(companyDropDown));
        dropdown.selectByValue(String.valueOf(n));
    }


    //---buttons
    public void pressCreateButton() {
        getElement(createButton).click();
    }

    public void pressCancelButton() {
        getElement(cancelButton).click();
    }


    //---warnings---
    public boolean isComputerNameHighlighted() {
        return isFieldHighlighted(computerNameInput);
    }

    public boolean isIntroducedDateHighlighted() {
        return isFieldHighlighted(introducedDateInput);
    }

    public boolean isDiscontinuedHighlighted() {
        return isFieldHighlighted(discontinuedInput);
    }


}

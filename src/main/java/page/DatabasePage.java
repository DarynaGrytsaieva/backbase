package page;

import com.google.common.collect.Ordering;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class DatabasePage extends Page {
    private static final String URL = "http://computer-database.herokuapp.com/";

    public DatabasePage(WebDriver driver) {
        super(URL, driver);
        this.driver = driver;
    }


    private By filterInput = By.id("searchbox");
    private By filterButton = By.id("searchsubmit");
    private By addNewComputerButton = By.id("add");
    private By nextPageButton = By.cssSelector("#pagination li.next > a");
    private By nextPage = By.cssSelector("#pagination li.next");
    private By previousPageButton = By.cssSelector("#pagination  li.prev > a");
    private By previousPage = By.cssSelector("#pagination  li.prev");
    private By pageDisplay = By.cssSelector("#pagination  li.current > a");
    private By computersTable = By.className("computers");
    private By emptyResult = By.className("well");

    public void pressAddNewComputer() {
        getElement(addNewComputerButton).click();
    }

    public boolean isComputerFound() {
        return isElementPresent(computersTable);

    }

    public boolean isEmptyResult() {
        return getHeader().contains("No computers found") && isElementPresent(emptyResult);
    }

    public void openComputer(String computerName) {
        getElement(By.xpath("//a[text()='" + computerName + "']")).click();
    }


    //---filter---
    public void fillFilterInput(String value) {
        WebElement element = getElement(filterInput);
        element.clear();
        element.sendKeys(value);
    }

    public void pressFilterButton() {
        getElement(filterButton).click();
    }


    //---order column---
    public void orderByComputerName() {
        getElement(By.xpath("//a[contains(text(), 'Computer name')]")).click();
    }

    public void orderByIntroduced() {
        getElement(By.xpath("//a[contains(text(), 'Introduced')]")).click();
    }

    public void orderByDiscontinued() {
        getElement(By.xpath("//a[contains(text(), 'Discontinued')]")).click();
    }

    public void orderByCompany() {
        getElement(By.xpath("//a[text()='Company']")).click();
    }


    //---page navigation---
    public boolean isNextButtonActive() {
        return !getElement(nextPage).getAttribute("class").contains("disabled");
    }

    public void pressNextPage() {
        getElement(nextPageButton).click();
    }

    public boolean isPreviousButtonActive() {
        return !getElement(previousPage).getAttribute("class").contains("disabled");
    }

    public void pressPreviousPage() {
        getElement(previousPageButton).click();
    }

    public int getTotalPages() {
        double d = this.getTotalComputers();
        if (d % 10 != 0) {
            return (int) (d / 10 + 1);
        }
        return (int) (d / 10);
    }

    public String getPageData() {
        return getElement(pageDisplay).getText();
    }

    public int getTotalComputers() {
        String[] data = this.getPageData().split(" ");
        return Integer.parseInt(data[data.length - 1]);
    }


    //---alphabetical order---
    public boolean isOrderedByComputerName() {
        List<String> computers = getColumnValuesOnPage("Computer name");
        return Ordering.natural().isOrdered(computers);
    }

    public boolean isOrderedByIntroducedDate() {
        List<String> strDates = getColumnValuesOnPage("Introduced");
        List<LocalDate> dates = strDates.stream()
                .map(dateStr -> LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd MMM yyyy")))
                .collect(toList());

        return Ordering.from(LocalDate::compareTo).isOrdered(dates);
    }

    public boolean isOrderedByDiscontinuedDate() {
        List<String> strDates = getColumnValuesOnPage("Discontinued");
        List<LocalDate> dates = strDates.stream()
                .map(dateStr -> LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd MMM yyyy")))
                .collect(toList());

        return Ordering.from(LocalDate::compareTo).isOrdered(dates);
    }

    public boolean isOrderedByCompany() {
        List<String> computers = getColumnValuesOnPage("Company");
        return Ordering.natural().isOrdered(computers);
    }

    public List<String> getColumnValuesOnPage(String columnName) {
        int colId = 0;
        switch (columnName) {
            case "Computer name":
                colId = 1;
                break;
            case "Introduced":
                colId = 2;
                break;
            case "Discontinued":
                colId = 3;
                break;
            case "Company":
                colId = 4;
                break;
        }

        List<String> columnData = new ArrayList<>();
        int compTotal = getTotalComputers();
        int compOnPage = compTotal >= 10 ? 10 : compTotal;

        for (int i = 1; i <= compOnPage; i++) {
            if (isElementPresent(getCellSelector(i, colId))) {
                String text = getElement(getCellSelector(i, colId)).getText();
                if (!text.equals("-"))
                    columnData.add(text);
            }

        }
        return columnData;

    }

    private By getCellSelector(int i, int colId) {
        return (colId == 1 ?
                By.xpath("//table/tbody/tr[" + i + "]/td[" + colId + "]/a") : By.xpath("//table/tbody/tr[" + i + "]/td[" + colId + "]"));
    }

}
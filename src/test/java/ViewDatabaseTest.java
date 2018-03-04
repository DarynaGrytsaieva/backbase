import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ViewDatabaseTest extends Fixture {

    @BeforeClass
    public void setUp() {
        databasePage.openPage();
        databasePage.waitForUrlEnd("computers");
    }

    @Test
    public void shouldDisplayDatabase() {
        assertFalse(databasePage.isPreviousButtonActive());
        assertTrue(databasePage.getTotalComputers() > 0);
        assertTrue(databasePage.isNextButtonActive());
    }

    @Test
    public void shouldOrderByComputerName() {
        //when
        databasePage.orderByComputerName();

        //then
        assertTrue(databasePage.isOrderedByComputerName());
    }

    @Test
    public void shouldOrderByIntroducedDate() {
        ////when
        databasePage.orderByIntroduced();

        //then
        assertTrue(databasePage.isOrderedByIntroducedDate());
    }

    @Test
    public void shouldOrderByDiscontinuedDate() {
        //when
        databasePage.orderByDiscontinued();

        //then
        assertTrue(databasePage.isOrderedByDiscontinuedDate());
    }

    @Test
    public void shouldOrderByCompany() {
        //when
        databasePage.orderByCompany();

        //then
        assertTrue(databasePage.isOrderedByCompany());
    }

    @Test(dependsOnMethods = {"databaseHasMoteThanTenComputers"})
    public void shouldNavigateBetweenPages() {
        //given
        assertTrue(databasePage.getPageData().contains("Displaying 1 to 10"));
        assertTrue(databasePage.isNextButtonActive());

        //when
        databasePage.pressNextPage();
        databasePage.waitForUrlEnd("p=1");

        //then
        assertTrue(databasePage.isComputerFound());
        assertTrue(databasePage.getPageData().contains("Displaying 11 to"));
        assertTrue(databasePage.isPreviousButtonActive());

        //and
        databasePage.pressPreviousPage();
        assertTrue(databasePage.getPageData().contains("Displaying 1 to 10"));
    }

    @Test(dependsOnMethods = {"databaseHasMoteThanTenComputers"})
    public void shouldNavigateToLastPage() {
        //given
        int pages = databasePage.getTotalPages();

        //when
        for (int i = 0; i < pages; i++) {
            databasePage.pressNextPage();
        }

        //then
        assertTrue(databasePage.isComputerFound());
        assertTrue(databasePage.isPreviousButtonActive());
        assertFalse(databasePage.isNextButtonActive());
    }

    @Test(dataProvider = "getExistingComputers")
    public void shouldFilterResults(String computerName) {
        //when
        databasePage.fillFilterInput(computerName);
        databasePage.pressFilterButton();

        //then
        assertTrue(databasePage.isComputerFound());
        List<String> results = databasePage.getColumnValuesOnPage("Computer name");
        for (String result : results) {
            assertTrue(result.contains(computerName));
        }
    }

    @Test(dataProvider = "getInvalidComputers")
    public void shouldNotFindResults(String computerName) {
        //when
        databasePage.fillFilterInput(computerName);
        databasePage.pressFilterButton();

        //then
        assertTrue(databasePage.isEmptyResult());
    }

    @Test
    public void databaseHasMoteThanTenComputers() {
        assertTrue(databasePage.getTotalComputers() > 10);
    }

    @AfterMethod
    public void tearDown() {
        databasePage.gotoMainPage();
    }

    @DataProvider
    public Object[][] getExistingComputers() {
        return new Object[][]{{"Apple"}, {"Canon Cat"}, {"A"}};
    }

    @DataProvider
    public Object[][] getInvalidComputers() {
        return new Object[][]{{"0&6"}, {"SELECT * FROM COMPUTERS;"},{"<script>alert('Injected!');</script>"}};
    }

}

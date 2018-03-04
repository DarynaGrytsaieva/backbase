import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

public class AddNewComputerTest extends Fixture {

    @BeforeClass
    public void setUp() {
        databasePage.openPage();
        databasePage.waitForUrlEnd("computers");
    }

    @Test(dataProvider = "getValidData")
    public void shouldAddNewComputer(String computerName, String introducedDate, String discontinuedDate) {
        //when
        databasePage.pressAddNewComputer();
        databasePage.waitForUrlEnd("/new");
        assertTrue(newComputerPage.getHeader().equals("Add a computer"));

        //and
        newComputerPage.fillComputerNameInput(computerName);
        newComputerPage.fillIntroducedDateInput(introducedDate);
        newComputerPage.fillDiscontinuedDateInput(discontinuedDate);
        newComputerPage.chooseRandomCompanyFromDropdown();
        newComputerPage.pressCreateButton();

        //then
        assertFalse(newComputerPage.isErrorPresent());
        newComputerPage.waitForUrlEnd("/computers");
        databasePage.fillFilterInput(computerName);
        databasePage.pressFilterButton();
        assertTrue(databasePage.isComputerFound());

        List<String> nameResult = databasePage.getColumnValuesOnPage("Computer name");
        List<String> introducedResult = databasePage.getColumnValuesOnPage("Introduced");
        List<String> discontinuedResult = databasePage.getColumnValuesOnPage("Discontinued");

        //and
        assertTrue(nameResult.size() == 1 && introducedResult.size() == 1 && discontinuedResult.size() == 1);
        assertTrue(nameResult.get(0).equals(computerName));
        assertTrue(parseDate(introducedResult.get(0)).equals(introducedDate.trim()));
        assertTrue(parseDate(discontinuedResult.get(0)).equals(discontinuedDate.trim()));


    }

    @Test
    public void shouldDismissAddNewComputer() {
        //given
        databasePage.pressAddNewComputer();
        databasePage.waitForUrlEnd("/new");
        assertTrue(newComputerPage.getHeader().equals("Add a computer"));
        String name = randomizeName(TEST_NAME);

        //when
        newComputerPage.fillComputerNameInput(name);
        newComputerPage.pressCancelButton();
        newComputerPage.waitForUrlEnd("/computers");

        //then
        findCreatedComputer(name);
        assertTrue(databasePage.isEmptyResult());
    }

    @Test
    public void shouldNotAddNewComputerWithoutSaving() {
        //given
        databasePage.pressAddNewComputer();
        databasePage.waitForUrlEnd("/new");
        assertTrue(newComputerPage.getHeader().equals("Add a computer"));
        String name = randomizeName(TEST_NAME);

        //when
        newComputerPage.fillComputerNameInput(name);
        newComputerPage.gotoMainPage();

        //then
        findCreatedComputer(name);
        assertTrue(databasePage.isEmptyResult());
    }

    @Test
    public void shouldNotAddNewComputerWithoutName() {
        //given
        databasePage.pressAddNewComputer();
        databasePage.waitForUrlEnd("/new");

        //when
        newComputerPage.fillComputerNameInput(" ");
        newComputerPage.pressCreateButton();

        //then
        assertTrue(newComputerPage.isComputerNameHighlighted());
    }

    @Test(dataProvider = "getMissingDates")
    public void shouldAddNewComputerWithMissingDates(String name, String introduced, String discontinued) {
        //given
        databasePage.pressAddNewComputer();
        databasePage.waitForUrlEnd("/new");

        //when
        newComputerPage.fillComputerNameInput(name);
        newComputerPage.fillIntroducedDateInput(introduced);
        newComputerPage.fillDiscontinuedDateInput(discontinued);
        newComputerPage.pressCreateButton();

        //then
        assertFalse(newComputerPage.isErrorPresent());
        newComputerPage.waitForUrlEnd("/computers");

        //and
        findCreatedComputer(name);
        assertTrue(databasePage.isComputerFound());
    }

    @Test
    public void shouldAddNewComputerWithMissingCompany() {
        //given
        databasePage.pressAddNewComputer();
        databasePage.waitForUrlEnd("/new");
        String name = randomizeName(TEST_NAME);

        //when
        newComputerPage.fillComputerNameInput(name);
        editComputerPage.chooseCompanyFromDropdown("-- Choose a company --");
        newComputerPage.pressCreateButton();

        //then
        assertFalse(newComputerPage.isErrorPresent());
        newComputerPage.waitForUrlEnd("/computers");

        //and
        findCreatedComputer(name);
        assertTrue(databasePage.isComputerFound());
    }

    @Test(dataProvider = "getInvalidDates")
    public void shouldNotAddNewComputerWithInvalidIntroducedDate(String introduced) {
        //given
        databasePage.pressAddNewComputer();
        databasePage.waitForUrlEnd("/new");

        //when
        String name = randomizeName(TEST_NAME);
        newComputerPage.fillComputerNameInput(name);
        newComputerPage.fillIntroducedDateInput(introduced);
        newComputerPage.pressCreateButton();

        //then
        assertTrue(newComputerPage.isErrorPresent());
        assertTrue(newComputerPage.isIntroducedDateHighlighted());
    }

    @Test(dataProvider = "getInvalidDates")
    public void shouldNotAddNewComputerWithInvalidDiscontinuedDate(String discontinued) {
        //given
        databasePage.pressAddNewComputer();
        databasePage.waitForUrlEnd("/new");

        //when
        String name = randomizeName(TEST_NAME);
        newComputerPage.fillComputerNameInput(name);
        newComputerPage.fillDiscontinuedDateInput(discontinued);
        newComputerPage.pressCreateButton();

        //then
        assertTrue(newComputerPage.isErrorPresent());
        assertTrue(newComputerPage.isDiscontinuedHighlighted());
    }

    @Test(dataProvider = "getUnorderedDates")
    public void shouldNotAddNewComputerWithUnorderedDates(String introduced, String discontinued) {
        //given
        databasePage.pressAddNewComputer();
        databasePage.waitForUrlEnd("/new");

        //when
        String name = randomizeName(TEST_NAME);
        newComputerPage.fillComputerNameInput(name);
        newComputerPage.fillIntroducedDateInput(introduced);
        newComputerPage.fillDiscontinuedDateInput(discontinued);
        newComputerPage.pressCreateButton();

        //then
        assertTrue(newComputerPage.isErrorPresent());
    }

    @AfterMethod
    public void tearDown() {
        databasePage.gotoMainPage();
    }

    private void findCreatedComputer(String name) {
        databasePage.fillFilterInput(name);
        databasePage.pressFilterButton();
    }

    @DataProvider
    public Object[][] getValidData() {
        return new Object[][]{
                {randomizeName(TEST_NAME), "1995-01-01", "2001-01-01"},
                {randomizeName(TEST_NAME) + " ", "1995-01-01", "2001-01-01"},
                {" " + randomizeName(TEST_NAME), "1995-01-01", "2001-01-01"},
                {randomizeName("NULL"), "1995-01-01", "2001-01-01"},
                {randomizeName("<b>name</b><br/><br/>"), "1995-01-01", "2001-01-01"},
                {randomizeName("<script>alert('DPA);</script>"), "1995-01-01", "2001-01-01"}};
    }

    @DataProvider
    public Object[][] getMissingDates() {
        return new Object[][]{
                {randomizeName(TEST_NAME), "", "2001-01-01"},
                {randomizeName(TEST_NAME), "2001-01-01", ""},
                {randomizeName(TEST_NAME), "", ""},
                {randomizeName(TEST_NAME), " ", " "}};
    }

    @DataProvider
    public Object[][] getInvalidDates() {
        return new Object[][]{
                {"aaaa-aa-aa"},
                {"0000-01-01"},
                {"1999-13-01"},
                {"1999-10-35"},
                {"2019-02-29"},
                {"20000-05-15"},
                {"NULL"},
                {"<b>2000-01-01</b><br/><br/>"}};
    }

    @DataProvider
    public Object[][] getUnorderedDates() {
        return new Object[][]{
                {"2020-01-01", "1999-01-01"}};
    }


}
//introduced date before discontinued
//no trim
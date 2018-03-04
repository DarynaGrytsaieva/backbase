import org.testng.annotations.*;
import java.util.List;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

public class EditComputerTest extends Fixture {
    private static String firstTestComputerName;

    @BeforeClass
    public void setUp() {
        firstTestComputerName = createTestComputer(TEST_NAME);
    }

    @Test(dataProvider = "getValidData")
    public void shouldEditComputer(String computerName, String introduced, String discontinued) {
        //given
        String secondTestComputerName = createTestComputer(TEST_NAME);
        findCreatedComputer(secondTestComputerName);
        databasePage.openComputer(secondTestComputerName);

        //when
        editComputerPage.editComputerNameInput(computerName);
        editComputerPage.editIntroducedDateInput(introduced);
        editComputerPage.editDiscontinuedDateInput(discontinued);
        editComputerPage.chooseRandomCompanyFromDropdown();
        String newCompany = editComputerPage.getCompany();
        editComputerPage.pressSaveButton();

        //then
        assertFalse(editComputerPage.isErrorPresent());
        findCreatedComputer(computerName);
        assertTrue(databasePage.isComputerFound());
        assertTrue(databasePage.getColumnValuesOnPage("Computer name").size() == 1);

        //and
        databasePage.openComputer(computerName);
        assertTrue(editComputerPage.getComputerName().equals(computerName.trim()));
        assertTrue(editComputerPage.getIntroducedDate().equals(introduced.trim()));
        assertTrue(editComputerPage.getDiscontinuedDate().equals(discontinued.trim()));
        assertTrue(editComputerPage.getCompany().equals(newCompany));

        //cleanup
        editComputerPage.gotoMainPage();
        deleteCreatedComputer(computerName);
    }

    @Test
    public void shouldDismissEditComputer() {
        //given
        String newName = randomizeName("New Computer");
        findCreatedComputer(firstTestComputerName);

        //when
        databasePage.openComputer(firstTestComputerName);
        editComputerPage.editComputerNameInput(newName);
        editComputerPage.pressCancelButton();
        editComputerPage.waitForUrlEnd("computers");

        //then
        databasePage.fillFilterInput(newName);
        assertFalse(databasePage.isEmptyResult());
    }

    @Test(dataProvider = "getMissingDates")
    public void shouldEditComputerWithMissingDates(String introduced, String discontinued) {
        //given
        findCreatedComputer(firstTestComputerName);
        databasePage.openComputer(firstTestComputerName);

        //when
        editComputerPage.editIntroducedDateInput(introduced);
        editComputerPage.editDiscontinuedDateInput(discontinued);
        editComputerPage.pressSaveButton();

        //then
        assertFalse(editComputerPage.isErrorPresent());
        databasePage.fillFilterInput(firstTestComputerName);
        databasePage.pressFilterButton();
        assertTrue(databasePage.isComputerFound());

        //and
        databasePage.openComputer(firstTestComputerName);
        assertTrue(editComputerPage.getIntroducedDate().equals(introduced));
        assertTrue(editComputerPage.getDiscontinuedDate().equals(discontinued));
    }

    @Test
    public void shouldEditComputerWithMissingCompany() {
        //given
        findCreatedComputer(firstTestComputerName);
        databasePage.openComputer(firstTestComputerName);

        //when
        editComputerPage.chooseCompanyFromDropdown("-- Choose a company --");
        editComputerPage.pressSaveButton();

        //then
        assertFalse(editComputerPage.isErrorPresent());

        //and
        databasePage.fillFilterInput(firstTestComputerName);
        databasePage.pressFilterButton();
        assertTrue(databasePage.isComputerFound());

        List<String> companyResult = databasePage.getColumnValuesOnPage("Company");
        assertTrue(companyResult.isEmpty());
    }

    @Test
    public void shouldNotEditComputerWithoutSaving() {
        //given
        String newName = randomizeName("New Computer");
        findCreatedComputer(firstTestComputerName);
        databasePage.openComputer(firstTestComputerName);

        //when
        editComputerPage.editComputerNameInput(newName);
        editComputerPage.gotoMainPage();

        //then
        databasePage.fillFilterInput(newName);
        assertFalse(databasePage.isEmptyResult());
    }

    @Test
    public void shouldNotEditComputerWithoutName() {
        //given
        findCreatedComputer(firstTestComputerName);
        databasePage.openComputer(firstTestComputerName);

        //when
        editComputerPage.editComputerNameInput("");
        editComputerPage.pressSaveButton();

        //then
        assertTrue(editComputerPage.isComputerNameHighlighted());
    }

    @Test(dataProvider = "getInvalidDates")
    public void shouldNotEditComputerWithInvalidIntroducedDate(String introduced) {
        //given
        findCreatedComputer(firstTestComputerName);
        databasePage.openComputer(firstTestComputerName);

        //when
        editComputerPage.editIntroducedDateInput(introduced);
        editComputerPage.pressSaveButton();

        //then
        assertTrue(editComputerPage.isErrorPresent());
        assertTrue(editComputerPage.isIntroducedDateHighlighted());
    }

    @Test(dataProvider = "getInvalidDates")
    public void shouldNotEditComputerWithInvalidDiscontinuedDate(String discontinued) {
        //given
        findCreatedComputer(firstTestComputerName);
        databasePage.openComputer(firstTestComputerName);

        //when
        editComputerPage.editDiscontinuedDateInput(discontinued);
        editComputerPage.pressSaveButton();

        //then
        assertTrue(editComputerPage.isErrorPresent());
        assertTrue(editComputerPage.isDiscontinuedHighlighted());
    }

    @Test(dataProvider = "getUnorderedDates")
    public void shouldNotEditComputerWithUnorderedDates(String introduced, String discontinued) {
        //given
        findCreatedComputer(firstTestComputerName);
        databasePage.openComputer(firstTestComputerName);

        //when
        editComputerPage.editIntroducedDateInput(introduced);
        editComputerPage.editDiscontinuedDateInput(discontinued);
        editComputerPage.pressSaveButton();

        //then
        assertTrue(editComputerPage.isErrorPresent());
        assertTrue(editComputerPage.isDiscontinuedHighlighted());
    }

    @AfterMethod
    public void gotoMainPage() {
        databasePage.gotoMainPage();
    }

    @AfterClass
    public void tearDown() {
        deleteCreatedComputer(firstTestComputerName);
    }

    private void deleteCreatedComputer(String name) {
        databasePage.fillFilterInput(name);
        databasePage.pressFilterButton();
        databasePage.openComputer(name);
        editComputerPage.pressDeleteButton();
        databasePage.waitForUrlEnd("computers");
    }

    private String createTestComputer(String testName) {
        String name = randomizeName(testName);
        databasePage.openPage();
        databasePage.waitForUrlEnd("computers");
        databasePage.pressAddNewComputer();
        newComputerPage.fillComputerNameInput(name);
        newComputerPage.fillIntroducedDateInput("2015-10-10");
        newComputerPage.fillDiscontinuedDateInput("2018-05-05");
        newComputerPage.chooseRandomCompanyFromDropdown();
        newComputerPage.pressCreateButton();
        databasePage.waitForUrlEnd("computers");
        return name;
    }

    private void findCreatedComputer(String name) {
        databasePage.fillFilterInput(name);
        databasePage.pressFilterButton();
    }

    @DataProvider
    public Object[][] getValidData() {
        return new Object[][]{
                {randomizeName(TEST_NAME), "1995-01-01", "2001-01-01"},
                {" " + randomizeName(TEST_NAME), "1995-01-01", "2001-01-01"},
                {randomizeName(TEST_NAME) + " ", "1995-01-01", "2001-01-01"},
                {randomizeName("NULL"), "1995-01-01", "2001-01-01"},
                {randomizeName("<b>name</b><br/><br/>"), "1995-01-01", "2001-01-01"}};
    }

    @DataProvider
    public Object[][] getMissingDates() {
        return new Object[][]{
                {"", "2001-01-01"},
                {"2001-01-01", ""},
                {"", ""},
                {" ", " "}};
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

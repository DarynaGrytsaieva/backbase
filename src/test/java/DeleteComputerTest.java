import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

public class DeleteComputerTest extends Fixture {

    @BeforeClass
    public void setUp() {
        databasePage.openPage();
        databasePage.waitForUrlEnd("computers");
    }

    @Test
    public void shouldDeleteComputer() {
        //given
        databasePage.pressAddNewComputer();
        String name = randomizeName(TEST_NAME);
        newComputerPage.fillComputerNameInput(name);
        newComputerPage.pressCreateButton();
        databasePage.waitForUrlEnd("computers");

        //and
        databasePage.fillFilterInput(name);
        databasePage.pressFilterButton();
        assertTrue(databasePage.isComputerFound());
        databasePage.openComputer(name);

        //when
        editComputerPage.pressDeleteButton();
        databasePage.waitForUrlEnd("computers");

        //then
        databasePage.fillFilterInput(name);
        databasePage.pressFilterButton();
        assertTrue(databasePage.isEmptyResult());
    }


}
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import page.DatabasePage;
import page.EditComputerPage;
import page.NewComputerPage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Fixture {
    public static WebDriver driver;
    public static DatabasePage databasePage;
    public static NewComputerPage newComputerPage;
    public static EditComputerPage editComputerPage;
    public static String TEST_NAME = "Test computer";


    @BeforeSuite
    public void setEnv() {
        System.setProperty(
                "webdriver.chrome.driver",
                "webdriver/chromedriver");

        driver = new ChromeDriver();
        driver.manage().window().maximize();

        databasePage = new DatabasePage(driver);
        newComputerPage = new NewComputerPage(driver);
        editComputerPage = new EditComputerPage(driver);
    }

    @AfterSuite
    public void resetEnv() {
        driver.quit();
    }

    static String randomizeName(String name) {
        return name + " " + (new Random().nextInt(999) + 100);
    }

    String parseDate(String dateStr){
        //date= "12 Jan 2000" convert to 2000-01-12
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd MMM yyyy"));
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date);
    }


}

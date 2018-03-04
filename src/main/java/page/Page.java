package page;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Page {
    private String page;
    WebDriver driver;
    private Wait<WebDriver> wait;
    private By logo = By.xpath("//header/h1/a");
    private By header = By.cssSelector("#main > h1");


    Page(String page, WebDriver driver) {
        this.page = page;
        this.driver = driver;
        wait = new FluentWait<>(driver)
                .withTimeout(7, SECONDS)
                .pollingEvery(1, SECONDS)
                .ignoring(NoSuchElementException.class);
    }

    Page(WebDriver driver) {
        this.driver = driver;
        wait = new FluentWait<>(driver)
                .withTimeout(7, SECONDS)
                .pollingEvery(1, SECONDS)
                .ignoring(NoSuchElementException.class);
    }

    public boolean openPage() {
        driver.get(page);
        return true;
    }

    public String getHeader() {
        return driver.findElement(header).getText();
    }

    WebElement getElement(By selector) {
        return wait.until(driver -> driver.findElement(selector));
    }

    boolean isElementPresent(By selector) {
        try {
            return wait.until(driver -> driver.findElement(selector).isDisplayed());
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean hasText(By selector, String text) {
        try {
            return wait.until(driver -> text.equals(driver.findElement(selector).getText()));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void waitForUrlEnd(String text) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(10, SECONDS)
                .pollingEvery(1, SECONDS);
        wait.until(webDriver -> webDriver.getCurrentUrl().endsWith(text));

    }

    public void gotoMainPage() {
        getElement(logo).click();
    }

    boolean isFieldHighlighted(By selector) {
        return wait.until(driver -> driver.findElement(selector).getCssValue("border-color").equals("rgb(200, 120, 114)"));
    }

    public boolean isErrorPresent(){
        return isElementPresent(By.className("error"));
    }
}

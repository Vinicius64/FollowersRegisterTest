package followers.signUpFollowers;

import app.pageObjects.SignUpPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testing the functionality of registering a follower")
public class SignUpFollowersTest {
    private static WebDriver driver;
    private static SignUpPage signUpPage;

    @BeforeAll
    public static void setUp(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        signUpPage = new SignUpPage(driver);
        signUpPage.open();
    }
}

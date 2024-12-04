package followers.followersList;

import app.pageObjects.SignUpPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FollowersListTest {
    private static WebDriver driver;
    private static SignUpPage signUpPage;
    private WebDriverWait wait;

    @BeforeAll
    public static void setUp(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        signUpPage = new SignUpPage(driver);
    }

    @TestClassOrder(ClassOrderer.OrderAnnotation.class)
    @Nested
    @DisplayName("All followers list test")
    class FollowersTest{

        @Order(1)
        @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
        @Nested
        @DisplayName("Test the follower insertion and simple interactions on the listing page flow")
        class TestSimpleFlux{
            @Order(1)
            @ParameterizedTest
            @DisplayName("Test simple insert")
            @CsvSource({
                    "John Doe, Male, 100, true",
                    "Jane Smith, Female, 200, true"
            })
            public void testSimpleInsert(String name, String gender, String nivel, boolean expected){
                try{
                    signUpPage.open();
                    insertData(name, gender, nivel);
                    signUpPage.waitForOkButtonToBeVisible();
                    assertEquals(expected, signUpPage.isRegisterSuccessMessageVisible());
                    clickOkButton();
                }catch (Error e){
                    clickOkButton();
                    throw new AssertionFailedError("The result is not the expected");
                }
            }
        }
    }

    private void insertData(String name, String gender, String nivel) {
        signUpPage.fillName(name);
        signUpPage.selectGender(gender);
        signUpPage.fillNivel(nivel);
        signUpPage.selectFirstRadioOption();
        signUpPage.selectSelectedImageOption();
        signUpPage.clickRegisterButton();
    }

    private void clickOkButton() {
        signUpPage.waitForOkButtonToBeVisible();
        signUpPage.clickOkButton();
        signUpPage.waitForRegisterTitleToBeVisible();
        signUpPage.scrollToTop();
        signUpPage.reloadPage();
    }

}

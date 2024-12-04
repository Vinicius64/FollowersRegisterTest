package followers.signUpFollowers;

import app.pageObjects.SignUpPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.opentest4j.AssertionFailedError;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("Testing the functionality of registering a follower")
public class SignUpFollowersTest {
    private static WebDriver driver;
    private static SignUpPage signUpPage;

    public void clickOkButton() {
        signUpPage.waitForOkButtonToBeVisible();
        signUpPage.clickOkButton();
        signUpPage.waitForRegisterTitleToBeVisible();
        signUpPage.scrollToTop();
        signUpPage.reloadPage();
    }

    @BeforeAll
    public static void setUp(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        signUpPage = new SignUpPage(driver);
        signUpPage.open();
    }

    @Nested
    @DisplayName("Tests for insert followers from csv")
    class insertFollowersCsv {
        @ParameterizedTest
        @DisplayName("Test with dynamic data sets")
        @CsvSource({
                "John Doe, Male, 100, true",
                "Jane Smith, Female, 200, true",
                "Alex Johnson, Other,12, true",
                "'', Male, 100, false",
                "Lucas, '', 100, false",
                "Laura, Male, '', false",
                "' ', Female, 200, false",
                "Lisa Simpsom, ' ', 120, false"
        })

        public void testFieldFillFunctionalityWithCsv(String name, String gender, String nivel, boolean expected) {
            try {
                insertData(name, gender, nivel);
                signUpPage.waitForOkButtonToBeVisible();
                assertEquals(expected, signUpPage.isRegisterSuccessMessageVisible());
                clickOkButton();
            } catch (Error e) {
                clickOkButton();
                throw new AssertionFailedError("The result is not the expected");
            }

        }
    }

    @Nested
    @DisplayName("Tests followers with negative numbers")
    class followersWithNegativeNumbers {
        public static Stream<Arguments> testFieldFillFunctionality() {
            return Stream.of(
                    Arguments.of("Maria DB", "123", String.valueOf(-321))
            );
        }

        @ParameterizedTest
        @DisplayName("Test to insert follower with negative number")
        @MethodSource("testFieldFillFunctionality")
        public void testFieldFillFunctionalityWithNegativeNumber(String name, String gender, String nivel) {
            try {
                insertData(name, gender, nivel);
                assertFalse(signUpPage.isRegisterSuccessMessageVisible());
                clickOkButton();
            } catch (Error e) {
                clickOkButton();
                throw new AssertionFailedError("The result is not the expected");
            }
        }
    }

    public void insertData(String name, String gender, String nivel) {
        signUpPage.fillName(name);
        signUpPage.selectGender(gender);
        signUpPage.fillNivel(nivel);
        signUpPage.selectFirstRadioOption();
        signUpPage.selectSelectedImageOption();
        signUpPage.clickRegisterButton();
    }
}

package followers.signUpFollowers;

import app.pageObjects.ListPage;
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

import static org.junit.jupiter.api.Assertions.*;

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

    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Nested
    @DisplayName("Tests for insert followers from csv")
    class insertFollowersCsv {
        @Order(1)
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
        @Order(2)
        @ParameterizedTest
        @DisplayName("Test if followers are being inserted correctly")
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

        public void testIfFollowersAreBeingInsertedCorrectly(String name, String gender, String nivel, boolean expected) {
            signUpPage.scrollToBottom();
            signUpPage.waitForListOfFollowersToBeVisible();
            signUpPage.listFollowers();
            ListPage listPage = new ListPage(driver);
            Boolean exist = listPage.verifyFollowerInList(name,gender,nivel);
            assertEquals(expected,exist);
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

    @Nested
    @DisplayName("Test followers with large numbers")
    class followersWithLargeNumbers {
        public static Stream<Arguments> generateExtremeNumbers() {
            return Stream.of(
                    Arguments.of("1234567890123456789012345678901234567890")
            );
        }

        @ParameterizedTest
        @DisplayName("Test with extremely large numbers")
        @MethodSource("generateExtremeNumbers")
        public void testFieldFillWithExtremeNumbers(String largeNumber) {
            try {
                insertData(largeNumber, "male", largeNumber);
                assertFalse(signUpPage.isRegisterSuccessMessageVisible());
                clickOkButton();
            } catch (Error e) {
                clickOkButton();
                throw new AssertionFailedError("The result is not the expected");
            }


        }
    }

    @Nested
    @DisplayName("Tests followers with decimal numbers")
    class followersWithDecimalNumbers {
        public static Stream<Arguments> generateLargeDecimalInput() {
            String name = "John Doe";
            String gender = "Male";
            String nivel = "99999999999999999999999999999999.123456";

            return Stream.of(
                    Arguments.of(name, gender, nivel)
            );
        }

        @ParameterizedTest
        @DisplayName("Test with very large or decimal numbers")
        @MethodSource("generateLargeDecimalInput")
        public void testFieldWithLargeOrDecimalNumber(String name, String gender, String nivel) {
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

    @Nested
    @DisplayName("Tests followers with many zeros in nivel")
    class followersWithManyZeros {
        public static Stream<Arguments> generateMultipleZerosData() {
            String name = "Test User";
            String gender = "Male";
            String nivel = "00000000000000000000000000000000000000000000000000";

            return Stream.of(
                    Arguments.of(name, gender, nivel)
            );
        }

        @ParameterizedTest
        @DisplayName("Test to insert many zeros in the 'nivel' field")
        @MethodSource("generateMultipleZerosData")
        public void testToInsertManyZerosInNivel(String name, String gender, String nivel) {
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


    @Nested
    @DisplayName("Tests for insert same datas")
    class TestsForInsertSameDatas {
        @ParameterizedTest
        @DisplayName("Test to check if it is possible to insert a follower with the same data as another follower")
        @MethodSource("generateDuplicateData")
        public void testToCheckIfItIsPossibleToInsertFollowerWithTheSameDataAsAnotherFollower(String name, String gender, String nivel) {
            insertData(name, gender, nivel);
            signUpPage.waitForOkButtonToBeVisible();
            signUpPage.clickOkButton();
            signUpPage.waitForRegisterTitleToBeVisible();
            signUpPage.reloadPage();

            try {
                insertData(name, gender, nivel);
                assertFalse(signUpPage.isRegisterSuccessMessageVisible());
                clickOkButton();
            } catch (Error e) {
                clickOkButton();
                throw new AssertionFailedError("The result is not the expected");
            }
        }

        public static Stream<Arguments> generateDuplicateData() {
            String name = "Duplicate User";
            String gender = "Male";
            String nivel = "500";

            return Stream.of(
                    Arguments.of(name, gender, nivel)
            );
        }
    }

    @ParameterizedTest
    @DisplayName("Test to generate multiple follorwers data and large name")
    @MethodSource("generateMultipleFollowersDataAndLargeName")
    public void testToGenerateMultipleFollowersDataAndLargeName(String name, String gender, String nivel) {
        insertData(name, gender, nivel);
        signUpPage.waitForOkButtonToBeVisible();
        signUpPage.clickOkButton();
        signUpPage.waitForRegisterTitleToBeVisible();
        signUpPage.reloadPage();
        insertData(name, gender, nivel);
        try {
            assertFalse(signUpPage.isRegisterSuccessMessageVisible());
            clickOkButton();
        }catch (Error e){
            clickOkButton();
            throw new AssertionFailedError("The result is not the expected");
        }
    }

    public static Stream<Arguments> generateMultipleFollowersDataAndLargeName() {
        String name = "Duplicate User ".repeat(10) + "SomeExtraLongName";
        String gender = "Male";
        String nivel = "500";

        return Stream.of(
                Arguments.of(name, gender, nivel)
        );
    }



        @ParameterizedTest
    @DisplayName("Test with special characters in input fields")
    @MethodSource("generateSpecialCharacterInput")
    public void testFieldWithSpecialCharacters(String name, String gender, String nivel) {
        try{
            insertData(name, gender, nivel);
            assertTrue(signUpPage.isRegisterSuccessMessageVisible());
            clickOkButton();
        }catch (Error e){
            clickOkButton();
            throw new AssertionFailedError("The result is not the expected");
        }
    }


    public static Stream<Arguments> generateSpecialCharacterInput() {
        String name = "John@Doe!#";
        String gender = "@Male%";
        String nivel = "999";

        return Stream.of(
                Arguments.of(name, gender, nivel)
        );
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

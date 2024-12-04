package followers.followersList;

import app.pageObjects.ListPage;
import app.pageObjects.SignUpPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.opentest4j.AssertionFailedError;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class FollowersListTest {
    private static WebDriver driver;
    private static SignUpPage signUpPage;
    private static ListPage listPage;
    private static SignUpPage editPage;
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

            @Order(2)
            @Test
            @DisplayName("Test whether followers are listed correctly")
            public void testListingCorrectly(){
                try {
                    signUpPage.listFollowers();
                    listPage = new ListPage(driver);

                    assertTrue(listPage.verifyFollowerInList("John Doe", "Male", "100"));
                    assertTrue(listPage.verifyFollowerInList("Jane Smith", "Female", "200"));
                }catch (Exception e){
                    throw new AssertionFailedError("Error during follower listing verification", e);
                }
            }

            @Order(3)
            @Test
            @DisplayName("Test deleting the first follower")
            public void testDeleteFirstFollower() {
                try {

                    WebElement firstFollowerCard = listPage.getFirstFollowerCard();
                    assertNotNull(firstFollowerCard, "There should be at least one follower to delete");

                    String name = firstFollowerCard.findElement(By.className("card-header")).getText().trim();
                    String gender = firstFollowerCard.findElement(By.xpath(".//p[strong[text()='Gênero:']]"))
                            .getText().replace("Gênero: ", "").trim();
                    String nivel = firstFollowerCard.findElement(By.xpath(".//p[strong[text()='Nível de Devoção:']]"))
                            .getText().replace("Nível de Devoção: ", "").trim();

                    WebElement deleteButton = firstFollowerCard.findElement(By.xpath(".//button[contains(text(),'Excluir')]"));
                    assertNotNull(deleteButton, "Delete button should be visible for the first follower");

                    deleteButton.click();
                    closeModalIfPresent();

                    listPage.refreshFollowersList();
                    assertFalse(listPage.verifyFollowerInList(name, gender, nivel), "Follower should be deleted from the list");

                } catch (AssertionError | Exception e) {
                    throw new AssertionFailedError("Error during test for deleting the first follower", e);
                }
            }

            @Order(4)
            @Test
            @DisplayName("Test editing the first follower without changes with a reload in the middle of the process")
            public void testEditFirstFollowerNoChangesAndReloadPage(){
                try {
                    WebElement firstFollowerCard = listPage.getFirstFollowerCard();
                    assertNotNull(firstFollowerCard, "There should be at least one follower to edit");

                    WebElement editButton = firstFollowerCard.findElement(By.xpath(".//button[contains(text(),'Editar')]"));
                    editButton.click();


                    editPage = new SignUpPage(driver);

                    editPage.reloadPage();

                    editPage.selectSelectedImageOption();
                    editPage.selectFirstRadioOption();

                    editPage.clickRegisterButton();

                    editPage.waitForOkButtonToBeVisible();
                    assertTrue(editPage.isRegisterSuccessMessageVisible());
                    clickOkEditButton();

                    editPage.scrollToBottom();
                    editPage.listFollowers();

                }catch (Error e){
                    clickOkEditButton();

                    editPage.scrollToBottom();
                    editPage.listFollowers();
                    throw new AssertionError("The result is not the expected");
                }
            }

            @Order(5)
            @Test
            @DisplayName("Test editing the first follower")
            public void testEditFirstFollower(){
                try {
                    String name = "Anne Harris";
                    String gender = " Other";
                    String nivel = "2000";

                    WebElement firstFollowerCard = listPage.getFirstFollowerCard();
                    assertNotNull(firstFollowerCard, "There should be at least one follower to edit");

                    WebElement editButton = firstFollowerCard.findElement(By.xpath(".//button[contains(text(),'Editar')]"));
                    editButton.click();

                    editPage = new SignUpPage(driver);
                    editPage.clearFields();
                    insertData(name,gender,nivel);

                    editPage.waitForOkButtonToBeVisible();
                    assertTrue(editPage.isRegisterSuccessMessageVisible());
                    clickOkEditButton();

                    editPage.scrollToBottom();
                    editPage.listFollowers();

                }catch (Error e){
                    clickOkEditButton();

                    editPage.scrollToBottom();
                    editPage.listFollowers();
                    throw new AssertionError("The result is not the expected");
                }
            }

            @Order(6)
            @Test
            @DisplayName("Test editing the first follower with blank name")
            public void testEditWithBlankName(){
                try {
                    String name = "  ";
                    String gender = " Any";
                    String nivel = "5000";

                    WebElement firstFollowerCard = listPage.getFirstFollowerCard();
                    assertNotNull(firstFollowerCard, "There should be at least one follower to edit");

                    WebElement editButton = firstFollowerCard.findElement(By.xpath(".//button[contains(text(),'Editar')]"));
                    editButton.click();

                    editPage = new SignUpPage(driver);
                    editPage.clearFields();
                    insertData(name,gender,nivel);

                    editPage.waitForOkButtonToBeVisible();
                    assertFalse(editPage.isRegisterSuccessMessageVisible());
                    clickOkEditButton();

                    editPage.scrollToBottom();
                    editPage.listFollowers();

                }catch (Error e){
                    clickOkEditButton();

                    editPage.scrollToBottom();
                    editPage.listFollowers();
                    throw new AssertionError("The result is not the expected");
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

    private void clickOkEditButton() {
        editPage.waitForOkButtonToBeVisible();
        editPage.clickOkButton();
        editPage.waitForRegisterTitleToBeVisible();
        editPage.scrollToTop();
        editPage.reloadPage();
    }

    private void closeModalIfPresent() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement modal = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("swal2-container")));
            if (modal.isDisplayed()) {
                WebElement okButton = modal.findElement(By.xpath(".//button[contains(text(),'OK')]"));
                okButton.click();
                wait.until(ExpectedConditions.invisibilityOf(modal));
            }
        } catch (TimeoutException e) {
            // No modal present, continue execution
        }
    }

}

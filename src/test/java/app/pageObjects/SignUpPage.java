package app.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SignUpPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public SignUpPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("https://followers-register.vercel.app");
    }

    private By registerButton = By.className("register-button");
    private By nameInput = By.className("register-input");
    private By genderInput = By.name("gender");
    private By nivelInput = By.name("nivel");
    private By listInput = By.linkText("Lista de Seguidores");
    private By okButton = By.cssSelector(".swal2-actions button");
    private By registerSuccessMessage = By.cssSelector(".swal2-success");
    private By registerTitle = By.className("register-title");
    private By firstRadioOption = By.xpath("//*[@id=\"root\"]/div/div/div[4]/label[1]");
    private By selectedImageOption = By.xpath("//*[@id=\"root\"]/div/div/div[5]/div/div/div[5]/div/div/img");

    public void clickOkButton() { driver.findElement(okButton).click();}

    public void initialPage() {
        driver.navigate().to("https://followers-register.vercel.app");
    }

    public void clickRegisterButton() {
        driver.findElement(registerButton).click();
    }

    public void fillName(String name) {
        driver.findElement(nameInput).sendKeys(name);
    }

    public void selectGender(String gender) {
        driver.findElement(genderInput).sendKeys(gender);
    }

    public void fillNivel(String nivel) {
        driver.findElement(nivelInput).sendKeys(nivel);
    }

    public void selectFirstRadioOption() {
        WebElement radioButton = driver.findElement(firstRadioOption);
        radioButton.click();
    }

    public void selectSelectedImageOption() {
        WebElement radioButton = driver.findElement(selectedImageOption);
        radioButton.click();
    }


    public void waitForOkButtonToBeVisible() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(okButton));
    }
}

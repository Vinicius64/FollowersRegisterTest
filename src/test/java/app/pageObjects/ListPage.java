package app.pageObjects;

import followers.Follower;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class ListPage {
    private WebDriver driver;
    private List<Follower> followers;

    public ListPage(WebDriver driver) {
        this.driver = driver;
        this.followers = new ArrayList<>();
        loadFollowers();
    }

    public boolean verifyFollowerInList(String name, String gender, String nivel) {
        for (Follower follower : followers) {
            if (follower.matches(name.trim(), gender.trim(), nivel.trim())) {
                return true;
            }
        }
        return false;
    }

    public boolean verifyDuplicatedFollowerInList(String name,String gender, String nivel) {
        int count = 0;
        for (Follower follower : followers) {
            if (follower.matches(name.trim(), gender.trim(), nivel.trim())) {
                count++;
                System.out.println("same follower found: " + name);
            }
        }
        if(count > 1){
            return true;
        }else return false;
    }

    public WebElement getFirstFollowerCard() {
        List<WebElement> cards = driver.findElements(By.className("card-modal"));
        return cards.isEmpty() ? null : cards.get(0);
    }

    public WebElement getFollowerCard(String name, String gender, String nivel) {
        List<WebElement> followerElements = driver.findElements(By.className("card-modal"));
        for (WebElement element : followerElements) {
            String cardName = element.findElement(By.className("card-header")).getText().trim();
            String cardGender = element.findElement(By.xpath(".//p[strong[text()='Gênero:']]"))
                    .getText().replace("Gênero: ", "").trim();
            String cardNivel = element.findElement(By.xpath(".//p[strong[text()='Nível de Devoção:']]"))
                    .getText().replace("Nível de Devoção: ", "").trim();

            if (cardName.equals(name) && cardGender.equals(gender) && cardNivel.equals(nivel)) {
                return element;
            }
        }
        throw new AssertionError("Follower card not found for the given data");
    }

    public void refreshFollowersList() {
        followers.clear();
        loadFollowers();
    }

    private void loadFollowers(){
        List<WebElement> followerElements = driver.findElements(By.className("card-modal"));
        for (WebElement element : followerElements) {
            String name = element.findElement(By.className("card-header")).getText().trim();
            String gender = element.findElement(By.xpath(".//p[strong[text()='Gênero:']]"))
                    .getText().replace("Gênero:", "").trim();
            String nivel = element.findElement(By.xpath(".//p[strong[text()='Nível de Devoção:']]"))
                    .getText().replace("Nível de Devoção:", "").trim();

            followers.add(new Follower(name, gender, nivel));
        }
    }
}

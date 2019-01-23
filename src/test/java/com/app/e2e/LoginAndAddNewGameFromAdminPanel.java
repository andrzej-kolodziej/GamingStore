package com.app.e2e;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginAndAddNewGameFromAdminPanel {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        String url = "http://localhost:8080";
        driver.get(url);

        WebElement loginLink = driver.findElement(By.cssSelector("a[href='/login']"));
        loginLink.click();
        WebElement usernameInput = driver.findElement(By.name("username"));
        usernameInput.sendKeys("admin");
        WebElement passwordInput = driver.findElement(By.name("password"));
        passwordInput.sendKeys("password");

        WebElement loginButton = driver.findElement(By.id("login"));
        loginButton.submit();

        WebElement adminPage = driver.findElement(By.cssSelector("a[href='/admin']"));
        adminPage.click();

        WebElement addProduct = driver.findElement(By.cssSelector("a[href='/product/new']"));
        addProduct.click();

        WebElement nameInput = driver.findElement(By.id("productName"));
        nameInput.sendKeys("The Game");

        WebElement priceInput = driver.findElement(By.id("productPrice"));
        priceInput.sendKeys("10");

        WebElement descriptionInput = driver.findElement(By.name("productDescription"));
        usernameInput.sendKeys("description");

        WebElement imageUrl = driver.findElement(By.id("productImageUrl"));
        usernameInput.sendKeys("https://dummyimage.com/300");

        WebElement youtubeUrl = driver.findElement(By.id("productYoutubeUrl"));
        youtubeUrl.sendKeys("https://youtube.com");

        WebElement developers = driver.findElement(By.id("productDeveloper.id"));
        developers.click();

        WebElement developerOption = driver.findElement(By.cssSelector("#productDeveloper.id option:nth-of-type(2)"));
        developerOption.click();
    }
}

package com.app.e2e;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class CreateNewUserAndLogin {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        String url = "http://localhost:8080";
        driver.get(url);

        WebElement loginLink = driver.findElement(By.cssSelector("a[href='/login']"));
        loginLink.click();

        WebElement signUp = driver.findElement(By.cssSelector("a[href='/customer/new']"));
        signUp.click();

        WebElement usernameInput = driver.findElement(By.name("userName"));
        usernameInput.sendKeys("user123");

        WebElement passwordInput = driver.findElement(By.name("userPassword"));
        usernameInput.sendKeys("pass123");

        WebElement emailInput = driver.findElement(By.name("userEmail"));
        usernameInput.sendKeys("user@email.com");

        WebElement submit = driver.findElement(By.cssSelector("button[type='submit']"));
        submit.submit();
    }
}

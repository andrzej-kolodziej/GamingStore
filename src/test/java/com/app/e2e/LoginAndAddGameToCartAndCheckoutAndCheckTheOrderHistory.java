package com.app.e2e;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginAndAddGameToCartAndCheckoutAndCheckTheOrderHistory {
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

        WebElement storeLink = driver.findElement(By.cssSelector("a[href='/store']"));
        storeLink.click();

        WebElement addToCart = driver.findElement(By.cssSelector("button[name='addToCart']"));
        addToCart.click();

        WebElement checkOut = driver.findElement(By.cssSelector("a[href='/cart']"));
        checkOut.click();

        WebElement checkoutAsUser = driver.findElement(By.cssSelector("button[type='submit']:last-of-type"));
        checkoutAsUser.submit();

        WebElement payWithCard = driver.findElement(By.name("payAsUser"));
        payWithCard.click();

        WebElement userDropdown = driver.findElement(By.className("dropdown-toggle"));
        userDropdown.click();

        WebElement purchases = driver.findElement(By.cssSelector("a[href='/customer/orderhistory']"));
        purchases.click();

        System.out.println("Test passed");
        //driver.close();
    }
}

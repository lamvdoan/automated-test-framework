package com.fetchrewards;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class FetchNoFrameworkTest {
    static WebDriver driver;

    @BeforeClass
    public static void beforeAnyTest() {
        System.setProperty("webdriver.chrome.driver", "src/main/WEB-INF/lib/chromedriver");
        driver = new ChromeDriver();
    }

    @Test
    public void loadCareersSite() {
        driver.get("https://www.fetchrewards.com");
        driver.findElement(By.linkText("Invite Friends")).click();

        driver.findElement(By.linkText("Careers")).click();

        Assert.assertEquals("Is current URL the careers URL?", "https://www.fetchrewards.com/careers", driver.getCurrentUrl());
    }

    @Test
    public void failingToLoadCareersSite() {
        driver.get("https://www.fetchrewards.com");
        driver.findElement(By.linkText("Invite Friends")).click();

        Assert.assertEquals("Is current URL the careers URL?", "https://www.fetchrewards.com/careers", driver.getCurrentUrl());
    }

    @AfterClass
    public static void cleanUp() {
        driver.quit();
    }
}

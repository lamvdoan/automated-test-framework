package com.fetchrewards.common.driver;

import com.fetchrewards.common.framework.TestBase;
import org.junit.AfterClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverTestBase extends TestBase {
    protected static WebDriverWait wait;
    protected static WebDriver webDriver;

    public static void quitDriver() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    public WebDriverTestBase() {
        if (webDriver == null) {
            /* Chromedriver 79 - Updated 1.19.2020 */
            System.setProperty("webdriver.chrome.driver", "src/main/WEB-INF/lib/chromedriver");
            webDriver = new ChromeDriver();
            wait = new WebDriverWait(webDriver, config.getWebDriverWaitTime());
        }
    }
}

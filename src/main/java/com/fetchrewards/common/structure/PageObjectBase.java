package com.fetchrewards.common.structure;

import com.fetchrewards.common.driver.WebDriverTestBase;
import com.fetchrewards.common.utility.Utils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * Any page classes that uses webdriver, aka browser, must extend this class, NOT LayoutObjectBase.
 */

public class PageObjectBase extends WebDriverTestBase {
    private String url;
    private String browserTitle = null;
    private static final int MAX_RETRIES = 5;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void close() {
        log.info("Closing browser page: " + this.browserTitle);
        webDriver.close();
    }

    public void waitForPageToFinishLoading() {
        log.debug("Waiting for page to finish loading...");
        executeJavaScriptInBrowser("return document.readyState");
    }

    public void navigateTo(String url) {
        log.info("Navigating to URL: " + url);
        webDriver.get(url);
        waitForPageToFinishLoading();
    }

    public void navigateBack() {
        log.info("    [Action] Navigating back in browser history...");
        webDriver.navigate().back();
    }

    public void refresh() {
        log.info("    [Action] Refreshing browser...");
        webDriver.navigate().refresh();
        waitForPageToFinishLoading();
    }

    public void setBrowserSize(int width, int height) {
        webDriver.manage().window().setSize(new org.openqa.selenium.Dimension(width, height));
    }

    private void executeJavaScriptInBrowser(final String javascriptCommand) {
        for (int i = 0; i <= MAX_RETRIES; i++) {
            try {
                ExpectedCondition<Boolean> javascriptComplete = driver -> ((JavascriptExecutor) driver).executeScript(javascriptCommand).equals("complete");
                wait.until(javascriptComplete);
            } catch (Exception e) {
                if (i < MAX_RETRIES) {
                    log.trace("Retrying... browser not finished loading: " + e);
                    Utils.sleep(1);
                } else {
                    log.error("Browser fails to load after " + MAX_RETRIES + " retries");
                }
            }
        }
    }

    public String getCurrentUrl() {
        return webDriver.getCurrentUrl();
    }
}

package com.fetchrewards.common.structure;

import com.fetchrewards.common.driver.WebDriverTestBase;
import com.fetchrewards.common.utility.Utils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.concurrent.TimeUnit;


/** Any elements that uses Webdriver, aka browser, must extend this class, NOT MobileControl.
 *
 */

public class WebControl extends WebDriverTestBase {
    private By by = null;
    private WebElement elementCache = null;
    private int maxRetries = 5;

    public WebControl(By by) {
        this.by = by;
    }

    /**
     * Find an the element on the page using this.by
     * @return
     */
    public WebElement findElement() {
        WebElement foundElement = null;

        for (int i = 0; i <= maxRetries; i++) {
            try {
                log.trace("Finding element: " + this.by);
                foundElement = webDriver.findElement(this.by);
                elementCache = foundElement;
                break;

            } catch (Exception e) {
                if (i < maxRetries) {
                    log.trace("Retrying... Can't find element: " + e);
                    Utils.sleep(1);
                } else {
                    log.error("Can't find element after " + maxRetries + " retries");
                }
            }
        }

        return foundElement;
    }

    public void click() {
        log.debug("    [Click] " + this.by);

        for (int i = 1; i < maxRetries; i++) {
            try {
                WebElement webElement = findElement();
                webElement.click();
                break;
            } catch (WebDriverException e) {
                log.warning("Element not found or stale... trying again!");
                Utils.sleep(1);
            }
        }
    }

    public void setText(String text) {
        log.debug("    [SetText] " + this.by + " ---> '" + text + "'");

        for (int i = 1; i <= maxRetries; i++) {
            try {
                WebElement webElement = findElement();
                webElement.sendKeys(text);
                break;

            } catch (Exception e) {
                if (i == maxRetries)
                    log.error("Cannot find element: " + e);
                else
                    log.warning("[" + e.getClass().getSimpleName() + "] Exception trapped in setText().  Trying again...");

                Utils.sleep(1);
            }
        }
    }

    public void sendKeys(CharSequence... keysToSend) {
        try {
            log.debug("    [SendKeys] " + this.by + " ---> '" + ((Keys) keysToSend[0]).name() + "'");
        } catch (Exception e) {
            log.debug("    [SendKeys] " + this.by + " ---> '" + (keysToSend[0]).toString() + "'");
        }

        WebElement webElement = findElement();
        webElement.sendKeys(keysToSend);
    }

    public void clear() {
        log.debug("    [Clear] " + this.by);
        WebElement webElement = findElement();
        webElement.clear();
    }

    public void submit() {
        log.debug("    [Submit] " + this.by);
        WebElement webElement = findElement();
        webElement.submit();
    }

    public String getText() {
        log.debug("    [Get Text] " + this.by);
        String returnText = null;

        for (int i = 0; i <= maxRetries; i++) {
            try {
                returnText = findElement().getText();
                break;
            } catch (Exception e) {
                if (i == maxRetries) {
                    log.error("Cannot find element: " + e);
                } else {
                    log.warning("[" + e.getClass().getSimpleName() + "] Exception trapped in getText().  Trying again...");
                }

                Utils.sleep(1);
            }
        }

        return returnText;
    }

    public boolean isEnabled() {
        log.debug("    [Get isEnabled] " + this.by);
        boolean isEnabled = false;

        try {
            isEnabled = findElement().isEnabled();
        } catch (Exception e) {
            log.error("Cannot find element: " + e);
        }

        return isEnabled;
    }

    public boolean isSelected() {
        log.debug("    [Get isSelected] " + this.by);
        return findElement().isSelected();
    }

    public boolean isDisplayed() {
        log.debug("    [Get isDisplayed] " + this.by);
        try {
            return findElement().isDisplayed();
        } catch (Exception e) {
            log.error("Cannot find element: " + e);
        }

        return false;
    }

    public boolean exists() {
        return this.exists(0);
    }

    public boolean exists(int seconds) {
        log.debug("    [Get isExists] " + this.by);
        long timeToWait = (seconds == 0) ? 1 : (seconds * 1000);

        try {
            if (elementCache == null) {
                wait.withTimeout(timeToWait, TimeUnit.MILLISECONDS).until(ExpectedConditions.elementToBeClickable(this.by));
            } else {
                wait.withTimeout(timeToWait, TimeUnit.MILLISECONDS).until(ExpectedConditions.elementToBeClickable(elementCache));
            }

            return true;

        } catch (TimeoutException exception) {
            return false;
        }
    }

    public String getValue() {
        log.debug("    [Get Value] " + this.by);
        return this.getAttribute("value");
    }

    public String getAttribute(String attributeName) {
        log.debug("    [Get Attribute] of " + attributeName + ": " + this.by);
        return this.findElement().getAttribute(attributeName);
    }

    /**
     *  For debugging the xpath purposes
     * @param webElement
     * @return
     */
    public String getAllAttributes(WebElement webElement) {
        String debug = "Text: " + webElement.getText() + " | ";
        debug += "TagName: " + webElement.getTagName() + " | ";
        debug += "Class: " + webElement.getAttribute("class") + " | ";
        debug += "value: " + webElement.getAttribute("value") + " | ";
        debug += "Location: " + webElement.getLocation().toString() + "|";
        debug += "Size: " + webElement.getSize().toString() + "|";
        debug += "Displayed: " + webElement.isDisplayed() + "|";
        debug += "Enabled: " + webElement.isEnabled() + "|";
        debug += "href: " + webElement.getAttribute("href") + "|";
        debug += "style: " + webElement.getAttribute("style") + "|";

        return debug;
    }

    public void dragAndDropTo(WebControl toElement) {
        Actions builder = new Actions(webDriver);

        Action dragAndDrop = builder.clickAndHold(this.findElement())
                .moveToElement(toElement.findElement())
                .release(toElement.findElement())
                .build();

        log.debug("Item being dragged: " + this.findElement().getText());
        dragAndDrop.perform();
    }
}

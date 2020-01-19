package com.fetchrewards.common.structure;

import com.fetchrewards.common.driver.AppiumDriverTestBase;
import com.fetchrewards.common.utility.Utils;
import io.appium.java_client.TouchAction;
import org.openqa.selenium.*;

/** Any elements that uses Appium must extend this class, NOT WebControl.
 *
 */

public class MobileControl extends AppiumDriverTestBase {
    private By by = null;
    private WebElement elementCache = null;
    private int maxRetries = 5;

    public MobileControl(By by) {
        this.by = by;
    }

    private WebElement findElement() {
        WebElement foundElement = null;

        if (elementCache != null) {
            log.trace("Cache hit... Retrieving element for: " + this.by);
            foundElement = elementCache;
        } else {
            for (int i = 0; i < maxRetries; i++) {
                try {
                    log.trace("Finding element: " + this.by);
                    foundElement = appiumDriver.findElement(this.by);
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
        }

        return foundElement;
    }

    public void click() {
        log.debug("      [Click] on element: " + this.by);
        WebElement webElement;

        for (int i = 0; i < maxRetries; i++) {
            webElement = findElement();

            try {
                webElement.click();
                break;
            } catch (Exception e) {
                log.trace("Element not found or stale... trying again!");
                Utils.sleep(1);
            }
        }
    }

    public String getText() {
        log.debug("      [Get Text] on element: " + this.by);
        String returnText = null;
        WebElement webElement;

        for (int i = 0; i < maxRetries; i++) {
            try {
                webElement = findElement();
                returnText = webElement.getText();
                break;
            } catch (Exception e) {
                if (i == maxRetries) {
                    log.error("Cannot find element after multiple maxRetries: " + e);
                } else {
                    log.trace("[" + e.getClass().getSimpleName() + "] Exception trapped in getText().  Trying again...");
                }

                Utils.sleep(1);
            }
        }

        return returnText;
    }


    public void clickAndHold() {
        TouchAction action = new TouchAction(appiumDriver);
        action.longPress(this.findElement()).release().perform();
    }
}

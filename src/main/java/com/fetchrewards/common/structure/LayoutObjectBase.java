package com.fetchrewards.common.structure;

import com.fetchrewards.common.driver.AppiumDriverTestBase;
import org.openqa.selenium.Dimension;

/** Any page classes that uses Appium must extend this class.
 *
 */

public class LayoutObjectBase extends AppiumDriverTestBase {

    public void scrollDown() {
        log.info("[Scroll down]");
        Dimension size = appiumDriver.manage().window().getSize();

        int starty = (int) (size.height * 0.80);
        int endy = (int) (size.height * 0.20);
        int startx = size.width / 2;

        swipe(startx, endy, startx, starty, 3000);  // Swipe from Top to Bottom.
    }

    public void scrollUp() {
        log.info("[Scroll up]");
        Dimension size = appiumDriver.manage().window().getSize();

        int starty = (int) (size.height * 0.80);
        int endy = (int) (size.height * 0.20);
        int startx = size.width / 2;

        swipe(startx, starty, startx, endy, 3000); // Swipe from Bottom to Top.
    }

    protected void swipe(int startx, int starty, int endx, int endy, int duration) {
        log.info("[Swipe]: " + startx + ", " + starty + " to " + endx + ", " + endy);
        appiumDriver.swipe(startx, starty, endx, endy, duration);
    }
}

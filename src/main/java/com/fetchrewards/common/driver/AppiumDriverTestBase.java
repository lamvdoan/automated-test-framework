package com.fetchrewards.common.driver;


import com.fetchrewards.common.framework.TestBase;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.AfterClass;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;

public class AppiumDriverTestBase extends TestBase {
    protected static AppiumDriver appiumDriver = null;
    protected static WebDriverWait wait;

    private String appDevice = config.getAppDevice();
    private DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

    @AfterClass
    public static void afterClass() {
        log.endSuite();
        appiumDriver.quit();
    }

    public AppiumDriverTestBase() {

        if (appiumDriver == null) {
            switch (appDevice) {
                case "deviceType":
                    desiredCapabilities.setCapability("appPackage", "com.company.app_name");
                    desiredCapabilities.setCapability("appActivity", "AppController");
                    break;
                default:
                    log.fatal("Device not recognized: " + appDevice + ". Please update the config.properties file appropriately");
                    break;
            }

            // Common capabilities shared between all devices.  This is to test real devices, not emulators.
            desiredCapabilities.setCapability("deviceName", "Android"); // value doesn't matter
            desiredCapabilities.setCapability("platformName", "Android");
            desiredCapabilities.setCapability("newCommandTimeout", "3600");
            desiredCapabilities.setCapability("deviceOrientation", "portrait");
            desiredCapabilities.setCapability("fullReset", true);
            desiredCapabilities.setCapability("noReset", false);

            // Create the actual AndroidDriver to connect test scripts with device
            try {
                appiumDriver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), desiredCapabilities);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
}

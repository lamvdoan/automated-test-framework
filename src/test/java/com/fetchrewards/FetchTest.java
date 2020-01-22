package com.fetchrewards;

import com.fetchrewards.apps.web.page.Home;
import com.fetchrewards.common.framework.TestBase;
import com.fetchrewards.common.framework.TestDescription;
import com.fetchrewards.common.utility.Utils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

public class FetchTest extends TestBase {
    Home home = new Home();

    @Test
    @TestDescription(description = "Testing the careers page")
    public void loadCareersSite() {
        log.step("Go to Invite Friends page");
        home.navigateTo(home.getUrl());
        home.inviteFriendsLink.click();

        log.step("Go to careers page");
        home.careersLink.click();

        log.verify("Is current URL the careers URL?", home.getUrl() + "/careers", home.getCurrentUrl());
    }

    @Test
    @TestDescription(description = "Testing the careers page")
    public void failingToLoadCareersSite() {
        log.step("Go to Invite Friends page");
        home.navigateTo(home.getUrl());
        home.inviteFriendsLink.click();

        log.verify("Is current URL the careers URL?", home.getUrl() + "/careers", home.getCurrentUrl());
    }
}

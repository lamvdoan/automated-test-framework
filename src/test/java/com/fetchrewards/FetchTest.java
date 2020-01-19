package com.fetchrewards;

import com.fetchrewards.apps.web.page.Home;
import org.junit.After;
import org.junit.Test;

public class FetchTest {
    Home home = new Home();

    @Test
    public void testing() {
        home.navigateTo(home.getUrl());
        home.inviteFriendsLink.click();
        home.careersLink.click();
    }

    @After
    public void cleanUp() {
        home.closeBrowsers();
    }
}

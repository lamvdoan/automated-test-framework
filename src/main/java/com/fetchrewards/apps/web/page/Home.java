package com.fetchrewards.apps.web.page;

import com.fetchrewards.common.structure.PageObjectBase;
import com.fetchrewards.common.structure.WebControl;
import org.openqa.selenium.By;

public class Home extends PageObjectBase{
    public WebControl inviteFriendsLink = new WebControl(By.linkText("Invite Friends"));
    public WebControl careersLink = new WebControl(By.linkText("Careers"));

    public Home() {
        String url = "https://www.fetchrewards.com";
        setUrl(url);
    }
}

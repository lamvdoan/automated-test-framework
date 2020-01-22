package com.fetchrewards.apps.service.rewards;

import com.fetchrewards.common.structure.JsonObjectBase;

public class RewardJson extends JsonObjectBase {
    public String id;
    public String partnerSku;
    public int pointsNeeded;
    public String category;
    public String name;
    public String title;
    public String description;
    public String imageUrl;
    public int denomination;
    public String startDate;
    public String endDate;
    public String legal;
    public String officialRulesUrl;
    public String merchant;
    public String redemptionOptionLabel;
    public boolean userHasEnoughPoints;
    public String rewardType;
    public Object numEntries;
}

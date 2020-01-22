package com.fetchrewards.apps.service.rewards;

import com.fetchrewards.common.structure.JsonObjectBase;


public class RewardsPayload extends JsonObjectBase {
    public static RewardJson[] stringToJson(String jsonString) {
        return (RewardJson[]) fromJson(jsonString, new RewardJson[0]);
    }
}

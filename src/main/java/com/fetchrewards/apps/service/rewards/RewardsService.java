package com.fetchrewards.apps.service.rewards;

import com.fetchrewards.common.structure.ApiBase;

public class RewardsService extends ApiBase {
    public RewardsService(String userId) {
        super();
        setEndpointPath(config.getRewardsServiceUrl() + "/rewards");

        addParameter("userId=", userId);
    }

    public RewardJson[] getRewards() {
        return RewardsPayload.stringToJson(super.get());
    }
}

package com.fetchrewards;


import com.fetchrewards.apps.service.rewards.RewardJson;
import com.fetchrewards.apps.service.rewards.RewardsService;
import com.fetchrewards.common.framework.TestBase;
import org.junit.Test;

public class RewardsApiTest extends TestBase {
    @Test
    public void testRewardRequest() {
        log.step("Call User Rewards GET request");
        String userId = "5b16b0b5e4b0f7c5af562f67";
        RewardsService service = new RewardsService(userId);

        RewardJson[] payload = service.getRewards();

        log.verify("reward[0] name", "test1_10_28_19", payload[0].name);
        log.verify("reward[0] partnerSku", "UGC-V-AZN", payload[0].partnerSku);

        log.verify("reward[1] name", "AMC Theatres®", payload[1].name);
        log.verify("reward[1] partnerSku", "U555541", payload[1].partnerSku);
    }
}

package com.fetchrewards;


import com.fetchrewards.apps.service.postman.PostmanEchoPayload;
import com.fetchrewards.apps.service.postman.PostmanEchoService;
import com.fetchrewards.common.framework.TestBase;
import org.junit.Test;

public class PostmanApiTest extends TestBase {
    @Test
    public void testPostmanGetRequest() {
        // https://docs.postman-echo.com/?version=latest
        log.step("Call postman GET request");
        PostmanEchoService service = new PostmanEchoService();
        PostmanEchoPayload payload = service.getPostman();

        log.verify("Check foo1 value", "bar1", payload.args.foo1);
        log.verify("Check foo2 value", "bar2", payload.args.foo2);
        log.verify("Check url", service.getEntireUrl(), payload.url);
    }
}

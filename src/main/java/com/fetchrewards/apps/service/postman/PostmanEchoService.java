package com.fetchrewards.apps.service.postman;

import com.fetchrewards.common.structure.ApiBase;

public class PostmanEchoService extends ApiBase {
    public PostmanEchoService() {
        super();
        setEndpointPath("https://postman-echo.com/get");

        addParameter("foo1", "bar1");
        addParameter("foo2", "bar2");

        addHeader("Connection", "keep-alive");
    }

    public PostmanEchoPayload getPostman() {
        return PostmanEchoPayload.stringToJson(super.get());
    }
}

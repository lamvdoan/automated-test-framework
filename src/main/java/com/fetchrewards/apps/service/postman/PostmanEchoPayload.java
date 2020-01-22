package com.fetchrewards.apps.service.postman;

import com.fetchrewards.common.structure.JsonObjectBase;

public class PostmanEchoPayload extends JsonObjectBase {
    public ArgsJson args;
    public HeadersJson headers;
    public String url;

    public static PostmanEchoPayload stringToJson(String jsonString) {
        return (PostmanEchoPayload) fromJson(jsonString, new PostmanEchoPayload());
    }
}

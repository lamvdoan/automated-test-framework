package com.fetchrewards.apps.service.postman;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fetchrewards.common.structure.JsonObjectBase;

public class HeadersJson extends JsonObjectBase {
        /*
         *  @JsonProperty is needed because Java doesn't allow - (dash) in the variable name
         */

        @JsonProperty("x-forwarded-proto")
        public String xForwardedProto;

        public String host;
        public String accept;

        @JsonProperty("accept-encoding")
        public String acceptEncoding;

        @JsonProperty("cache-control")
        public String cacheControl;

        public String cookie;

        @JsonProperty("postman-token")
        public String postmanToken;

        @JsonProperty("user-agent")
        public String userAgent;

        @JsonProperty("x-forwarded-port")
        public String xForwardedPort;
}
